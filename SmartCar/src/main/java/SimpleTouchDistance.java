import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;

/**
 * Distance Values from the Ultra sensor are stored in this class.
 * 
 * @author david ziegler
 *
 */
public class SimpleTouchDistance extends AbstractFilter {
    private float[] sample;

    /**
     * Simpletouch distance is stored.
     * 
     * @param source source
     */
    public SimpleTouchDistance(SampleProvider source) {
        super(source);
        sample = new float[sampleSize];

    }

    /**
     * new Distance in Arrray.
     * 
     * @return sample
     */
    public float getDistance() {
        super.fetchSample(sample, 0);

        return sample[0];
    }

}
