package amigos.com.conexionarduino;

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

import amigos.com.conexionarduino.adapters.AdapterExcersise;


public class DropsetActivity extends Activity implements AdapterView.OnItemClickListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private ListView listViewExcersise;
    private TextView textViewLoadedWeight;
    private Button buttonStartEnd;
    private boolean isStart;
    private int positionItem;
    private int progress;
    private String lb;

    private ListView listViewDropset;
    private AdapterExcersise adapterExcersise;

    private Button buttonNextWeight;
    private View buttonIncreRep;

    private int positionItemCurrent;
    private int progressCurrent;


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
        textViewLoadedWeight = (TextView) findViewById(R.id.textViewLoadedWeight);
        textViewLoadedWeight.setText(getString(R.string.title_loaded_weight) + " 1" + lb);

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBarLoadedWeight);
        seekBar.setOnSeekBarChangeListener(this);
        progress = 1;
        progressCurrent = 1;

        buttonStartEnd = (Button) findViewById(R.id.buttonStartEnd);
        buttonStartEnd.setOnClickListener(this);
        isStart = false;

        buttonNextWeight = (Button) findViewById(R.id.buttonNextWeight);
        buttonNextWeight.setOnClickListener(this);

        buttonIncreRep = findViewById(R.id.buttonIncreRep);
        buttonIncreRep.setOnClickListener(this);

        listViewDropset = (ListView) findViewById(R.id.listViewDropset);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        listViewExcersise.setItemChecked(position, true);
        positionItem = position;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonStartEnd:

                if (isStart) {
                    buttonStartEnd.setText(R.string.btn_title_start);
                    buttonNextWeight.setVisibility(View.GONE);
                    buttonIncreRep.setVisibility(View.GONE);
                    isStart = false;
                } else {
                    if (positionItem != -1) {
                        buttonStartEnd.setText(R.string.btn_title_exit);
                        isStart = true;
                        buttonNextWeight.setVisibility(View.VISIBLE);
                        buttonIncreRep.setVisibility(View.VISIBLE);

                        creteLisDropset();

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

    private void creteLisDropset() {

        if (adapterExcersise == null || adapterExcersise.getCount() == 10 || progressCurrent != progress || positionItemCurrent != positionItem) {
            positionItemCurrent = positionItem;
            progressCurrent = progress;
            adapterExcersise = new AdapterExcersise(progressCurrent, this);
            listViewDropset.setAdapter(adapterExcersise);
            listViewDropset.setItemChecked(adapterExcersise.getCount() - 1, true);
        }

    }

    public void nextWeight() {
        if (isStart && adapterExcersise.getCount() < 10) {
            double multiplo = (10 - adapterExcersise.getCount()) / 10.0;
            adapterExcersise.addItemDropset((int) (progressCurrent * multiplo));
            listViewDropset.setItemChecked(adapterExcersise.getCount() - 1, true);
        }

    }

    public void incrementeRep() {
        if (isStart) {
            if (listViewDropset.getLastVisiblePosition() == adapterExcersise.getCount() - 1) {
                adapterExcersise.incrementRepetitions();
            } else {
                adapterExcersise.incrementRepetitionsInvisible();
            }

        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textViewLoadedWeight.setText(getString(R.string.title_loaded_weight) + " " + (progress + 1) + lb);

        this.progress = progress + 1;
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
