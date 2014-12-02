package amigos.com.conexionarduino.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import amigos.com.conexionarduino.R;
import amigos.com.conexionarduino.util.ItemDropset;

/**
 * Created by sati on 22/10/2014.
 */
public class AdapterDropsetAndNegative extends BaseAdapter {


    private List<ItemDropset> itemDropsets;
    private LayoutInflater inflater;

    private String[] arrayWeights;
    private String repetition;
    private String lb;

    private TextView textViewRepetition;
    private TextView textViewWeight;

    private int typeItem;

    public AdapterDropsetAndNegative(Context context, int typeItem) {

        itemDropsets = new ArrayList<ItemDropset>();
        itemDropsets.add(new ItemDropset(0));
        inflater = LayoutInflater.from(context);
        this.typeItem = typeItem;

        arrayWeights = context.getResources().getStringArray(R.array.weights);

        repetition = " " + context.getString(R.string.repetition);
        lb = " " + context.getString(R.string.lb);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View container = convertView;
        ViewHolderDropset viewHolderDropset;

        if (container == null) {
            if (typeItem == 1) {
                container = inflater.inflate(R.layout.item_excersise_dropset, parent, false);
            } else if (typeItem == 2) {
                container = inflater.inflate(R.layout.item_excersise_negative, parent, false);
            }

            viewHolderDropset = new ViewHolderDropset(container, R.id.textViewNumWeight, R.id.textViewWeight, R.id.textViewNumRep);

            container.setOnClickListener(null);
            container.setOnLongClickListener(null);

            container.setTag(viewHolderDropset);
        } else {
            viewHolderDropset = (ViewHolderDropset) container.getTag();
        }

        viewHolderDropset.getTextViewNumWeight().setText(arrayWeights[position]);
        viewHolderDropset.getTextViewWeight().setText(itemDropsets.get(position).getWeight() + lb);
        viewHolderDropset.getTextViewNumRep().setText(itemDropsets.get(position).getRepetitionsCounts() + repetition);

        if (position == 0) {
            textViewWeight = viewHolderDropset.getTextViewWeight();
        }

        if (position == itemDropsets.size() - 1) {
            textViewRepetition = viewHolderDropset.getTextViewNumRep();
        }

        return container;
    }


    public void addItemDropset(int weight) {
        itemDropsets.add(new ItemDropset(weight));
        notifyDataSetChanged();
    }

    public void incrementRepetitions() {
        itemDropsets.get(itemDropsets.size() - 1).incrementRepetitionsCounts();
        textViewRepetition.setText(itemDropsets.get(itemDropsets.size() - 1).getRepetitionsCounts() + repetition);
    }


    public void incrementRepetitionsInvisible(){
        itemDropsets.get(itemDropsets.size() - 1).incrementRepetitionsCounts();
    }

    public void changeWeight(int weight) {
        itemDropsets.get(0).setWeight(weight);
        textViewWeight.setText(itemDropsets.get(0).getWeight()+lb);


    }

    public void changeWeightInvisible(int weight) {
        itemDropsets.get(0).setWeight(weight);
    }

    @Override
    public int getCount() {
        return itemDropsets.size();
    }

    @Override
    public Object getItem(int position) {
        return itemDropsets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemDropsets.indexOf(itemDropsets.get(position));
    }

    private static class ViewHolderDropset {

        private TextView textViewNumWeight;
        private TextView textViewWeight;
        private TextView textViewNumRep;

        private ViewHolderDropset(View container, int numWeightContainerId, int weightContainerId, int numRepContainerId) {

            textViewNumWeight = (TextView) container.findViewById(numWeightContainerId);
            textViewWeight = (TextView) container.findViewById(weightContainerId);
            textViewNumRep = (TextView) container.findViewById(numRepContainerId);
        }

        public TextView getTextViewNumWeight() {
            return textViewNumWeight;
        }

        public TextView getTextViewWeight() {
            return textViewWeight;
        }

        public TextView getTextViewNumRep() {
            return textViewNumRep;
        }
    }


}
