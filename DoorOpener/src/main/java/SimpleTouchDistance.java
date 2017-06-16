import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;

/**
 * Distance Values from the Ultra sensor are stored in this class.
 *
 * @author david ziegler
 */
public class SimpleTouchDistance extends AbstractFilter {
    private float[] sample;

    public SimpleTouchDistance(SampleProvider source) {
        super(source);
        sample = new float[sampleSize];

    }

    /*
     * new Distance in Arrray
     */
    public float getDistance() {
        super.fetchSample(sample, 0);

        return sample[0];
    }

}