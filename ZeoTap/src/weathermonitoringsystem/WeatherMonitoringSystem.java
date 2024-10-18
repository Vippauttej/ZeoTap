package weathermonitoringsystem;

public class WeatherMonitoringSystem {
    public static void main(String[] args) {
        WeatherService weatherService = new WeatherService();
        
        // Schedule to fetch data every 5 minutes
        while (true) {
            weatherService.fetchWeatherData();
            try {
                Thread.sleep(300000); // Sleep for 5 minutes
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

