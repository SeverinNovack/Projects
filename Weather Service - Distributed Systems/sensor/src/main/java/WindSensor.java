import java.util.Random;

/**
 * WindSensor class.
 *
 * @author serwar
 * @version $Id: $Id
 */
public class WindSensor {

  private byte speed;

  /** Constructor for WindSensor. */
  public WindSensor() {
    Random r = new Random();
    speed = (byte) (r.nextFloat() * (133.0 - 0.0) + 0.0); // starting value
  }

  /**
   * update.
   *
   * @return a byte.
   */
  public byte update() {
    Random r = new Random();
    speed = (byte) (speed + (r.nextFloat() * (10.0 - (-10.0)) + (-10.0))); // value change
    if (speed < 0) { // check not too low
      speed = 0;
    }
    if (speed > 133) { // check not too high
      speed = (byte) 133.0;
    }
    return speed;
  }

  /**
   * Getter for the field <code>speed</code>.
   *
   * @return a byte.
   */
  public byte getSpeed() {
    return speed;
  }

  /**
   * Setter for the field <code>speed</code>.
   *
   * @param speed a byte.
   */
  public void setSpeed(byte speed) {
    this.speed = speed;
  }
}
