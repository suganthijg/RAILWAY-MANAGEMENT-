package RailwayService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.List;

import RailwayBean.Railway;
import RailwayDatabase.DBConnection;
import RailwayException.RailwayException;

public class RailwayService {

    // =========================
    // ADD TRAIN
    // =========================

    public void addTrain(Railway railway) throws Exception {

        String sql =
            "INSERT INTO train " +
            "(train_no, train_name, source, destination, " +
            "total_seats, available_seats, ticket_price) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, railway.getTrainNo());
            ps.setString(2, railway.getTrainName());
            ps.setString(3, railway.getSource());
            ps.setString(4, railway.getDestination());
            ps.setInt(5, railway.getTotalSeats());
            ps.setInt(6, railway.getAvailableSeats());
            ps.setDouble(7, railway.getTicketPrice());

            ps.executeUpdate();
        }
    }

    // =========================
    // DISPLAY TRAINS
    // =========================

    public String getAllTrains() throws Exception {

        String sql = "SELECT * FROM train";

        StringBuilder result = new StringBuilder();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            while (rs.next()) {

                found = true;

                result.append(
                    "Train No: " +
                    rs.getInt("train_no") +
                    " | " +
                    rs.getString("train_name") +
                    " | " +
                    rs.getString("source") +
                    " → " +
                    rs.getString("destination") +
                    " | Available: " +
                    rs.getInt("available_seats") +
                    " | Price: ₹" +
                    rs.getDouble("ticket_price") +
                    "\n"
                );
            }

            if (!found) {
                return "No trains available.";
            }
        }

        return result.toString();
    }

    // =========================
    // SEARCH TRAIN
    // =========================

    public Railway searchTrain(int trainNo) throws Exception {

        String sql =
            "SELECT * FROM train WHERE train_no = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, trainNo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Railway railway = new Railway();

                    railway.setTrainNo(
                        rs.getInt("train_no"));

                    railway.setTrainName(
                        rs.getString("train_name"));

                    railway.setSource(
                        rs.getString("source"));

                    railway.setDestination(
                        rs.getString("destination"));

                    railway.setTotalSeats(
                        rs.getInt("total_seats"));

                    railway.setAvailableSeats(
                        rs.getInt("available_seats"));

                    railway.setTicketPrice(
                        rs.getDouble("ticket_price"));

                    return railway;
                }
            }
        }

        throw new RailwayException("Train not found.");
    }

    // =========================
    // ADD PASSENGER
    // =========================

    public int addPassenger(Railway railway)
            throws Exception {

        String sql =
            "INSERT INTO passenger " +
            "(name, age, gender, phone) " +
            "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                 con.prepareStatement(
                     sql,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(
                1, railway.getPassengerName());

            ps.setInt(
                2, railway.getAge());

            ps.setString(
                3, railway.getGender());

            ps.setString(
                4, railway.getPhone());

            ps.executeUpdate();

            try (ResultSet rs =
                     ps.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new RailwayException(
            "Passenger could not be added.");
    }

    // =========================
    // BOOK MULTIPLE SEATS
    // =========================

    public int bookTickets(
            int passengerId,
            int trainNo,
            Date journeyDate,
            List<Integer> seatNumbers)
            throws Exception {

        if (seatNumbers == null ||
            seatNumbers.isEmpty()) {

            throw new RailwayException(
                "Please select at least one seat.");
        }

        try (Connection con =
                 DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {

                // -------------------------
                // Check passenger
                // -------------------------

                String passengerSql =
                    "SELECT name FROM passenger " +
                    "WHERE passenger_id = ?";

                String passengerName;

                try (PreparedStatement ps =
                         con.prepareStatement(
                             passengerSql)) {

                    ps.setInt(1, passengerId);

                    try (ResultSet rs =
                             ps.executeQuery()) {

                        if (!rs.next()) {

                            throw new RailwayException(
                                "Passenger not found.");
                        }

                        passengerName =
                            rs.getString("name");
                    }
                }

                // -------------------------
                // Get train
                // -------------------------

                String trainSql =
                    "SELECT train_name, source, destination, " +
                    "total_seats, available_seats, ticket_price " +
                    "FROM train WHERE train_no = ?";

                String trainName;
                String source;
                String destination;
                int totalSeats;
                int availableSeats;
                double ticketPrice;

                try (PreparedStatement ps =
                         con.prepareStatement(trainSql)) {

                    ps.setInt(1, trainNo);

                    try (ResultSet rs =
                             ps.executeQuery()) {

                        if (!rs.next()) {

                            throw new RailwayException(
                                "Train not found.");
                        }

                        trainName =
                            rs.getString("train_name");

                        source =
                            rs.getString("source");

                        destination =
                            rs.getString("destination");

                        totalSeats =
                            rs.getInt("total_seats");

                        availableSeats =
                            rs.getInt("available_seats");

                        ticketPrice =
                            rs.getDouble("ticket_price");
                    }
                }

                // -------------------------
                // Remove duplicate seats
                // -------------------------

                for (int i = 0;
                     i < seatNumbers.size();
                     i++) {

                    for (int j = i + 1;
                         j < seatNumbers.size();
                         j++) {

                        if (seatNumbers.get(i)
                            .equals(seatNumbers.get(j))) {

                            throw new RailwayException(
                                "Seat " +
                                seatNumbers.get(i) +
                                " selected more than once.");
                        }
                    }
                }

                // -------------------------
                // Check number of seats
                // -------------------------

                if (seatNumbers.size() >
                    availableSeats) {

                    throw new RailwayException(
                        "Only " +
                        availableSeats +
                        " seats are available.");
                }

                // -------------------------
                // Generate booking ID
                // -------------------------

                int bookingId =
                    (int)(System.currentTimeMillis()
                    % 100000000);

                // -------------------------
                // Insert every seat
                // -------------------------

                String seatCheckSql =
                    "SELECT reservation_id " +
                    "FROM reservation " +
                    "WHERE train_no = ? " +
                    "AND journey_date = ? " +
                    "AND seat_no = ? " +
                    "AND status = 'CONFIRMED'";

                String insertSql =
                    "INSERT INTO reservation " +
                    "(booking_id, passenger_id, train_no, " +
                    "journey_date, seat_no, ticket_price, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, 'CONFIRMED')";

                for (int seatNo : seatNumbers) {

                    // Check seat range

                    if (seatNo < 1 ||
                        seatNo > totalSeats) {

                        throw new RailwayException(
                            "Invalid seat number: " +
                            seatNo);
                    }

                    // Check already booked

                    try (PreparedStatement ps =
                             con.prepareStatement(
                                 seatCheckSql)) {

                        ps.setInt(1, trainNo);
                        ps.setDate(2, journeyDate);
                        ps.setInt(3, seatNo);

                        try (ResultSet rs =
                                 ps.executeQuery()) {

                            if (rs.next()) {

                                throw new RailwayException(
                                    "Seat " +
                                    seatNo +
                                    " is already booked.");
                            }
                        }
                    }

                    // Insert reservation

                    try (PreparedStatement ps =
                             con.prepareStatement(
                                 insertSql)) {

                        ps.setInt(1, bookingId);
                        ps.setInt(2, passengerId);
                        ps.setInt(3, trainNo);
                        ps.setDate(4, journeyDate);
                        ps.setInt(5, seatNo);
                        ps.setDouble(6, ticketPrice);

                        ps.executeUpdate();
                    }
                }

                // -------------------------
                // Reduce available seats
                // -------------------------

                String updateSql =
                    "UPDATE train " +
                    "SET available_seats = " +
                    "available_seats - ? " +
                    "WHERE train_no = ? " +
                    "AND available_seats >= ?";

                try (PreparedStatement ps =
                         con.prepareStatement(updateSql)) {

                    ps.setInt(
                        1, seatNumbers.size());

                    ps.setInt(2, trainNo);

                    ps.setInt(
                        3, seatNumbers.size());

                    int updated =
                        ps.executeUpdate();

                    if (updated == 0) {

                        throw new RailwayException(
                            "Not enough seats available.");
                    }
                }

                con.commit();

                // -------------------------
                // Print ticket
                // -------------------------

                double totalPrice =
                    ticketPrice *
                    seatNumbers.size();

                System.out.println();
                System.out.println(
                    "================================");

                System.out.println(
                    "       TICKET BOOKED");

                System.out.println(
                    "================================");

                System.out.println(
                    "Booking ID     : " +
                    bookingId);

                System.out.println(
                    "Passenger      : " +
                    passengerName);

                System.out.println(
                    "Train          : " +
                    trainName);

                System.out.println(
                    "Route          : " +
                    source +
                    " → " +
                    destination);

                System.out.println(
                    "Journey Date   : " +
                    journeyDate);

                System.out.println(
                    "Seats          : " +
                    seatNumbers);

                System.out.println(
                    "Price / Seat   : ₹" +
                    ticketPrice);

                System.out.println(
                    "Total Price    : ₹" +
                    totalPrice);

                System.out.println(
                    "================================");

                return bookingId;

            } catch (Exception e) {

                con.rollback();

                throw e;

            } finally {

                con.setAutoCommit(true);
            }
        }
    }

    // =========================
    // DISPLAY RESERVATIONS
    // =========================

    public String getReservations()
            throws Exception {

        String sql =
            "SELECT r.reservation_id, " +
            "r.booking_id, p.name, " +
            "t.train_no, t.train_name, " +
            "r.journey_date, r.seat_no, " +
            "r.ticket_price, r.status " +
            "FROM reservation r " +
            "JOIN passenger p " +
            "ON r.passenger_id = p.passenger_id " +
            "JOIN train t " +
            "ON r.train_no = t.train_no " +
            "ORDER BY r.booking_id, r.seat_no";

        StringBuilder result =
            new StringBuilder();

        try (Connection con =
                 DBConnection.getConnection();
             PreparedStatement ps =
                 con.prepareStatement(sql);
             ResultSet rs =
                 ps.executeQuery()) {

            boolean found = false;

            while (rs.next()) {

                found = true;

                result.append(
                    "Reservation ID: " +
                    rs.getInt("reservation_id") +
                    " | Booking ID: " +
                    rs.getInt("booking_id") +
                    " | Passenger: " +
                    rs.getString("name") +
                    " | Train: " +
                    rs.getInt("train_no") +
                    " | Seat: " +
                    rs.getInt("seat_no") +
                    " | Price: ₹" +
                    rs.getDouble("ticket_price") +
                    " | Status: " +
                    rs.getString("status") +
                    "\n"
                );
            }

            if (!found) {
                return "No reservations.";
            }
        }

        return result.toString();
    }

    // =========================
    // CANCEL MULTIPLE SEATS
    // =========================

    public void cancelSeats(
            int bookingId,
            List<Integer> seatNumbers)
            throws Exception {

        if (seatNumbers == null ||
            seatNumbers.isEmpty()) {

            throw new RailwayException(
                "Select at least one seat.");
        }

        try (Connection con =
                 DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {

                String findSql =
                    "SELECT train_no, status " +
                    "FROM reservation " +
                    "WHERE booking_id = ? " +
                    "AND seat_no = ?";

                String cancelSql =
                    "UPDATE reservation " +
                    "SET status = 'CANCELLED' " +
                    "WHERE booking_id = ? " +
                    "AND seat_no = ? " +
                    "AND status = 'CONFIRMED'";

                String updateTrainSql =
                    "UPDATE train " +
                    "SET available_seats = " +
                    "available_seats + ? " +
                    "WHERE train_no = ?";

                int trainNo = 0;
                int cancelledCount = 0;

                for (int seatNo : seatNumbers) {

                    // Find reservation

                    try (PreparedStatement ps =
                             con.prepareStatement(
                                 findSql)) {

                        ps.setInt(1, bookingId);
                        ps.setInt(2, seatNo);

                        try (ResultSet rs =
                                 ps.executeQuery()) {

                            if (!rs.next()) {

                                throw new RailwayException(
                                    "Seat " +
                                    seatNo +
                                    " not found in booking.");
                            }

                            trainNo =
                                rs.getInt("train_no");

                            String status =
                                rs.getString("status");

                            if (!"CONFIRMED"
                                .equals(status)) {

                                throw new RailwayException(
                                    "Seat " +
                                    seatNo +
                                    " is already cancelled.");
                            }
                        }
                    }

                    // Cancel reservation

                    try (PreparedStatement ps =
                             con.prepareStatement(
                                 cancelSql)) {

                        ps.setInt(1, bookingId);
                        ps.setInt(2, seatNo);

                        int rows =
                            ps.executeUpdate();

                        if (rows == 1) {
                            cancelledCount++;
                        }
                    }
                }

                // Return seats to train

                try (PreparedStatement ps =
                         con.prepareStatement(
                             updateTrainSql)) {

                    ps.setInt(1, cancelledCount);
                    ps.setInt(2, trainNo);

                    ps.executeUpdate();
                }

                con.commit();

                System.out.println(
                    cancelledCount +
                    " seat(s) cancelled successfully.");

            } catch (Exception e) {

                con.rollback();

                throw e;

            } finally {

                con.setAutoCommit(true);
            }
        }
    }
}