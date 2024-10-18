package weathermonitoringsystem;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WeatherService {
    private static final String API_KEY = "b8e877968166e601ae53ec23192555ac";

    public void fetchWeatherData() {
        Scanner scanner = new Scanner(System.in);
        List<String> cities = new ArrayList<>();

        System.out.println("Enter city names separated by commas (e.g., Delhi,Mumbai): ");
        String input = scanner.nextLine();
        String[] cityArray = input.split(",");

        for (String city : cityArray) {
            cities.add(city.trim());
        }

        for (String city : cities) {
            try {
                String urlString = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", city, API_KEY);
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    processWeatherData(response.toString(), city);
                } else {
                    System.out.println("Error fetching data for " + city + ": " + conn.getResponseCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        scanner.close();
    }

    private void processWeatherData(String json, String city) {
        JSONObject jsonObject = new JSONObject(json);
        double tempK = jsonObject.getJSONObject("main").getDouble("temp");
        double tempC = tempK - 273.15; // Convert to Celsius

        String dominantCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");

        WeatherSummary summary = new WeatherSummary(city, tempC, tempC, tempC, dominantCondition);
        saveWeatherSummary(summary);
    }

    private void saveWeatherSummary(WeatherSummary summary) {
        try (FileOutputStream fos = new FileOutputStream("weather_summaries.txt", true);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(summary);
            System.out.println("Saved: " + summary);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
