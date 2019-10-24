import java.util.Random;

/**
 * RainSensor class.
 *
 * @author serwar
 * @version $Id: $Id
 */
public class RainSensor {

  private double amount;

  /** Constructor for RainSensor. */
  public RainSensor() {
    Random r = new Random();
    setAmount((double) (r.nextDouble() * (1000.0 - 0.0) + 1000.0)); // starting value
  }

  /**
   * update.
   *
   * @return a double.
   */
  public double update() {
    Random r = new Random();
    amount = (float) (amount + r.nextFloat() * (20 - (-20)) + (-20)); // value change
    if (amount < 0) { // check not too low
      amount = 0;
    }
    return amount;
  }

  /**
   * Getter for the field <code>amount</code>.
   *
   * @return a double.
   */
  public double getAmount() {
    return amount;
  }

  /**
   * Setter for the field <code>amount</code>.
   *
   * @param amount a double.
   */
  public void setAmount(double amount) {
    this.amount = amount;
  }
}
