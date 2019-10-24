import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.json.*;

/**
 * UDPTransmitter class.
 *
 * @author serwar
 * @version $Id: $Id
 */
@SuppressWarnings("ALL")
public class UDPTransmitter {

  private DatagramSocket socket;
  private InetAddress destinationIP;

  private int destinationPort;

  /**
   * Constructor for UDPTransmitter.
   *
   * @param destinationIP a {@link java.lang.String} object.
   * @param destinationPort a int.
   * @throws java.io.IOException if any.
   */
  public UDPTransmitter(String destinationIP, int destinationPort) throws IOException {
    this.destinationPort = destinationPort;
    this.destinationIP = InetAddress.getByName(destinationIP);
  }

  /**
   * sendMessage.
   *
   * @param jsonMessage a {@link org.json.JSONObject} object.
   * @throws java.io.IOException if any.
   */
  public void sendMessage(JSONObject jsonMessage) throws IOException {
    socket = new DatagramSocket();
    byte[] buffer;
    buffer = jsonMessage.toString().getBytes();
    DatagramPacket packet =
        new DatagramPacket(buffer, buffer.length, destinationIP, destinationPort);
    socket.send(packet);
    socket.close();
  }
}
