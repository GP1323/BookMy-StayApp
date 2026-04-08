import java.util.HashMap;
import java.util.Map;

class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 10);
        inventory.put("Double", 5);
        inventory.put("Suite", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int count) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, count);
        }
    }

    public void displayInventory() {
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Rooms Available: " + entry.getValue());
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        String appName = "Book My Stay";
        String version = "v3.1";

        System.out.println("Welcome to " + appName);
        System.out.println("Hotel Booking System " + version);

        RoomInventory inventory = new RoomInventory();

        System.out.println("\nCurrent Room Inventory:");
        inventory.displayInventory();

        System.out.println("\nUpdating Room Availability...");
        inventory.updateAvailability("Single", 8);
        inventory.updateAvailability("Suite", 1);

        System.out.println("\nUpdated Room Inventory:");
        inventory.displayInventory();
    }
}