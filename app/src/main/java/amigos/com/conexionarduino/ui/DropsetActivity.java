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
import amigos.com.conexionarduino.adapters.AdapterDropsetAndNegative;


public class DropsetActivity extends Activity implements AdapterView.OnItemClickListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private ListView listViewExcersise;
    private TextView textViewLoadedWeight;
    private Button buttonStartEnd;
    private boolean isStart;
    private int positionItem;
    private int progressWeight;
    private String lb;

    private ListView listViewDropset;
    private AdapterDropsetAndNegative adapterDropsetAndNegative;

    private Button buttonNextWeight;
    private View buttonIncreRep;

    private int positionItemCurrent;
    private int progressWeightCurrent;

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
        positionItemCurrent = -1;

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

        listViewDropset = (ListView) findViewById(R.id.listViewTable);

        adapterDropsetAndNegative = new AdapterDropsetAndNegative(this, 1);
        listViewDropset.setAdapter(adapterDropsetAndNegative);

        isListViewVisible = false;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        listViewExcersise.setItemChecked(position, true);
        positionItem = position;

        if (!isStart && adapterDropsetAndNegative.getCount() > 1 && positionItem != positionItemCurrent) {
            adapterDropsetAndNegative.setNewWeight(progressWeight);
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
                    if (positionItem != -1) {
                        buttonStartEnd.setText(R.string.btn_title_exit);
                        buttonNextWeight.setVisibility(View.VISIBLE);
                        buttonIncreRep.setVisibility(View.VISIBLE);
                        initListDropset();
                        isStart = true;
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

        if (adapterDropsetAndNegative.getCount() == 10) {
            adapterDropsetAndNegative.setNewWeight(progressWeight);
        } else if (progressWeightCurrent != progressWeight || positionItemCurrent != positionItem) {
            positionItemCurrent = positionItem;
            progressWeightCurrent = progressWeight;
            adapterDropsetAndNegative.changeWeight(progressWeight);

        }
        listViewDropset.setItemChecked(adapterDropsetAndNegative.getCount() - 1, true);
    }

    public void nextWeight() {
        if (isStart && adapterDropsetAndNegative.getCount() < 10) {
            double multiplo = (10 - adapterDropsetAndNegative.getCount()) / 10.0;
            adapterDropsetAndNegative.addItemDropset((int) (progressWeightCurrent * multiplo));
            listViewDropset.setItemChecked(adapterDropsetAndNegative.getCount() - 1, true);
        }

    }

    public void incrementeRep() {

        if (listViewDropset.getLastVisiblePosition() == adapterDropsetAndNegative.getCount() - 1) {
            adapterDropsetAndNegative.incrementRepetitions();
        } else {
            adapterDropsetAndNegative.incrementRepetitionsInvisible();
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        textViewLoadedWeight.setText(getString(R.string.title_loaded_weight) + " " + (progress + 1) + lb);
        this.progressWeight = progress + 1;

        if (!isStart) {
            if (adapterDropsetAndNegative.getCount() > 1 && !isStart) {
                adapterDropsetAndNegative.setNewWeight(progressWeight);
            } else if (listViewDropset.getFirstVisiblePosition() == 0) {
                if (isListViewVisible) {
                    adapterDropsetAndNegative.changeWeight(progressWeight);
                } else {
                    adapterDropsetAndNegative.changeWeightInvisible(progressWeight);
                    adapterDropsetAndNegative.notifyDataSetChanged();
                    isListViewVisible = true;
                }
            } else {
                adapterDropsetAndNegative.changeWeightInvisible(progressWeight);
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

}
