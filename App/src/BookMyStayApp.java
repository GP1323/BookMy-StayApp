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

class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    public List<Reservation> getAllReservations() {
        return history;
    }
}

class BookingReportService {
    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    public void displayAllBookings() {
        for (Reservation r : history.getAllReservations()) {
            System.out.println("Reservation ID: " + r.getReservationId());
            System.out.println("Guest: " + r.getGuestName());
            System.out.println("Room Type: " + r.getRoomType());
            System.out.println("------------------------");
        }
    }

    public void generateSummary() {
        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : history.getAllReservations()) {
            summary.put(r.getRoomType(),
                    summary.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("Booking Summary:");
        for (String type : summary.keySet()) {
            System.out.println(type + ": " + summary.get(type));
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        String appName = "Book My Stay";
        String version = "v8.0";

        System.out.println("Welcome to " + appName);
        System.out.println("Hotel Booking System " + version);

        BookingHistory history = new BookingHistory();

        history.addReservation(new Reservation("S1", "Alice", "Single"));
        history.addReservation(new Reservation("D1", "Bob", "Double"));
        history.addReservation(new Reservation("S2", "Charlie", "Single"));
        history.addReservation(new Reservation("SU1", "David", "Suite"));

        BookingReportService reportService = new BookingReportService(history);

        System.out.println("\nAll Bookings:");
        reportService.displayAllBookings();

        System.out.println("\nSummary Report:");
        reportService.generateSummary();
    }
}