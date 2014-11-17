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
import amigos.com.conexionarduino.util.ConstantsService;


public class DropsetActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView listViewExcersise;
    private TextView textViewLoadedWeight;
    private Button buttonStartEnd;
    private boolean isStart;
    private int positionItem;
    private int weight;
    private String lb;

    private ListView listViewDropset;
    private AdapterDropsetAndNegative adapterDropsetAndNegative;

    private Button buttonNextWeight;
    private View buttonIncreRep;

    private boolean isListViewVisible;

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

        buttonStartEnd = (Button) findViewById(R.id.buttonStartEnd);
        buttonStartEnd.setOnClickListener(this);
        isStart = false;

        buttonNextWeight = (Button) findViewById(R.id.buttonNextWeight);
        buttonNextWeight.setOnClickListener(this);

        buttonIncreRep = findViewById(R.id.buttonIncreRep);
        buttonIncreRep.setOnClickListener(this);

        listViewDropset = (ListView) findViewById(R.id.listViewTable);

        adapterDropsetAndNegative = new AdapterDropsetAndNegative(this, 1);
        listViewDropset.setAdapter(adapterDropsetAndNegative);

        isListViewVisible = false;

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

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ConstantsService.DEBUG) Log.d(ConstantsService.TAG, "onReceive() " + action);
            if (ConstantsService.DATA_RECEIVED_INTENT.equals(action)) {
                final byte[] data = intent.getByteArrayExtra(ConstantsService.DATA_EXTRA);
            } else if (ConstantsService.USB_DEVICE_DETACHED.equals(action)) {
                Toast.makeText(context, getString(R.string.device_detaches), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    };


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (!isStart) {
            if ((adapterDropsetAndNegative.getCount() > 0 || adapterDropsetAndNegative.getRepetitionsCounts()) && positionItem != position) {
                adapterDropsetAndNegative.setNewWeight(weight);
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
                    buttonNextWeight.setVisibility(View.GONE);
                    buttonIncreRep.setVisibility(View.GONE);
                    listViewDropset.setItemChecked(adapterDropsetAndNegative.getCount() - 1, false);
                    isStart = false;
                } else {
                    if (weight == 0) {
                        Toast.makeText(this, R.string.warning_message_weight_min, Toast.LENGTH_SHORT).show();
                    } else if (positionItem != -1) {
                        buttonStartEnd.setText(R.string.btn_title_exit);
                        buttonNextWeight.setVisibility(View.VISIBLE);
                        buttonIncreRep.setVisibility(View.VISIBLE);
                        initListDropset();
                        isStart = true;
                    } else {
                        Toast.makeText(this, R.string.select_exercise, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.buttonNextWeight:
                nextWeight();
                break;
            case R.id.buttonIncreRep:
                incrementeRep();
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

                    if (adapterDropsetAndNegative.getCount() > 1) {
                        adapterDropsetAndNegative.setNewWeight(weight);
                    } else if (adapterDropsetAndNegative.getRepetitionsCounts()) {
                        adapterDropsetAndNegative.clearReptitionsCounts();
                    }
                    adapterDropsetAndNegative.changeWeight(weight);
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
                if (adapterDropsetAndNegative.getCount() > 1) {
                    adapterDropsetAndNegative.setNewWeight(weight);
                } else if (isListViewVisible) {
                    adapterDropsetAndNegative.changeWeight(weight);
                    if (adapterDropsetAndNegative.getRepetitionsCounts()) {
                        adapterDropsetAndNegative.clearReptitionsCounts();
                    }
                } else {
                    adapterDropsetAndNegative.changeWeightInvisible(weight);
                    adapterDropsetAndNegative.notifyDataSetChanged();
                    isListViewVisible = true;
                }
            } else {
                Toast.makeText(this, R.string.warning_message_weight, Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void initListDropset() {

        if (adapterDropsetAndNegative.getCount() == 10) {
            adapterDropsetAndNegative.setNewWeight(weight);
        }

        listViewDropset.setItemChecked(adapterDropsetAndNegative.getCount() - 1, true);
        isListViewVisible = true;
    }

    private void nextWeight() {
        if (isStart && adapterDropsetAndNegative.getCount() < 10) {
            double multiplo = (10 - adapterDropsetAndNegative.getCount()) / 10.0;
            adapterDropsetAndNegative.addItemDropset((int) (weight * multiplo));
            listViewDropset.setItemChecked(adapterDropsetAndNegative.getCount() - 1, true);
        }

    }

    private void incrementeRep() {

        if (listViewDropset.getLastVisiblePosition() == adapterDropsetAndNegative.getCount() - 1) {
            adapterDropsetAndNegative.incrementRepetitions();
        } else {
            adapterDropsetAndNegative.incrementRepetitionsInvisible();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

}
