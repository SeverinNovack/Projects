import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * <p>StationDataHandler class.</p>
 *
 * @author serwar
 * @version $Id: $Id
 */
public class StationDataHandler {

    private static StationDataHandler instance;
    private ArrayList<StationSensorInstance> sensors = new ArrayList<StationSensorInstance>();
    private ArrayList<StationSensorData> receivedData = new ArrayList<StationSensorData>();
    private String historyPath;

    private StationDataHandler() {
    }

    /**
     * <p>Getter for the field <code>instance</code>.</p>
     *
     * @return a {@link StationDataHandler} object.
     */
    public static synchronized StationDataHandler getInstance() {
        if (StationDataHandler.instance == null) {
            StationDataHandler.instance = new StationDataHandler();
        }
        return StationDataHandler.instance;
    }

    /**
     * <p>receiveSensorData.</p>
     *
     * @param ipAddress a {@link java.lang.String} object.
     * @param port a int.
     * @param data a {@link org.json.JSONObject} object.
     */
    public void receiveSensorData(JSONObject data) {

        boolean sensorFound = false;
        int sensorIndex = 0;

        for (int i = 0; i < this.sensors.size(); i++) {
            StationSensorInstance sensor = this.sensors.get(i);
            if (sensor.getSensorType().equals(data.getString("type"))) {
                sensorFound = true;
                sensorIndex = i;
                break;
            }
        }

        if (!sensorFound) {
            StationSensorInstance newSensor = new StationSensorInstance(data.getString("type"));
            this.sensors.add(newSensor);
            System.out.println(
                "New sensor of type " + newSensor.getHumanType() + " registered.");

            sensorIndex = this.sensors.size() - 1;
            switch (newSensor.getHumanType()) {
                case "Temperature": {
                    this.historyPath = "/Temperature-History.txt";
                    createHistoryFile();
                    break;
                }
                case "Windspeed": {
                    this.historyPath = "/Windspeed-History.txt";
                    createHistoryFile();
                    break;
                }
                case "Rain": {
                    this.historyPath = "/Rain-History.txt";
                    createHistoryFile();
                    break;
                }
                case "Humidity": {
                    this.historyPath = "/Humidity-History.txt";
                    createHistoryFile();
                    break;
                }
                case "WindDirection": {
                    this.historyPath = "/WindDirection-History.txt";
                    createHistoryFile();
                    break;
                }
                case "AtmosphericPressure": {
                    this.historyPath = "/AtmosphericPressure-History.txt";
                    createHistoryFile();
                    break;
                }
            }
        }

        StationSensorInstance currentSensor = this.sensors.get(sensorIndex);

        System.out.println("Data received from sensor #" + sensorIndex +
            "(" + currentSensor.getHumanType() + "): " + data.toString());
        updateHistory(currentSensor.getHumanType(), data.getFloat("value"));

        if (data.get("event").toString().equals("value")) {
            this.receivedData.add(new StationSensorData(currentSensor, data.getFloat("value")));
        }
    }

    private void updateHistory(String type, float data) {
        String directory = "./DataHistory";
        String hFilePath = directory + historyPath;
        Path filepath = Paths.get(directory, historyPath);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("value", data);

        try (FileWriter writer = new FileWriter(hFilePath, true)) {
            writer.write(jsonObject.toString());
            writer.write("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * <p>numberOfSensors.</p>
     *
     * @return a int.
     */
    public int numberOfSensors() {
        return this.sensors.size();
    }

    /**
     * <p>getLastValue.</p>
     *
     * @return an array of {@link float} objects.
     */
    public float[] getLastValue() {
        float[] values = new float[]{0, 0, 0, 0, 0, 0};
        values[0] = getLastValue("t");
        values[1] = getLastValue("h");
        values[2] = getLastValue("w");
        values[3] = getLastValue("r");
        values[4] = getLastValue("p");
        values[5] = getLastValue("d");
        return values;
    }

    /**
     * <p>debugPrint.</p>
     */
    public void debugPrint() {
        for (int i = this.receivedData.size() - 1; i >= 0; i--) {
            System.out.println("Element: " + receivedData.get(i).getSensorType());
        }
    }

    /**
     * <p>getLastValue.</p>
     *
     * @param type a {@link java.lang.String} object.
     * @return a float.
     */
    public float getLastValue(String type) {
        float value = -200;

        for (int i = this.receivedData.size() - 1; i >= 0; i--) {
            StationSensorData data = this.receivedData.get(i);
            if (data.getSensorType().equals(type)) {
                value = data.getValue();
                break;
            }
        }
        return value;
    }

    /**
     * <p>valueHistory.</p>
     *
     * @return a {@link org.json.JSONArray} object.
     */
    public JSONArray valueHistory() {
        JSONArray values = new JSONArray();
        String[] filenames = {"./DataHistory/Temperature-History.txt",
            "./DataHistory/Windspeed-History.txt", "./DataHistory/Rain-History.txt",
            "./DataHistory/Humidity-History.txt", "./DataHistory/WindDirection-History.txt", " ./DataHistory/AtmosphericPressure-History.txt"};
        for (int i = 0; i < filenames.length; i++) {
            try (Scanner scanner = new Scanner(new File(filenames[i]))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    JSONObject jsonObject = new JSONObject(line);
                    values.put(jsonObject);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    /**
     * <p>createHistoryFile.</p>
     */
    public void createHistoryFile() {
        String directory = "./DataHistory";
        File dFile = new File(directory);
        if (!dFile.isDirectory()) {
            dFile.mkdir();
        }
        String hFilePath = directory + historyPath;
        Path filepath = Paths.get(directory, historyPath);
        File hFile = new File(hFilePath);
        if (!hFile.isFile()) {
            try {
                hFile.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
