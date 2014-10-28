package amigos.com.conexionarduino.util;

/**
 * Created by sati on 23/10/2014.
 */
public class ItemPositiveNegative {

    private int weightNegative;
    private int weightPositive;
    private int repetitionsCounts;

    public ItemPositiveNegative() {
        weightNegative = -1;
        weightPositive = -1;
        repetitionsCounts = 0;
    }

    public int getWeightNegative() {
        return weightNegative;
    }

    public int getWeightPositive() {
        return weightPositive;
    }

    public void setWeightNegative(int weightNegative) {
        this.weightNegative = weightNegative;
    }

    public void setWeightPositive(int weightPositive) {
        this.weightPositive = weightPositive;
    }

    public void initilizerepetitionsCounts () {
        repetitionsCounts = 0;
    }


    public int getRepetitionsCounts() {
        return repetitionsCounts;
    }

    public void incrementRepetitionsCounts() {
        repetitionsCounts++;
    }
}
