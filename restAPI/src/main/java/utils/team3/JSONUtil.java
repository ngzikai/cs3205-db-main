import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entity.steps.*;

/**
 * @author Yee
 *
 *         This is a class to convert from JSON to CSV and Meta.
 */

public class JSONUtil {
    private static final String JSON_EXT = ".json";
    public static String META_EXT = "_meta.json";
    public static String CSV_EXT = ".csv";

    private static String jsonStoredLocation = "/";
    private static String outputPath = "";

    public static String[] jsonToCSVAndMeta(String storedLocation, String fileName, String outputStoredLocation) {
        String[] fileNames = null;

        try {
            Steps data = JSONUtil.jsonToStepsData(storedLocation + fileName);
            fileNames = JSONUtil.stepsToCSVAndMeta(data, outputStoredLocation, fileName);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fileNames;
    }

    /**
     * @param filepath
     *            of the JSON file
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static Steps jsonToStepsData(String filepath) throws FileNotFoundException, UnsupportedEncodingException {
        Steps data = null;

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filepath));
            Gson gson = new GsonBuilder().create();
            data = gson.fromJson(reader, Steps.class);

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public static String[] stepsToCSVAndMeta(Steps data, String folderPath, String filename) {
        String[] fileNames = null;

        fileNames = new String[2];
        fileNames[0] = filename.replace(JSON_EXT, META_EXT);
        fileNames[1] = filename.replace(JSON_EXT, CSV_EXT);

        try {
            processCSV(data, folderPath, fileNames[1]);
            processMETA(data, folderPath, fileNames[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileNames;
    }

    private static String processMETA(Steps data) throws IOException {
        Steps_Time time = data.getTime();
        Steps_Channels channels = data.getChannels();

        time.setValues(null);

        for (int i = 0; i < channels.getData().size(); i++) {
            channels.getData().get(i).setValues(null);
        }

        data.setTime(time);
        data.setChannels(channels);

        Gson gson = new Gson();

        System.out.println(gson.toJson(data));
        return gson.toJson(data);
        // FileWriter writer = new FileWriter(fileFolder + fileName);
        // gson.toJson(data, writer);
        // writer.close();

    }

    private static void processCSV(Steps data, String fileFolder, String fileName) throws IOException {
        ArrayList<Steps_Channel> channels = data.getChannels().getData();

        ArrayList<Long> timeValues = data.getTime().getValues();
        ArrayList<ArrayList<Long>> channelsValues = new ArrayList<ArrayList<Long>>();

        for (int i = 0; i < timeValues.size(); i++) {
            long timestamp = timeValues.get(i) + data.getTimestamp();
            timeValues.set(i, timestamp);
        }

        for (Steps_Channel channel : channels) {
            channelsValues.add(channel.getValues());
        }

        FileWriter writer = new FileWriter(fileFolder + fileName);

        for (int j = 0; j < timeValues.size(); j++) {
            List<String> list = new ArrayList<String>();
            list.add(timeValues.get(j).toString());
            for (int k = 0; k < channelsValues.size(); k++) {
                list.add(channelsValues.get(k).get(j).toString());
            }
            CSVUtils.writeLine(writer, list);
        }

        writer.flush();
        writer.close();
    }

    public static void main(String[] args) {
        // specify the correct common output location and common stored location
        // like jsonStoredLocation, outputPath

        String[] files = JSONUtil.jsonToCSVAndMeta(jsonStoredLocation, "steps_1509392698908.json", outputPath);
        System.out.println(Arrays.toString(files));
    }
}
