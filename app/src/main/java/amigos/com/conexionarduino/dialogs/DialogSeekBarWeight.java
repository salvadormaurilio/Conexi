package amigos.com.conexionarduino.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import amigos.com.conexionarduino.R;
import amigos.com.conexionarduino.util.PlaceWeightListener;

/**
 * Created by sati on 28/10/2014.
 */
public class DialogSeekBarWeight extends DialogFragment {

    private static final String MIN_WEIGHT = "min_weight";
    private static final String MAX_WEIGHT = "max_weight";

    PlaceWeightListener placeWeightListener;


    public static DialogSeekBarWeight newInstance(int minWeight, int maxWight) {
        DialogSeekBarWeight dialogFragment = new DialogSeekBarWeight();
        Bundle bundle = new Bundle();
        bundle.putInt(MIN_WEIGHT, minWeight);
        bundle.putInt(MAX_WEIGHT, maxWight);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        placeWeightListener = (PlaceWeightListener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_weight, null);
        builder.setView(view);

        final String lb = getString(R.string.lb);
        final String loadedWeight = getString(R.string.title_loaded_weight);


        final int minWeight = getArguments().getInt(MIN_WEIGHT);
        final int maxWeight = getArguments().getInt(MAX_WEIGHT);

        final TextView textViewDialogLoadedWeight = (TextView) view.findViewById(R.id.textViewDialogLoadedWeight);
        textViewDialogLoadedWeight.setText(loadedWeight + " " +  minWeight + lb);


        TextView textViewDialogMinWeight = (TextView) view.findViewById(R.id.textViewDialogMinWeight);
        TextView textViewDialogMaxWeight = (TextView) view.findViewById(R.id.textViewDialogMaxWeight);
        textViewDialogMinWeight.setText(minWeight + "");
        textViewDialogMaxWeight.setText(maxWeight + "");

        final SeekBar seekBarDialogLoadedWeight = (SeekBar) view.findViewById(R.id.seekBarDialogLoadedWeight);

        seekBarDialogLoadedWeight.setMax(maxWeight - minWeight);



        seekBarDialogLoadedWeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewDialogLoadedWeight.setText(loadedWeight + " " + (progress + minWeight) + lb);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                placeWeightListener.onNewWight(seekBarDialogLoadedWeight.getProgress() + minWeight);

            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        placeWeightListener = null;
    }
}
