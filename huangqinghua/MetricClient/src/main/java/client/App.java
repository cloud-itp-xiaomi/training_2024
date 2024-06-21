package client;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MetricClient client = new MetricClient();
            client.setVisible(true);
        });
    }
}
