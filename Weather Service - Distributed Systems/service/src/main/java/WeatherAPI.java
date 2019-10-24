import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.json.JSONArray;
import org.json.JSONObject;

import weatherService.DateException;
import weatherService.Location;
import weatherService.LocationException;
import weatherService.ReportException;
import weatherService.SystemWarning;
import weatherService.UnknownUserException;
import weatherService.Weather;
import weatherService.WeatherReport;
import weatherService.WeatherWarning;

/**
 * <p>
 * WeatherAPI class.
 * </p>
 *
 * @author serwar
 * @version $Id: $Id
 */
public class WeatherAPI implements Weather.Iface {
    private ArrayList<TTransport> servers = new ArrayList<TTransport>();
    private ArrayList<String> registeredServers = new ArrayList<String>();
    private ArrayList<Location> locations = new ArrayList<Location>();
    private Map<Short, WeatherReport> reports = new TreeMap<>();
    private String serverName;
    private int serverID;
    private short logicalCounter;
    Random randomGenerator = new Random();
    private DBCollection collection;
    private int[] allServers = { 7676, 7677, 7678 };

    /**
     * <p>
     * Constructor for WeatherAPI.
     * </p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public WeatherAPI(String name, int serverID) {
        super();
        int tmp = randomGenerator.nextInt(10) + 1;
        this.logicalCounter = (short) tmp;
        this.serverID = serverID;
        this.serverName = name;

        MongoClient mongoClient = new MongoClient();
        DB database = mongoClient.getDB("VSDB");
        this.collection = database.getCollection("ReportsInServer" + serverID);

        Runnable windUpdater = new Runnable() {
            public void run() {
                findAllWeatherServices();
            }
        };
        ScheduledExecutorService windExecutor = Executors.newScheduledThreadPool(1);
        windExecutor.scheduleAtFixedRate(windUpdater, 0, 30, TimeUnit.SECONDS);
    }

    /** {@inheritDoc} */
    @Override
    public long login(Location location) throws LocationException, TException {
        for (int i = 0; i < this.locations.size(); i++) {
            if (location.getLocationID() == this.locations.get(i).getLocationID()) {
                throw new LocationException(location, "The location ID is already taken.");
            }
        }
        this.locations.add(location);
        return (long) this.locations.size();
    }

