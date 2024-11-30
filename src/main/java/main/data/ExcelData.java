package main.data;

import main.Constants;

import java.util.HashMap;
import java.util.Map;

public class ExcelData implements BasicDataUtility {

    private String time;

    private String temperature;

    private String apparentTemperature;

    private String relativeHumidity;

    private String windSpeed;

    private String windDirection;

    private String precipitation;

    private String precipitationProbability;

    private String rain;

    private String showers;

    public ExcelData(String time, String temperature,
                     String apparentTemperature, String relativeHumidity,
                     String windSpeed, String windDirection,
                     String precipitation, String precipitationProbability,
                     String rain, String showers) {
        this.setTime(time);
        this.setTemperature(temperature);
        this.setApparentTemperature(apparentTemperature);
        this.setRelativeHumidity(relativeHumidity);
        this.setWindSpeed(windSpeed);
        this.setWindDirection(windDirection);
        this.setPrecipitation(precipitation);
        this.setPrecipitationProbability(precipitationProbability);
        this.setRain(rain);
        this.setShowers(showers);
    }

    public ExcelData(Map<String, String> map) {
        this(map.get(Constants.DATA_TYPE_TIME), map.get(Constants.DATA_TYPE_TEMPERATURE),
                map.get(Constants.DATA_TYPE_APPARENT_TEMPERATURE), map.get(Constants.DATA_TYPE_RELATIVE_HUMIDITY),
                map.get(Constants.DATA_TYPE_WIND_SPEED), map.get(Constants.DATA_TYPE_WIND_DIRECTION),
                map.get(Constants.DATA_TYPE_PRECIPITATION), map.get(Constants.DATA_TYPE_PRECIPITATION_PROBABILITY),
                map.get(Constants.DATA_TYPE_RAIN), map.get(Constants.DATA_TYPE_SHOWERS));
    }

    public String getTime() {
        return BasicDataUtility.getNonNullContent(this.time);
    }

    public void setTime(String time) {
        this.time = BasicDataUtility.getNonNullContent(time);
    }

    public String getTemperature() {
        return BasicDataUtility.getNonNullContent(this.temperature);
    }

    public void setTemperature(String temperature) {
        this.temperature = BasicDataUtility.getNumericalValue(
                BasicDataUtility.getNonNullContent(temperature));
    }

    public String getApparentTemperature() {
        return BasicDataUtility.getNonNullContent(this.apparentTemperature);
    }

    public void setApparentTemperature(String apparentTemperature) {
        this.apparentTemperature = BasicDataUtility.getNumericalValue(
                BasicDataUtility.getNonNullContent(apparentTemperature));
    }

    public String getRelativeHumidity() {
        return BasicDataUtility.getNonNullContent(this.relativeHumidity);
    }

    public void setRelativeHumidity(String relativeHumidity) {
        this.relativeHumidity = BasicDataUtility.getNumericalValue(
                BasicDataUtility.getNonNullContent(relativeHumidity));
    }

    public String getWindSpeed() {
        return BasicDataUtility.getNonNullContent(this.windSpeed);
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = BasicDataUtility.getNumericalValue(
                BasicDataUtility.getNonNullContent(windSpeed));
    }

    public String getWindDirection() {
        return BasicDataUtility.getNonNullContent(this.windDirection);
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = BasicDataUtility.getNumericalValue(
                BasicDataUtility.getNonNullContent(windDirection));
    }

    public String getPrecipitation() {
        return BasicDataUtility.getNonNullContent(this.precipitation);
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = BasicDataUtility.getNumericalValue(
                BasicDataUtility.getNonNullContent(precipitation));
    }

    public String getPrecipitationProbability() {
        return BasicDataUtility.getNonNullContent(this.precipitationProbability);
    }

    public void setPrecipitationProbability(String precipitationProbability) {
        this.precipitationProbability = BasicDataUtility.getNumericalValue(
                BasicDataUtility.getNonNullContent(precipitationProbability));
    }

    public String getRain() {
        return BasicDataUtility.getNonNullContent(this.rain);
    }

    public void setRain(String rain) {
        this.rain = BasicDataUtility.getNumericalValue(
                BasicDataUtility.getNonNullContent(rain));
    }

    public String getShowers() {
        return BasicDataUtility.getNonNullContent(this.showers);
    }

    public void setShowers(String showers) {
        this.showers = BasicDataUtility.getNumericalValue(
                BasicDataUtility.getNonNullContent(showers));
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.DATA_TYPE_TIME, this.getTime());
        map.put(Constants.DATA_TYPE_TEMPERATURE, this.getTemperature());
        map.put(Constants.DATA_TYPE_APPARENT_TEMPERATURE, this.getApparentTemperature());
        map.put(Constants.DATA_TYPE_RELATIVE_HUMIDITY, this.getRelativeHumidity());
        map.put(Constants.DATA_TYPE_WIND_SPEED, this.getWindSpeed());
        map.put(Constants.DATA_TYPE_WIND_DIRECTION, this.getWindDirection());
        map.put(Constants.DATA_TYPE_PRECIPITATION, this.getPrecipitation());
        map.put(Constants.DATA_TYPE_PRECIPITATION_PROBABILITY, this.getPrecipitationProbability());
        map.put(Constants.DATA_TYPE_RAIN, this.getRain());
        map.put(Constants.DATA_TYPE_SHOWERS, this.getShowers());
        return map;
    }

    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

}