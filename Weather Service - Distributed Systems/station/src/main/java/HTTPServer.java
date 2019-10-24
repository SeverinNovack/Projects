import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * <p>HTTPServer class.</p>
 *
 * @author serwar
 * @version $Id: $Id
 */
public class HTTPServer implements Runnable {

    private ServerSocket socket;
    private StationDataHandler dataHandler;

    /**
     * <p>Constructor for HTTPServer.</p>
     *
     * @param port a int.
     * @throws java.io.IOException if any.
     */
    public HTTPServer(int port) throws IOException {
        this.dataHandler = StationDataHandler.getInstance();
        this.socket = new ServerSocket(port);
    }

    private String writeHTTPHeader(String status, String contentLength) {
        String header = "HTTP/1.1 " + status + "\r\n";
        header += "Content-Length: " + contentLength + "\r\n";
        header += "Content-Type: application/json\r\n\r\n";

        return header;
    }

    /** {@inheritDoc} */
    @Override
    public void run() {
        boolean error = false;
        while (!error) {
            try {
                Socket connection = this.socket.accept();
                BufferedReader input = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
                DataOutputStream output = new DataOutputStream(connection.getOutputStream());

                HTTPRequest httpRequest = new HTTPRequest(input);

                JSONObject content = new JSONObject();
                String body = "";
                int statusCode = 404;

                if (httpRequest.method.equals("GET")) {
                    float sensorValue;
                    switch (httpRequest.url) {
                        case "/":
                            statusCode = 200;
                            content.put("status", "ok");
                            content.put("currentNumberOfSensors", this.dataHandler.numberOfSensors());
                            break;
                        case "/data":
                            statusCode = 200;
                            float[] data = this.dataHandler.getLastValue();
                            content.put("temperature", data[0]);
                            content.put("humidity", data[1]);
                            content.put("wind-speed", data[2]);
                            content.put("rain", data[3]);
                            content.put("pressure", data[4]);
                            content.put("direction", data[5]);
                            break;
                        case "/data/history":
                            statusCode = 200;
                            content.put("values", this.dataHandler.valueHistory());
                            break;
                        case "/data/temperature":
                            sensorValue = this.dataHandler.getLastValue("t");
                            if (sensorValue > -200) {
                                statusCode = 200;
                                content.put("value", sensorValue);
                            }
                            else{
                                statusCode = 404;
                            }
                            break;
                        case "/data/humidity":
                            sensorValue = this.dataHandler.getLastValue("h");
                            if (sensorValue > -200) {
                                statusCode = 200;
                                content.put("value", sensorValue);
                            }
                            else{
                                statusCode = 404;
                            }
                            break;
                        case "/data/wind-speed":
                            sensorValue = this.dataHandler.getLastValue("w");
                            if (sensorValue > -200) {
                                statusCode = 200;
                                content.put("value", sensorValue);
                            }
                            else{
                                statusCode = 404;
                            }
                            break;
                        case "/data/rain":
                            sensorValue = this.dataHandler.getLastValue("r");
                            if (sensorValue > -200) {
                                statusCode = 200;
                                content.put("value", sensorValue);
                            }
                            else{
                                statusCode = 404;
                            }
                            break;
                        case "/data/pressure":
                            sensorValue = this.dataHandler.getLastValue("p");
                            if (sensorValue > -200) {
                                statusCode = 200;
                                content.put("value", sensorValue);
                            }
                            else{
                                statusCode = 404;
                            }
                            break;
                        case "data/direction":
                            sensorValue = this.dataHandler.getLastValue("d");
                            if (sensorValue > -200) {
                                statusCode = 200;
                                content.put("value", sensorValue);
                            }
                            else{
                                statusCode = 404;
                            }
                            break;
                    }
                }

                String status = "";
                switch (statusCode) {
                    case 200:
                        status = "200 Ok";
                        body = content.toString();
                        break;
                    case 404:
                        status = "404 Not Found";
                        break;
                    default:
                        status = "500 Internal Server Error";
                }

                String header = this.writeHTTPHeader(status, Integer.toString(body.length()));
                output.writeBytes(header.concat(body));
                connection.close();
            } catch (IOException e) {
                error = true;
                e.printStackTrace();
            }
        }
    }

}
