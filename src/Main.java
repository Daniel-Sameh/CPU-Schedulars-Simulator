import process.PriorityProcess;
import process.Process;
import schedular.IScheduler;
import schedular.PriorityScheduler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Create JFrame for GUI
        JFrame frame = new JFrame("CPU Scheduling Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Panel for Scheduler Selection
        JPanel topPanel = new JPanel(new FlowLayout());
        JComboBox<String> schedulerDropdown = new JComboBox<>(new String[]{"Priority Scheduler"});
        topPanel.add(new JLabel("Select Scheduler:"));
        topPanel.add(schedulerDropdown);
        frame.add(topPanel, BorderLayout.NORTH);

        // Panel for Process Input and Table
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Input Panel for Process Attributes
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        JTextField processIdField = new JTextField();
        JTextField burstTimeField = new JTextField();
        JTextField priorityField = new JTextField();
        JTextField arrivalTimeField = new JTextField();
        JButton addProcessButton = new JButton("Add Process");

        inputPanel.add(new JLabel("Process ID:"));
        inputPanel.add(processIdField);
        inputPanel.add(new JLabel("Priority:"));
        inputPanel.add(priorityField);
        inputPanel.add(new JLabel("Burst Time:"));
        inputPanel.add(burstTimeField);
        inputPanel.add(new JLabel("Arrival Time:"));
        inputPanel.add(arrivalTimeField);
        inputPanel.add(addProcessButton);

        centerPanel.add(inputPanel, BorderLayout.NORTH);

        // Process Table
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Process ID", "Priority", "Burst Time", "Arrival Time"}, 0);
        JTable processTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(processTable);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        frame.add(centerPanel, BorderLayout.CENTER);

        // Panel for Simulation Results
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.drawString("Graph will be displayed here", 10, 20);
            }
        };
        graphPanel.setPreferredSize(new Dimension(600, 200));
        graphPanel.setBackground(Color.LIGHT_GRAY);

        bottomPanel.add(graphPanel, BorderLayout.CENTER);

        JLabel awtLabel = new JLabel("AWT: ");
        JLabel atatLabel = new JLabel("ATAT: ");
        JPanel statsPanel = new JPanel(new FlowLayout());
        statsPanel.add(awtLabel);
        statsPanel.add(atatLabel);

        bottomPanel.add(statsPanel, BorderLayout.SOUTH);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Button to Simulate the Scheduler
        JButton simulateButton = new JButton("Simulate");
        topPanel.add(simulateButton);

        // Action Listeners
        ArrayList<Process> processes = new ArrayList<>();

        addProcessButton.addActionListener(e -> {
            // Get input values
            try {
                int pid = Integer.parseInt(processIdField.getText());
                int priority = Integer.parseInt(priorityField.getText());
                int burstTime = Integer.parseInt(burstTimeField.getText());
                int arrivalTime = Integer.parseInt(arrivalTimeField.getText());

                // Create a new Process and add it to the table
                Process process = new PriorityProcess(pid, arrivalTime, burstTime, priority);
                processes.add(process);
                tableModel.addRow(new Object[]{pid, priority, burstTime, arrivalTime});

                // Clear input fields
                processIdField.setText("");
                priorityField.setText("");
                burstTimeField.setText("");
                arrivalTimeField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input! Please enter numbers only.");
            }
        });

        simulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get selected scheduler
                String selectedScheduler = (String) schedulerDropdown.getSelectedItem();
                IScheduler scheduler;

                // Choose scheduler based on selection
                if ("Priority Scheduler".equals(selectedScheduler)) {
                    scheduler = new PriorityScheduler(processes);
                } else {
                    JOptionPane.showMessageDialog(frame, "Unknown scheduler selected.");
                    return;
                }

                // Run the scheduler
                scheduler.run();

                // Update statistics and graph
//                awtLabel.setText("AWT: " + scheduler.getAverageWaitingTime());
//                atatLabel.setText("ATAT: " + scheduler.getAverageTurnaroundTime());

                // Example Graph Drawing (replace with actual logic)
                graphPanel.repaint();
            }
        });

        // Display the GUI
        frame.setVisible(true);
    }
}
