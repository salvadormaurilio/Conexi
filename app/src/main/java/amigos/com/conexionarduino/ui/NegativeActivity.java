package amigos.com.conexionarduino.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import amigos.com.conexionarduino.R;
import amigos.com.conexionarduino.adapters.AdapterDropsetAndNegative;
import amigos.com.conexionarduino.dialogs.DialogExit;
import amigos.com.conexionarduino.util.ConstantsService;


public class NegativeActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener, DialogExit.OnListenerExit {

    private ListView listViewExcersise;
    private TextView textViewLoadedWeight;
    private boolean isStart;
    private int positionItem;
    private int weight;
    private String lb;

    private ListView listViewNegative;

    private AdapterDropsetAndNegative adapterDropsetAndNegative;

    private boolean isListViewVisible;

    private boolean isReceivingWeight;

    private int newWeight;
    private boolean enableNextSet;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ConstantsService.DEBUG) Log.d(ConstantsService.TAG, "onReceive() " + action);
            if (ConstantsService.DATA_RECEIVED_INTENT.equals(action)) {
                final byte[] data = intent.getByteArrayExtra(ConstantsService.DATA_EXTRA);
                if (isReceivingWeight) {
                    if (data[0] != -1) {
                        newWeight += data[0];
                    } else {
                        isReceivingWeight = false;
                        nextWeight(newWeight);
                        newWeight = 0;
                    }
                } else if (data[0] == 1) {
                    isReceivingWeight = true;
                } else if (data[0] == 2) {
                    incrementeRep();
                } else if (data[0] == 3) {
                    isStart = false;
                    enableNextSet = true;
                    Button buttonNextSet = (Button) findViewById(R.id.buttonNextSet);
                    buttonNextSet.setVisibility(View.VISIBLE);
                    buttonNextSet.setOnClickListener(NegativeActivity.this);
                }
            } else if (ConstantsService.USB_DEVICE_DETACHED.equals(action)) {
                finish();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropset);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        listViewExcersise = (ListView) findViewById(R.id.listViewExcersise);
        listViewExcersise.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, getResources().getStringArray(R.array.excersises)));
        listViewExcersise.setOnItemClickListener(this);
        positionItem = -1;

        lb = " " + getString(R.string.lb);
        textViewLoadedWeight = (TextView) findViewById(R.id.textViewLoadedWeight);

        weight = 0;
        textViewLoadedWeight.setText(getString(R.string.title_loaded_weight) + " " + weight + lb);

        findViewById(R.id.buttonStartEnd).setOnClickListener(this);
        isStart = false;
        enableNextSet = false;

        listViewNegative = (ListView) findViewById(R.id.listViewTable);

        adapterDropsetAndNegative = new AdapterDropsetAndNegative(this, 2);
        listViewNegative.setAdapter(adapterDropsetAndNegative);

        isListViewVisible = false;
        isReceivingWeight = false;
        newWeight = 0;

        findViewById(R.id.btn_key_0).setOnClickListener(this);
        findViewById(R.id.btn_key_1).setOnClickListener(this);
        findViewById(R.id.btn_key_2).setOnClickListener(this);
        findViewById(R.id.btn_key_3).setOnClickListener(this);
        findViewById(R.id.btn_key_4).setOnClickListener(this);
        findViewById(R.id.btn_key_5).setOnClickListener(this);
        findViewById(R.id.btn_key_6).setOnClickListener(this);
        findViewById(R.id.btn_key_7).setOnClickListener(this);
        findViewById(R.id.btn_key_8).setOnClickListener(this);
        findViewById(R.id.btn_key_9).setOnClickListener(this);
        findViewById(R.id.btn_key_clear).setOnClickListener(this);


        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantsService.DATA_RECEIVED_INTENT);
        filter.addAction(ConstantsService.DATA_SENT_INTERNAL_INTENT);
        filter.addAction(ConstantsService.USB_DEVICE_DETACHED);
        registerReceiver(mReceiver, filter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (!isStart && !enableNextSet) {
            if (positionItem != position) {
                positionItem = position;
                sendData(new byte[]{(byte) positionItem});
            }
        } else {
            listViewExcersise.setItemChecked(positionItem, true);
        }
    }

    @Override
    public void onClick(View v) {

        if ((enableNextSet && v.getId() != R.id.buttonNextSet) || isStart) {
            return;
        }

        switch (v.getId()) {
            case R.id.buttonStartEnd:
                if (weight < 10) {
                    Toast.makeText(this, R.string.warning_message_weight_min, Toast.LENGTH_SHORT).show();
                } else if (positionItem != -1) {
                    v.setVisibility(View.GONE);
                    isStart = true;
                    initListDropset();
                } else {
                    Toast.makeText(this, R.string.select_exercise, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonNextSet:
                sendData(new byte[]{3});
                finish();
                break;
            case R.id.btn_key_0:
                valueWeight(0);
                break;
            case R.id.btn_key_1:
                valueWeight(1);
                break;
            case R.id.btn_key_2:
                valueWeight(2);
                break;
            case R.id.btn_key_3:
                valueWeight(3);
                break;
            case R.id.btn_key_4:
                valueWeight(4);
                break;
            case R.id.btn_key_5:
                valueWeight(5);
                break;
            case R.id.btn_key_6:
                valueWeight(6);
                break;
            case R.id.btn_key_7:
                valueWeight(7);
                break;
            case R.id.btn_key_8:
                valueWeight(8);
                break;
            case R.id.btn_key_9:
                valueWeight(9);
                break;
            case R.id.btn_key_clear:
                if (weight > 0) {
                    weight = 0;
                    textViewLoadedWeight.setText(getString(R.string.title_loaded_weight) + " " + weight + lb);
                    adapterDropsetAndNegative.changeWeight(weight);
                }
                break;
        }

    }


    public void valueWeight(int num) {

        int weightAux;

        if (weight > 0) {
            weightAux = (weight * 10) + num;
        } else {
            if (num == 0) {
                return;
            }
            weightAux = num;
        }

        if (weightAux <= 720) {
            weight = weightAux;
            textViewLoadedWeight.setText(getString(R.string.title_loaded_weight) + " " + weight + lb);
            if (isListViewVisible) {
                adapterDropsetAndNegative.changeWeight(weight);
            } else {
                adapterDropsetAndNegative.changeWeightInvisible(weight);
                adapterDropsetAndNegative.notifyDataSetChanged();
                isListViewVisible = true;
            }
        } else {
            Toast.makeText(this, R.string.warning_message_weight, Toast.LENGTH_SHORT).show();
        }

    }


    private void initListDropset() {

        listViewNegative.setItemChecked(adapterDropsetAndNegative.getCount() - 1, true);

        sendData(new byte[]{0});
        int auxWeight = weight;
        while (auxWeight > 127) {
            sendData(new byte[]{127});
            auxWeight -= 127;
        }
        sendData(new byte[]{(byte) auxWeight});
        sendData(new byte[]{0});
    }


    private void sendData(byte[] data) {

        Intent intent = new Intent(ConstantsService.SEND_DATA_INTENT);
        intent.putExtra(ConstantsService.DATA_EXTRA, data);
        sendBroadcast(intent);

    }


    private void nextWeight(int weight) {
        if (adapterDropsetAndNegative.getCount() < 4) {
            adapterDropsetAndNegative.addItemDropset(weight);
            listViewNegative.setItemChecked(adapterDropsetAndNegative.getCount() - 1, true);
        }

    }

    private void incrementeRep() {

        if (listViewNegative.getLastVisiblePosition() == adapterDropsetAndNegative.getCount() - 1) {
            adapterDropsetAndNegative.incrementRepetitions();
        } else {
            adapterDropsetAndNegative.incrementRepetitionsInvisible();
        }

    }

    @Override
    public void onBackPressed() {

        if (isStart) {
            DialogExit dialogExit = new DialogExit();
            dialogExit.show(getFragmentManager(), null);
        } else {
            if (enableNextSet) {
                sendData(new byte[]{3});
            } else {
                sendData(new byte[]{6});
            }
            super.onBackPressed();
        }
    }

    @Override
    public void onListenerExit() {
        isStart = false;
        sendData(new byte[]{3});
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                if (isStart) {
                    DialogExit dialogExit = new DialogExit();
                    dialogExit.show(getFragmentManager(), null);
                } else {
                    if (enableNextSet) {
                        sendData(new byte[]{3});
                    } else {
                        sendData(new byte[]{6});
                    }
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isStart) {
            sendData(new byte[]{3});
        }
        unregisterReceiver(mReceiver);
    }
}
