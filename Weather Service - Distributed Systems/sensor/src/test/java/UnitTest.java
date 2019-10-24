import org.junit.Test;

import static org.junit.Assert.*;

public class UnitTest {

  @Test
  public void rainSensorTest() {
    RainSensor rainSensor = new RainSensor();
    double val = rainSensor.update();
    assertTrue(val >= 0);
  }

  @Test
  public void tempSensorTest() {
    TempSensor tempSensor = new TempSensor();
    double val = tempSensor.update();
    assertTrue(val >= -50 && val <= 50);
  }

  @Test
  public void windSensorTest() {
    WindSensor windSensor = new WindSensor();
    byte val = windSensor.update();
    assertTrue(val >= 0 && val <= 133);
  }

  @Test
  public void humiditySensorTest() {
    HumiditySensor humiditySensor = new HumiditySensor();
    byte val = humiditySensor.update();
    assertTrue(val >= 5 && val <= 100);
  }

  @Test
  public void atmosphericPressureSensorTest() {
    AtmosphericPressureSensor atmosphericPressureSensor = new AtmosphericPressureSensor();
    short val = atmosphericPressureSensor.update();
    assertTrue(val >= 800 && val <= 1100);
  }

  @Test
  public void windDirectionSensorTest() {
    WindDirectionSensor windDirectionSensor = new WindDirectionSensor();
    short val = windDirectionSensor.update();
    assertTrue(val >= 0 && val <= 360);
  }
}
