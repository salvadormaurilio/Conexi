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
import amigos.com.conexionarduino.adapters.AdapterNegativePositive;
import amigos.com.conexionarduino.dialogs.DialogExit;
import amigos.com.conexionarduino.dialogs.DialogWeight;
import amigos.com.conexionarduino.util.ConstantsService;
import amigos.com.conexionarduino.util.PlaceWeightListener;


public class PosNegActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener, PlaceWeightListener, DialogExit.OnListenerExit {

    private ListView listViewExcersise;
    private TextView textViewLoadedWeight;
    private Button buttonStartEnd;
    private boolean isStart;
    private int positionItem;
    private int weight;
    private String lb;

    private ListView listViewNegaPosi;
    private AdapterNegativePositive adapterNegativePositive;

//    private Button buttonNextWeight;
//    private Button buttonIncreRep;

    private boolean isListViewVisible;

    private boolean wasStart;

    private boolean isStartExercise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_neg);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        listViewExcersise = (ListView) findViewById(R.id.listViewExcersise);
        listViewExcersise.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, getResources().getStringArray(R.array.excersises)));
        listViewExcersise.setOnItemClickListener(this);
        positionItem = -1;

        lb = " " + getString(R.string.lb);
        textViewLoadedWeight = (TextView) findViewById(R.id.textViewLoadedWeight);

        weight = 0;

        textViewLoadedWeight.setText(getString(R.string.title_loaded_weight) + " " + weight + lb);

        buttonStartEnd = (Button) findViewById(R.id.buttonStartEnd);
        buttonStartEnd.setOnClickListener(this);
        isStart = false;

