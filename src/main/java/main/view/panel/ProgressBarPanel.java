package main.view.panel;

import main.Constants;
import main.view.PanelUtility;

import javax.swing.*;
import java.awt.*;

public class ProgressBarPanel extends JPanel implements PanelUtility {

    private final JLabel statusLabel = new JLabel();

    private final JProgressBar progressBar = new JProgressBar(0, 100);

    private final static ProgressBarPanel INSTANCE = new ProgressBarPanel();

    private ProgressBarPanel() {
        PanelUtility.setPanelName(this, Constants.PROGRESS_BAR_PANEL);
        this.setLayout(new GridLayout(2, 1, 0, 0));
        this.setPreferredSize(new Dimension(Constants.MAINFRAME_WIDTH, Constants.UNIFORM_HEIGHT + Constants.PROGRESS_BAR_HEIGHT));
        init();
    }

    public static ProgressBarPanel getInstance() {
        return INSTANCE;
    }

    @Override
    public void setComponentInfo(Component component, String jComponentName, int x, int y, int width, int height) {
        this.setComponentInfo(this, component, jComponentName);
        component.setBounds(x, y, width, height);
    }

    private void init() {
        this.setComponentInfo(this.statusLabel, Constants.STATUS_LABEL,
                Constants.PROGRESS_BAR_X, Constants.PROGRESS_BAR_Y - Constants.UNIFORM_HEIGHT,
                Constants.PROGRESS_BAR_WIDTH, Constants.UNIFORM_HEIGHT);
        this.statusLabel.setVisible(false);

        this.setComponentInfo(this.progressBar, Constants.PROGRESS_BAR,
                Constants.PROGRESS_BAR_X, Constants.PROGRESS_BAR_Y,
                Constants.PROGRESS_BAR_WIDTH, Constants.PROGRESS_BAR_HEIGHT);
        this.progressBar.setVisible(false);
    }

    /**
     * 設定狀態顯示文字
     * */
    private void setStatus(String status) {
        if (status == null || status.isBlank()) {
            this.statusLabel.setText(Constants.EMPTY);
        } else {
            this.statusLabel.setText("   " + status); // 讓文字不要那麼貼近邊緣
        }
    }

    /**
     * 設定是否顯示狀態
     * */
    private void setStatusVisibility(boolean flag) {
        this.statusLabel.setVisible(flag);
    }

    /**
     * 設定進度條數值
     * */
    private void setProgressBarValue(int value) {
        this.progressBar.setValue(value);
    }

    /**
     * 設定是否顯示進度條
     * */
    private void setProgressBarVisibility(boolean flag) {
        this.progressBar.setVisible(flag);
    }

    /**
     * @see ProgressBarPanel#setStatus(String)
     * @see ProgressBarPanel#setProgressBarValue(int)
     * */
    public void setStatusAndProgressBar(String status, int value) {
        this.setStatus(status);
        this.setProgressBarValue(value);
    }

    /**
     * @see ProgressBarPanel#setStatusVisibility(boolean)
     * @see ProgressBarPanel#setProgressBarVisibility(boolean)
     * */
    public void setVisibility(boolean flag) {
        this.setStatusVisibility(flag);
        this.setProgressBarVisibility(flag);
        if (!flag) {
            this.setStatus(Constants.EMPTY);
            this.setProgressBarValue(0);
        }
    }

}