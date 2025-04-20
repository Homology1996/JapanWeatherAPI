package main.service;

import main.Constants;
import main.data.ExcelData;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelService {

    private static final Logger LOGGER = LogManager.getLogger(ExcelService.class);

    private ExcelService() {}

    public static JSONArray temperatureArray;

    public static JSONArray apparentTemperatureArray;

    public static JSONArray relativeHumidityArray;

    public static JSONArray windSpeedArray;

    public static JSONArray windDirectionArray;

    public static JSONArray precipitationArray;

    public static JSONArray precipitationProbabilityArray;

    public static JSONArray rainArray;

    public static JSONArray showersArray;

    public static List<Double> temperatureList;

    public static List<Double> apparentTemperatureList;

    public static List<Double> relativeHumidityList;

    public static List<Double> windSpeedList;

    public static List<Double> windDirectionList;

    public static List<Double> precipitationList;

    public static List<Double> precipitationProbabilityList;

    public static List<Double> rainList;

    public static List<Double> showersList;

    public static XSSFWorkbook workbook;

    /**
     * @param type 1: 字串, 2: 數字
     * */
    private static void putDataInMap(Map<String, String> map, JSONObject info, String dataType, int type) {
        if (type == 1) {
            map.put(dataType, info.getString(dataType));
        }
        if (type == 2) {
            map.put(dataType, Double.toString(info.getDouble(dataType)));
        }
    }

    /**
     * 產生報表資料
     * @see ExcelService#putDataInMap(Map, JSONObject, String, int)
     * @see APIService#getTemperature()
     * @see APIService#getApparentTemperature()
     * @see APIService#getRelativeHumidity()
     * @see APIService#getWindSpeed()
     * @see APIService#getWindDirection()
     * @see APIService#getPrecipitation()
     * @see APIService#getPrecipitationProbability()
     * @see APIService#getRain()
     * @see APIService#getShowers()
     * */
    public static List<ExcelData> generateExcelData() {
        if (temperatureArray != null && apparentTemperatureArray != null &&
                relativeHumidityArray != null && windSpeedArray != null && windDirectionArray != null &&
                precipitationArray != null && precipitationProbabilityArray != null &&
                rainArray != null && showersArray != null) {
            int length = (temperatureArray.length() + apparentTemperatureArray.length() +
                    relativeHumidityArray.length() + windSpeedArray.length() + windDirectionArray.length() +
                    precipitationArray.length() + precipitationProbabilityArray.length() +
                    rainArray.length() + showersArray.length()) / 9;
            List<ExcelData> result = new LinkedList<>();
            for (int i = 0; i < length; i++) {
                JSONObject temperatureInfo = temperatureArray.getJSONObject(i);
                JSONObject apparentTemperatureInfo = apparentTemperatureArray.getJSONObject(i);
                JSONObject relativeHumidityInfo = relativeHumidityArray.getJSONObject(i);
                JSONObject windSpeedInfo = windSpeedArray.getJSONObject(i);
                JSONObject windDirectionInfo = windDirectionArray.getJSONObject(i);
                JSONObject precipitationInfo = precipitationArray.getJSONObject(i);
                JSONObject precipitationProbabilityInfo = precipitationProbabilityArray.getJSONObject(i);
                JSONObject rainInfo = rainArray.getJSONObject(i);
                JSONObject showersInfo = showersArray.getJSONObject(i);
                Map<String, String> map = new HashMap<>();
                putDataInMap(map, temperatureInfo, Constants.DATA_TYPE_TIME, 1);
                putDataInMap(map, temperatureInfo, Constants.DATA_TYPE_TEMPERATURE, 2);
                putDataInMap(map, apparentTemperatureInfo, Constants.DATA_TYPE_APPARENT_TEMPERATURE, 2);
                putDataInMap(map, relativeHumidityInfo, Constants.DATA_TYPE_RELATIVE_HUMIDITY, 2);
                putDataInMap(map, windSpeedInfo, Constants.DATA_TYPE_WIND_SPEED, 2);
                putDataInMap(map, windDirectionInfo, Constants.DATA_TYPE_WIND_DIRECTION, 2);
                putDataInMap(map, precipitationInfo, Constants.DATA_TYPE_PRECIPITATION, 2);
                putDataInMap(map, precipitationProbabilityInfo, Constants.DATA_TYPE_PRECIPITATION_PROBABILITY, 2);
                putDataInMap(map, rainInfo, Constants.DATA_TYPE_RAIN, 2);
                putDataInMap(map, showersInfo, Constants.DATA_TYPE_SHOWERS, 2);
                result.add(new ExcelData(map));
            }
            return result;
        } else {
            LOGGER.error("The data is null");
            return new ArrayList<>();
        }
    }

    /**
     * 儲存報表資料
     * @see ExcelService#generateExcelData()
     * */
    public static void setDataList(List<ExcelData> excelDataList) {
        temperatureList = new LinkedList<>();
        apparentTemperatureList = new LinkedList<>();
        relativeHumidityList = new LinkedList<>();
        windSpeedList = new LinkedList<>();
        windDirectionList = new LinkedList<>();
        precipitationList = new LinkedList<>();
        precipitationProbabilityList = new LinkedList<>();
        rainList = new LinkedList<>();
        showersList = new LinkedList<>();
        for (ExcelData data : excelDataList) {
            temperatureList.add(Double.parseDouble(data.getTemperature()));
            apparentTemperatureList.add(Double.parseDouble(data.getApparentTemperature()));
            relativeHumidityList.add(Double.parseDouble(data.getRelativeHumidity()));
            windSpeedList.add(Double.parseDouble(data.getWindSpeed()));
            windDirectionList.add(Double.parseDouble(data.getWindDirection()));
            precipitationList.add(Double.parseDouble(data.getPrecipitation()));
            precipitationProbabilityList.add(Double.parseDouble(data.getPrecipitationProbability()));
            rainList.add(Double.parseDouble(data.getRain()));
            showersList.add(Double.parseDouble(data.getShowers()));
        }
    }

    /**
     * 計算標準差
     * */
    private static double calculateStandardDeviation(List<Double> numbers) {
        double sum = 0.0;
        for (Double number : numbers) {
            sum += number;
        }
        int length = numbers.size();
        double mean = sum / length;
        double standardDeviation = 0.0;
        for (Double number : numbers) {
            standardDeviation += Math.pow(number - mean, 2);
        }
        return Math.sqrt(standardDeviation / length);
    }

    /**
     * 取得最小值、最大值、平均值、標準差
     * @see ExcelService#calculateStandardDeviation(List)
     * */
    private static Map<String, Double> getStatistics(List<Double> numbers) {
        Map<String, Double> result = new HashMap<>();
        List<Double> sortedNumbers = numbers.stream().sorted().collect(Collectors.toList());
        result.put(Constants.NUMBER_MIN, sortedNumbers.get(0));
        result.put(Constants.NUMBER_MAX, sortedNumbers.get(sortedNumbers.size() - 1));
        double sum = 0;
        for (Double number : sortedNumbers) {
            sum += number;
        }
        result.put(Constants.NUMBER_AVERAGE, sum / sortedNumbers.size());
        result.put(Constants.NUMBER_STANDARD_DEVIATION, calculateStandardDeviation(numbers));
        return result;
    }

    /**
     * 取得儲存格
     * */
    private static Cell getCell(Sheet sheet, int rowIndex, int columnIndex) {
        if (sheet.getRow(rowIndex) == null) {
            sheet.createRow(rowIndex);
            sheet.getRow(rowIndex).createCell(columnIndex);
        } else {
            if (sheet.getRow(rowIndex).getCell(columnIndex) == null) {
                sheet.getRow(rowIndex).createCell(columnIndex);
            }
        }
        return sheet.getRow(rowIndex).getCell(columnIndex);
    }

    /**
     * 設定Excel裡面數值欄位的顯示格式
     * */
    private static void setNumericCellValue(Sheet sheet, int rowIndex, int columnIndex, String content) {
        Workbook workbook = sheet.getWorkbook();
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        double expense = -48763.1016, percentage = -1016.48763;
        try {
            String percentSign = "%";
            if (content.endsWith(percentSign)) {
                percentage = Double.parseDouble(content.substring(0, content.length() - percentSign.length()));
                percentage /= 100;
            } else {
                expense = Double.parseDouble(content);
            }
        } catch (Exception ignore) {}
        if (percentage != -1016.48763) {
            style.setDataFormat(format.getFormat("0%"));
            getCell(sheet, rowIndex, columnIndex).setCellStyle(style);
            getCell(sheet, rowIndex, columnIndex).setCellValue(percentage);
        } else if (expense != -48763.1016) {
            int tryParseInt = Integer.MIN_VALUE;
            try {
                tryParseInt = Integer.parseInt(content);
            } catch (Exception ignore) {}
            if (tryParseInt != Integer.MIN_VALUE) {
                style.setDataFormat(format.getFormat(Constants.EXCEL_NUMERIC_CELL_DATA_FORMAT_INTEGER));
                getCell(sheet, rowIndex, columnIndex).setCellStyle(style);
                getCell(sheet, rowIndex, columnIndex).setCellValue(tryParseInt);
            } else {
                style.setDataFormat(format.getFormat(Constants.EXCEL_NUMERIC_CELL_DATA_FORMAT));
                getCell(sheet, rowIndex, columnIndex).setCellStyle(style);
                getCell(sheet, rowIndex, columnIndex).setCellValue(expense);
            }
        } else {
            getCell(sheet, rowIndex, columnIndex).setCellValue(content);
        }
    }

    /**
     * 設定報表指定列數的資料內容
     * @see ExcelService#getCell(Sheet, int, int)
     * @see ExcelService#setNumericCellValue(Sheet, int, int, String)
     * */
    private static void setSheetContentForRow(Sheet sheet, int rowIndex, String time,
                                              String temperature, String apparentTemperature,
                                              String relativeHumidity, String windSpeed,
                                              String windDirection, String precipitation,
                                              String precipitationProbability, String rain, String showers) {
        sheet.setColumnWidth(Constants.EXCEL_COLUMN_TIME, Constants.EXCEL_WORD_WIDTH * 14);
        sheet.setColumnWidth(Constants.EXCEL_COLUMN_TEMPERATURE, Constants.EXCEL_WORD_WIDTH * 14);
        sheet.setColumnWidth(Constants.EXCEL_COLUMN_APPARENT_TEMPERATURE, Constants.EXCEL_WORD_WIDTH * 18);
        sheet.setColumnWidth(Constants.EXCEL_COLUMN_RELATIVE_HUMIDITY, Constants.EXCEL_WORD_WIDTH * 16);
        sheet.setColumnWidth(Constants.EXCEL_COLUMN_WIND_SPEED, Constants.EXCEL_WORD_WIDTH * 14);
        sheet.setColumnWidth(Constants.EXCEL_COLUMN_WIND_DIRECTION, Constants.EXCEL_WORD_WIDTH * 15);
        sheet.setColumnWidth(Constants.EXCEL_COLUMN_PRECIPITATION, Constants.EXCEL_WORD_WIDTH * 14);
        sheet.setColumnWidth(Constants.EXCEL_COLUMN_PRECIPITATION_PROBABILITY, Constants.EXCEL_WORD_WIDTH * 18);
        sheet.setColumnWidth(Constants.EXCEL_COLUMN_RAIN, Constants.EXCEL_WORD_WIDTH * 8);
        sheet.setColumnWidth(Constants.EXCEL_COLUMN_SHOWERS, Constants.EXCEL_WORD_WIDTH * 8);
        getCell(sheet, rowIndex, Constants.EXCEL_COLUMN_TIME).setCellValue(time);
        setNumericCellValue(sheet, rowIndex, Constants.EXCEL_COLUMN_TEMPERATURE, temperature);
        setNumericCellValue(sheet, rowIndex, Constants.EXCEL_COLUMN_APPARENT_TEMPERATURE, apparentTemperature);
        setNumericCellValue(sheet, rowIndex, Constants.EXCEL_COLUMN_RELATIVE_HUMIDITY, relativeHumidity);
        setNumericCellValue(sheet, rowIndex, Constants.EXCEL_COLUMN_WIND_SPEED, windSpeed);
        setNumericCellValue(sheet, rowIndex, Constants.EXCEL_COLUMN_WIND_DIRECTION, windDirection);
        setNumericCellValue(sheet, rowIndex, Constants.EXCEL_COLUMN_PRECIPITATION, precipitation);
        setNumericCellValue(sheet, rowIndex, Constants.EXCEL_COLUMN_PRECIPITATION_PROBABILITY, precipitationProbability);
        setNumericCellValue(sheet, rowIndex, Constants.EXCEL_COLUMN_RAIN, rain);
        setNumericCellValue(sheet, rowIndex, Constants.EXCEL_COLUMN_SHOWERS, showers);
    }

    /**
     * 產生報表
     * @see ExcelService#generateExcelData()
     * @see ExcelService#getStatistics(List)
     * @see ExcelService#setSheetContentForRow(Sheet, int, String, String, String, String, String, String, String, String, String, String)
     * */
    public static XSSFWorkbook generateReport(List<ExcelData> data) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet();
        workbook.createSheet();
        int summarySheetIndex = 0, detailsSheetIndex = 1;
        workbook.setSheetName(summarySheetIndex, "Summary");
        workbook.setSheetName(detailsSheetIndex, "Details");
        Sheet summarySheet = workbook.getSheetAt(summarySheetIndex);
        summarySheet.setColumnWidth(Constants.EXCEL_COLUMN_TIME, Constants.EXCEL_WORD_WIDTH * 14);
        summarySheet.setColumnWidth(Constants.EXCEL_COLUMN_TEMPERATURE, Constants.EXCEL_WORD_WIDTH * 14);
        summarySheet.setColumnWidth(Constants.EXCEL_COLUMN_APPARENT_TEMPERATURE, Constants.EXCEL_WORD_WIDTH * 18);
        summarySheet.setColumnWidth(Constants.EXCEL_COLUMN_RELATIVE_HUMIDITY, Constants.EXCEL_WORD_WIDTH * 16);
        summarySheet.setColumnWidth(Constants.EXCEL_COLUMN_WIND_SPEED, Constants.EXCEL_WORD_WIDTH * 14);
        summarySheet.setColumnWidth(Constants.EXCEL_COLUMN_WIND_DIRECTION, Constants.EXCEL_WORD_WIDTH * 15);
        summarySheet.setColumnWidth(Constants.EXCEL_COLUMN_PRECIPITATION, Constants.EXCEL_WORD_WIDTH * 14);
        summarySheet.setColumnWidth(Constants.EXCEL_COLUMN_PRECIPITATION_PROBABILITY, Constants.EXCEL_WORD_WIDTH * 18);
        summarySheet.setColumnWidth(Constants.EXCEL_COLUMN_RAIN, Constants.EXCEL_WORD_WIDTH * 8);
        summarySheet.setColumnWidth(Constants.EXCEL_COLUMN_SHOWERS, Constants.EXCEL_WORD_WIDTH * 8);
        getCell(summarySheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_TIME).setCellValue(Constants.EMPTY);
        getCell(summarySheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_TEMPERATURE).setCellValue(Constants.DATA_TYPE_TEMPERATURE);
        getCell(summarySheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_APPARENT_TEMPERATURE).setCellValue(Constants.DATA_TYPE_APPARENT_TEMPERATURE);
        getCell(summarySheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_RELATIVE_HUMIDITY).setCellValue(Constants.DATA_TYPE_RELATIVE_HUMIDITY);
        getCell(summarySheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_WIND_SPEED).setCellValue(Constants.DATA_TYPE_WIND_SPEED);
        getCell(summarySheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_WIND_DIRECTION).setCellValue(Constants.DATA_TYPE_WIND_DIRECTION);
        getCell(summarySheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_PRECIPITATION).setCellValue(Constants.DATA_TYPE_PRECIPITATION);
        getCell(summarySheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_PRECIPITATION_PROBABILITY).setCellValue(Constants.DATA_TYPE_PRECIPITATION_PROBABILITY);
        getCell(summarySheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_RAIN).setCellValue(Constants.DATA_TYPE_RAIN);
        getCell(summarySheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_SHOWERS).setCellValue(Constants.DATA_TYPE_SHOWERS);

        Map<String, Double> temperatureStatistics = getStatistics(temperatureList);
        Map<String, Double> apparentTemperatureStatistics = getStatistics(apparentTemperatureList);
        Map<String, Double> relativeHumidityStatistics = getStatistics(relativeHumidityList);
        Map<String, Double> windSpeedStatistics = getStatistics(windSpeedList);
        Map<String, Double> windDirectionStatistics = getStatistics(windDirectionList);
        Map<String, Double> precipitationStatistics = getStatistics(precipitationList);
        Map<String, Double> precipitationProbabilityStatistics = getStatistics(precipitationProbabilityList);
        Map<String, Double> rainStatistics = getStatistics(rainList);
        Map<String, Double> showersStatistics = getStatistics(showersList);
        setSheetContentForRow(summarySheet, Constants.EXCEL_ROW_MIN, Constants.EMPTY,
                Double.toString(temperatureStatistics.get(Constants.NUMBER_MIN)),
                Double.toString(apparentTemperatureStatistics.get(Constants.NUMBER_MIN)),
                Double.toString(relativeHumidityStatistics.get(Constants.NUMBER_MIN)),
                Double.toString(windSpeedStatistics.get(Constants.NUMBER_MIN)),
                Double.toString(windDirectionStatistics.get(Constants.NUMBER_MIN)),
                Double.toString(precipitationStatistics.get(Constants.NUMBER_MIN)),
                Double.toString(precipitationProbabilityStatistics.get(Constants.NUMBER_MIN)),
                Double.toString(rainStatistics.get(Constants.NUMBER_MIN)),
                Double.toString(showersStatistics.get(Constants.NUMBER_MIN)));
        setSheetContentForRow(summarySheet, Constants.EXCEL_ROW_MAX, Constants.EMPTY,
                Double.toString(temperatureStatistics.get(Constants.NUMBER_MAX)),
                Double.toString(apparentTemperatureStatistics.get(Constants.NUMBER_MAX)),
                Double.toString(relativeHumidityStatistics.get(Constants.NUMBER_MAX)),
                Double.toString(windSpeedStatistics.get(Constants.NUMBER_MAX)),
                Double.toString(windDirectionStatistics.get(Constants.NUMBER_MAX)),
                Double.toString(precipitationStatistics.get(Constants.NUMBER_MAX)),
                Double.toString(precipitationProbabilityStatistics.get(Constants.NUMBER_MAX)),
                Double.toString(rainStatistics.get(Constants.NUMBER_MAX)),
                Double.toString(showersStatistics.get(Constants.NUMBER_MAX)));
        setSheetContentForRow(summarySheet, Constants.EXCEL_ROW_AVERAGE, Constants.EMPTY,
                Double.toString(temperatureStatistics.get(Constants.NUMBER_AVERAGE)),
                Double.toString(apparentTemperatureStatistics.get(Constants.NUMBER_AVERAGE)),
                Double.toString(relativeHumidityStatistics.get(Constants.NUMBER_AVERAGE)),
                Double.toString(windSpeedStatistics.get(Constants.NUMBER_AVERAGE)),
                Double.toString(windDirectionStatistics.get(Constants.NUMBER_AVERAGE)),
                Double.toString(precipitationStatistics.get(Constants.NUMBER_AVERAGE)),
                Double.toString(precipitationProbabilityStatistics.get(Constants.NUMBER_AVERAGE)),
                Double.toString(rainStatistics.get(Constants.NUMBER_AVERAGE)),
                Double.toString(showersStatistics.get(Constants.NUMBER_AVERAGE)));
        setSheetContentForRow(summarySheet, Constants.EXCEL_ROW_STANDARD_DEVIATION, Constants.EMPTY,
                Double.toString(temperatureStatistics.get(Constants.NUMBER_STANDARD_DEVIATION)),
                Double.toString(apparentTemperatureStatistics.get(Constants.NUMBER_STANDARD_DEVIATION)),
                Double.toString(relativeHumidityStatistics.get(Constants.NUMBER_STANDARD_DEVIATION)),
                Double.toString(windSpeedStatistics.get(Constants.NUMBER_STANDARD_DEVIATION)),
                Double.toString(windDirectionStatistics.get(Constants.NUMBER_STANDARD_DEVIATION)),
                Double.toString(precipitationStatistics.get(Constants.NUMBER_STANDARD_DEVIATION)),
                Double.toString(precipitationProbabilityStatistics.get(Constants.NUMBER_STANDARD_DEVIATION)),
                Double.toString(rainStatistics.get(Constants.NUMBER_STANDARD_DEVIATION)),
                Double.toString(showersStatistics.get(Constants.NUMBER_STANDARD_DEVIATION)));
        getCell(summarySheet, Constants.EXCEL_ROW_MIN, 0).setCellValue(Constants.NUMBER_MIN);
        getCell(summarySheet, Constants.EXCEL_ROW_MAX, 0).setCellValue(Constants.NUMBER_MAX);
        getCell(summarySheet, Constants.EXCEL_ROW_AVERAGE, 0).setCellValue(Constants.NUMBER_AVERAGE);
        getCell(summarySheet, Constants.EXCEL_ROW_STANDARD_DEVIATION, 0).setCellValue(Constants.NUMBER_STANDARD_DEVIATION);

        Sheet detailsSheet = workbook.getSheetAt(detailsSheetIndex);
        getCell(detailsSheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_TIME).setCellValue(Constants.DATA_TYPE_TIME);
        getCell(detailsSheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_TEMPERATURE).setCellValue(Constants.DATA_TYPE_TEMPERATURE);
        getCell(detailsSheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_APPARENT_TEMPERATURE).setCellValue(Constants.DATA_TYPE_APPARENT_TEMPERATURE);
        getCell(detailsSheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_RELATIVE_HUMIDITY).setCellValue(Constants.DATA_TYPE_RELATIVE_HUMIDITY);
        getCell(detailsSheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_WIND_SPEED).setCellValue(Constants.DATA_TYPE_WIND_SPEED);
        getCell(detailsSheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_WIND_DIRECTION).setCellValue(Constants.DATA_TYPE_WIND_DIRECTION);
        getCell(detailsSheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_PRECIPITATION).setCellValue(Constants.DATA_TYPE_PRECIPITATION);
        getCell(detailsSheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_PRECIPITATION_PROBABILITY).setCellValue(Constants.DATA_TYPE_PRECIPITATION_PROBABILITY);
        getCell(detailsSheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_RAIN).setCellValue(Constants.DATA_TYPE_RAIN);
        getCell(detailsSheet, Constants.EXCEL_ROW_TITLE, Constants.EXCEL_COLUMN_SHOWERS).setCellValue(Constants.DATA_TYPE_SHOWERS);
        for (int i = 0; i < data.size(); i++) {
            ExcelData d = data.get(i);
            setSheetContentForRow(detailsSheet, Constants.EXCEL_ROW_TITLE + 1 + i, d.getTime(),
                    d.getTemperature(), d.getApparentTemperature(), d.getRelativeHumidity(), d.getWindSpeed(), d.getWindDirection(),
                    d.getPrecipitation(), d.getPrecipitationProbability(), d.getRain(), d.getShowers());
        }
        return workbook;
    }

    /**
     * 將報表加入壓縮檔內
     * */
    public static void addWorkbookToZipFile(ZipFile zipFile, XSSFWorkbook workbook, String fileNameInZip) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            ZipParameters parameters = new ZipParameters();
            parameters.setFileNameInZip(fileNameInZip);
            /*
             * workbook可以利用InputStream來讀取Excel
             * workbook可以利用OutputStream來寫入Excel
             * 可以利用以下方法把OutputStream轉換成InputStream
             * */
            byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            zipFile.addStream(byteArrayInputStream, parameters);
        } catch (IOException ioe) {
            LOGGER.error("Unable to zip Excel", ioe);
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException ioe) {
                    LOGGER.error("Unable to close ByteArrayOutputStream", ioe);
                }
            }
            if (byteArrayInputStream != null) {
                try {
                    byteArrayInputStream.close();
                } catch (IOException ioe) {
                    LOGGER.error("Unable to close ByteArrayInputStream", ioe);
                }
            }
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException ioe) {
                    LOGGER.error("Unable to close Workbook", ioe);
                }
            }
        }
    }

}