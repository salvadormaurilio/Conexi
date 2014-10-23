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
public class AdapterExcersise extends BaseAdapter {


    private List<ItemDropset> itemDropsets;
    private LayoutInflater inflater;
    private String[] arrayWeights;
    private TextView textViewRepetition;
    private String weight;
    private String repetition;
    private String lb;

    public AdapterExcersise(int weitghtInitial, Context context) {

        itemDropsets = new ArrayList<ItemDropset>();
        itemDropsets.add(new ItemDropset(weitghtInitial));
        inflater = LayoutInflater.from(context);
        arrayWeights = context.getResources().getStringArray(R.array.weights);
        weight = " " + context.getString(R.string.weight);
        repetition = " " + context.getString(R.string.repetition);
        lb = " " + context.getString(R.string.lb);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View container = convertView;
        ViewHolderDropset viewHolderDropset;

        if (container == null) {
            container = inflater.inflate(R.layout.item_excersise_dropset, parent, false);
            viewHolderDropset = new ViewHolderDropset(container, R.id.textViewNumWeight, R.id.textViewWeight, R.id.textViewNumRep);
            container.setTag(viewHolderDropset);
        } else {
            viewHolderDropset = (ViewHolderDropset) container.getTag();
        }

        viewHolderDropset.getTextViewNumWeight().setText(arrayWeights[position] + weight);
        viewHolderDropset.getTextViewWeight().setText(itemDropsets.get(position).getWeight() + lb);
        viewHolderDropset.getTextViewNumRep().setText(itemDropsets.get(position).getRepetitionsCounts() + repetition);

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

    private class ViewHolderDropset {

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
