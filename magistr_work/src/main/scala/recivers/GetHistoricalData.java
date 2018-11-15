package recivers;

import http.HttpClient;

public class GetHistoricalData {
    public static String getHistoricalFlightDataByFlightID(String flightId) {
        String url = "https://data-live.flightradar24.com/clickhandler/?version=1.5&flight=" + flightId;
        return HttpClient.getBody(url);
    }
}
