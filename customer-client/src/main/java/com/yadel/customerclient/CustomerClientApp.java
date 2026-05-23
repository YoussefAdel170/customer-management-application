package com.yadel.customerclient;

import com.yadel.customerclient.ui.MainFrame;
import javax.swing.*;

/**
 * Entry point for the Swing desktop client.
 * @author y.adel
 */
public class CustomerClientApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame().setVisible(true);
        });
    }
}