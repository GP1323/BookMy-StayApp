import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String toString() {
        return guestName + " (" + roomType + ")";
    }
}

class RoomInventory implements Serializable {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 2);
        inventory.put("Double", 1);
    }

    public boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void releaseRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        inventory.put(roomType, available + 1);
    }

    public String toString() {
        return inventory.toString();
    }
}

class PersistenceService {
    public static void saveState(RoomInventory inventory, List<Reservation> history, String filePath) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(inventory);
            out.writeObject(history);
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    public static Object[] loadState(String filePath) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            RoomInventory inventory = (RoomInventory) in.readObject();
            List<Reservation> history = (List<Reservation>) in.readObject();
            return new Object[]{inventory, history};
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous state found or failed to load. Starting fresh.");
            return null;
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        String filePath = "bookingState.dat";

        Object[] state = PersistenceService.loadState(filePath);
        RoomInventory inventory;
        List<Reservation> bookingHistory;

        if (state != null) {
            inventory = (RoomInventory) state[0];
            bookingHistory = (List<Reservation>) state[1];
            System.out.println("Restored previous state.");
        } else {
            inventory = new RoomInventory();
            bookingHistory = new ArrayList<>();
            System.out.println("Initialized fresh state.");
        }

        Reservation r1 = new Reservation("Alice", "Single");
        if (inventory.allocateRoom(r1.getRoomType())) bookingHistory.add(r1);
        Reservation r2 = new Reservation("Bob", "Double");
        if (inventory.allocateRoom(r2.getRoomType())) bookingHistory.add(r2);

        System.out.println("Current Inventory: " + inventory);
        System.out.println("Booking History: " + bookingHistory);

        PersistenceService.saveState(inventory, bookingHistory, filePath);
        System.out.println("State saved. Application can be restarted safely.");
    }
}