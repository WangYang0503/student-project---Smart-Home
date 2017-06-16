import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;

/**
 * 
 * @author Sujanthan Class for isPressed method.
 */
public class SimpleTouch extends AbstractFilter {
    private float[] sample;

    public SimpleTouch(SampleProvider source) {
        super(source);
        sample = new float[sampleSize];
    }

    /*
     * Method for isPressed
     */
    public boolean isPressed() {
        super.fetchSample(sample, 0);
        if (sample[0] == 0)
            return false;
        return true;
    }

}
