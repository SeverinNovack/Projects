import java.util.Random;

/**
 * TempSensor class.
 *
 * @author serwar
 * @version $Id: $Id
 */
public class TempSensor {

  double temp;

  /** Constructor for TempSensor. */
  public TempSensor() {
    Random r = new Random();
    temp = (double) (r.nextDouble() * (50.0 - (-50.0)) + (-50.0)); // starting value
  }

  /**
   * update.
   *
   * @return a double.
   */
  public double update() {
    Random r = new Random();
    temp = (double) (temp + (r.nextDouble() * (1.0 - (-1.0)) + (-1.0))); // value change
    if (temp > 50) { // check not too high
      temp = 50;
    }
    if (temp < -50) { // check not too low
      temp = -50;
    }
    return temp;
  }

  /**
   * Getter for the field <code>temp</code>.
   *
   * @return a double.
   */
  public double getTemp() {
    return temp;
  }

  /**
   * Setter for the field <code>temp</code>.
   *
   * @param temp a double.
   */
  public void setTemp(double temp) {
    this.temp = temp;
  }
}
