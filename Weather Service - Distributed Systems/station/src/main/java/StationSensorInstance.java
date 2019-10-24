/**
 * <p>StationSensorInstance class.</p>
 *
 * @author serwar
 * @version $Id: $Id
 */
public class StationSensorInstance {

    private String sensorType;
    private String sensorIP;

    /**
     * <p>Constructor for StationSensorInstance.</p>
     *
     * @param sensorType a {@link java.lang.String} object.
     * @param sensorIP a {@link java.lang.String} object.
     */
    public StationSensorInstance(String sensorType) {
        this.sensorType = sensorType;
    }

    /**
     * <p>Getter for the field <code>sensorIP</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSensorIP() {
        return this.sensorIP;
    }

    /**
     * <p>Getter for the field <code>sensorType</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSensorType() {
        return sensorType;
    }

    /**
     * <p>getHumanType.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getHumanType() {
        switch (this.sensorType) {
            case "t":
                return "Temperature";
            case "h":
                return "Humidity";
            case "w":
                return "Windspeed";
            case "r":
                return "Rain";
            case "d":
                return "WindDirection";
            case "p":
                return "AtmosphericPressure";
            default:
                return "ERROR Sensor sensorType is undefined";
        }
    }
}
