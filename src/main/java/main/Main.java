package main;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
                new MainFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}