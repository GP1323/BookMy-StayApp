import java.util.*;

class Reservation {
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
}

class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, inventory.get(roomType) - 1);
        }
    }
}

class BookingService {
    private RoomInventory inventory;
    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> roomAllocations;
    private int counter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRoomIds = new HashSet<>();
        roomAllocations = new HashMap<>();
    }

    public void processRequest(Reservation reservation) {
        String type = reservation.getRoomType();

        if (inventory.getAvailability(type) > 0) {
            String roomId = generateRoomId(type);

            allocatedRoomIds.add(roomId);

            roomAllocations.putIfAbsent(type, new HashSet<>());
            roomAllocations.get(type).add(roomId);

            inventory.decrement(type);

            System.out.println("Booking Confirmed for " + reservation.getGuestName() +
                    " | Room Type: " + type + " | Room ID: " + roomId);
        } else {
            System.out.println("Booking Failed for " + reservation.getGuestName() +
                    " | No rooms available for type: " + type);
        }
    }

    private String generateRoomId(String type) {
        String roomId;
        do {
            roomId = type.substring(0, 1).toUpperCase() + counter++;
        } while (allocatedRoomIds.contains(roomId));
        return roomId;
    }
}

public class UseCase6RoomAllocationService {
    public static void main(String[] args) {
        String appName = "Book My Stay";
        String version = "v6.0";

        System.out.println("Welcome to " + appName);
        System.out.println("Hotel Booking System " + version);

        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Double"));
        queue.addRequest(new Reservation("Charlie", "Suite"));
        queue.addRequest(new Reservation("David", "Suite"));

        BookingService bookingService = new BookingService(inventory);

        while (!queue.isEmpty()) {
            Reservation reservation = queue.getNextRequest();
            bookingService.processRequest(reservation);
        }
    }
}