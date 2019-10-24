import java.util.Random;

/**
 * AtmosphericPressureSensor class.
 *
 * @author serwar
 * @version $Id: $Id
 */
public class AtmosphericPressureSensor {

  private short pressure;

  /** Constructor for AtmosphericPressureSensor. */
  public AtmosphericPressureSensor() {
    Random r = new Random();
    pressure = (short) (r.nextFloat() * (1100.0 - 800.0) + 800.0); // starting value
  }

  /**
   * update.
   *
   * @return a short.
   */
  public short update() {
    Random r = new Random();
    pressure = (short) (pressure + (r.nextFloat() * (50.0 - (-50.0)) + (-50.0))); // value change
    if (pressure < 800) { // check not too low
      pressure = 0;
    }
    if (pressure > 1100) { // check not too high
      pressure = 35;
    }
    return pressure;
  }

  /**
   * Getter for the field <code>pressure</code>.
   *
   * @return a short.
   */
  public short getPressure() {
    return pressure;
  }

  /**
   * Setter for the field <code>pressure</code>.
   *
   * @param pressure a short.
   */
  public void setPressure(short pressure) {
    this.pressure = pressure;
  }
}
