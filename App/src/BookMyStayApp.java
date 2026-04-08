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
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getNextRequest() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 2);
        inventory.put("Double", 1);
    }

    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }
}

class BookingProcessor extends Thread {
    private BookingRequestQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingRequestQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {
            Reservation r;
            synchronized (queue) {
                if (queue.isEmpty()) break;
                r = queue.getNextRequest();
            }

            if (r != null) {
                boolean success = inventory.allocateRoom(r.getRoomType());
                if (success) {
                    System.out.println(Thread.currentThread().getName() +
                            " confirmed booking for " + r.getGuestName() +
                            " (" + r.getRoomType() + ")");
                } else {
                    System.out.println(Thread.currentThread().getName() +
                            " failed booking for " + r.getGuestName() +
                            " (" + r.getRoomType() + ")");
                }
            }
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        String appName = "Book My Stay";
        String version = "v11.0";

        System.out.println("Welcome to " + appName);
        System.out.println("Hotel Booking System " + version);

        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();

        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("David", "Double"));
        queue.addRequest(new Reservation("Eve", "Double"));

        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);

        t1.setName("Thread-1");
        t2.setName("Thread-2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }

        System.out.println("Processing complete");
    }
}