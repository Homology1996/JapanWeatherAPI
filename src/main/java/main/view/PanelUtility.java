package main.view;

import main.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public interface PanelUtility {

    Font TITLE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, Constants.TITLE_FONT_SIZE);

    Font DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, Constants.DEFAULT_FONT_SIZE);

    Font TEXT_FIELD_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, Constants.TEXT_FIELD_FONT_SIZE);

    /**
     * JLabel支援HTML格式，但是卻不能顯示多行文字<br/>
     * 因此要在JLabel顯示多行文字就要把內容轉成HTML格式
     * */
    static String generateMultiLineHTMLMessage(List<String> messages) {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < Integer.min(messages.size(), Constants.MAX_ERROR_QUANTITY); index++) {
            String message = messages.get(index);
            if (!message.isBlank()) {
                builder.append(message).append("<br>");
            }
        }
        String content = builder.toString();
        return content.isBlank() ? Constants.EMPTY : "<html><body>" + content + "</body></html>";
    }

    /**
     * 命名Panel與設定Panel的layout
     * */
    static void setPanelName(JPanel jPanel, String panelName) {
        jPanel.setName(panelName);
        jPanel.setLayout(null); // AbsoluteLayout
    }

    /**
     * 取得Panel裡面Component的設定名稱
     * @see Constants#COMPONENT_NAME_FORMAT
     * */
    static String getComponentName(String panelName, String jComponentName) {
        return String.format(Constants.COMPONENT_NAME_FORMAT, panelName, jComponentName);
    }

    /**
     * 1. 把Component加進去JPanel裡面<br/>
     * 2. 命名Component<br/>
     * 3. 設定Component為預設字形
     * */
    default void setComponentInfo(JPanel jPanel, Component component, String jComponentName) {
        jPanel.add(component);
        component.setName(getComponentName(jPanel.getName() == null ? Constants.EMPTY : jPanel.getName(), jComponentName));
        component.setFont(DEFAULT_FONT);
    }

    /**
     * 簡化程式碼
     * @see PanelUtility#setComponentInfo(JPanel, Component, String)
     * */
    void setComponentInfo(Component component, String jComponentName, int x, int y, int width, int height);

}