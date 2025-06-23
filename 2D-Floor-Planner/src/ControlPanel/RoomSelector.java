package ControlPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import Room.Room;

public class RoomSelector extends JDialog {
    private JComboBox<String> roomList;
    private JComboBox<String> directionList;
    private JComboBox<String> alignmentList;
    private JButton submitButton;
    private String[] result; // Store the latest result as a single String array

    public RoomSelector(Frame parent, List<String> rooms) {
        super(parent, "Room Selector", true); // Modal dialog

        setSize(400, 400);
        setLayout(new GridLayout(7, 2, 5, 5)); // Adjusted layout for better spacing

        result = new String[]{"0", "None", "None", "0", "0"}; // Default values: index, direction, alignment, length, breadth

        // Remove duplicates from the list of rooms
        Set<String> uniqueRooms = new HashSet<>(rooms);
        rooms = List.copyOf(uniqueRooms); // Create a new list with unique room names

        if (rooms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No rooms available. Defaulting to 'None'.", "Info", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the dialog as no input is required
            return;
        }

        // Room selection
        add(new JLabel("Select Room:"));
        roomList = new JComboBox<>(rooms.toArray(new String[0]));
        add(roomList);

        // Direction selection
        add(new JLabel("Select Direction:"));
        String[] directions = {"North", "South", "East", "West"};
        directionList = new JComboBox<>(directions);
        add(directionList);

        // Alignment selection
        add(new JLabel("Select Alignment:"));
        alignmentList = new JComboBox<>();
        updateAlignmentOptions();
        add(alignmentList);

        // Update alignment options based on direction
        directionList.addActionListener(e -> updateAlignmentOptions());

        // Submit button
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitListener());
        add(submitButton);

        // Empty placeholder for grid alignment
        add(new JLabel());
    }

    private void updateAlignmentOptions() {
        alignmentList.removeAllItems();
        String selectedDirection = (String) directionList.getSelectedItem();
        if (selectedDirection != null) {
            if (selectedDirection.equals("North") || selectedDirection.equals("South")) {
                alignmentList.addItem("Left");
                alignmentList.addItem("Right");
            } else {
                alignmentList.addItem("Top");
                alignmentList.addItem("Bottom");
            }
        }
    }

    private class SubmitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRoomIndex = roomList.getSelectedIndex(); // Get the selected room index
            String selectedDirection = (String) directionList.getSelectedItem();
            String selectedAlignment = (String) alignmentList.getSelectedItem();

            try {
                if (selectedRoomIndex != -1 && selectedDirection != null && selectedAlignment != null) {
                    // Get the actual room object based on the selected index
                    Room selectedRoom = Room.getRooms().get(selectedRoomIndex);

                    result[0] = String.valueOf(Room.getRooms().indexOf(selectedRoom)); // Store the room's index in the list
                    result[1] = selectedDirection;
                    result[2] = selectedAlignment;

                    JOptionPane.showMessageDialog(RoomSelector.this, "Input saved!");
                } else {
                    throw new Exception("Invalid selection");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(RoomSelector.this, "Invalid selection. Defaulting to 'None' values.", "Info", JOptionPane.INFORMATION_MESSAGE);
                result = new String[]{"0", "None", "None", "0", "0"}; // Reset to default
            }

            dispose(); // Close the RoomSelector window
        }
    }

    public String[] getResult() {
        return result;
    }
}
