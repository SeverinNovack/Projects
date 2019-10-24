import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

/**
 * Main class.
 *
 * @author serwar
 * @version $Id: $Id
 */
public class Main {

  /**
   * main.
   *
   * @param args an array of {@link java.lang.String} objects.
   */
  public static void main(String[] args) {

    Sensor s = new Sensor();
    try {

      // s.IP = System.getenv("ip");
      // s.port = Integer.parseInt(System.getenv("port"));
      s.sensorType = System.getenv("sensortype");
      // s.IP = args[0];
      // s.port = Integer.parseInt(args[1]);
      // s.sensorType = args[2];

    } catch (ArrayIndexOutOfBoundsException e) {
      s.IP = "127.0.0.1";
      s.port = 6666;
      s.sensorType = "Generic Sensor";
      System.out.println("Sending to default address 127.0.0.1:51020 [Generic Sensor]");
    }

    System.out.println("Sensor started");

    /*try {
      s.udp = new UDPTransmitter(s.IP, s.port);
    } catch (IOException io) {
      System.out.println(io.getMessage());
    }*/

    String serverURI = "tcp://" + "iot.eclipse.org" + ":" + "1883";
    MqttClient client = null;
    try {
      client = new MqttClient(serverURI, MqttClient.generateClientId());
    } catch (MqttException e) {
      e.printStackTrace();
    }
    try {
      client.connect();
      s.mqttClient = client;
    } catch (MqttException e) {
      e.printStackTrace();
    }

    s.run();
  }
}
