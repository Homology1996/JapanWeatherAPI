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
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class DownloadDataController extends SwingWorker<String, Double> {

    private static final Logger LOGGER = LogManager.getLogger(DownloadDataController.class);

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

        List<Integer> counter = new LinkedList<>();
        CountDownLatch checkDate = new CountDownLatch(1);
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Runnable checkDateRunnable = () -> {
            if (counter.size() > (Constants.TIMEOUT / Constants.COUNTER_INTERVAL) ||
                    (APIService.startDate != null && !APIService.startDate.isBlank() &&
                            APIService.endDate != null && !APIService.endDate.isBlank())) {
                checkDate.countDown();
                executorService.shutdown();
            } else {
                counter.add(0);
            }
        };
        executorService.scheduleWithFixedDelay(checkDateRunnable, 0, Constants.COUNTER_INTERVAL, TimeUnit.MILLISECONDS);
        try {
            checkDate.await();
        } catch (InterruptedException e) {
            LOGGER.error("Error in CountDownLatch", e);
        }

        List<String> downloadErrorMessages = new LinkedList<>();
        this.status = Constants.MSG_DOWNLOAD_DATA;
        this.publish(0.0);
        MainFrame.setStatusAndProgressBarVisibility(true);
        if (APIService.startDate != null && !APIService.startDate.isBlank() &&
                APIService.endDate != null && !APIService.endDate.isBlank()) {

            long start = System.currentTimeMillis();
            List<String> allRunnable = Arrays.asList(Constants.DATA_TYPE_TEMPERATURE,
                    Constants.DATA_TYPE_APPARENT_TEMPERATURE, Constants.DATA_TYPE_RELATIVE_HUMIDITY,
                    Constants.DATA_TYPE_WIND_SPEED, Constants.DATA_TYPE_WIND_DIRECTION,
                    Constants.DATA_TYPE_PRECIPITATION, Constants.DATA_TYPE_PRECIPITATION_PROBABILITY,
                    Constants.DATA_TYPE_RAIN, Constants.DATA_TYPE_SHOWERS);
            List<String> lockedBy = new ArrayList<>(allRunnable);
            double percentage = 50.0 / allRunnable.size();
            int numberOfTasks = allRunnable.size(), initialValue = 0;
            List<Double> progressValues = new ArrayList<>();
            progressValues.add((double) initialValue);
            ReentrantLock reentrantLock = new ReentrantLock();
            CountDownLatch countDownLatch = new CountDownLatch(numberOfTasks);
            ExecutorService downloadService = Executors.newFixedThreadPool(Constants.MAX_API_COUNT);
            MainFrame.setStatusAndProgressBar(Constants.MSG_DOWNLOAD_DATA, initialValue);

            for (String dataType : allRunnable) {
                Runnable download = () -> {
                    JSONArray array;
                    switch (dataType) {
                        case Constants.DATA_TYPE_TEMPERATURE:
                            array = APIService.getTemperature();
                            break;
                        case Constants.DATA_TYPE_APPARENT_TEMPERATURE:
                            array = APIService.getApparentTemperature();
                            break;
                        case Constants.DATA_TYPE_RELATIVE_HUMIDITY:
                            array = APIService.getRelativeHumidity();
                            break;
                        case Constants.DATA_TYPE_WIND_SPEED:
                            array = APIService.getWindSpeed();
                            break;
                        case Constants.DATA_TYPE_WIND_DIRECTION:
                            array = APIService.getWindDirection();
                            break;
                        case Constants.DATA_TYPE_PRECIPITATION:
                            array = APIService.getPrecipitation();
                            break;
                        case Constants.DATA_TYPE_PRECIPITATION_PROBABILITY:
                            array = APIService.getPrecipitationProbability();
                            break;
                        case Constants.DATA_TYPE_RAIN:
                            array = APIService.getRain();
                            break;
                        case Constants.DATA_TYPE_SHOWERS:
                            array = APIService.getShowers();
                            break;
                        default:
                            array = new JSONArray();
                            break;
                    }
                    if (array.isEmpty()) {
                        String message = String.format(Constants.MSG_DOWNLOAD_DATA_ERROR_FORMAT, dataType);
                        LOGGER.error(message);
                        synchronized (downloadErrorMessages) {
                            downloadErrorMessages.add(message);
                            for (int left = 0; left < numberOfTasks - countDownLatch.getCount(); left++) {
                                countDownLatch.countDown();
                            }
                            downloadService.shutdown();
                        }
                    } else {
                        switch (dataType) {
                            case Constants.DATA_TYPE_TEMPERATURE:
                                ExcelService.temperatureArray = array;
                                break;
                            case Constants.DATA_TYPE_APPARENT_TEMPERATURE:
                                ExcelService.apparentTemperatureArray = array;
                                break;
                            case Constants.DATA_TYPE_RELATIVE_HUMIDITY:
                                ExcelService.relativeHumidityArray = array;
                                break;
                            case Constants.DATA_TYPE_WIND_SPEED:
                                ExcelService.windSpeedArray = array;
                                break;
                            case Constants.DATA_TYPE_WIND_DIRECTION:
                                ExcelService.windDirectionArray = array;
                                break;
                            case Constants.DATA_TYPE_PRECIPITATION:
                                ExcelService.precipitationArray = array;
                                break;
                            case Constants.DATA_TYPE_PRECIPITATION_PROBABILITY:
                                ExcelService.precipitationProbabilityArray = array;
                                break;
                            case Constants.DATA_TYPE_RAIN:
                                ExcelService.rainArray = array;
                                break;
                            case Constants.DATA_TYPE_SHOWERS:
                                ExcelService.showersArray = array;
                                break;
                            default:
                                break;
                        }
                    }
                    try {
                        reentrantLock.lock();
                        if (!lockedBy.isEmpty()) {
                            progressValues.add(percentage);
                            int DNE = -1;
                            int idx = lockedBy.stream().filter(i -> i.equals(dataType))
                                    .map(lockedBy::indexOf).findFirst().orElse(DNE);
                            if (idx > DNE) {
                                lockedBy.remove(idx);
                            }
                            MainFrame.setStatusAndProgressBar(
                                    String.format(Constants.MSG_DOWNLOAD_DATA_FORMAT, dataType),
                                    Integer.parseInt(Long.toString(Math.round(progressValues
                                            .stream().reduce(0.0, Double::sum)))));
                        }
                    } finally {
                        countDownLatch.countDown();
                        reentrantLock.unlock();
                    }
                };
                downloadService.execute(download);
            }

            try {
                countDownLatch.await();
            } catch (InterruptedException ex) {
                LOGGER.error("Error in CountDownLatch", ex);
            } finally {
                downloadService.shutdown();
            }
            LOGGER.info("Download time: " + (System.currentTimeMillis() - start) + " milliseconds");

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