package main.controller;

import main.Constants;
import main.MainFrame;
import main.service.APIService;
import main.view.PanelFactory;
import main.view.PanelUtility;
import main.view.panel.IndexPanel;
import main.view.panel.ResultPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultController implements ActionListener {

    private final static Logger LOGGER = LogManager.getLogger(DefaultController.class);

    private final static DefaultController INSTANCE = new DefaultController();

    private DefaultController() {}

    public static DefaultController getInstance() {
        return INSTANCE;
    }

    private MainFrame mainFrame;

    private PanelFactory panelFactory;

    public void setAttributes(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.panelFactory = PanelFactory.getInstance();
    }

    /**
     * 簡化程式碼
     * @see MainFrame#setCurrentPanel(JPanel)
     * */
    private void setCurrentPanel(JPanel jPanel) {
        this.mainFrame.setCurrentPanel(jPanel);
    }

    /**
     * 簡化程式碼
     * @see PanelFactory#getPanelByName(String)
     * */
    private JPanel getPanelByName(String panelName) {
        return this.panelFactory.getPanelByName(panelName);
    }

    /**
     * 簡化程式碼
     * @see PanelUtility#getComponentName(String, String)
     * */
    private static String getComponentName(String panelName, String componentName) {
        return PanelUtility.getComponentName(panelName, componentName);
    }

    /**
     * 檢查Component是否被觸發
     * */
    private boolean isComponentTriggered(ActionEvent e, String panelName, String componentName) {
        return e.getSource() == this.mainFrame.getComponentMapForCurrentPanel().get(getComponentName(panelName, componentName));
    }

    /**
     * 通常是要取得下一個跳轉頁面裡面的Component
     * @see MainFrame#getComponentMapForPanel(JPanel)
     * @see DefaultController#getComponentName(String, String)
     * */
    private static Component getComponentForPanel(JPanel jPanel, String componentName) {
        return MainFrame.getComponentMapForPanel(jPanel).get(getComponentName(jPanel.getName(), componentName));
    }

    private static String parseDate(String date) {
        if (!date.matches(Constants.INPUT_DATE_FORMAT)) {
            return Constants.MSG_INPUT_DATE_FORMAT_ERROR;
        } else {
            String[] array = date.split(Constants.DASH);
            int month = Integer.parseInt(array[1]), day = Integer.parseInt(array[2]);
            if (month < 1 || month > 12) {
                return Constants.MSG_INPUT_DATE_ERROR;
            }
            if (day < 1 || day > 31) {
                return Constants.MSG_INPUT_DATE_ERROR;
            }
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT.substring(0, 8));
                simpleDateFormat.parse(array[0] + array[1] + array[2]);
                return date;
            } catch (Exception e) {
                return Constants.MSG_INPUT_DATE_ERROR;
            }
        }
    }

    private static double parseCoordinate(String coordinate, StringBuilder checkError) {
        try {
            return Double.parseDouble(coordinate);
        } catch (Exception e) {
            LOGGER.error("Coordinate error", e);
            checkError.append(Constants.MSG_COORDINATE_ERROR);
            return -1;
        }
    }

    /**
     * 在這裡定義每個按鈕的行為
     * */
    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel currentPanel = this.mainFrame.getCurrentPanel();
        Map<String, Component> componentMap = this.mainFrame.getComponentMapForCurrentPanel();
        if (isComponentTriggered(e, Constants.INDEX_PANEL, Constants.INDEX_NEXT)) {
            JTextField startDateField = (JTextField) componentMap.get(
                    getComponentName(Constants.INDEX_PANEL, Constants.INDEX_START_DATE));
            JTextField endDateField = (JTextField) componentMap.get(
                    getComponentName(Constants.INDEX_PANEL, Constants.INDEX_END_DATE));
            JTextField longitudeField = (JTextField) componentMap.get(
                    getComponentName(Constants.INDEX_PANEL, Constants.INDEX_LONGITUDE));
            JTextField latitudeField = (JTextField) componentMap.get(
                    getComponentName(Constants.INDEX_PANEL, Constants.INDEX_LATITUDE));
            String startDate = startDateField.getText();
            String endDate = endDateField.getText();
            String longitudeString = longitudeField.getText();
            String latitudeString = latitudeField.getText();
            StringBuilder checkError4Longitude = new StringBuilder();
            StringBuilder checkError4Latitude = new StringBuilder();
            double longitude = parseCoordinate(longitudeString, checkError4Longitude);
            double latitude = parseCoordinate(latitudeString, checkError4Latitude);
            List<String> errorMessages = new ArrayList<>();
            if (!parseDate(startDate).matches(Constants.INPUT_DATE_FORMAT)) {
                errorMessages.add(startDate + parseDate(startDate));
            }
            if (!parseDate(endDate).matches(Constants.INPUT_DATE_FORMAT)) {
                errorMessages.add(endDate + parseDate(endDate));
            }
            if (checkError4Longitude.length() > 0) {
                errorMessages.add(longitudeString + checkError4Longitude);
            }
            if (checkError4Latitude.length() > 0) {
                errorMessages.add(latitudeString + checkError4Latitude);
            }
            if (!errorMessages.isEmpty()) {
                JPanel resultPanel = this.getPanelByName(Constants.RESULT_PANEL);
                JLabel content = ((ResultPanel) resultPanel).getContentLabel();
                content.setText(PanelUtility.generateMultiLineHTMLMessage(errorMessages));
                getComponentForPanel(resultPanel, Constants.RESULT_NEXT).setVisible(false);
                getComponentForPanel(resultPanel, Constants.RESULT_NEXT).setEnabled(false);
                getComponentForPanel(resultPanel, Constants.RESULT_CANCEL).setEnabled(true);
                getComponentForPanel(resultPanel, Constants.RESULT_CANCEL).setVisible(true);
                this.setCurrentPanel(resultPanel);
            } else {
                APIService.startDate = startDate;
                APIService.endDate = endDate;
                APIService.longitude = longitude;
                APIService.latitude = latitude;
                List<Integer> counter = new LinkedList<>();
                DownloadDataController action = ((IndexPanel) currentPanel).getDownloadDataController();
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                Runnable checkDownload = () -> {
                    if (counter.size() > (Constants.TIMEOUT / Constants.COUNTER_INTERVAL) || action.isCancelled()) {
                        LOGGER.error("Exceeds the time limit");
                        this.setCurrentPanel(this.getPanelByName(Constants.INDEX_PANEL));
                        executorService.shutdown();
                    } else if (action.isDone()) {
                        try {
                            JPanel resultPanel = this.getPanelByName(Constants.RESULT_PANEL);
                            JLabel content = ((ResultPanel) resultPanel).getContentLabel();
                            String message = action.get();
                            boolean hasError = true;
                            if (message != null && !message.isBlank()) {
                                if (message.startsWith(Constants.SUCCEED)) {
                                    content.setText(message.substring(Constants.SUCCEED.length()));
                                    hasError = false;
                                } else if (message.startsWith(Constants.FAIL)) {
                                    content.setText(message.substring(Constants.FAIL.length()));
                                } else {
                                    LOGGER.error(String.format("message = %s", message));
                                    JOptionPane.showMessageDialog(null, Constants.MSG_DOWNLOAD_DATA_FAIL);
                                }
                            } else {
                                LOGGER.error(String.format("message = %s", message));
                                JOptionPane.showMessageDialog(null, Constants.MSG_DOWNLOAD_DATA_FAIL);
                            }
                            if (hasError) {
                                getComponentForPanel(resultPanel, Constants.RESULT_NEXT).setVisible(false);
                                getComponentForPanel(resultPanel, Constants.RESULT_NEXT).setEnabled(false);
                                getComponentForPanel(resultPanel, Constants.RESULT_CANCEL).setEnabled(true);
                                getComponentForPanel(resultPanel, Constants.RESULT_CANCEL).setVisible(true);
                            }
                            this.setCurrentPanel(resultPanel);
                        } catch (InterruptedException | ExecutionException ex) {
                            LOGGER.error("Unable to download Excel", ex);
                        } finally {
                            executorService.shutdown();
                        }
                    } else {
                        counter.add(0);
                    }
                };
                executorService.scheduleWithFixedDelay(checkDownload, 0, Constants.COUNTER_INTERVAL, TimeUnit.MILLISECONDS);
            }
        } else if (isComponentTriggered(e, Constants.RESULT_PANEL, Constants.RESULT_NEXT)) {
            List<Integer> counter = new LinkedList<>();
            GenerateReportController action = ((ResultPanel) currentPanel).getGenerateReportController();
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            Runnable checkDownload = () -> {
                if (counter.size() > (Constants.TIMEOUT / Constants.COUNTER_INTERVAL) || action.isCancelled()) {
                    LOGGER.error("Exceeds the time limit");
                    this.setCurrentPanel(this.getPanelByName(Constants.INDEX_PANEL));
                    executorService.shutdown();
                } else if (action.isDone()) {
                    try {
                        String message = action.get();
                        if (message != null && !message.isBlank()) {
                            if (message.startsWith(Constants.SUCCEED)) {
                                JOptionPane.showMessageDialog(null, message.substring(Constants.SUCCEED.length()));
                            } else if (message.startsWith(Constants.FAIL)) {
                                JOptionPane.showMessageDialog(null, message.substring(Constants.FAIL.length()));
                            } else {
                                LOGGER.error(String.format("message = %s", message));
                                JOptionPane.showMessageDialog(null, Constants.MSG_DOWNLOAD_FILE_FAIL);
                            }
                        } else {
                            LOGGER.error(String.format("message = %s", message));
                            JOptionPane.showMessageDialog(null, Constants.MSG_DOWNLOAD_FILE_FAIL);
                        }
                        this.setCurrentPanel(this.getPanelByName(Constants.INDEX_PANEL));
                    } catch (InterruptedException | ExecutionException ex) {
                        LOGGER.error("Unable to download Zip", ex);
                    } finally {
                        executorService.shutdown();
                    }
                } else {
                    counter.add(0);
                }
            };
            executorService.scheduleWithFixedDelay(checkDownload, 0, Constants.COUNTER_INTERVAL, TimeUnit.MILLISECONDS);
        } else {
            this.setCurrentPanel(this.getPanelByName(Constants.INDEX_PANEL));
        }
    }

}