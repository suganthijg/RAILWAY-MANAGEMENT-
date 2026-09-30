package RailwayControl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import RailwayBean.Railway;
import RailwayException.RailwayException;
import RailwayService.RailwayService;

public class RailwayController {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        RailwayService service = new RailwayService();

        while (true) {

            System.out.println();
            System.out.println("===== RAILWAY RESERVATION SYSTEM =====");
            System.out.println("1. Add Train");
            System.out.println("2. Display Trains");
            System.out.println("3. Search Train");
            System.out.println("4. Add Passenger");
            System.out.println("5. Book Ticket");
            System.out.println("6. View Reservations");
            System.out.println("7. Cancel Ticket");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            try {

                int choice = sc.nextInt();

                switch (choice) {

                    // =====================================
                    // 1. ADD TRAIN
                    // =====================================

                    case 1:

                        System.out.print(
                            "Enter Train Number: ");

                        int trainNo = sc.nextInt();
                        sc.nextLine();

                        System.out.print(
                            "Enter Train Name: ");

                        String trainName =
                            sc.nextLine();

                        System.out.print(
                            "Enter Source: ");

                        String source =
                            sc.nextLine();

                        System.out.print(
                            "Enter Destination: ");

                        String destination =
                            sc.nextLine();

                        System.out.print(
                            "Enter Total Seats: ");

                        int totalSeats =
                            sc.nextInt();

                        if (totalSeats <= 0) {

                            throw new RailwayException(
                                "Total seats must be greater than 0.");
                        }

                        // NEW: Ticket price

                        System.out.print(
                            "Enter Ticket Price: ");

                        double ticketPrice =
                            sc.nextDouble();

                        if (ticketPrice <= 0) {

                            throw new RailwayException(
                                "Ticket price must be greater than 0.");
                        }

                        Railway train =
                            new Railway(
                                trainNo,
                                trainName,
                                source,
                                destination,
                                totalSeats,
                                totalSeats,
                                ticketPrice);

                        service.addTrain(train);

                        System.out.println(
                            "Train added successfully.");

                        break;


                    // =====================================
                    // 2. DISPLAY TRAINS
                    // =====================================

                    case 2:

                        service.getAllTrains();

                        break;


                    // =====================================
                    // 3. SEARCH TRAIN
                    // =====================================

                    case 3:

                        System.out.print(
                            "Enter Train Number: ");

                        int searchTrainNo =
                            sc.nextInt();

                        System.out.println(
                            service.searchTrain(
                                searchTrainNo));

                        break;


                    // =====================================
                    // 4. ADD PASSENGER
                    // =====================================

                    case 4:

                        sc.nextLine();

                        System.out.print(
                            "Enter Passenger Name: ");

                        String name =
                            sc.nextLine();

                        System.out.print(
                            "Enter Age: ");

                        int age =
                            sc.nextInt();

                        sc.nextLine();

                        System.out.print(
                            "Enter Gender: ");

                        String gender =
                            sc.nextLine();

                        System.out.print(
                            "Enter Phone: ");

                        String phone =
                            sc.nextLine();

                        Railway passenger =
                            new Railway(
                                name,
                                age,
                                gender,
                                phone);

                        service.addPassenger(
                            passenger);

                        System.out.println(
                            "Passenger added successfully.");

                        break;


                    // =====================================
                    // 5. BOOK MULTIPLE SEATS
                    // =====================================

                    case 5:

                        System.out.print(
                            "Enter Passenger ID: ");

                        int passengerId =
                            sc.nextInt();

                        System.out.println(
                            "\nSelect a train for booking:");

                        service.getAllTrains();

                        System.out.print(
                            "\nEnter Train Number: ");

                        int bookingTrainNo =
                            sc.nextInt();

                        sc.nextLine();

                        System.out.print(
                            "Enter Journey Date (YYYY-MM-DD): ");

                        Date journeyDate =
                            Date.valueOf(
                                sc.nextLine());

                        Railway selectedTrain =
                            service.searchTrain(
                                bookingTrainNo);

                        System.out.println();

                        System.out.println(
                            "Selected Train: " +
                            selectedTrain.getTrainName());

                        System.out.println(
                            "Route: " +
                            selectedTrain.getSource() +
                            " to " +
                            selectedTrain.getDestination());

                        System.out.println(
                            "Available Seats: " +
                            selectedTrain.getAvailableSeats());

                        System.out.println(
                            "Ticket Price per Seat: ₹" +
                            selectedTrain.getTicketPrice());

                        // =================================
                        // NEW: NUMBER OF SEATS
                        // =================================

                        System.out.print(
                            "\nHow many seats do you want? ");

                        int numberOfSeats =
                            sc.nextInt();

                        if (numberOfSeats <= 0) {

                            throw new RailwayException(
                                "Number of seats must be greater than 0.");
                        }

                        if (numberOfSeats >
                            selectedTrain.getAvailableSeats()) {

                            throw new RailwayException(
                                "Only " +
                                selectedTrain.getAvailableSeats() +
                                " seats are available.");
                        }

                        // =================================
                        // GET EACH SEAT NUMBER
                        // =================================

                        List<Integer> seatNumbers =
                            new ArrayList<>();

                        for (int i = 1;
                             i <= numberOfSeats;
                             i++) {

                            System.out.print(
                                "Enter Seat Number " +
                                i +
                                " of " +
                                numberOfSeats +
                                ": ");

                            int seatNo =
                                sc.nextInt();

                            seatNumbers.add(seatNo);
                        }

                        // =================================
                        // CALCULATE TOTAL PRICE
                        // =================================

                        double totalPrice =
                            selectedTrain.getTicketPrice()
                            * numberOfSeats;

                        System.out.println();

                        System.out.println(
                            "Selected Seats: " +
                            seatNumbers);

                        System.out.println(
                            "Price per Seat: ₹" +
                            selectedTrain.getTicketPrice());

                        System.out.println(
                            "Total Price: ₹" +
                            totalPrice);

                        System.out.print(
                            "Confirm booking? (yes/no): ");

                        sc.nextLine();

                        String confirmation =
                            sc.nextLine();

                        if (!confirmation.equalsIgnoreCase("yes")) {

                            System.out.println(
                                "Booking cancelled by user.");

                            break;
                        }

                        // =================================
                        // CALL NEW SERVICE METHOD
                        // =================================

                        int bookingId =
                            service.bookTickets(
                                passengerId,
                                bookingTrainNo,
                                journeyDate,
                                seatNumbers);

                        System.out.println();

                        System.out.println(
                            "================================");

                        System.out.println(
                            "       BOOKING SUCCESSFUL");

                        System.out.println(
                            "================================");

                        System.out.println(
                            "Booking ID    : " +
                            bookingId);

                        System.out.println(
                            "Passenger ID  : " +
                            passengerId);

                        System.out.println(
                            "Train         : " +
                            selectedTrain.getTrainName());

                        System.out.println(
                            "Route         : " +
                            selectedTrain.getSource() +
                            " to " +
                            selectedTrain.getDestination());

                        System.out.println(
                            "Journey Date  : " +
                            journeyDate);

                        System.out.println(
                            "Seats         : " +
                            seatNumbers);

                        System.out.println(
                            "No. of Seats  : " +
                            numberOfSeats);

                        System.out.println(
                            "Price/Seat    : ₹" +
                            selectedTrain.getTicketPrice());

                        System.out.println(
                            "Total Price   : ₹" +
                            totalPrice);

                        System.out.println(
                            "Status        : CONFIRMED");

                        System.out.println(
                            "================================");

                        break;


                    // =====================================
                    // 6. VIEW RESERVATIONS
                    // =====================================

                    case 6:

                        service.getReservations();

                        break;


                    // =====================================
                    // 7. CANCEL MULTIPLE SEATS
                    // =====================================

                    case 7:

                        System.out.print(
                            "Enter Booking ID: ");

                        int bookingId1 =
                            sc.nextInt();

                        System.out.print(
                            "How many seats do you want to cancel? ");

                        int cancelCount =
                            sc.nextInt();

                        if (cancelCount <= 0) {

                            throw new RailwayException(
                                "Number of seats must be greater than 0.");
                        }

                        List<Integer> cancelSeats =
                            new ArrayList<>();

                        for (int i = 1;
                             i <= cancelCount;
                             i++) {

                            System.out.print(
                                "Enter Seat Number " +
                                i +
                                " of " +
                                cancelCount +
                                ": ");

                            int seatNo =
                                sc.nextInt();

                            cancelSeats.add(seatNo);
                        }

                        System.out.println();

                        System.out.println(
                            "Booking ID: " +
                            bookingId1);

                        System.out.println(
                            "Seats to cancel: " +
                            cancelSeats);

                        System.out.print(
                            "Confirm cancellation? (yes/no): ");

                        sc.nextLine();

                        String cancelConfirmation =
                            sc.nextLine();

                        if (!cancelConfirmation
                                .equalsIgnoreCase("yes")) {

                            System.out.println(
                                "Cancellation stopped.");

                            break;
                        }

                        // =================================
                        // CALL NEW SERVICE METHOD
                        // =================================

                        service.cancelSeats(
                            bookingId1,
                            cancelSeats);

                        System.out.println();

                        System.out.println(
                            "Cancellation completed successfully.");

                        break;


                    // =====================================
                    // 8. EXIT
                    // =====================================

                    case 8:

                        System.out.println(
                            "Thank you!");

                        sc.close();

                        return;


                    // =====================================
                    // INVALID CHOICE
                    // =====================================

                    default:

                        System.out.println(
                            "Invalid choice.");
                }

            } catch (RailwayException e) {

                System.out.println(
                    "Error: " +
                    e.getMessage());

            } catch (Exception e) {

                System.out.println(
                    "Error: " +
                    e.getMessage());
            }
        }
   
    }
}
