package main.view.panel;

import main.Constants;
import main.MainFrame;
import main.controller.DefaultController;
import main.controller.GenerateReportController;
import main.view.PanelUtility;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ResultPanel extends JPanel implements PanelUtility {

    private final DefaultController defaultController;

    private final GenerateReportController generateReportController;

    public ResultPanel() {
        this.defaultController = DefaultController.getInstance();
        PanelUtility.setPanelName(this, Constants.RESULT_PANEL);
        this.generateReportController = new GenerateReportController(this);
        init();
    }

    @Override
    public void setComponentInfo(Component component, String jComponentName, int x, int y, int width, int height) {
        this.setComponentInfo(this, component, jComponentName);
        component.setBounds(x, y, width, height);
    }

    private void init() {
        JLabel title = new JLabel(Constants.LABEL_RESULT);
        this.setComponentInfo(title, Constants.RESULT_TITLE, Constants.TITLE_X, Constants.TITLE_Y, Constants.TITLE_WIDTH, Constants.TITLE_HEIGHT);
        title.setFont(PanelUtility.TITLE_FONT);

        JLabel content = new JLabel();
        this.setComponentInfo(this, content, Constants.RESULT_CONTENT);
        content.setVerticalAlignment(SwingConstants.TOP);

        JScrollPane scrollPane = new JScrollPane(content);
        this.setComponentInfo(scrollPane, Constants.RESULT_SCROLL_PANE, Constants.TITLE_X, 100, 650, 300);

        JButton getBack = new JButton(Constants.BUTTON_CANCEL);
        this.setComponentInfo(getBack, Constants.RESULT_CANCEL, Constants.NEXT_X, Constants.NEXT_Y, Constants.NEXT_WIDTH, Constants.NEXT_HEIGHT);
        getBack.addActionListener(this.defaultController);
        getBack.setVisible(false);
        getBack.setEnabled(false);

        JButton nextStep = new JButton(Constants.BUTTON_DOWNLOAD);
        this.setComponentInfo(nextStep, Constants.RESULT_NEXT, Constants.NEXT_X, Constants.NEXT_Y, Constants.NEXT_WIDTH, Constants.NEXT_HEIGHT);
        nextStep.addActionListener(this.defaultController);
        nextStep.addActionListener(e -> this.generateReportController.execute());
    }

    public GenerateReportController getGenerateReportController() {
        return this.generateReportController;
    }

    public JLabel getContentLabel() {
        Map<String, Component> componentMap = MainFrame.getComponentMapForPanel(this);
        JScrollPane scrollPane = (JScrollPane) componentMap.get(PanelUtility.getComponentName(Constants.RESULT_PANEL, Constants.RESULT_SCROLL_PANE));
        JViewport viewport = (JViewport) scrollPane.getComponents()[0];
        return (JLabel) viewport.getView();
    }

}