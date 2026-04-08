import java.util.HashMap;
import java.util.Map;

class Room {
    private String type;
    private double price;

    public Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }
}

class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 10);
        inventory.put("Double", 0);
        inventory.put("Suite", 3);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getAllInventory() {
        return inventory;
    }
}

class SearchService {
    private RoomInventory inventory;
    private Map<String, Room> roomDetails;

    public SearchService(RoomInventory inventory) {
        this.inventory = inventory;
        roomDetails = new HashMap<>();
        roomDetails.put("Single", new Room("Single", 2000));
        roomDetails.put("Double", new Room("Double", 3500));
        roomDetails.put("Suite", new Room("Suite", 6000));
    }

    public void searchAvailableRooms() {
        for (String type : inventory.getAllInventory().keySet()) {
            int available = inventory.getAvailability(type);
            if (available > 0) {
                Room room = roomDetails.get(type);
                System.out.println("Room Type: " + room.getType());
                System.out.println("Price: " + room.getPrice());
                System.out.println("Available: " + available);
                System.out.println("---------------------------");
            }
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        String appName = "Book My Stay";
        String version = "v4.0";

        System.out.println("Welcome to " + appName);
        System.out.println("Hotel Booking System " + version);

        RoomInventory inventory = new RoomInventory();
        SearchService searchService = new SearchService(inventory);

        System.out.println("\nAvailable Rooms:");
        searchService.searchAvailableRooms();
    }
}