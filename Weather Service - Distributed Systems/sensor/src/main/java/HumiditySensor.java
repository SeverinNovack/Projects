import java.util.Random;

/**
 * HumiditySensor class.
 *
 * @author serwar
 * @version $Id: $Id
 */
public class HumiditySensor {

  private byte humidity;

  /** Constructor for HumiditySensor. */
  public HumiditySensor() {
    Random r = new Random();
    setHumidity((byte) (r.nextFloat() * (100.0 - 5.0) + 5.0)); // starting value
  }

  /**
   * update.
   *
   * @return a byte.
   */
  public byte update() {
    Random r = new Random();
    humidity = (byte) (humidity + (r.nextFloat() * (5.0 - (-5.0)) + (-5.0))); // value change
    if (humidity > 100) {
      humidity = 100;
    }
    if (humidity < 5) {
      humidity = 5;
    }
    return humidity;
  }

  /**
   * Getter for the field <code>humidity</code>.
   *
   * @return a byte.
   */
  public byte getHumidity() {
    return humidity;
  }

  /**
   * Setter for the field <code>humidity</code>.
   *
   * @param humidity a byte.
   */
  public void setHumidity(byte humidity) {
    this.humidity = humidity;
  }
}
