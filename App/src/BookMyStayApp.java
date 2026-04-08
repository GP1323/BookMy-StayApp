import java.util.*;

class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }
}

class AddOnServiceManager {
    private Map<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    public double calculateTotalCost(String reservationId) {
        double total = 0;
        List<AddOnService> services = serviceMap.get(reservationId);
        if (services != null) {
            for (AddOnService s : services) {
                total += s.getCost();
            }
        }
        return total;
    }

    public void displayServices(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);
        if (services != null && !services.isEmpty()) {
            System.out.println("Services for Reservation ID: " + reservationId);
            for (AddOnService s : services) {
                System.out.println(s.getName() + " - " + s.getCost());
            }
        } else {
            System.out.println("No services selected for Reservation ID: " + reservationId);
        }
    }
}

public class UseCase7AddOnServiceSelection {
    public static void main(String[] args) {
        String appName = "Book My Stay";
        String version = "v7.0";

        System.out.println("Welcome to " + appName);
        System.out.println("Hotel Booking System " + version);

        AddOnServiceManager manager = new AddOnServiceManager();

        String reservationId1 = "S1";
        String reservationId2 = "D1";

        manager.addService(reservationId1, new AddOnService("Breakfast", 500));
        manager.addService(reservationId1, new AddOnService("WiFi", 200));

        manager.addService(reservationId2, new AddOnService("Airport Pickup", 1000));

        manager.displayServices(reservationId1);
        System.out.println("Total Cost: " + manager.calculateTotalCost(reservationId1));

        System.out.println();

        manager.displayServices(reservationId2);
        System.out.println("Total Cost: " + manager.calculateTotalCost(reservationId2));
    }
}