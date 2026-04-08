import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 1);
        inventory.put("Double", 1);
        inventory.put("Suite", 0);
    }

    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    public void validateAvailability(String roomType) throws InvalidBookingException {
        if (inventory.get(roomType) <= 0) {
            throw new InvalidBookingException("No availability for room type: " + roomType);
        }
    }

    public void decrement(String roomType) throws InvalidBookingException {
        int current = inventory.get(roomType);
        if (current <= 0) {
            throw new InvalidBookingException("Cannot decrement. No rooms available for: " + roomType);
        }
        inventory.put(roomType, current - 1);
    }
}

class BookingService {
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void confirmBooking(Reservation reservation) {
        try {
            String type = reservation.getRoomType();

            inventory.validateRoomType(type);
            inventory.validateAvailability(type);
            inventory.decrement(type);

            System.out.println("Booking Confirmed for " + reservation.getGuestName() +
                    " | Room Type: " + type);
        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed for " + reservation.getGuestName() +
                    " | Reason: " + e.getMessage());
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        String appName = "Book My Stay";
        String version = "v9.0";

        System.out.println("Welcome to " + appName);
        System.out.println("Hotel Booking System " + version);

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        bookingService.confirmBooking(new Reservation("Alice", "Single"));
        bookingService.confirmBooking(new Reservation("Bob", "Suite"));
        bookingService.confirmBooking(new Reservation("Charlie", "Deluxe"));
        bookingService.confirmBooking(new Reservation("David", "Double"));
    }
}