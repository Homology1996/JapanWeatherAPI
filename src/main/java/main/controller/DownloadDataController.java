package main.controller;

import main.Constants;
import main.MainFrame;
import main.data.ExcelData;
import main.service.APIService;
import main.service.ExcelService;
import main.view.PanelUtility;
import main.view.panel.IndexPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class DownloadDataController extends SwingWorker<String, Double> {

    private final static Logger LOGGER = LogManager.getLogger(DownloadDataController.class);

    private final IndexPanel indexPanel;

    private String status;

    public DownloadDataController(IndexPanel indexPanel) {
        this.indexPanel = indexPanel;
    }

    @Override
    protected String doInBackground() {
        JButton nextStep = (JButton) MainFrame.getComponentMapForPanel(this.indexPanel)
                .get(PanelUtility.getComponentName(Constants.INDEX_PANEL, Constants.INDEX_NEXT));
        nextStep.setEnabled(false);
        List<String> downloadErrorMessages = new LinkedList<>();
        this.status = Constants.EMPTY;
        this.publish(0.0);
        MainFrame.setStatusAndProgressBarVisibility(true);

        try {
            /*
             * 原本預定是先設定好APIService相關屬性之後再啟動ExecutorService
             * 不過因為執行緒的關係，在設定好相關屬性之前就有可能會先跑到這裡來
             * 導致判斷錯誤，以為屬性沒有被設定
             * 因此在這裡先強迫執行緒暫停一段時間，等到屬性設定好之後再執行
             * */
            Thread.sleep(250);
        } catch (Exception ignore) {}

        if (APIService.startDate != null && !APIService.startDate.isBlank() &&
                APIService.endDate != null && !APIService.endDate.isBlank()) {
            this.status = String.format(Constants.MSG_DOWNLOAD_DATA_FORMAT, Constants.DATA_TYPE_TEMPERATURE);
            this.publish(0.0);
            JSONArray temperatureArray = APIService.getTemperature();
            if (temperatureArray.isEmpty()) {
                String message = String.format(Constants.MSG_DOWNLOAD_DATA_ERROR_FORMAT, Constants.DATA_TYPE_TEMPERATURE);
                LOGGER.error(message);
                downloadErrorMessages.add(message);
            } else {
                ExcelService.temperatureArray = temperatureArray;
            }

            this.status = String.format(Constants.MSG_DOWNLOAD_DATA_FORMAT, Constants.DATA_TYPE_APPARENT_TEMPERATURE);
            this.publish(10.0);
            JSONArray apparentTemperatureArray = APIService.getApparentTemperature();
            if (apparentTemperatureArray.isEmpty()) {
                String message = String.format(Constants.MSG_DOWNLOAD_DATA_ERROR_FORMAT, Constants.DATA_TYPE_APPARENT_TEMPERATURE);
                LOGGER.error(message);
                downloadErrorMessages.add(message);
            } else {
                ExcelService.apparentTemperatureArray = apparentTemperatureArray;
            }

            this.status = String.format(Constants.MSG_DOWNLOAD_DATA_FORMAT, Constants.DATA_TYPE_RELATIVE_HUMIDITY);
            this.publish(20.0);
            JSONArray relativeHumidityArray = APIService.getRelativeHumidity();
            if (relativeHumidityArray.isEmpty()) {
                String message = String.format(Constants.MSG_DOWNLOAD_DATA_ERROR_FORMAT, Constants.DATA_TYPE_RELATIVE_HUMIDITY);
                LOGGER.error(message);
                downloadErrorMessages.add(message);
            } else {
                ExcelService.relativeHumidityArray = relativeHumidityArray;
            }

            this.status = String.format(Constants.MSG_DOWNLOAD_DATA_FORMAT, Constants.DATA_TYPE_WIND_SPEED);
            this.publish(30.0);
            JSONArray windSpeedArray = APIService.getWindSpeed();
            if (windSpeedArray.isEmpty()) {
                String message = String.format(Constants.MSG_DOWNLOAD_DATA_ERROR_FORMAT, Constants.DATA_TYPE_WIND_SPEED);
                LOGGER.error(message);
                downloadErrorMessages.add(message);
            } else {
                ExcelService.windSpeedArray = windSpeedArray;
            }

            this.status = String.format(Constants.MSG_DOWNLOAD_DATA_FORMAT, Constants.DATA_TYPE_WIND_DIRECTION);
            this.publish(40.0);
            JSONArray windDirectionArray = APIService.getWindDirection();
            if (windDirectionArray.isEmpty()) {
                String message = String.format(Constants.MSG_DOWNLOAD_DATA_ERROR_FORMAT, Constants.DATA_TYPE_WIND_DIRECTION);
                LOGGER.error(message);
                downloadErrorMessages.add(message);
            } else {
                ExcelService.windDirectionArray = windDirectionArray;
            }

            this.status = String.format(Constants.MSG_DOWNLOAD_DATA_FORMAT, Constants.DATA_TYPE_PRECIPITATION);
            this.publish(50.0);
            JSONArray precipitationArray = APIService.getPrecipitation();
            if (precipitationArray.isEmpty()) {
                String message = String.format(Constants.MSG_DOWNLOAD_DATA_ERROR_FORMAT, Constants.DATA_TYPE_PRECIPITATION);
                LOGGER.error(message);
                downloadErrorMessages.add(message);
            } else {
                ExcelService.precipitationArray = precipitationArray;
            }

            this.status = String.format(Constants.MSG_DOWNLOAD_DATA_FORMAT, Constants.DATA_TYPE_PRECIPITATION_PROBABILITY);
            this.publish(60.0);
            JSONArray precipitationProbabilityArray = APIService.getPrecipitationProbability();
            if (precipitationProbabilityArray.isEmpty()) {
                String message = String.format(Constants.MSG_DOWNLOAD_DATA_ERROR_FORMAT, Constants.DATA_TYPE_PRECIPITATION_PROBABILITY);
                LOGGER.error(message);
                downloadErrorMessages.add(message);
            } else {
                ExcelService.precipitationProbabilityArray = precipitationProbabilityArray;
            }

            this.status = String.format(Constants.MSG_DOWNLOAD_DATA_FORMAT, Constants.DATA_TYPE_RAIN);
            this.publish(70.0);
            JSONArray rainArray = APIService.getRain();
            if (rainArray.isEmpty()) {
                String message = String.format(Constants.MSG_DOWNLOAD_DATA_ERROR_FORMAT, Constants.DATA_TYPE_RAIN);
                LOGGER.error(message);
                downloadErrorMessages.add(message);
            } else {
                ExcelService.rainArray = rainArray;
            }

            this.status = String.format(Constants.MSG_DOWNLOAD_DATA_FORMAT, Constants.DATA_TYPE_SHOWERS);
            this.publish(80.0);
            JSONArray showersArray = APIService.getShowers();
            if (showersArray.isEmpty()) {
                String message = String.format(Constants.MSG_DOWNLOAD_DATA_ERROR_FORMAT, Constants.DATA_TYPE_SHOWERS);
                LOGGER.error(message);
                downloadErrorMessages.add(message);
            } else {
                ExcelService.showersArray = showersArray;
            }

            if (!downloadErrorMessages.isEmpty()) {
                return Constants.FAIL + PanelUtility.generateMultiLineHTMLMessage(downloadErrorMessages);
            } else {
                this.status = Constants.MSG_GENERATE_EXCEL;
                this.publish(90.0);
                List<ExcelData> excelDataList = ExcelService.generateExcelData();
                ExcelService.setDataList(excelDataList);
                ExcelService.workbook = ExcelService.generateReport(excelDataList);
                return Constants.SUCCEED + Constants.MSG_DOWNLOAD_DATA_SUCCEED;
            }
        } else {
            return Constants.FAIL + Constants.MSG_DOWNLOAD_DATA_FAIL;
        }
    }

    @Override
    protected void process(List<Double> chunks) {
        MainFrame.setStatusAndProgressBar(this.status, Integer.parseInt(Long.toString(Math.round(chunks.get(chunks.size() - 1)))));
    }

    @Override
    protected void done() {
        MainFrame.setStatusAndProgressBarVisibility(false);
        JButton nextStep = (JButton) MainFrame.getComponentMapForPanel(this.indexPanel)
                .get(PanelUtility.getComponentName(Constants.RESULT_PANEL, Constants.INDEX_NEXT));
        if (nextStep != null) {
            nextStep.setEnabled(true);
        }
    }

}