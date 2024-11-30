package main;

import main.controller.DefaultController;
import main.view.PanelFactory;
import main.view.PanelUtility;
import main.view.panel.ProgressBarPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("日本天氣資料");
        System.setProperty("logFilename", Paths.get(Constants.EMPTY).toAbsolutePath() + File.separator + Constants.LOG_FILE_NAME); // 設定log位置
        // logFilename對應到log4j2.properties的appender.file.fileName = ${sys:logFilename}
        this.setBounds(0, 0, Constants.MAINFRAME_WIDTH, Constants.MAINFRAME_HEIGHT); // (x,y) = 左上角座標, (width, height) = 畫面大小
        this.setResizable(false); // 固定大小，不能縮放
        this.setLocationRelativeTo(null); // 放在正中間
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // 事情做完才會關閉程式
        DefaultController.getInstance().setAttributes(this);
        this.setLayout(new BorderLayout(0, 0)); // 畫面排版風格
        this.setCurrentPanel(PanelFactory.getInstance().getPanelByName(Constants.INDEX_PANEL)); // 初始畫面
        this.add(PanelFactory.getInstance().getPanelByName(Constants.PROGRESS_BAR_PANEL), BorderLayout.PAGE_END);
        this.setVisible(true); // 這個要放在最後，如此一來才會顯示所有的元素
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {}
        });
    }

    /**
     * 取得目前畫面顯示的Panel
     */
    public JPanel getCurrentPanel() {
        for (Component component : this.getContentPane().getComponents()) {
            if (component instanceof JPanel && !component.getName().equals(Constants.PROGRESS_BAR_PANEL)) {
                return (JPanel) component;
            }
        }
        return new JPanel();
    }

    /**
     * 設定要顯示的Panel
     */
    public void setCurrentPanel(JPanel jPanel) {
        /*
         * 此程式的版面設計是採用BorderLayout
         * 在PAGE_END的部份放的是進度條
         * 在CENTER的部份放的是指定的JPanel
         * 所以畫面一開始就已經先分割成上下兩個部份
         * 而對於指定的JPanel則是採用AbsoluteLayout
         * */
        if (this.getContentPane() != null && this.getContentPane().getComponents() != null) {
            for (Component component : this.getContentPane().getComponents()) {
                if (component instanceof JPanel && !component.getName().equals(Constants.PROGRESS_BAR_PANEL)) {
                    this.getContentPane().remove(component);
                }
            }
        } else {
            this.getContentPane().removeAll();
            this.setContentPane(jPanel);
        }
        this.getContentPane().add(jPanel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
        this.setVisible(true);
    }

    /**
     * 透過命名來抓取Component<br/>
     * 註記1: 在Panel裡面建立Component時，要記得命名Component<br/>
     * 這樣子才能透過此方法抓到該Component<br/>
     * 註記2: Component命名方式可以參考PanelUtility#getComponentName
     * @see PanelUtility#getComponentName(String, String)
     */
    public static Map<String, Component> getComponentMapForPanel(JPanel jPanel) {
        return Arrays.stream(jPanel.getComponents()).collect(
                Collectors.toMap(Component::getName, Function.identity(), (before, after) -> after));
    }

    /**
     * 取得當前Panel對應的ComponentMap
     */
    public Map<String, Component> getComponentMapForCurrentPanel() {
        return getComponentMapForPanel(this.getCurrentPanel());
    }

    /**
     * @see ProgressBarPanel#setStatusAndProgressBar(String, int)
     */
    public static void setStatusAndProgressBar(String status, int value) {
        ((ProgressBarPanel) PanelFactory.getInstance()
                .getPanelByName(Constants.PROGRESS_BAR_PANEL)).setStatusAndProgressBar(status, value);
    }

    /**
     * @see ProgressBarPanel#setVisibility(boolean)
     */
    public static void setStatusAndProgressBarVisibility(boolean flag) {
        ((ProgressBarPanel) PanelFactory.getInstance()
                .getPanelByName(Constants.PROGRESS_BAR_PANEL)).setVisibility(flag);
    }

}