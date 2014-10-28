package amigos.com.conexionarduino.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import amigos.com.conexionarduino.R;
import amigos.com.conexionarduino.util.ItemPositiveNegative;
import amigos.com.conexionarduino.util.PlaceWeightListener;

/**
 * Created by sati on 22/10/2014.
 */
public class AdapterNegativePositive extends BaseAdapter {


    private List<ItemPositiveNegative> itemPositiveNegatives;
    private LayoutInflater inflater;

    private String[] arrayWeights;
    private String weight;
    private String repetition;
    private String lb;
    private String placeWeight;

    private TextView textViewRepetition;
    private TextView textViewWeight;

    private boolean isClickable;

    private Context context;

    private int currentPosition;
    private int idTextViewWeight;

    private PlaceWeightListener placeWeightListener;

    public AdapterNegativePositive(Context context, PlaceWeightListener placeWeightListener) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.placeWeightListener = placeWeightListener;

        arrayWeights = context.getResources().getStringArray(R.array.weights);
        weight = " " + context.getString(R.string.weight);
        repetition = " " + context.getString(R.string.repetition);
        lb = " " + context.getString(R.string.lb);
        placeWeight = context.getString(R.string.place_weight);
        isClickable = true;

        currentPosition = 0;
        idTextViewWeight = R.id.textViewPositiveWeight;

        itemPositiveNegatives = new ArrayList<ItemPositiveNegative>();
        itemPositiveNegatives.add(new ItemPositiveNegative());
        itemPositiveNegatives.get(0).setWeightNegative(1);
        itemPositiveNegatives.add(new ItemPositiveNegative());
        itemPositiveNegatives.add(new ItemPositiveNegative());

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View container = convertView;
        ViewHolderDropset viewHolderDropset = null;

        if (container == null) {
            container = inflater.inflate(R.layout.item_excersise_posi_nega, parent, false);
            viewHolderDropset = new ViewHolderDropset(container, R.id.textViewNumWeight, R.id.textViewNegativeWeight,
                    R.id.textViewPositiveWeight, R.id.textViewNumRep);
            container.setTag(viewHolderDropset);
        } else {
            viewHolderDropset = (ViewHolderDropset) container.getTag();
        }

        viewHolderDropset.getTextViewNumWeight().setText(arrayWeights[position] + weight);

        if (itemPositiveNegatives.get(position).getWeightNegative() > -1) {
            viewHolderDropset.getTextViewNegativeWeight().setText(itemPositiveNegatives.get(position).getWeightNegative() + lb);
        } else {
            viewHolderDropset.getTextViewNegativeWeight().setText(placeWeight);
        }

        if (itemPositiveNegatives.get(position).getWeightPositive() > -1) {
            viewHolderDropset.getTextViewPositiveWeight().setText(itemPositiveNegatives.get(position).getWeightPositive() + lb);
        } else {
            viewHolderDropset.getTextViewPositiveWeight().setText(placeWeight);
        }


        viewHolderDropset.getTextViewNumRep().setText(itemPositiveNegatives.get(position).getRepetitionsCounts() + repetition);

        if (position == 0) {
            textViewWeight = viewHolderDropset.getTextViewNegativeWeight();
        }

        if (position == itemPositiveNegatives.size() - 1) {
            textViewRepetition = viewHolderDropset.getTextViewNumRep();
        }

