package amigos.com.conexionarduino.util;

/**
 * Created by sati on 28/10/2014.
 */
public interface PlaceWeightListener {
    public void onDialogoPlaceWeight(int minWeight, int maxWeight);
    public void onNewWight(int weight);

}
