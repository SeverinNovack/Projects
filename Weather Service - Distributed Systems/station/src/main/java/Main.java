import static org.eclipse.paho.client.mqttv3.MqttClient.generateClientId;

import java.io.IOException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

/**
 * <p>
 * Main class.
 * </p>
 *
 * @author serwar
 * @version $Id: $Id
 */
public class Main {

    /**
     * <p>
     * main.
     * </p>
     *
     * @param args an array of {@link java.lang.String} objects.
     * @throws java.io.IOException if any.
     */
    public static void main(String[] args) throws IOException {

        /*
         * if (args.length == 0) { throw new
         * IOException("Please provide the name of the weather service you would like to connect to!"
         * ); } if (args.length == 1) { throw new
         * IOException("Please provide the name of the stations location!"); } String
         * location = args[1];
         */

        // String service = args[0];
        // String service = System.getenv("service");
        // String location = System.getenv("location");
        try {
            WeatherAPI weatherAPI = new WeatherAPI("S1", System.getenv("location"));
            Thread apiThread = new Thread(weatherAPI);
            apiThread.start();

            String serverURI = "tcp://" + "iot.eclipse.org" + ":" + "1883";
            MqttClient client = new MqttClient(serverURI, generateClientId());

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                }

                @Override
                public void messageArrived(String t, MqttMessage m) throws Exception {
                    StationDataHandler stationDataHandler = StationDataHandler.getInstance();
                    JSONObject jsonmsg = new JSONObject(new String(m.getPayload()));
                    stationDataHandler.receiveSensorData(jsonmsg);
                    // JSONObject jsonmsg = (JSONObject) JSONObject.stringToValue(message);
                    // System.out.println(jsonmsg.toString());
                    // stationDataHandler.receiveSensorData(jsonmsg);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken t) {
                }
            });

            client.connect();

            client.subscribe("VSSASNsensorValue");

            /*
             * StationUDPReceiver receiver = new StationUDPReceiver(); Thread updThread =
             * new Thread(receiver); updThread.start();
             */

            HTTPServer server = new HTTPServer(3030);
            Thread httpThread = new Thread(server);
            httpThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
