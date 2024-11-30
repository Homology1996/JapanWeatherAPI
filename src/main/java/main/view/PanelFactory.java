package main.view;

import main.Constants;
import main.view.panel.IndexPanel;
import main.view.panel.ProgressBarPanel;
import main.view.panel.ResultPanel;

import javax.swing.*;

public class PanelFactory {

    private final static PanelFactory INSTANCE = new PanelFactory();

    private PanelFactory() {}

    public static PanelFactory getInstance() {
        return INSTANCE;
    }

    /**
     * 用這個方法來產生各種Panel
     * */
    public JPanel getPanelByName(final String panelName) {
        switch (panelName) {
            case Constants.RESULT_PANEL:
                return new ResultPanel();
            case Constants.PROGRESS_BAR_PANEL:
                return ProgressBarPanel.getInstance();
            default:
                return new IndexPanel();
        }
    }

}