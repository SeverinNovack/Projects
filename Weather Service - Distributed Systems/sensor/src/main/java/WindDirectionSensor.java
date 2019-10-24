import java.util.Random;

/**
 * WindDirectionSensor class.
 *
 * @author serwar
 * @version $Id: $Id
 */
public class WindDirectionSensor {

  private short direction;

  /** Constructor for WindDirectionSensor. */
  public WindDirectionSensor() {
    Random r = new Random();
    direction = (short) (r.nextFloat() * (360.0 - 0.0) + 0.0); // starting value
  }

  /**
   * update.
   *
   * @return a short.
   */
  public short update() {
    Random r = new Random();
    direction = (short) (direction + (r.nextFloat() * (20.0 - (-20.0)) + (-20.0))); // value change
    if (direction < 0) { // check not too low
      direction = 0;
    }
    if (direction > 360) { // check not too high
      direction = 360;
    }
    return direction;
  }

  /**
   * Getter for the field <code>direction</code>.
   *
   * @return a short.
   */
  public short getDirection() {
    return direction;
  }

  /**
   * Setter for the field <code>direction</code>.
   *
   * @param direction a short.
   */
  public void setDirection(short direction) {
    this.direction = direction;
  }
}
