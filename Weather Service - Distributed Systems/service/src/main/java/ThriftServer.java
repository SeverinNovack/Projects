import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import weatherService.Weather;

/**
 * <p>
 * ThriftServer class.
 * </p>
 *
 * @author serwar
 * @version $Id: $Id
 */
public class ThriftServer extends Thread {
    public static WeatherAPI handler;
    public static Weather.Processor processor;

    private static int port;

    /**
     * <p>
     * Constructor for ThriftServer.
     * </p>
     */
    public ThriftServer(String port) {
        this.port = Integer.parseInt(port);
    }

    /**
     * <p>
     * run.
     * </p>
     */
    public void run() {
        try {
            handler = new WeatherAPI(Integer.toString(port), port);
            processor = new Weather.Processor(handler);
            simple(processor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * simple.
     * </p>
     *
     * @param processor a {@link weatherService.Weather.Processor} object.
     */
    public static void simple(Weather.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(port);

            // Use this for a multithreaded server
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

            System.out.println("Starting thrift server on port: " + port);
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
