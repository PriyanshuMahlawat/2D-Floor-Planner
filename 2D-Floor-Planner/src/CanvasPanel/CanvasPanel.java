package CanvasPanel;

import Room.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CanvasPanel extends Canvas {
    private final int rows = 70;
    private final int columns = 150;
    private int cellSize;
    private Room selectedRoom; // The currently selected room for deletion

    public CanvasPanel() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        cellSize = Math.min(screenWidth / columns, screenHeight / rows);
        setPreferredSize(new Dimension(columns * cellSize, rows * cellSize));

        // Add mouse listener to select a room on click
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectRoomAt(e.getX(), e.getY());
            }
        });
    }

    public void addRoom(String[] alignment, String roomName) {
        int prevRoomIndex = Integer.parseInt(alignment[0]);
        String direction = alignment[1];
        String alignmentPosition = alignment[2];
        int length = Integer.parseInt(alignment[3]);
        int breadth = Integer.parseInt(alignment[4]);

        int x = 0, y = 0;

        if (prevRoomIndex == 0) {
            // Place the first room in the center of the canvas
            x = (columns / 2) * cellSize - (breadth / 2);
            y = (rows / 2) * cellSize - (length / 2);
        } else {
            Room prevRoom = Room.getRooms().get(prevRoomIndex); // Get the previously added room
            int prevX = prevRoom.getX();
            int prevY = prevRoom.getY();
            int prevLength = prevRoom.getLength();
            int prevBreadth = prevRoom.getWidth();

            // Calculate new room position based on direction
            switch (direction) {
                case "North" -> {
                    x = prevX;
                    y = prevY - length;
                }
                case "South" -> {
                    x = prevX;
                    y = prevY + prevLength;
                }
                case "East" -> {
                    x = prevX + prevBreadth;
                    y = prevY;
                }
                case "West" -> {
                    x = prevX - breadth;
                    y = prevY;
                }
            }

            // Adjust alignment based on direction
            if (direction.equals("North") || direction.equals("South")) {
                switch (alignmentPosition) {
                    case "Left" -> x = prevX; // Align left edges
                    case "Right" -> x = prevX + prevBreadth - breadth; // Align right edges
                }
            } else if (direction.equals("East") || direction.equals("West")) {
                switch (alignmentPosition) {
                    case "Top" -> y = prevY; // Align top edges
                    case "Bottom" -> y = prevY + prevLength - length; // Align bottom edges
                }
            }
        }

        Room newRoom = new Room(x, y, length, breadth, roomName);
        repaint();  // Refresh canvas to draw the new room
    }

    public void removeRoom(Room room) {
        Room.removeRoom(room);
        repaint();  // Refresh canvas after removing the room
    }

    public void selectRoomAt(int x, int y) {
        for (Room room : Room.getRooms()) {
            if (x >= room.getX() && x <= room.getX() + room.getWidth() &&
                y >= room.getY() && y <= room.getY() + room.getLength()) {
                selectedRoom = room;
                break;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Room room : Room.getRooms()) {
            room.draw(g);  // Draw each room on the canvas
        }
    }
}
