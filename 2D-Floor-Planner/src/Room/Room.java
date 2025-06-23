package Room;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private static List<Room> roomsList = new ArrayList<>();
    private int x, y;
    private int length, width;
    private String roomName;

    public Room(int x, int y, int length, int width, String roomName) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.width = width;
        this.roomName = roomName;

        roomsList.add(this);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public String getName() {
        return roomName;
    }

    public static List<Room> getRooms() {
        return roomsList;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, length);

        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(roomName);
        int textHeight = fm.getHeight();

        int textX = x + (width - textWidth) / 2;
        int textY = y + (length + textHeight / 2) / 2;

        g.drawString(roomName, textX, textY);
    }

    public static void addRoom(Room room) {
        roomsList.add(room);
    }

    public static int getNextRoomIndex(String roomType) {
        return (int) roomsList.stream()
                .filter(room -> room.getName().startsWith(roomType))
                .count();
    }

    // Remove a room from the list by reference
    public static void removeRoom(Room room) {
        roomsList.remove(room);  // Removes the room from the list
    }
}
