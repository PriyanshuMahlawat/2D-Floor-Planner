package Launcher;

import CanvasPanel.CanvasPanel;
import Room.Room;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import ControlPanel.RoomSelector;
    

public class MainWindow extends JFrame {
    private CanvasPanel canvas;

    public MainWindow() {
        setTitle("Room Manager");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        canvas = new CanvasPanel();
        add(canvas, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Rooms");

        JMenuItem createRoomItem = new JMenuItem("Create Room");
        createRoomItem.addActionListener(e -> createRoom("Room", canvas));
        menu.add(createRoomItem);

        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    public void createRoom(String roomType, CanvasPanel canvas) {
        // Create a list of room descriptions
        List<String> roomDescriptions = Room.getRooms().stream()
                .map(room -> room.getName() + " (" + room.getX() + ", " + room.getY() + ")")
                .collect(Collectors.toList());

        if (roomDescriptions.isEmpty()) {
            // No rooms exist, allow user to create the first one
            String lengthStr = JOptionPane.showInputDialog(this, "Enter the length of the room:");
            String widthStr = JOptionPane.showInputDialog(this, "Enter the width of the room:");

            if (lengthStr != null && widthStr != null) {
                try {
                    int length = Integer.parseInt(lengthStr);
                    int width = Integer.parseInt(widthStr);

                    String roomName = getNextRoomName(roomType);
                    String[] alignment = new String[]{"0", "None", "None", String.valueOf(length), String.valueOf(width)};
                    canvas.addRoom(alignment, roomName);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input for length or width.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Length and width must be provided.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Existing rooms exist, use RoomSelector for new room creation
            RoomSelector roomSelector = new RoomSelector(this, roomDescriptions);
            roomSelector.setVisible(true);

            String[] alignments = roomSelector.getResult();

            if (alignments != null) {
                // Extract selected room index, direction, and alignment from result
                int prevRoomIndex = Integer.parseInt(alignments[0]); // Get the previous room index
                String direction = alignments[1];
                String alignmentPosition = alignments[2];

                String roomName = getNextRoomName(roomType);
                String lengthStr = JOptionPane.showInputDialog(this, "Enter the length of the room:");
                String widthStr = JOptionPane.showInputDialog(this, "Enter the width of the room:");

                if (lengthStr != null && widthStr != null) {
                    try {
                        int length = Integer.parseInt(lengthStr);
                        int width = Integer.parseInt(widthStr);

                        String[] alignment = new String[]{String.valueOf(prevRoomIndex), direction, alignmentPosition, String.valueOf(length), String.valueOf(width)};
                        canvas.addRoom(alignment, roomName);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid input for length or width.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Length and width must be provided.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private String getNextRoomName(String roomType) {
        return roomType + (Room.getRooms().size() + 1);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
