import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.json.*;

/**
 * Sensor class.
 *
 * @author serwar
 * @version $Id: $Id
 */
public class Sensor {

  public String IP;
  public int port;
  public String sensorType;
  public UDPTransmitter udp;
  public MqttClient mqttClient;

  private float index = 0;

  /** run. */
  public void run() {

    Random randomGenerator = new Random();
    int seconds = 3; // rate
    switch (sensorType) {
      default:
        Runnable updater =
            new Runnable() {
              public void run() {
                float sensorValue = randomGenerator.nextInt(101) - 50;
                sendPackage(sensorValue);
              }
            };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(updater, 0, seconds, TimeUnit.SECONDS);
        break;
      case "r": // rain
        RainSensor rainSensor = new RainSensor();
        Runnable rainUpdater =
            new Runnable() {
              public void run() {
                double sensorValue = rainSensor.update();
                sendPackage(sensorValue);
              }
            };
        ScheduledExecutorService rainExecutor = Executors.newScheduledThreadPool(1);
        rainExecutor.scheduleAtFixedRate(rainUpdater, 0, seconds, TimeUnit.SECONDS);
        break;
      case "w": // windspeed
        WindSensor windSensor = new WindSensor();
        Runnable windUpdater =
            new Runnable() {
              public void run() {
                byte sensorValue = windSensor.update();
                sendPackage(sensorValue);
              }
            };
        ScheduledExecutorService windExecutor = Executors.newScheduledThreadPool(1);
        windExecutor.scheduleAtFixedRate(windUpdater, 0, seconds, TimeUnit.SECONDS);
        break;
      case "h": // humidity
        HumiditySensor humiditySensor = new HumiditySensor();
        Runnable humidityUpdater =
            new Runnable() {
              public void run() {
                byte sensorValue = humiditySensor.update();
                sendPackage(sensorValue);
              }
            };
        ScheduledExecutorService humidityExecutor = Executors.newScheduledThreadPool(1);
        humidityExecutor.scheduleAtFixedRate(humidityUpdater, 0, seconds, TimeUnit.SECONDS);
        break;
      case "t": // temperature
        TempSensor tempSensor = new TempSensor();
        Runnable tempUpdater =
            new Runnable() {
              public void run() {
                double sensorValue = tempSensor.update();
                sendPackage(sensorValue);
              }
            };
        ScheduledExecutorService tempExecutor = Executors.newScheduledThreadPool(1);
        tempExecutor.scheduleAtFixedRate(tempUpdater, 0, seconds, TimeUnit.SECONDS);
        break;
      case "p": // pressure
        AtmosphericPressureSensor atmosphericPressureSensor = new AtmosphericPressureSensor();
        Runnable pressUpdater =
            new Runnable() {
              public void run() {
                double sensorValue = atmosphericPressureSensor.update();
                sendPackage(sensorValue);
              }
            };
        ScheduledExecutorService pressExecutor = Executors.newScheduledThreadPool(1);
        pressExecutor.scheduleAtFixedRate(pressUpdater, 0, seconds, TimeUnit.SECONDS);
        break;
      case "d": // direction
        WindDirectionSensor windDirectionSensor = new WindDirectionSensor();
        Runnable dirUpdater =
            new Runnable() {
              public void run() {
                double sensorValue = windDirectionSensor.update();
                sendPackage(sensorValue);
              }
            };
        ScheduledExecutorService dirExecutor = Executors.newScheduledThreadPool(1);
        dirExecutor.scheduleAtFixedRate(dirUpdater, 0, seconds, TimeUnit.SECONDS);
        break;
    }
  }

  /**
   * sendPackage.
   *
   * @param sensorValue a float.
   */
  public void sendPackage(float sensorValue) {
    try {
      JSONObject messageObj = new JSONObject();
      messageObj.put("event", "value");
      messageObj.put("value", sensorValue);
      messageObj.put("type", sensorType);
      index++;
      // udp.sendMessage(messageObj);
      System.out.println(index + " " + sensorValue);
      mqttClient.publish("VSSASNsensorValue", messageObj.toString().getBytes(), 2, true);
    } catch (MqttPersistenceException e) {
      e.printStackTrace();
    } catch (MqttException e) {
      e.printStackTrace();
    }
  }

  /**
   * sendPackage.
   *
   * @param sensorValue a double.
   */
  public void sendPackage(double sensorValue) {
    try {
      JSONObject messageObj = new JSONObject();
      messageObj.put("event", "value");
      messageObj.put("value", sensorValue);
      messageObj.put("type", sensorType);
      index++;
      // udp.sendMessage(messageObj);
      System.out.println(index + " " + sensorValue);
      mqttClient.publish("VSSASNsensorValue", messageObj.toString().getBytes(), 2, true);
    } catch (MqttPersistenceException e) {
      e.printStackTrace();
    } catch (MqttException e) {
      e.printStackTrace();
    }
  }

  /**
   * sendPackage.
   *
   * @param sensorValue a short.
   */
  public void sendPackage(short sensorValue) {
    try {
      JSONObject messageObj = new JSONObject();
      messageObj.put("event", "value");
      messageObj.put("value", sensorValue);
      messageObj.put("type", sensorType);
      index++;
      // udp.sendMessage(messageObj);
      System.out.println(index + " " + sensorValue);
      mqttClient.publish("VSSASNsensorValue", messageObj.toString().getBytes(), 2, true);

    } catch (MqttPersistenceException e) {
      e.printStackTrace();
    } catch (MqttException e) {
      e.printStackTrace();
    }
  }

  /**
   * sendPackage.
   *
   * @param sensorValue a byte.
   */
  public void sendPackage(byte sensorValue) {
    try {
      JSONObject messageObj = new JSONObject();
      messageObj.put("event", "value");
      messageObj.put("value", sensorValue);
      messageObj.put("type", sensorType);
      index++;
      // udp.sendMessage(messageObj);
      System.out.println(index + " " + sensorValue);
      mqttClient.publish("VSSASNsensorValue", messageObj.toString().getBytes(), 2, true);
    } catch (MqttPersistenceException e) {
      e.printStackTrace();
    } catch (MqttException e) {
      e.printStackTrace();
    }
  }
}
