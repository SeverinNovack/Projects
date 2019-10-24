import java.sql.Timestamp;

/**
 * <p>StationSensorData class.</p>
 *
 * @author serwar
 * @version $Id: $Id
 */
public class StationSensorData {
    private StationSensorInstance sensor;
    private float value;
    private Timestamp time;
    private String historyFile;

    /**
     * <p>Constructor for StationSensorData.</p>
     *
     * @param sensor a {@link StationSensorInstance} object.
     * @param value a float.
     */
    public StationSensorData(StationSensorInstance sensor, float value) {
        this.sensor = sensor;
        this.value = value;
        this.time = new Timestamp(System.currentTimeMillis());
    }

    
    /**
     * <p>getSensorType.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSensorType() {
        return this.sensor.getSensorType();
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a float.
     */
    public float getValue() {
        return this.value;
    }

    /**
     * <p>Getter for the field <code>time</code>.</p>
     *
     * @return a {@link java.sql.Timestamp} object.
     */
    public Timestamp getTime() {
        return this.time;
    }


}
