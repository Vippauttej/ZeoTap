package weathermonitoringsystem;

import java.io.Serializable;

public class WeatherSummary implements Serializable {
    private String city;
    private double averageTemp;
    private double maxTemp;
    private double minTemp;
    private String dominantCondition;

    public WeatherSummary(String city, double averageTemp, double maxTemp, double minTemp, String dominantCondition) {
        this.city = city;
        this.averageTemp = averageTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.dominantCondition = dominantCondition;
    }

    @Override
    public String toString() {
        return String.format("City: %s, Avg Temp: %.2f°C, Max Temp: %.2f°C, Min Temp: %.2f°C, Condition: %s",
                city, averageTemp, maxTemp, minTemp, dominantCondition);
    }

    // Getters and setters omitted for brevity
}
