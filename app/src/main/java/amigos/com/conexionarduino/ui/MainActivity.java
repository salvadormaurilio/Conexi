package amigos.com.conexionarduino.ui;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;

import amigos.com.conexionarduino.R;
import amigos.com.conexionarduino.services.ArduinoCommunicatorService;
import amigos.com.conexionarduino.util.ConstantsService;


public class MainActivity extends Activity implements View.OnClickListener {

    private boolean isConnectedArduino = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonDropset).setOnClickListener(this);
        findViewById(R.id.buttonPosNeg).setOnClickListener(this);
        findViewById(R.id.buttonNegative).setOnClickListener(this);

        findDevice();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (ConstantsService.DEBUG) Log.d(ConstantsService.TAG, "onNewIntent() " + intent);
        super.onNewIntent(intent);

        if (UsbManager.ACTION_USB_DEVICE_ATTACHED.contains(intent.getAction())) {
            if (ConstantsService.DEBUG) Log.d(ConstantsService.TAG, "onNewIntent() " + intent);
            findDevice();
        }
    }

    private void findDevice() {
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        UsbDevice usbDevice = null;
        HashMap<String, UsbDevice> usbDeviceList = usbManager.getDeviceList();
        if (ConstantsService.DEBUG) Log.d(ConstantsService.TAG, "length: " + usbDeviceList.size());

        Iterator<UsbDevice> deviceIterator = usbDeviceList.values().iterator();

        if (deviceIterator.hasNext()) {
            UsbDevice tempUsbDevice = deviceIterator.next();

            // Print device information. If you think your device should be able
            // to communicate with this app, add it to accepted products below.
            if (ConstantsService.DEBUG)
                Log.d(ConstantsService.TAG, "VendorId: " + tempUsbDevice.getVendorId());
            if (ConstantsService.DEBUG)
                Log.d(ConstantsService.TAG, "ProductId: " + tempUsbDevice.getProductId());
            if (ConstantsService.DEBUG)
                Log.d(ConstantsService.TAG, "DeviceName: " + tempUsbDevice.getDeviceName());
            if (ConstantsService.DEBUG)
                Log.d(ConstantsService.TAG, "DeviceId: " + tempUsbDevice.getDeviceId());
            if (ConstantsService.DEBUG)
                Log.d(ConstantsService.TAG, "DeviceClass: " + tempUsbDevice.getDeviceClass());
            if (ConstantsService.DEBUG)
                Log.d(ConstantsService.TAG, "DeviceSubclass: " + tempUsbDevice.getDeviceSubclass());
            if (ConstantsService.DEBUG)
                Log.d(ConstantsService.TAG, "InterfaceCount: " + tempUsbDevice.getInterfaceCount());
            if (ConstantsService.DEBUG)
                Log.d(ConstantsService.TAG, "DeviceProtocol: " + tempUsbDevice.getDeviceProtocol());

            if (tempUsbDevice.getVendorId() == ConstantsService.ARDUINO_USB_VENDOR_ID) {
                if (ConstantsService.DEBUG) Log.i(ConstantsService.TAG, "Arduino device found!");

                switch (tempUsbDevice.getProductId()) {
                    case ConstantsService.ARDUINO_UNO_USB_PRODUCT_ID:
//                        Toast.makeText(getBaseContext(), "Arduino Uno " + getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                    case ConstantsService.ARDUINO_MEGA_2560_USB_PRODUCT_ID:
//                        Toast.makeText(getBaseContext(), "Arduino Mega 2560 " + getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                    case ConstantsService.ARDUINO_MEGA_2560_R3_USB_PRODUCT_ID:
//                        Toast.makeText(getBaseContext(), "Arduino Mega 2560 R3 " + getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                    case ConstantsService.ARDUINO_UNO_R3_USB_PRODUCT_ID:
//                        Toast.makeText(getBaseContext(), "Arduino Uno R3 " + getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                    case ConstantsService.ARDUINO_MEGA_2560_ADK_R3_USB_PRODUCT_ID:
//                        Toast.makeText(getBaseContext(), "Arduino Mega 2560 ADK R3 " + getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                    case ConstantsService.ARDUINO_MEGA_2560_ADK_USB_PRODUCT_ID:
//                        Toast.makeText(getBaseContext(), "Arduino Mega 2560 ADK " + getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                    case ConstantsService.ARDUINO_DUE_USB_PRODUCT_ID:
//                        Toast.makeText(getBaseContext(), "Arduino Due " + getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                }
            }
        }

        if (usbDevice == null) {
            if (ConstantsService.DEBUG) Log.i(ConstantsService.TAG, "No device found!");
            Toast.makeText(getBaseContext(), getString(R.string.no_device_found), Toast.LENGTH_SHORT).show();
            isConnectedArduino = false;
        } else {
            if (ConstantsService.DEBUG) Log.i(ConstantsService.TAG, "Device found!");
            Toast.makeText(this, R.string.device_found, Toast.LENGTH_SHORT).show();
            Intent startIntent = new Intent(getApplicationContext(), ArduinoCommunicatorService.class);
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, startIntent, 0);
            usbManager.requestPermission(usbDevice, pendingIntent);
            isConnectedArduino = true;
        }
    }


    @Override
    public void onClick(View v) {
//        isConnectedArduino = true;
        if (isConnectedArduino) {
            Intent intent = null;
            byte data[] = new byte[1];
            switch (v.getId()) {
                case R.id.buttonDropset:
                    data[0] = 1;
                    intent = new Intent(this, DropsetActivity.class);
                    break;
                case R.id.buttonPosNeg:
                    data[0] = 2;
                    intent = new Intent(this, PosNegActivity.class);
                    break;
                case R.id.buttonNegative:
                    data[0] = 3;
                    intent = new Intent(this, NegativeActivity.class);
                    break;
            }
            sendData(data);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.no_device_found), Toast.LENGTH_SHORT).show();
        }

    }

    private void sendData(byte[] data) {

        Intent intent = new Intent(ConstantsService.SEND_DATA_INTENT);
        intent.putExtra(ConstantsService.DATA_EXTRA, data);
        sendBroadcast(intent);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
