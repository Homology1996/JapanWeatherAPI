package main.service;

import main.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class APIService {

    private static final Logger LOGGER = LogManager.getLogger(APIService.class);

    private APIService() {}

    /**
     * 起始日期 (yyyy-MM-dd)
     * */
    public static String startDate;

    /**
     * 結束日期 (yyyy-MM-dd)
     * */
    public static String endDate;

    /**
     * 經度
     * */
    public static double longitude;

    /**
     * 緯度
     * */
    public static double latitude;

    /**
     * 從網站取得資料
     * @param dataType 資料內容
     * @see APIService#startDate
     * @see APIService#endDate
     * @see APIService#longitude
     * @see APIService#latitude
     * */
    private static JSONObject getDataByAPI(String dataType) {
        try {
            String and = "&", method = "GET";
            String baseUrl = "https://api.open-meteo.com/v1/forecast?";
            String latitudeContent = "latitude=" + latitude;
            String longitudeContent = "longitude=" + longitude;
            String hourlyContent = "hourly=" + dataType;
            String startContent = "start_date=" + startDate;
            String endContent = "end_date=" + endDate;
            String url = baseUrl + latitudeContent + and + longitudeContent +
                    and + startContent + and + endContent + and + hourlyContent;
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            return new JSONObject(HttpRequestService.sendRequest(url, method, headers, new JSONObject()));
        } catch (Exception e) {
            LOGGER.error(String.format("getDataByAPI(%s, %s, %s, %s, %s)",
                    latitude, longitude, startDate, endDate, dataType), e);
            return new JSONObject();
        }
    }

    /**
     * 根據指定的資料內容取出JSONArray
     * @return [{dataType, time}, ...]
     * */
    private static JSONArray extractArray(JSONObject response, String dataType) {
        String timeString = "time";
        JSONObject hourly = response.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray(dataType);
        JSONArray time = hourly.getJSONArray(timeString);
        JSONArray result = new JSONArray();
        for (int i = 0; i < (data.length() + time.length()) / 2; i++) {
            JSONObject item = new JSONObject();
            item.put(dataType, data.get(i));
            item.put(timeString, time.get(i));
            result.put(item);
        }
        return result;
    }

    /**
     * @see APIService#getDataByAPI(String)
     * @see APIService#extractArray(JSONObject, String)
     * */
    private static JSONArray extractArrayByAPI(String dataType) {
        return extractArray(getDataByAPI(dataType), dataType);
    }

    /**
     * @see Constants#DATA_TYPE_TEMPERATURE
     * @see APIService#extractArrayByAPI(String)
     * */
    public static JSONArray getTemperature() {
        return extractArrayByAPI(Constants.DATA_TYPE_TEMPERATURE);
    }

    /**
     * @see Constants#DATA_TYPE_APPARENT_TEMPERATURE
     * @see APIService#extractArrayByAPI(String)
     * */
    public static JSONArray getApparentTemperature() {
        return extractArrayByAPI(Constants.DATA_TYPE_APPARENT_TEMPERATURE);
    }

    /**
     * @see Constants#DATA_TYPE_RELATIVE_HUMIDITY
     * @see APIService#extractArrayByAPI(String)
     * */
    public static JSONArray getRelativeHumidity() {
        return extractArrayByAPI(Constants.DATA_TYPE_RELATIVE_HUMIDITY);
    }

    /**
     * @see Constants#DATA_TYPE_WIND_SPEED
     * @see APIService#extractArrayByAPI(String)
     * */
    public static JSONArray getWindSpeed() {
        return extractArrayByAPI(Constants.DATA_TYPE_WIND_SPEED);
    }

    /**
     * @see Constants#DATA_TYPE_WIND_DIRECTION
     * @see APIService#extractArrayByAPI(String)
     * */
    public static JSONArray getWindDirection() {
        return extractArrayByAPI(Constants.DATA_TYPE_WIND_DIRECTION);
    }

    /**
     * @see Constants#DATA_TYPE_PRECIPITATION
     * @see APIService#extractArrayByAPI(String)
     * */
    public static JSONArray getPrecipitation() {
        return extractArrayByAPI(Constants.DATA_TYPE_PRECIPITATION);
    }

    /**
     * @see Constants#DATA_TYPE_PRECIPITATION_PROBABILITY
     * @see APIService#extractArrayByAPI(String)
     * */
    public static JSONArray getPrecipitationProbability() {
        return extractArrayByAPI(Constants.DATA_TYPE_PRECIPITATION_PROBABILITY);
    }

    /**
     * @see Constants#DATA_TYPE_RAIN
     * @see APIService#extractArrayByAPI(String)
     * */
    public static JSONArray getRain() {
        return extractArrayByAPI(Constants.DATA_TYPE_RAIN);
    }

    /**
     * @see Constants#DATA_TYPE_SHOWERS
     * @see APIService#extractArrayByAPI(String)
     * */
    public static JSONArray getShowers() {
        return extractArrayByAPI(Constants.DATA_TYPE_SHOWERS);
    }

    /**
     * 測試
     * */
    public static void main(String[] args) {
        startDate = "2024-11-01";
        endDate = "2024-11-02";
        longitude = Constants.LONGITUDE_TOKYO;
        latitude = Constants.LATITUDE_TOKYO;
        System.out.println(getTemperature());
        startDate = "2025-04-07";
        endDate = "2025-04-11";
        longitude = Constants.LONGITUDE_OSAKA;
        latitude = Constants.LATITUDE_OSAKA;
        System.out.println(getRain());
    }

}