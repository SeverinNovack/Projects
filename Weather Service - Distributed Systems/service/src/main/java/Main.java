import java.io.IOException;

/**
 * <p>Main class.</p>
 *
 * @author serwar
 * @version $Id: $Id
 */
public class Main {

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     * @throws java.io.IOException if any.
     */
    public static void main(String [] args) throws IOException {
        //Thread thriftServer = new ThriftServer(args[0]);
        String port = System.getenv("port");
        Thread thriftServer = new ThriftServer(port);
        System.out.println("Weather Service Started: ");
        thriftServer.start();
    }
}
