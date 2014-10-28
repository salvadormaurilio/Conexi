package amigos.com.conexionarduino.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import amigos.com.conexionarduino.R;
import amigos.com.conexionarduino.adapters.AdapterNegativePositive;
import amigos.com.conexionarduino.dialogs.DialogSeekBarWeight;
import amigos.com.conexionarduino.util.PlaceWeightListener;


public class PosNegActivity extends Activity implements AdapterView.OnItemClickListener, SeekBar.OnSeekBarChangeListener,
        View.OnClickListener, PlaceWeightListener {

    private ListView listViewExcersise;
    private TextView textViewLoadedWeight;
    private Button buttonStartEnd;
    private boolean isStart;
    private int positionItem;
    private int progressWeight;
    private String lb;

    private ListView listViewNegaPosi;
    private AdapterNegativePositive adapterNegativePositive;

    private Button buttonNextWeight;
    private View buttonIncreRep;

    private int positionItemCurrentExcersise;
    private int progressWeightCurrent;

    private boolean isListViewVisible;

    private int positionItemCurrentTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_neg);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        listViewExcersise = (ListView) findViewById(R.id.listViewExcersise);
        listViewExcersise.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, getResources().getStringArray(R.array.excersises)));
        listViewExcersise.setOnItemClickListener(this);
        positionItem = -1;
        positionItemCurrentExcersise = -1;

        lb = " " + getString(R.string.lb);
        textViewLoadedWeight = (TextView) findViewById(R.id.textViewDialogLoadedWeight);
        textViewLoadedWeight.setText(getString(R.string.title_loaded_weight) + " 1" + lb);

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBarDialogLoadedWeight);
        seekBar.setOnSeekBarChangeListener(this);
        progressWeight = 1;
        progressWeightCurrent = 1;

        buttonStartEnd = (Button) findViewById(R.id.buttonStartEnd);
        buttonStartEnd.setOnClickListener(this);
        isStart = false;

        buttonNextWeight = (Button) findViewById(R.id.buttonNextWeight);
        buttonNextWeight.setOnClickListener(this);

        buttonIncreRep = findViewById(R.id.buttonIncreRep);
        buttonIncreRep.setOnClickListener(this);

        listViewNegaPosi = (ListView) findViewById(R.id.listViewTable);

        adapterNegativePositive = new AdapterNegativePositive(this, this);

        listViewNegaPosi.setAdapter(adapterNegativePositive);

        isListViewVisible = false;

        positionItemCurrentTable = 0;


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        listViewExcersise.setItemChecked(position, true);
        positionItem = position;

        if (!isStart && positionItemCurrentTable > 0 && positionItem != positionItemCurrentExcersise) {
            positionItemCurrentTable = 0;
            adapterNegativePositive.setNewWeightInitial(progressWeight);
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
                    adapterNegativePositive.setClickable(true);
                    listViewNegaPosi.setItemChecked(positionItemCurrentTable, false);
                    isStart = false;
                } else {
                    if (positionItem != -1) {

                        if (adapterNegativePositive.isFullTable()) {
                            buttonStartEnd.setText(R.string.btn_title_exit);
                            buttonNextWeight.setVisibility(View.VISIBLE);
                            buttonIncreRep.setVisibility(View.VISIBLE);
                            initListDropset();
                        } else {
                            Toast.makeText(this, R.string.warning_message_full_table, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, R.string.select_excersise, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.buttonNextWeight:
                nextWeight();
                break;
            case R.id.buttonIncreRep:
                incrementeRep();
                break;

        }

    }

    private void initListDropset() {


        if (positionItemCurrentTable == 2) {
            positionItemCurrentTable = 0;
        }

        if (progressWeightCurrent != progressWeight || positionItemCurrentExcersise != positionItem) {
            positionItemCurrentExcersise = positionItem;
            progressWeightCurrent = progressWeight;
            adapterNegativePositive.setNewWeightInitial(progressWeight);
            Toast.makeText(this, R.string.warning_message_full_table, Toast.LENGTH_SHORT).show();
        } else {
            adapterNegativePositive.setClickable(false);
            listViewNegaPosi.setItemChecked(positionItemCurrentTable, true);
            isStart = true;
        }

    }

    public void nextWeight() {
        if (isStart && positionItemCurrentTable < 3) {
            positionItemCurrentTable++;
            listViewNegaPosi.setItemChecked(positionItemCurrentTable, true);
        }

    }

    public void incrementeRep() {

        if (listViewNegaPosi.getLastVisiblePosition() == positionItemCurrentTable) {
            adapterNegativePositive.incrementRepetitions(positionItemCurrentTable);
        } else {
            adapterNegativePositive.incrementRepetitionsInvisible(positionItemCurrentTable);
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        textViewLoadedWeight.setText(getString(R.string.title_loaded_weight) + " " + (progress + 1) + lb);
        this.progressWeight = progress + 1;

        if (!isStart) {
            if (positionItemCurrentTable > 0 && !isStart) {
                positionItemCurrentTable = 0;
                adapterNegativePositive.setNewWeightInitial(progressWeight);
            } else if (listViewNegaPosi.getFirstVisiblePosition() == 0) {
                if (isListViewVisible) {
                    adapterNegativePositive.changeWeightInitial(progressWeight);
                } else {
                    adapterNegativePositive.changeWeightInvisibleInitial(progressWeight);
                    adapterNegativePositive.notifyDataSetChanged();
                    isListViewVisible = true;
                }
            } else {
                adapterNegativePositive.changeWeightInvisibleInitial(progressWeight);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
    public void onDialogoPlaceWeight(int minWeight, int maxWeight) {
        DialogSeekBarWeight dialogSeekBarWeight = DialogSeekBarWeight.newInstance(minWeight, maxWeight);
        dialogSeekBarWeight.show(getFragmentManager(), "");
    }

    @Override
    public void onNewWight(int weight) {
        adapterNegativePositive.setWeight(weight);
    }
}
