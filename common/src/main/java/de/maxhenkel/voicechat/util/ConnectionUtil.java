package de.maxhenkel.voicechat.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionUtil {
    public static String format(String namespace, String path) {
        return String.format("%s_%s", namespace, path);
    }

    public static String readText(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            StringBuffer content = new StringBuffer();

            while ((inputLine = reader.readLine()) != null) {
                content.append(inputLine);
            }

            reader.close();
            connection.disconnect();

            return content.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
