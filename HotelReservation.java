// Code Alpha Internship -  TASK-4
// TASK - 4 ( Hotel Reservation System )
// Follow on GitHub -> @Warrior-Gosai

package hotelreservation;

import java.sql.*;
import java.util.*;

public class HotelReservation {

    // JDBC Config
    static final String URL = "jdbc:mysql://localhost:3306/hotel_db";
    static final String USER = "root";
    static final String PASS = "1234";

    // ===== Database Utility =====
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // ===== Room Management =====
    static void searchRooms(String category) {
        String sql = "SELECT * FROM rooms WHERE category=? AND is_booked=FALSE";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nAvailable " + category + " Rooms:");
            boolean found = false;
            while (rs.next()) {
                System.out.println("Room ID: " + rs.getInt("room_id") +
                        " | Price: ₹" + rs.getDouble("price"));
                found = true;
            }
            if (!found)
                System.out.println("No available " + category + " rooms found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ===== Booking =====
    static void bookRoom(String guestName, String category, String checkIn, String checkOut, String paymentMode) {
        try (Connection conn = getConnection()) {
            // Find available room
            String findRoom = "SELECT * FROM rooms WHERE category=? AND is_booked=FALSE LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(findRoom);
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int roomId = rs.getInt("room_id");
                double price = rs.getDouble("price");

                // Payment simulation
                if (!simulatePayment(paymentMode, price)) {
                    System.out.println("\nPayment failed. Booking not completed.");
                    return;
                }

                // Insert reservation
                String insertRes = "INSERT INTO reservations (guest_name, room_id, check_in, check_out, payment_mode) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps2 = conn.prepareStatement(insertRes);
                ps2.setString(1, guestName);
                ps2.setInt(2, roomId);
                ps2.setString(3, checkIn);
                ps2.setString(4, checkOut);
                ps2.setString(5, paymentMode);
                ps2.executeUpdate();

                // Mark room as booked
                String updateRoom = "UPDATE rooms SET is_booked=TRUE WHERE room_id=?";
                PreparedStatement ps3 = conn.prepareStatement(updateRoom);
                ps3.setInt(1, roomId);
                ps3.executeUpdate();

                System.out.println("\nBooking confirmed for " + guestName + "!");
                System.out.println("Room ID: " + roomId + " (" + category + ")");
                System.out.println("Check-in: " + checkIn + " | Check-out: " + checkOut);
                System.out.println("Payment Mode: " + paymentMode + " | Total Paid: ₹" + price);
            } else {
                System.out.println("\nNo " + category + " rooms available right now.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ===== Cancel Reservation =====
    static void cancelReservation(int reservationId) {
        try (Connection conn = getConnection()) {
            String findRes = "SELECT * FROM reservations WHERE reservation_id=?";
            PreparedStatement ps = conn.prepareStatement(findRes);
            ps.setInt(1, reservationId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int roomId = rs.getInt("room_id");

                // Delete reservation
                PreparedStatement ps2 = conn.prepareStatement("DELETE FROM reservations WHERE reservation_id=?");
                ps2.setInt(1, reservationId);
                ps2.executeUpdate();

                // Free the room
                PreparedStatement ps3 = conn.prepareStatement("UPDATE rooms SET is_booked=FALSE WHERE room_id=?");
                ps3.setInt(1, roomId);
                ps3.executeUpdate();

                System.out.println("\nReservation cancelled successfully!");
            } else {
                System.out.println("\nReservation not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ===== View Bookings =====
    static void viewAllReservations() {
        String sql = "SELECT r.reservation_id, r.guest_name, rm.room_id, rm.category," +
"                           rm.price, r.check_in, r.check_out, r.payment_mode" +
"                    FROM reservations r" +
"                    JOIN rooms rm ON r.room_id = rm.room_id";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n=== All Bookings ===");
            boolean found = false;
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("reservation_id") +
                        " | Guest: " + rs.getString("guest_name") +
                        " | Room: " + rs.getInt("room_id") +
                        " (" + rs.getString("category") + ")" +
                        " | Price: ₹" + rs.getDouble("price") +
                        " | " + rs.getString("check_in") +
                        " ==> " + rs.getString("check_out") +
                        " | Payment: " + rs.getString("payment_mode"));
                found = true;
            }
            if (!found)
                System.out.println("No bookings found.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ===== Payment Simulation =====
    static boolean simulatePayment(String paymentMode, double amount) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n💰 Payment Simulation");
        System.out.println("Total amount: ₹" + amount);
        if (paymentMode.equalsIgnoreCase("card")) {
            System.out.print("Enter card number (4 digits): ");
            String card = sc.nextLine();
            if (card.length() == 4) {
                System.out.println("Card payment of ₹" + amount + " successful!");
                return true;
            } else {
                System.out.println("Invalid card number!");
                return false;
            }
        } else if (paymentMode.equalsIgnoreCase("cash")) {
            System.out.println("Cash payment received: ₹" + amount);
            return true;
        } else {
            System.out.println("Unknown payment mode!");
            return false;
        }
    }

    // ===== Main Menu =====
    public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int choice;

    do {
        System.out.println("\n=== HOTEL RESERVATION SYSTEM ===");
        System.out.println("1. Search Available Rooms");
        System.out.println("2. Book a Room");
        System.out.println("3. Cancel Reservation");
        System.out.println("4. View All Bookings");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
        choice = sc.nextInt();
        sc.nextLine(); // consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter category (Standard/Deluxe/Suite): ");
                String category = sc.nextLine();
                searchRooms(category);
                break;

            case 2:
                System.out.print("Enter guest name: ");
                String name = sc.nextLine();
                System.out.print("Enter room category: ");
                String cat = sc.nextLine();
                System.out.print("Enter check-in date: ");
                String in = sc.nextLine();
                System.out.print("Enter check-out date: ");
                String out = sc.nextLine();
                System.out.print("Enter payment mode (cash/card): ");
                String pay = sc.nextLine();
                bookRoom(name, cat, in, out, pay);
                break;

            case 3:
                System.out.print("Enter reservation ID: ");
                int id = sc.nextInt();
                cancelReservation(id);
                break;

            case 4:
                viewAllReservations();
                break;

            case 5:
                System.out.println("\nThank you for using Hotel Reservation System!");
                break;

            default:
                System.out.println("Invalid option! Try again.");
        }
    } while (choice != 5);
    sc.close();
   }
}