import java.util.*;

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
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
        inventory.put("Suite", 1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

class BookingHistory {
    private Map<String, Reservation> bookings;

    public BookingHistory() {
        bookings = new HashMap<>();
    }

    public void addReservation(Reservation reservation) {
        bookings.put(reservation.getReservationId(), reservation);
    }

    public Reservation getReservation(String reservationId) {
        return bookings.get(reservationId);
    }

    public void removeReservation(String reservationId) {
        bookings.remove(reservationId);
    }

    public boolean exists(String reservationId) {
        return bookings.containsKey(reservationId);
    }
}

class CancellationService {
    private RoomInventory inventory;
    private BookingHistory history;
    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        rollbackStack = new Stack<>();
    }

    public void cancel(String reservationId) {
        if (!history.exists(reservationId)) {
            System.out.println("Cancellation Failed | Reservation not found: " + reservationId);
            return;
        }

        Reservation reservation = history.getReservation(reservationId);
        String roomType = reservation.getRoomType();

        rollbackStack.push(reservationId);
        inventory.increment(roomType);
        history.removeReservation(reservationId);

        System.out.println("Cancellation Successful for " + reservation.getGuestName() +
                " | Room Type: " + roomType + " | Reservation ID: " + reservationId);
    }

    public void showRollbackStack() {
        System.out.println("Rollback Stack: " + rollbackStack);
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        String appName = "Book My Stay";
        String version = "v10.0";

        System.out.println("Welcome to " + appName);
        System.out.println("Hotel Booking System " + version);

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("S1", "Alice", "Single");
        Reservation r2 = new Reservation("D1", "Bob", "Double");

        history.addReservation(r1);
        history.addReservation(r2);

        inventory.decrement("Single");
        inventory.decrement("Double");

        CancellationService service = new CancellationService(inventory, history);

        service.cancel("S1");
        service.cancel("X1");

        service.showRollbackStack();
    }
}