        if (isClickable) {

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (v.getId()) {
                        case R.id.textViewNegativeWeight:

                            if (position == 0 && itemPositiveNegatives.get(position - 1).getWeightNegative() > -1) {
                                currentPosition = position;
                                idTextViewWeight = R.id.textViewNegativeWeight;
                                placeWeightListener.onDialogoPlaceWeight(itemPositiveNegatives.get(position - 1).getWeightNegative() - 1);
                            } else {
                                Toast.makeText(context, R.string.warning_message_weight_negative, Toast.LENGTH_SHORT).show();
                            }

                            break;
                        case R.id.textViewPositiveWeight:

                            if (itemPositiveNegatives.get(position).getWeightNegative() > -1) {
                                currentPosition = position;
                                idTextViewWeight = R.id.textViewPositiveWeight;
                                placeWeightListener.onDialogoPlaceWeight(itemPositiveNegatives.get(position - 1).getWeightNegative() / 2);

                            } else {
                                Toast.makeText(context, R.string.warning_message_weight_positive, Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }

                }
            };

            if (position != 0) {
                viewHolderDropset.getTextViewNegativeWeight().setOnClickListener(onClickListener);
            }

            viewHolderDropset.getTextViewPositiveWeight().setOnClickListener(onClickListener);
        } else {
            viewHolderDropset.getTextViewNegativeWeight().setOnClickListener(null);
            viewHolderDropset.getTextViewPositiveWeight().setOnClickListener(null);
        }


        return container;
    }


    public void incrementRepetitions() {
        itemPositiveNegatives.get(itemPositiveNegatives.size() - 1).incrementRepetitionsCounts();
        textViewRepetition.setText(itemPositiveNegatives.get(itemPositiveNegatives.size() - 1).getRepetitionsCounts() + repetition);
    }


    public void incrementRepetitionsInvisible() {
        itemPositiveNegatives.get(itemPositiveNegatives.size() - 1).incrementRepetitionsCounts();
    }

    public void changeWeightInitial(int weight) {
        itemPositiveNegatives.get(0).setWeightNegative(weight);
        textViewWeight.setText(itemPositiveNegatives.get(0).getWeightNegative() + lb);
    }

    public void changeWeightInvisibleInitial(int weight) {
        itemPositiveNegatives.get(0).setWeightNegative(weight);
    }


    public void setNewWeightInitial(int weight) {
        itemPositiveNegatives.get(0).setWeightNegative(weight);
        itemPositiveNegatives.get(0).setWeightPositive(-1);
        itemPositiveNegatives.get(1).setWeightNegative(-1);
        itemPositiveNegatives.get(1).setWeightPositive(-1);
        itemPositiveNegatives.get(2).setWeightNegative(-1);
        itemPositiveNegatives.get(2).setWeightPositive(-1);
        notifyDataSetChanged();
    }


    private void setWeight(int weight) {

        switch (idTextViewWeight) {
            case R.id.textViewNegativeWeight:
                itemPositiveNegatives.get(currentPosition).setWeightNegative(weight);
                break;
            case R.id.textViewPositiveWeight:
                itemPositiveNegatives.get(currentPosition).setWeightPositive(weight);
                break;
        }
        notifyDataSetChanged();
    }

    public void setClickable(boolean isClickable) {
        this.isClickable = isClickable;
    }

    @Override
    public int getCount() {
        return itemPositiveNegatives.size();
    }

    @Override
    public Object getItem(int position) {
        return itemPositiveNegatives.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemPositiveNegatives.indexOf(itemPositiveNegatives.get(position));
    }

    private static class ViewHolderDropset {

        private TextView textViewNumWeight;
        private TextView textViewNegativeWeight;
        private TextView textViewPositiveWeight;
        private TextView textViewNumRep;

        private ViewHolderDropset(View container, int numWeightContainerId, int weightNegativeContainerId, int weightPositiveContainerId, int numRepContainerId) {

            textViewNumWeight = (TextView) container.findViewById(numWeightContainerId);
            textViewNegativeWeight = (TextView) container.findViewById(weightNegativeContainerId);
            textViewPositiveWeight = (TextView) container.findViewById(weightPositiveContainerId);
            textViewNumRep = (TextView) container.findViewById(numRepContainerId);
        }


        public TextView getTextViewNumWeight() {
            return textViewNumWeight;
        }

        public TextView getTextViewNegativeWeight() {
            return textViewNegativeWeight;
        }

        public TextView getTextViewPositiveWeight() {
            return textViewPositiveWeight;
        }

        public TextView getTextViewNumRep() {
            return textViewNumRep;
        }
    }


}
