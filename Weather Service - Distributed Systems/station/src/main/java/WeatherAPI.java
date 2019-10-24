import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.json.JSONObject;

import weatherService.Location;
import weatherService.LocationException;
import weatherService.Weather;
import weatherService.WeatherReport;

/**
 * <p>
 * WeatherAPI class.
 * </p>
 *
 * @author serwar
 * @version $Id: $Id
 */
public class WeatherAPI implements Runnable {
    private long session = -1;
    private String service = "";
    private Location location;
    private String locationName;
    private Double latitude;
    private Double longitude;
    private TTransport transport;
    private Weather.Client client;
    private StationDataHandler dataHandler;
    private Byte locationId = 0;
    private Short logicalCounter = 0;
    private static int port = 7676;
    private int[] weatherServices = { 7676, 7677, 7678 };

    /**
     * <p>
     * Constructor for WeatherAPI.
     * </p>
     *
     * @param service      a {@link java.lang.String} object.
     * @param locationName a {@link java.lang.String} object.
     */
    public WeatherAPI(String service, String locationName) {
        this.getWeatherServicePort();
        this.service = service;
        this.locationName = locationName;
        this.dataHandler = StationDataHandler.getInstance();
        this.connectToServer();
    }

    private void connectToServer() {
        try {
            this.transport = new TSocket("localhost", port);
            this.transport.open();
            TProtocol protocol = new TBinaryProtocol(this.transport);
            this.client = new Weather.Client(protocol);

            getCoordinates(this.locationName);

            boolean registered = false;
            while (!registered) {
                registered = this.registerLocation(this.locationName, longitude, latitude);
            }

            this.transport.close();
        } catch (TTransportException x) {
            System.out.println("Connection to the Weather Station on port " + port + " could not be established.");
            this.getWeatherServicePort();
            System.out.println("Will connect to another Service");
            this.connectToServer();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private boolean registerLocation(String locationName, double longitude, double latitude) throws Exception {
        try {
            Location loc = new Location((byte) (longitude * latitude), locationName, longitude, latitude);
            this.location = loc;
            this.session = this.client.login(location);
            return true;
        } catch (LocationException x) {
            System.out.println("A location with the ID " + this.locationId + " has already been registered.");
            this.locationId++;
            return false;
        }
    }

    private void getWeatherServicePort() {
        Random randomGenerator = new Random();
        this.port = weatherServices[randomGenerator.nextInt(3) % 3];
    }

    private JSONObject getTimestamp() {
        try {
            String url = "http://worldclockapi.com/api/json/utc/now";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return new JSONObject(response.toString());
        } catch (Exception x) {
            x.printStackTrace();
            return new JSONObject();
        }
    }

    private WeatherReport getWeatherReport() {
        float[] data = this.dataHandler.getLastValue();
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i] > -200 ? data[i] : 0;
        }
        JSONObject timestamp = this.getTimestamp();
        WeatherReport weatherReport = new WeatherReport();
        weatherReport.setLocation(this.location);
        weatherReport.setTemperature((double) data[0]);
        weatherReport.setHumidity((byte) data[1]);
        weatherReport.setWindStrength((byte) data[2]);
        weatherReport.setRainfall((double) data[3]);
        weatherReport.setAtmosphericpressure((short) data[4]);
        weatherReport.setWindDirection((short) data[5]);
        weatherReport.setDateTime(timestamp.getString("currentDateTime"));
        weatherReport.setTimestamp(++logicalCounter);

        return weatherReport;
    }

    private void getCoordinates(String locationName) {
        if (locationName.equals("Berlin")) {
            latitude = 52.520008;
            longitude = 13.404954;
        } else if (locationName.equals("Hamburg")) {
            latitude = 53.551086;
            longitude = 9.993682;
        } else if (locationName.equals("Darmstadt")) {
            latitude = 49.878708;
            longitude = 8.646927;
        } else if (locationName.equals("Paris")) {
            latitude = 48.8566;
            longitude = 2.3522;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void run() {

        if (this.session == -1) {
            return;
        }

        WeatherAPI weatherStation = this;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    weatherStation.transport.open();
                    weatherStation.client.sendWeatherReport(getWeatherReport(), weatherStation.session);
                    weatherStation.transport.close();
                } catch (TTransportException x) {
                    weatherStation.transport.close();
                    System.out.println("The thrift server seems to be offline!");
                    connectToServer();
                } catch (TException x) {
                    weatherStation.transport.close();
                    x.printStackTrace();
                }
            }
        }, 0, 5000);
    }
}
