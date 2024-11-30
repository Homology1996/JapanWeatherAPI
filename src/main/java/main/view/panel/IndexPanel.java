package main.view.panel;

import main.Constants;
import main.controller.DefaultController;
import main.controller.DownloadDataController;
import main.view.PanelUtility;

import javax.swing.*;
import java.awt.*;

public class IndexPanel extends JPanel implements PanelUtility {

    private final DefaultController defaultController;

    private final DownloadDataController downloadDataController;

    public IndexPanel() {
        this.defaultController = DefaultController.getInstance();
        PanelUtility.setPanelName(this, Constants.INDEX_PANEL);
        this.downloadDataController = new DownloadDataController(this);
        init();
    }

    @Override
    public void setComponentInfo(Component component, String jComponentName, int x, int y, int width, int height) {
        this.setComponentInfo(this, component, jComponentName);
        component.setBounds(x, y, width, height);
    }

    private void init() {
        JLabel title = new JLabel(Constants.LABEL_INDEX_TITLE);
        this.setComponentInfo(title, Constants.INDEX_TITLE, Constants.TITLE_X, Constants.TITLE_Y, Constants.TITLE_WIDTH, Constants.TITLE_HEIGHT);
        title.setFont(PanelUtility.TITLE_FONT);

        JLabel startDateLabel = new JLabel(Constants.LABEL_START);
        this.setComponentInfo(startDateLabel, Constants.EMPTY, 50, 200, 100, Constants.INDEX_HEIGHT);

        JTextField startDateField = new JTextField(Constants.LABEL_DATE_HINT);
        this.setComponentInfo(startDateField, Constants.INDEX_START_DATE, 125, 200, Constants.INDEX_TEXT_FIELD_WIDTH, Constants.INDEX_HEIGHT);
        startDateField.setFont(PanelUtility.TEXT_FIELD_FONT);

        JLabel toLabel = new JLabel(Constants.LABEL_TO);
        this.setComponentInfo(toLabel, Constants.EMPTY, 300, 200, 50, Constants.INDEX_HEIGHT);

        JLabel endDateLabel = new JLabel(Constants.LABEL_END);
        this.setComponentInfo(endDateLabel, Constants.EMPTY, 350, 200, 100, Constants.INDEX_HEIGHT);

        JTextField endDateField = new JTextField(Constants.LABEL_DATE_HINT);
        this.setComponentInfo(endDateField, Constants.INDEX_END_DATE, 425, 200, Constants.INDEX_TEXT_FIELD_WIDTH, Constants.INDEX_HEIGHT);
        endDateField.setFont(PanelUtility.TEXT_FIELD_FONT);

        JLabel longitudeLabel = new JLabel(Constants.LABEL_LONGITUDE);
        this.setComponentInfo(longitudeLabel, Constants.EMPTY, 50, 350, 50, Constants.INDEX_HEIGHT);

        JTextField longitudeField = new JTextField();
        this.setComponentInfo(longitudeField, Constants.INDEX_LONGITUDE, 100, 350, Constants.INDEX_TEXT_FIELD_WIDTH, Constants.INDEX_HEIGHT);
        longitudeField.setFont(PanelUtility.TEXT_FIELD_FONT);

        JLabel latitudeLabel = new JLabel(Constants.LABEL_LATITUDE);
        this.setComponentInfo(latitudeLabel, Constants.EMPTY, 350, 350, 50, Constants.INDEX_HEIGHT);

        JTextField latitudeField = new JTextField();
        this.setComponentInfo(latitudeField, Constants.INDEX_LATITUDE, 400, 350, Constants.INDEX_TEXT_FIELD_WIDTH, Constants.INDEX_HEIGHT);
        latitudeField.setFont(PanelUtility.TEXT_FIELD_FONT);

        JButton nextStep = new JButton(Constants.BUTTON_NEXT);
        this.setComponentInfo(nextStep, Constants.INDEX_NEXT, Constants.NEXT_X, Constants.NEXT_Y, Constants.NEXT_WIDTH, Constants.NEXT_HEIGHT);
        nextStep.addActionListener(this.defaultController);
        nextStep.addActionListener(e -> this.downloadDataController.execute());
    }

    public DownloadDataController getDownloadDataController() {
        return this.downloadDataController;
    }

}