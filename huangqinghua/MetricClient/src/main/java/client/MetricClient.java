package client;

import Http.MetricHttpClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MetricClient extends JFrame {

    private JTextField endpointField;
    private JTextField metricField;
    private JTextField startTsField;
    private JTextField endTsField;
    private JTable resultTable;
    private MetricHttpClient httpClient;

    public MetricClient() {
        setTitle("Metric Client");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        httpClient = new MetricHttpClient();

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(null);  // 使用绝对布局

        JLabel endpointLabel = new JLabel("Endpoint:");
        endpointLabel.setBounds(20, 20, 100, 25);
        panel.add(endpointLabel);

        endpointField = new JTextField();
        endpointField.setBounds(120, 20, 300, 25);
        panel.add(endpointField);

        JLabel metricLabel = new JLabel("Metric:");
        metricLabel.setBounds(20, 60, 100, 25);
        panel.add(metricLabel);

        metricField = new JTextField();
        metricField.setBounds(120, 60, 300, 25);
        panel.add(metricField);

        JLabel startTsLabel = new JLabel("Start Timestamp:");
        startTsLabel.setBounds(20, 100, 100, 25);
        panel.add(startTsLabel);

        startTsField = new JTextField();
        startTsField.setBounds(120, 100, 300, 25);
        panel.add(startTsField);

        JLabel endTsLabel = new JLabel("End Timestamp:");
        endTsLabel.setBounds(20, 140, 100, 25);
        panel.add(endTsLabel);

        endTsField = new JTextField();
        endTsField.setBounds(120, 140, 300, 25);
        panel.add(endTsField);

        JButton queryButton = new JButton("查询");
        queryButton.setBounds(170, 180, 100, 30);
        queryButton.addActionListener(new QueryButtonListener());
        panel.add(queryButton);

        resultTable = new JTable();
        resultTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Metric", "Timestamp", "Value"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 设置表格为不可编辑状态
            }
        });

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBounds(20, 220, 400, 300);
        panel.add(scrollPane);

        add(panel);
    }


    private class QueryButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 清空表格数据
            DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
            model.setRowCount(0);


            String endpoint = endpointField.getText().trim();
            String metric = metricField.getText().trim();
            String start_ts = startTsField.getText().trim();
            String end_ts = endTsField.getText().trim();

            if (endpoint.isEmpty() || start_ts.isEmpty() || end_ts.isEmpty()) {
                JOptionPane.showMessageDialog(MetricClient.this, "Endpoint, Start Timestamp, and End Timestamp are required.");
                return;
            }

            try {
                long startTimestamp = Long.parseLong(start_ts);
                long endTimestamp = Long.parseLong(end_ts);

                String response = httpClient.queryMetrics(endpoint, metric, startTimestamp, endTimestamp);
                System.out.println(response);

                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response);
                JsonNode dataNode = rootNode.get("data");

                if (dataNode.isArray()) {
                    for (JsonNode metricNode : dataNode) {
                        String metricName = metricNode.get("metric").asText();
                        JsonNode valuesNode = metricNode.get("values");
                        for (JsonNode valueNode : valuesNode) {
                            long timestamp = valueNode.get("timestamp").asLong();
                            double value = valueNode.get("value").asDouble();
                            ((DefaultTableModel) resultTable.getModel()).addRow(new Object[]{metricName, timestamp, value});
                        }
                    }
                }

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(MetricClient.this, "Start Timestamp and End Timestamp must be valid long values.");
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(MetricClient.this, "Error: " + ex.getMessage());
            }
        }
    }

}
