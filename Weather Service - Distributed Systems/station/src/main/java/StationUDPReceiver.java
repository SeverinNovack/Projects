import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

/**
 * <p>StationUDPReceiver class.</p>
 *
 * @author serwar
 * @version $Id: $Id
 */
public class StationUDPReceiver implements Runnable {
    private MulticastSocket clientSocket;
    private byte[] buf = new byte[256];
    private DatagramPacket msgPacket;
    private String ip;
    private StationDataHandler dataHandler;

    /**
     * <p>Constructor for StationUDPReceiver.</p>
     *
     * @throws java.net.UnknownHostException if any.
     */
    public StationUDPReceiver() throws UnknownHostException {
        this.dataHandler = StationDataHandler.getInstance();
        init();
    }

    private void init() {
        try {
            this.ip = InetAddress.getLocalHost().getHostAddress();
            InetAddress address = InetAddress.getByName("224.0.0.1");
            this.clientSocket = new MulticastSocket(1111);
            clientSocket.joinGroup(address);

            this.msgPacket = new DatagramPacket(buf, buf.length);
            System.out.println("Setup finished");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void run() {
        try {
            while (true) {
                this.clientSocket.receive(msgPacket);

                String msg = new String(buf, 0, buf.length).replaceAll("\u0000.*", "");
                Arrays.fill(buf, (byte) 0);
                JSONObject message = new JSONObject(msg);
                InetAddress IPAddress = msgPacket.getAddress();
              //  this.dataHandler.receiveSensorData(IPAddress.getHostAddress(), msgPacket.getPort(), message);

                if (message.get("event").toString().equals("startup")) {
                    // Wait for 1 second as the sensor may not be able to start the UDP listener otherwise
                    TimeUnit.SECONDS.sleep(1);
                    byte[] reply = this.ip.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket sendPacket = new DatagramPacket(reply, reply.length, IPAddress, 1279);
                    clientSocket.send(sendPacket);
                }
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
