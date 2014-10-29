package amigos.com.conexionarduino.util;

/**
 * Created by sati on 23/10/2014.
 */
public class ItemDropset {

    private int weight;
    private int repetitionsCounts;

    public ItemDropset(int weight) {
        this.weight = weight;
        repetitionsCounts = 0;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getRepetitionsCounts() {
        return repetitionsCounts;
    }

    public void incrementRepetitionsCounts() {
        repetitionsCounts++;
    }

    public void initilizeRepetitionsCounts() {
        repetitionsCounts = 0;
    }

}