//        buttonNextWeight = (Button) findViewById(R.id.buttonNextWeight);
//        buttonNextWeight.setOnClickListener(this);
//
//        buttonIncreRep = findViewById(R.id.buttonIncreRep);
//        buttonIncreRep.setOnClickListener(this);

        listViewNegaPosi = (ListView) findViewById(R.id.listViewTable);

        adapterNegativePositive = new AdapterNegativePositive(this, this);

        listViewNegaPosi.setAdapter(adapterNegativePositive);

        isListViewVisible = false;

        isStartExercise = false;

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

        wasStart = false;

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantsService.DATA_RECEIVED_INTENT);
        filter.addAction(ConstantsService.DATA_SENT_INTERNAL_INTENT);
        filter.addAction(ConstantsService.USB_DEVICE_DETACHED);
        registerReceiver(mReceiver, filter);

    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ConstantsService.DEBUG) Log.d(ConstantsService.TAG, "onReceive() " + action);
            if (ConstantsService.DATA_RECEIVED_INTENT.equals(action)) {

                final byte[] data = intent.getByteArrayExtra(ConstantsService.DATA_EXTRA);
                if (data[0] == 1) {
                    nextWeight();
                } else if (data[0] == 2) {
                    incrementeRep();
                }

            } else if (ConstantsService.USB_DEVICE_DETACHED.equals(action)) {
                finish();
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (!isStart) {
            if (wasStart && positionItem != position) {
                adapterNegativePositive.setNewWeightInitial(weight);
                wasStart = false;
                isStartExercise = false;
            }
            positionItem = position;
        } else {
            listViewExcersise.setItemChecked(positionItem, true);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonStartEnd:
                if (isStart) {
                    buttonStartEnd.setText(R.string.btn_title_start);
//                    buttonNextWeight.setVisibility(View.GONE);
//                    buttonIncreRep.setVisibility(View.GONE);
                    adapterNegativePositive.setClickable(true);
                    listViewNegaPosi.setItemChecked(adapterNegativePositive.getPositionItemPositiveNegatives(), false);
                    isStart = false;
                } else {
                    if (positionItem != -1) {
                        if (adapterNegativePositive.isFullTable()) {
                            buttonStartEnd.setText(R.string.btn_title_exit);
//                            buttonNextWeight.setVisibility(View.VISIBLE);
//                            buttonIncreRep.setVisibility(View.VISIBLE);
                            isStart = true;
                            initListDropset();
                        } else {
                            Toast.makeText(this, R.string.warning_message_full_table, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, R.string.select_exercise, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
//            case R.id.buttonNextWeight:
//                nextWeight();
//                break;
//            case R.id.buttonIncreRep:
//                incrementeRep();
//                break;


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
                    if (adapterNegativePositive.isValuesPlaced()) {
                        adapterNegativePositive.setNewWeightInitial(weight);
                        wasStart = false;
                    } else {
                        adapterNegativePositive.changeWeightInitial(weight);
                    }
                    isStartExercise = false;

                }
                break;

        }

    }


    public void valueWeight(int num) {

        if (!isStart) {
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
                if (adapterNegativePositive.isValuesPlaced()) {
                    adapterNegativePositive.setNewWeightInitial(weight);
                    wasStart = false;
                } else if (isListViewVisible) {
                    adapterNegativePositive.changeWeightInitial(weight);
                } else {
                    adapterNegativePositive.changeWeightInvisibleInitial(weight);
                    adapterNegativePositive.notifyDataSetChanged();
                    isListViewVisible = true;
                }
                isStartExercise = false;
            } else {
                Toast.makeText(this, R.string.warning_message_weight, Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void initListDropset() {

        if (adapterNegativePositive.getPositionItemPositiveNegatives() == 2) {
            adapterNegativePositive.clearItemPosition();
            adapterNegativePositive.clearRepetitionsCounts();
            isStart = false;
        }
        adapterNegativePositive.setClickable(false);
        listViewNegaPosi.setItemChecked(adapterNegativePositive.getPositionItemPositiveNegatives(), true);
        wasStart = true;

        if (!isStartExercise) {
            isStartExercise = true;
            sendData(new byte[]{0});
            sendData(new byte[]{(byte) positionItem});

            for (int i = 0; i < 3; i++) {
                sendWeight(adapterNegativePositive.getItemPositionWeight(i, 1));
            }

            for (int i = 0; i < 3; i++) {
                sendWeight(adapterNegativePositive.getItemPositionWeight(i, 2));
            }

        } else {
            sendData(new byte[]{1});
        }

    }

    private void sendWeight(int auxWeight)
    {
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

    public void nextWeight() {
        if (isStart && adapterNegativePositive.getPositionItemPositiveNegatives() < 2) {
            adapterNegativePositive.incrementItemPosition();
            listViewNegaPosi.setItemChecked(adapterNegativePositive.getPositionItemPositiveNegatives(), true);
        }

    }

    public void incrementeRep() {

        if (adapterNegativePositive.getPositionItemPositiveNegatives() >= listViewNegaPosi.getFirstVisiblePosition()
                && adapterNegativePositive.getPositionItemPositiveNegatives() <= listViewNegaPosi.getLastVisiblePosition()) {
            adapterNegativePositive.incrementRepetitions();
        } else {
            adapterNegativePositive.incrementRepetitionsInvisible();
        }

    }

    @Override
    public void onDialogoInputWeight(int minWeight, int maxWeight, boolean isNegative) {

        DialogWeight dialogWeight = DialogWeight.newInstance(minWeight, maxWeight, isNegative);
        dialogWeight.show(getFragmentManager(), "dia_wei");
    }

    @Override
    public void onNewWight(int weight) {
        adapterNegativePositive.setWeight(weight);
    }

    @Override
    public void onBackPressed() {

        if (isStart) {
            DialogExit dialogExit = new DialogExit();
            dialogExit.show(getFragmentManager(), null);
        } else {
            sendData(new byte[]{3});
            super.onBackPressed();
        }
    }

    @Override
    public void onListenerExit() {
        sendData(new byte[]{3});
        sendData(new byte[]{3});
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