    /** {@inheritDoc} */
    @Override
    public boolean logout(long sessionToken) throws UnknownUserException, TException {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean sendWeatherReport(WeatherReport report, long sessionToken)
            throws UnknownUserException, ReportException, LocationException, DateException, TException {
        report.setTimestamp(getMaxTimestamp(report.getTimestamp()));
        this.saveDataDB(report);
        this.shareReportsWithOthers(report);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean sendWeatherReportToServer(WeatherReport report, String server) {
        System.out.println("Received a weather report from another Server ");
        report.setTimestamp(getMaxTimestamp(report.getTimestamp()));
        this.saveDataDB(report);
        return true;
    }

    private short getMaxTimestamp(short reportTimestamp) {
        if (reportTimestamp > this.logicalCounter) {
            this.logicalCounter = ++reportTimestamp;
            return this.logicalCounter;
        } else {
            return ++this.logicalCounter;
        }
    }

    private void saveDataDB(WeatherReport report) {
        try {
            BasicDBObject weatherReport = new BasicDBObject();
            weatherReport.append("latitude", report.location.getLatitude());
            weatherReport.append("longitude", report.location.getLongitude());
            weatherReport.append("temperature", report.temperature);
            weatherReport.append("humidity", report.humidity);
            weatherReport.append("windStrength", report.windStrength);
            weatherReport.append("rainfall", report.rainfall);
            weatherReport.append("atmosphericpressure", report.atmosphericpressure);
            weatherReport.append("windDirection", report.windDirection);
            weatherReport.append("dateTime", report.dateTime);
            weatherReport.append("timestamp", report.timestamp);
            this.collection.insert(weatherReport);
            System.out.println("Weather report has been saved in the databank.");
        } catch (Exception x) {
            x.printStackTrace();
            System.out.println(report);
        }
    }

    private void saveData(WeatherReport report) {

        System.out.println("Status at time: " + report.dateTime.toString() + " in city: " + report.location.getName()
                + " Temperature: " + report.temperature + " Wind-Speed: " + (int) report.windStrength + " " + " Rain: "
                + (int) report.rainfall + " " + " Humidity: " + (int) report.humidity + " Pressure: "
                + (int) report.atmosphericpressure + " Direction: " + (int) report.windDirection + " Timestamp: "
                + (int) report.getTimestamp());

        String currentWorkingDir = System.getProperty("user.dir");
        String filePath = currentWorkingDir + "/data/" + report.location.getName() + ".txt";

        File tempFile = new File(filePath);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", report.dateTime.toString());
        jsonObject.put("locationID", report.location.locationID);
        jsonObject.put("location", report.location.getName());
        jsonObject.put("locationLongitude", report.location.longitude);
        jsonObject.put("locationLatitude", report.location.latitude);
        jsonObject.put("locationDescription", report.location.description);
        jsonObject.put("temperature", report.temperature);
        jsonObject.put("wind-speed", report.windStrength);
        jsonObject.put("rain", report.rainfall);
        jsonObject.put("humidity", report.humidity);
        jsonObject.put("pressure", report.atmosphericpressure);
        jsonObject.put("direction", report.windDirection);
        jsonObject.put("timestamp", report.timestamp);
        boolean exists = tempFile.exists() && tempFile.isFile();
        if (!exists) {
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(jsonObject.toString());
                writer.write("\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try (FileWriter writer = new FileWriter(filePath, true)) {
                writer.write(jsonObject.toString());
                writer.write("\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    public void findAllWeatherServices() {
        if (registeredServers.size() == 3) {
            return;
        }
        for (int i = 1; i < 4; i++) {
            int otherPort = allServers[i - 1];
            String name = Integer.toString(otherPort);
            if (otherPort != this.serverID) {
                TTransport transport = new TSocket("localhost", otherPort);
                try {
                    List<DBObject> lastItem = collection.find().sort(new BasicDBObject("timestamp", -1)).limit(1)
                            .toArray();
                    int lastTimestamp = this.logicalCounter;
                    if (lastItem.size() > 0) {
                        lastTimestamp = (Integer) lastItem.get(0).get("timestamp");
                    }

                    transport.open();
                    TProtocol protocol = new TBinaryProtocol(transport);
                    Weather.Client client = new Weather.Client(protocol);
                    JSONArray missingItems = new JSONArray(
                            client.registerWeatherServer(this.serverName, lastTimestamp));
                    for (int b = 0; b < missingItems.length(); b++) {
                        JSONObject item = missingItems.getJSONObject(b);
                        BasicDBObject weatherReport = new BasicDBObject();
                        weatherReport.append("latitude", item.getDouble("latitude"));
                        weatherReport.append("longitude", item.getDouble("longitude"));
                        weatherReport.append("temperature", item.getDouble("temperature"));
                        weatherReport.append("humidity", (byte) item.getInt("humidity"));
                        weatherReport.append("windStrength", (byte) item.getInt("windStrength"));
                        weatherReport.append("rainfall", item.getDouble("rainfall"));
                        weatherReport.append("atmosphericpressure", (short) item.getInt("atmosphericpressure"));
                        weatherReport.append("windDirection", (short) item.getInt("windDirection"));
                        weatherReport.append("dateTime", item.getString("dateTime"));
                        weatherReport.append("timestamp", (short) item.getInt("timestamp"));
                        this.collection.insert(weatherReport);
                    }
                    transport.close();

                    boolean found = false;
                    for (int c = 0; c < this.registeredServers.size(); c++) {
                        if (this.registeredServers.get(c).equals(name)) {
                            found = true;
                        }
                    }
                    if (!found) {
                        this.servers.add(transport);
                        this.registeredServers.add(name);
                    } else {
                        System.out.println("The server " + name + " has already been registered.");
                    }

                    System.out.println("Received: " + missingItems.length() + " missing reports");
                } catch (TTransportException x) {
                    transport.close();
                    System.out.println("The server " + name + " is not online.");
                } catch (Exception x) {
                    transport.close();
                    x.printStackTrace();
                }
            }
        }
    }

    private void shareReportsWithOthers(WeatherReport report) {
        for (int i = 0; i < this.servers.size(); i++) {
            TTransport transport = this.servers.get(i);
            try {
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                Weather.Client client = new Weather.Client(protocol);
                client.sendWeatherReportToServer(report, this.serverName);
                transport.close();
            } catch (TTransportException x) {
                transport.close();
            } catch (Exception x) {
                transport.close();
                x.printStackTrace();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public String registerWeatherServer(String server, double timestamp) {
        short timeStmp = (short) timestamp;
        List<DBObject> response = collection.find(new BasicDBObject("timestamp", new BasicDBObject("$gt", timeStmp)))
                .toArray();

        boolean found = false;
        for (int i = 0; i < this.registeredServers.size(); i++) {
            if (this.registeredServers.get(i).equals(server)) {
                found = true;
            }
        }
        if (!found) {
            TTransport transport = new TSocket("localhost", Integer.parseInt(server));
            this.servers.add(transport);
            this.registeredServers.add(server);
            System.out.println(
                    "Registered server " + server + " and send " + response.size() + " missing weather reports.");
        } else {
            System.out.println("The server " + server + " has already been registered.");
        }

        return new JSONArray(response).toString();
    }

    /** {@inheritDoc} */
    @Override
    public WeatherReport receiveForecastFor(long userId, String time)
            throws UnknownUserException, DateException, TException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public WeatherWarning checkWeatherWarnings(long userId) throws UnknownUserException, TException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean sendWarning(SystemWarning systemWarning, long userId) throws UnknownUserException, TException {
        return false;
    }
}
