/*
 * Copyright (C) 2012 Mathias Jeppsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package amigos.com.conexionarduino.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import amigos.com.conexionarduino.R;
import amigos.com.conexionarduino.util.ConstantsService;

public class ArduinoCommunicatorService extends Service {

    private boolean mIsRunning = false;
    private SenderThread mSenderThread;

    private volatile UsbDevice mUsbDevice = null;
    private volatile UsbDeviceConnection mUsbConnection = null;
    private volatile UsbEndpoint mInUsbEndpoint = null;
    private volatile UsbEndpoint mOutUsbEndpoint = null;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        if (ConstantsService.DEBUG) Log.d(ConstantsService.TAG, "onCreate()");
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantsService.SEND_DATA_INTENT);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mReceiver, filter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ConstantsService.DEBUG)
            Log.d(ConstantsService.TAG, "onStartCommand() " + intent + " " + flags + " " + startId);

        if (mIsRunning) {
            if (ConstantsService.DEBUG) Log.i(ConstantsService.TAG, "Service already running.");
            return Service.START_REDELIVER_INTENT;
        }

        mIsRunning = true;

        if (!intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
            if (ConstantsService.DEBUG) Log.i(ConstantsService.TAG, "Permission denied");
            Toast.makeText(getBaseContext(), getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
            stopSelf();
            return Service.START_REDELIVER_INTENT;
        }

        if (ConstantsService.DEBUG) Log.d(ConstantsService.TAG, "Permission granted");
        mUsbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
        if (!initDevice()) {
            if (ConstantsService.DEBUG) Log.e(ConstantsService.TAG, "Init of device failed!");
            stopSelf();
            return Service.START_REDELIVER_INTENT;
        }

        if (ConstantsService.DEBUG) Log.i(ConstantsService.TAG, "Receiving!");
        Toast.makeText(getBaseContext(), getString(R.string.receiving), Toast.LENGTH_SHORT).show();
        startReceiverThread();
        startSenderThread();

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        if (ConstantsService.DEBUG) Log.i(ConstantsService.TAG, "onDestroy()");
        super.onDestroy();
        unregisterReceiver(mReceiver);
        mUsbDevice = null;
        if (mUsbConnection != null) {
            mUsbConnection.close();
        }
    }

    private byte[] getLineEncoding(int baudRate) {
        final byte[] lineEncodingRequest = {(byte) 0x80, 0x25, 0x00, 0x00, 0x00, 0x00, 0x08};
        switch (baudRate) {
            case 14400:
                lineEncodingRequest[0] = 0x40;
                lineEncodingRequest[1] = 0x38;
                break;

            case 19200:
                lineEncodingRequest[0] = 0x00;
                lineEncodingRequest[1] = 0x4B;
                break;
        }

        return lineEncodingRequest;
    }

    private boolean initDevice() {
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mUsbConnection = usbManager.openDevice(mUsbDevice);
        if (mUsbConnection == null) {
            if (ConstantsService.DEBUG) Log.e(ConstantsService.TAG, "Opening USB device failed!");
            Toast.makeText(getBaseContext(), getString(R.string.opening_device_failed), Toast.LENGTH_LONG).show();
            return false;
        }
        UsbInterface usbInterface = mUsbDevice.getInterface(1);
        if (!mUsbConnection.claimInterface(usbInterface, true)) {
            if (ConstantsService.DEBUG) Log.e(ConstantsService.TAG, "Claiming interface failed!");
            Toast.makeText(getBaseContext(), getString(R.string.claimning_interface_failed), Toast.LENGTH_LONG).show();
            mUsbConnection.close();
            return false;
        }

        // Arduino USB serial converter setup
        // Set control line state
        mUsbConnection.controlTransfer(0x21, 0x22, 0, 0, null, 0, 0);
        // Set line encoding.
        mUsbConnection.controlTransfer(0x21, 0x20, 0, 0, getLineEncoding(9600), 7, 0);

        for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
            if (usbInterface.getEndpoint(i).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                if (usbInterface.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_IN) {
                    mInUsbEndpoint = usbInterface.getEndpoint(i);
                } else if (usbInterface.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_OUT) {
                    mOutUsbEndpoint = usbInterface.getEndpoint(i);
                }
            }
        }

        if (mInUsbEndpoint == null) {
            if (ConstantsService.DEBUG) Log.e(ConstantsService.TAG, "No in endpoint found!");
            Toast.makeText(getBaseContext(), getString(R.string.no_in_endpoint_found), Toast.LENGTH_LONG).show();
            mUsbConnection.close();
            return false;
        }

        if (mOutUsbEndpoint == null) {
            if (ConstantsService.DEBUG) Log.e(ConstantsService.TAG, "No out endpoint found!");
            Toast.makeText(getBaseContext(), getString(R.string.no_out_endpoint_found), Toast.LENGTH_LONG).show();
            mUsbConnection.close();
            return false;
        }

        return true;
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ConstantsService.DEBUG) Log.d(ConstantsService.TAG, "onReceive() " + action);

            if (ConstantsService.SEND_DATA_INTENT.equals(action)) {
                final byte[] dataToSend = intent.getByteArrayExtra(ConstantsService.DATA_EXTRA);
                if (dataToSend == null) {
                    if (ConstantsService.DEBUG)
                        Log.i(ConstantsService.TAG, "No " + ConstantsService.DATA_EXTRA + " extra in intent!");
                    String text = String.format(getResources().getString(R.string.no_extra_in_intent), ConstantsService.DATA_EXTRA);
                    Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                    return;
                }

                mSenderThread.mHandler.obtainMessage(10, dataToSend).sendToTarget();
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                Intent intentDetached = new Intent(ConstantsService.USB_DEVICE_DETACHED);
                sendBroadcast(intentDetached);
                mSenderThread.mHandler.sendEmptyMessage(11);
                stopSelf();
            }
        }
    };

    private void startReceiverThread() {
        new Thread("arduino_receiver") {
            public void run() {
                byte[] inBuffer = new byte[4096];
                while (mUsbDevice != null) {
                    if (ConstantsService.DEBUG)
                        Log.d(ConstantsService.TAG, "calling bulkTransfer() in");
                    final int len = mUsbConnection.bulkTransfer(mInUsbEndpoint, inBuffer, inBuffer.length, 0);
                    if (len > 0) {
                        Intent intent = new Intent(ConstantsService.DATA_RECEIVED_INTENT);
                        byte[] buffer = new byte[len];
                        System.arraycopy(inBuffer, 0, buffer, 0, len);
                        intent.putExtra(ConstantsService.DATA_EXTRA, buffer);
                        sendBroadcast(intent);
                    } else {
                        if (ConstantsService.DEBUG) Log.i(ConstantsService.TAG, "zero data read!");
                    }
                }

                if (ConstantsService.DEBUG) Log.d(ConstantsService.TAG, "receiver thread stopped.");
            }
        }.start();
    }

    private void startSenderThread() {
        mSenderThread = new SenderThread("arduino_sender");
        mSenderThread.start();
    }

    private class SenderThread extends Thread {
        public Handler mHandler;

        public SenderThread(String string) {
            super(string);
        }

        public void run() {

            Looper.prepare();

            mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    if (ConstantsService.DEBUG)
                        Log.i(ConstantsService.TAG, "handleMessage() " + msg.what);
                    if (msg.what == 10) {
                        final byte[] dataToSend = (byte[]) msg.obj;

                        if (ConstantsService.DEBUG)
                            Log.d(ConstantsService.TAG, "calling bulkTransfer() out");
                        final int len = mUsbConnection.bulkTransfer(mOutUsbEndpoint, dataToSend, dataToSend.length, 0);
                        if (ConstantsService.DEBUG)
                            Log.d(ConstantsService.TAG, len + " of " + dataToSend.length + " sent.");
                        Intent sendIntent = new Intent(ConstantsService.DATA_SENT_INTERNAL_INTENT);
                        sendIntent.putExtra(ConstantsService.DATA_EXTRA, dataToSend);
                        sendBroadcast(sendIntent);
                    } else if (msg.what == 11) {
                        Looper.myLooper().quit();
                    }
                }
            };

            Looper.loop();
            if (ConstantsService.DEBUG) Log.i(ConstantsService.TAG, "sender thread stopped");
        }
    }
}
