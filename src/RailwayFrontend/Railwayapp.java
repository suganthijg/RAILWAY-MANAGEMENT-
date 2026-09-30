package RailwayFrontend;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Component;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import RailwayBean.Railway;
import RailwayDatabase.DBConnection;
import RailwayService.RailwayService;

public class Railwayapp extends JFrame {

    private RailwayService service;

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private JLabel trainCountLabel;
    private JLabel passengerCountLabel;
    private JTable dashboardTrainTable;

    // Train Management
    private JTable trainTable;

    // Search
    private JTable searchTable;

    // Reservations
    private JTable reservationTable;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Railwayapp() {

        service = new RailwayService();

        setTitle(
            "Railway Reservation Management System");

        setSize(1200, 750);

        setDefaultCloseOperation(
            JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        createUI();

        refreshDashboard();
    }


    // =========================================================
    // MAIN UI
    // =========================================================

    private void createUI() {

        setLayout(
            new BorderLayout());


        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
            new JPanel(
                new BorderLayout());

        header.setPreferredSize(
            new Dimension(1200, 75));


        JLabel title =
            new JLabel(
                "RAILWAY RESERVATION MANAGEMENT SYSTEM");

        title.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                25));

        title.setBorder(
            BorderFactory.createEmptyBorder(
                0,
                15,
                0,
                0));


        header.add(
            title,
            BorderLayout.WEST);


        add(
            header,
            BorderLayout.NORTH);


        // =====================================================
        // SIDEBAR
        // =====================================================

        JPanel sidebar =
            new JPanel(
                new GridLayout(
                    8,
                    1,
                    8,
                    8));

        sidebar.setPreferredSize(
            new Dimension(240, 650));

        sidebar.setBorder(
            BorderFactory.createEmptyBorder(
                15,
                8,
                15,
                8));


        JButton dashboardButton =
            new JButton("Dashboard");

        JButton trainButton =
            new JButton("Train Management");

        JButton passengerButton =
            new JButton("Add Passenger");

        JButton searchButton =
            new JButton("Search Train");

        JButton bookButton =
            new JButton("Book Ticket");

        JButton reservationButton =
            new JButton("Reservations");

        JButton cancelButton =
            new JButton("Cancel Seats");

        JButton exitButton =
            new JButton("Exit");


        sidebar.add(dashboardButton);
        sidebar.add(trainButton);
        sidebar.add(passengerButton);
        sidebar.add(searchButton);
        sidebar.add(bookButton);
        sidebar.add(reservationButton);
        sidebar.add(cancelButton);
        sidebar.add(exitButton);


        add(
            sidebar,
            BorderLayout.WEST);


        // =====================================================
        // CARD LAYOUT
        // =====================================================

        cardLayout =
            new CardLayout();

        contentPanel =
            new JPanel(
                cardLayout);


        contentPanel.add(
            createDashboardPanel(),
            "dashboard");

        contentPanel.add(
            createTrainPanel(),
            "train");

        contentPanel.add(
            createPassengerPanel(),
            "passenger");

        contentPanel.add(
            createSearchPanel(),
            "search");

        contentPanel.add(
            createBookingPanel(),
            "booking");

        contentPanel.add(
            createReservationPanel(),
            "reservation");

        contentPanel.add(
            createCancelPanel(),
            "cancel");


        add(
            contentPanel,
            BorderLayout.CENTER);


        // =====================================================
        // EVENTS
        // =====================================================

        dashboardButton.addActionListener(
            e -> {

                refreshDashboard();

                cardLayout.show(
                    contentPanel,
                    "dashboard");
            });


        trainButton.addActionListener(
            e -> {

                refreshTrainTable();

                cardLayout.show(
                    contentPanel,
                    "train");
            });


        passengerButton.addActionListener(
            e -> cardLayout.show(
                contentPanel,
                "passenger"));


        searchButton.addActionListener(
            e -> cardLayout.show(
                contentPanel,
                "search"));


        bookButton.addActionListener(
            e -> cardLayout.show(
                contentPanel,
                "booking"));


        reservationButton.addActionListener(
            e -> {

                refreshReservationTable();

                cardLayout.show(
                    contentPanel,
                    "reservation");
            });


        cancelButton.addActionListener(
            e -> cardLayout.show(
                contentPanel,
                "cancel"));


        exitButton.addActionListener(
            e -> System.exit(0));
    }


    // =========================================================
    // DASHBOARD
    // =========================================================

    private JPanel createDashboardPanel() {

        JPanel panel =
            new JPanel(
                new BorderLayout(
                    15,
                    15));

        panel.setBorder(
            BorderFactory.createEmptyBorder(
                15,
                15,
                15,
                15));


        JLabel heading =
            new JLabel(
                "Dashboard",
                SwingConstants.CENTER);

        heading.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                28));


        panel.add(
            heading,
            BorderLayout.NORTH);


        // =====================================================
        // DASHBOARD CARDS
        // =====================================================

        JPanel cards =
            new JPanel(
                new GridLayout(
                    1,
                    3,
                    15,
                    15));


        trainCountLabel =
            new JLabel(
                "0",
                SwingConstants.CENTER);

        trainCountLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                30));


        passengerCountLabel =
            new JLabel(
                "0",
                SwingConstants.CENTER);

        passengerCountLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                30));


        JLabel reservationLabel =
            new JLabel(
                "BOOK & CANCEL",
                SwingConstants.CENTER);

        reservationLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                18));


        cards.add(
            createDashboardCard(
                "TOTAL TRAINS",
                trainCountLabel));


        cards.add(
            createDashboardCard(
                "TOTAL PASSENGERS",
                passengerCountLabel));


        cards.add(
            createDashboardCard(
                "RESERVATION",
                reservationLabel));


        panel.add(
            cards,
            BorderLayout.NORTH);


        // =====================================================
        // TRAIN TABLE
        // =====================================================

        dashboardTrainTable =
            createTrainTable1();


        JScrollPane scroll =
            new JScrollPane(
                dashboardTrainTable);

        scroll.setBorder(
            BorderFactory.createTitledBorder(
                "Available Trains"));


        panel.add(
            scroll,
            BorderLayout.CENTER);


        // =====================================================
        // REFRESH
        // =====================================================

        JButton refresh =
            new JButton(
                "Refresh Dashboard");


        refresh.addActionListener(
            e -> refreshDashboard());


        panel.add(
            refresh,
            BorderLayout.SOUTH);


        return panel;
    }


    // =========================================================
    // DASHBOARD CARD
    // =========================================================

    private JPanel createDashboardCard(
        String title,
        JLabel value) {

        JPanel card =
            new JPanel(
                new BorderLayout());

        card.setBorder(
            BorderFactory.createTitledBorder(
                title));

        card.setPreferredSize(
            new Dimension(
                200,
                100));


        card.add(
            value,
            BorderLayout.CENTER);


        return card;
    }


    // =========================================================
    // TRAIN TABLE
    // =========================================================

    private JTable createTrainTable1() {

        String[] columns = {

            "Train No",
            "Train Name",
            "Source",
            "Destination",
            "Total Seats",
            "Available",
            "Price"
        };


        DefaultTableModel model =
            new DefaultTableModel(
                columns,
                0) {

                @Override
                public boolean isCellEditable(
                    int row,
                    int column) {

                    return false;
                }
            };


        JTable table =
            new JTable(model);


        table.setRowHeight(30);

        table.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                13));

        table.getTableHeader()
            .setFont(
                new Font(
                    "Arial",
                    Font.BOLD,
                    13));


        // Center everything

        DefaultTableCellRenderer center =
            new DefaultTableCellRenderer();

        center.setHorizontalAlignment(
            SwingConstants.CENTER);


        for (
            int i = 0;
            i < table.getColumnCount();
            i++) {

            table.getColumnModel()
                .getColumn(i)
                .setCellRenderer(center);
        }


        // Column widths

        int[] widths = {

            80,
            160,
            130,
            130,
            100,
            100,
            100
        };


        for (
            int i = 0;
            i < widths.length;
            i++) {

            table.getColumnModel()
                .getColumn(i)
                .setPreferredWidth(
                    widths[i]);
        }


        return table;
    }


    // =========================================================
    // REFRESH DASHBOARD
    // =========================================================

    private void refreshDashboard() {

        try {

            trainCountLabel.setText(
                String.valueOf(
                    getTrainCount()));


            passengerCountLabel.setText(
                String.valueOf(
                    getPassengerCount()));


            loadTrainData(
                dashboardTrainTable);

        }
        catch (Exception e) {

            showError(e);
        }
    }


    // =========================================================
    // GET TRAIN COUNT
    // =========================================================

    private int getTrainCount()
        throws Exception {

        String sql =
            "SELECT COUNT(*) FROM train";


        try (
            Connection con =
                DBConnection.getConnection();

            PreparedStatement ps =
                con.prepareStatement(sql);

            ResultSet rs =
                ps.executeQuery()) {

            if (rs.next()) {

                return rs.getInt(1);
            }
        }

        return 0;
    }


    // =========================================================
    // GET PASSENGER COUNT
    // =========================================================

    private int getPassengerCount()
        throws Exception {

        String sql =
            "SELECT COUNT(*) FROM passenger";


        try (
            Connection con =
                DBConnection.getConnection();

            PreparedStatement ps =
                con.prepareStatement(sql);

            ResultSet rs =
                ps.executeQuery()) {

            if (rs.next()) {

                return rs.getInt(1);
            }
        }

        return 0;
    }


    // =========================================================
    // LOAD TRAIN DATA
    // =========================================================

    private void loadTrainData(
        JTable table)
        throws Exception {

        String sql =
            "SELECT train_no, train_name, source, " +
            "destination, total_seats, " +
            "available_seats, ticket_price " +
            "FROM train " +
            "ORDER BY train_no";


        DefaultTableModel model =
            (DefaultTableModel)
                table.getModel();


        model.setRowCount(0);


        try (
            Connection con =
                DBConnection.getConnection();

            PreparedStatement ps =
                con.prepareStatement(sql);

            ResultSet rs =
                ps.executeQuery()) {


            while (rs.next()) {

                model.addRow(
                    new Object[] {

                        rs.getInt(
                            "train_no"),

                        rs.getString(
                            "train_name"),

                        rs.getString(
                            "source"),

                        rs.getString(
                            "destination"),

                        rs.getInt(
                            "total_seats"),

                        rs.getInt(
                            "available_seats"),

                        "₹" +
                        rs.getDouble(
                            "ticket_price")
                    });
            }
        }
    }


    // =========================================================
    // TRAIN MANAGEMENT
    // =========================================================

    private JPanel createTrainPanel() {

        JPanel panel =
            new JPanel(
                new BorderLayout(
                    15,
                    15));

        panel.setBorder(
            BorderFactory.createEmptyBorder(
                15,
                15,
                15,
                15));


        JLabel heading =
            new JLabel(
                "Train Management",
                SwingConstants.CENTER);

        heading.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                27));


        panel.add(
            heading,
            BorderLayout.NORTH);


        // =====================================================
        // ADD TRAIN FORM
        // =====================================================

        JPanel form =
            new JPanel(
                new GridLayout(
                    2,
                    7,
                    8,
                    8));

        form.setBorder(
            BorderFactory.createTitledBorder(
                "Add New Train"));


        JTextField trainNo =
            new JTextField();

        JTextField trainName =
            new JTextField();

        JTextField source =
            new JTextField();

        JTextField destination =
            new JTextField();

        JTextField totalSeats =
            new JTextField();

        JTextField price =
            new JTextField();


        JButton addTrain =
            new JButton(
                "ADD TRAIN");


        form.add(
            new JLabel(
                "Train No",
                SwingConstants.CENTER));

        form.add(
            new JLabel(
                "Train Name",
                SwingConstants.CENTER));

        form.add(
            new JLabel(
                "Source",
                SwingConstants.CENTER));

        form.add(
            new JLabel(
                "Destination",
                SwingConstants.CENTER));

        form.add(
            new JLabel(
                "Total Seats",
                SwingConstants.CENTER));

        form.add(
            new JLabel(
                "Price",
                SwingConstants.CENTER));

        form.add(
            new JLabel(""));


        form.add(trainNo);
        form.add(trainName);
        form.add(source);
        form.add(destination);
        form.add(totalSeats);
        form.add(price);
        form.add(addTrain);


        panel.add(
            form,
            BorderLayout.NORTH);


        // =====================================================
        // TABLE
        // =====================================================

        trainTable =
            createTrainTable1();


        JScrollPane scroll =
            new JScrollPane(
                trainTable);

        scroll.setBorder(
            BorderFactory.createTitledBorder(
                "Train List"));


        panel.add(
            scroll,
            BorderLayout.CENTER);


        // =====================================================
        // ADD TRAIN EVENT
        // =====================================================

        addTrain.addActionListener(
            e -> {

                try {

                    int no =
                        Integer.parseInt(
                            trainNo.getText());


                    String name =
                        trainName.getText();


                    String from =
                        source.getText();


                    String to =
                        destination.getText();


                    int seats =
                        Integer.parseInt(
                            totalSeats.getText());


                    double ticketPrice =
                        Double.parseDouble(
                            price.getText());


                    Railway train =
                        new Railway(
                            no,
                            name,
                            from,
                            to,
                            seats,
                            seats,
                            ticketPrice);


                    service.addTrain(
                        train);


                    JOptionPane.showMessageDialog(
                        this,
                        "Train added successfully!");


                    trainNo.setText("");
                    trainName.setText("");
                    source.setText("");
                    destination.setText("");
                    totalSeats.setText("");
                    price.setText("");


                    refreshTrainTable();

                    refreshDashboard();

                }
                catch (Exception ex) {

                    showError(ex);
                }
            });


        return panel;
    }


    // =========================================================
    // CREATE TRAIN TABLE
    // =========================================================

    private JTable createTrainTable() {

        return createTrainTableInternal();
    }


    private JTable createTrainTableInternal() {

        String[] columns = {

            "Train No",
            "Train Name",
            "Source",
            "Destination",
            "Total Seats",
            "Available",
            "Price"
        };


        DefaultTableModel model =
            new DefaultTableModel(
                columns,
                0) {

                @Override
                public boolean isCellEditable(
                    int row,
                    int column) {

                    return false;
                }
            };


        JTable table =
            new JTable(model);


        table.setRowHeight(28);

        table.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                13));


        table.getTableHeader()
            .setFont(
                new Font(
                    "Arial",
                    Font.BOLD,
                    13));


        DefaultTableCellRenderer center =
            new DefaultTableCellRenderer();

        center.setHorizontalAlignment(
            SwingConstants.CENTER);


        for (
            int i = 0;
            i < table.getColumnCount();
            i++) {

            table.getColumnModel()
                .getColumn(i)
                .setCellRenderer(center);
        }


        int[] widths = {

            80,
            170,
            130,
            130,
            100,
            100,
            100
        };


        for (
            int i = 0;
            i < widths.length;
            i++) {

            table.getColumnModel()
                .getColumn(i)
                .setPreferredWidth(
                    widths[i]);
        }


        return table;
    }


    // =========================================================
    // REFRESH TRAIN TABLE
    // =========================================================

    private void refreshTrainTable() {

        try {

            loadTrainData(
                trainTable);

        }
        catch (Exception e) {

            showError(e);
        }
    }


    // =========================================================
    // PASSENGER REGISTRATION
    // =========================================================

    private JPanel createPassengerPanel() {

        JPanel panel =
            new JPanel(
                new BorderLayout(
                    15,
                    15));

        panel.setBorder(
            BorderFactory.createEmptyBorder(
                15,
                15,
                15,
                15));


        JLabel heading =
            new JLabel(
                "Passenger Registration",
                SwingConstants.CENTER);

        heading.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                27));


        panel.add(
            heading,
            BorderLayout.NORTH);


        JPanel form =
            new JPanel(
                new GridLayout(
                    5,
                    2,
                    10,
                    10));


        form.setBorder(
            BorderFactory.createTitledBorder(
                "Passenger Details"));


        JTextField name =
            new JTextField();

        JTextField age =
            new JTextField();

        JTextField gender =
            new JTextField();

        JTextField phone =
            new JTextField();


        JButton register =
            new JButton(
                "REGISTER PASSENGER");


        form.add(
            new JLabel(
                "Passenger Name"));

        form.add(name);


        form.add(
            new JLabel(
                "Age"));

        form.add(age);


        form.add(
            new JLabel(
                "Gender"));

        form.add(gender);


        form.add(
            new JLabel(
                "Phone"));

        form.add(phone);


        form.add(
            new JLabel(""));

        form.add(register);


        panel.add(
            form,
            BorderLayout.CENTER);


        register.addActionListener(
            e -> {

                try {

                    String passengerName =
                        name.getText();


                    int passengerAge =
                        Integer.parseInt(
                            age.getText());


                    String passengerGender =
                        gender.getText();


                    String passengerPhone =
                        phone.getText();


                    Railway passenger =
                        new Railway(
                            passengerName,
                            passengerAge,
                            passengerGender,
                            passengerPhone);


                    int id =
                        service.addPassenger(
                            passenger);


                    JOptionPane.showMessageDialog(

                        this,

                        "Passenger registered successfully!\n\n"
                        + "Passenger ID : "
                        + id);


                    name.setText("");
                    age.setText("");
                    gender.setText("");
                    phone.setText("");


                    refreshDashboard();

                }
                catch (Exception ex) {

                    showError(ex);
                }
            });


        return panel;
    }


    // =========================================================
    // SEARCH TRAIN
    // =========================================================

    private JPanel createSearchPanel() {

        JPanel panel =
            new JPanel(
                new BorderLayout(
                    15,
                    15));


        panel.setBorder(
            BorderFactory.createEmptyBorder(
                15,
                15,
                15,
                15));


        JLabel heading =
            new JLabel(
                "Search Train",
                SwingConstants.CENTER);


        heading.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                27));


        panel.add(
            heading,
            BorderLayout.NORTH);


        // =====================================================
        // SEARCH BAR
        // =====================================================

        JPanel searchBar =
            new JPanel();


        JTextField trainNo =
            new JTextField(
                15);


        JButton search =
            new JButton(
                "SEARCH");


        searchBar.add(
            new JLabel(
                "Train Number:"));


        searchBar.add(
            trainNo);


        searchBar.add(
            search);


        panel.add(
            searchBar,
            BorderLayout.CENTER);


        // =====================================================
        // RESULT TABLE
        // =====================================================

        String[] columns = {

            "Train No",
            "Train Name",
            "Source",
            "Destination",
            "Total Seats",
            "Available Seats",
            "Ticket Price"
        };


        DefaultTableModel model =
            new DefaultTableModel(
                columns,
                0) {

                @Override
                public boolean isCellEditable(
                    int row,
                    int column) {

                    return false;
                }
            };


        searchTable =
            new JTable(model);


        searchTable.setRowHeight(
            35);


        searchTable.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                14));


        searchTable.getTableHeader()
            .setFont(
                new Font(
                    "Arial",
                    Font.BOLD,
                    13));


        DefaultTableCellRenderer center =
            new DefaultTableCellRenderer();

        center.setHorizontalAlignment(
            SwingConstants.CENTER);


        for (
            int i = 0;
            i < searchTable.getColumnCount();
            i++) {

            searchTable.getColumnModel()
                .getColumn(i)
                .setCellRenderer(center);
        }


        JScrollPane scroll =
            new JScrollPane(
                searchTable);


        scroll.setBorder(
            BorderFactory.createTitledBorder(
                "Train Details"));


        panel.add(
            scroll,
            BorderLayout.SOUTH);


        // =====================================================
        // SEARCH EVENT
        // =====================================================

        search.addActionListener(
            e -> {

                try {

                    int no =
                        Integer.parseInt(
                            trainNo.getText());


                    Railway train =
                        service.searchTrain(
                            no);


                    model.setRowCount(0);


                    model.addRow(
                        new Object[] {

                            train.getTrainNo(),

                            train.getTrainName(),

                            train.getSource(),

                            train.getDestination(),

                            train.getTotalSeats(),

                            train.getAvailableSeats(),

                            "₹" +
                            train.getTicketPrice()
                        });


                }
                catch (Exception ex) {

                    model.setRowCount(0);

                    showError(ex);
                }
            });


        return panel;
    }


    // =========================================================
    // BOOKING
    // =========================================================

    private JPanel createBookingPanel() {

        JPanel panel =
            new JPanel(
                new BorderLayout(
                    15,
                    15));


        panel.setBorder(
            BorderFactory.createEmptyBorder(
                15,
                15,
                15,
                15));


        JLabel heading =
            new JLabel(
                "Book Train Ticket",
                SwingConstants.CENTER);


        heading.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                27));


        panel.add(
            heading,
            BorderLayout.NORTH);


        JPanel form =
            new JPanel(
                new GridLayout(
                    6,
                    2,
                    10,
                    10));


        form.setBorder(
            BorderFactory.createTitledBorder(
                "Booking Details"));


        JTextField passengerId =
            new JTextField();

        JTextField trainNo =
            new JTextField();

        JTextField journeyDate =
            new JTextField();

        JTextField numberOfSeats =
            new JTextField();

        JTextField seatNumbers =
            new JTextField();


        JButton book =
            new JButton(
                "CONFIRM BOOKING");


        form.add(
            new JLabel(
                "Passenger ID"));

        form.add(
            passengerId);


        form.add(
            new JLabel(
                "Train Number"));

        form.add(
            trainNo);


        form.add(
            new JLabel(
                "Journey Date (YYYY-MM-DD)"));

        form.add(
            journeyDate);


        form.add(
            new JLabel(
                "Number of Seats"));

        form.add(
            numberOfSeats);


        form.add(
            new JLabel(
                "Seat Numbers"));

        form.add(
            seatNumbers);


        form.add(
            new JLabel(
                "Example: 10,11,12"));

        form.add(book);


        panel.add(
            form,
            BorderLayout.CENTER);


        book.addActionListener(
            e -> {

                try {

                    int pId =
                        Integer.parseInt(
                            passengerId.getText());


                    int tNo =
                        Integer.parseInt(
                            trainNo.getText());


                    Date date =
                        Date.valueOf(
                            journeyDate.getText());


                    int count =
                        Integer.parseInt(
                            numberOfSeats.getText());


                    if (count <= 0) {

                        throw new Exception(
                            "Number of seats must be greater than 0.");
                    }


                    String[] values =
                        seatNumbers
                            .getText()
                            .split(",");


                    if (
                        values.length != count) {

                        throw new Exception(
                            "Number of seat numbers must match number of seats.");
                    }


                    List<Integer> seats =
                        new ArrayList<>();


                    for (
                        String value :
                        values) {

                        seats.add(
                            Integer.parseInt(
                                value.trim()));
                    }


                    Railway train =
                        service.searchTrain(
                            tNo);


                    if (
                        count >
                        train.getAvailableSeats()) {

                        throw new Exception(
                            "Only "
                            + train.getAvailableSeats()
                            + " seats available.");
                    }


                    double total =
                        train.getTicketPrice()
                        * count;


                    int confirm =
                        JOptionPane.showConfirmDialog(

                            this,

                            "Train : "
                            + train.getTrainName()

                            + "\nSeats : "
                            + seats

                            + "\nPrice / Seat : ₹"
                            + train.getTicketPrice()

                            + "\nTotal Price : ₹"
                            + total,

                            "Confirm Booking",

                            JOptionPane.YES_NO_OPTION);


                    if (
                        confirm !=
                        JOptionPane.YES_OPTION) {

                        return;
                    }


                    int bookingId =
                        service.bookTickets(
                            pId,
                            tNo,
                            date,
                            seats);


                    JOptionPane.showMessageDialog(

                        this,

                        "TICKET BOOKED SUCCESSFULLY!\n\n"

                        + "Booking ID : "
                        + bookingId

                        + "\nPassenger ID : "
                        + pId

                        + "\nTrain : "
                        + train.getTrainName()

                        + "\nJourney Date : "
                        + date

                        + "\nSeats : "
                        + seats

                        + "\nPrice / Seat : ₹"
                        + train.getTicketPrice()

                        + "\nTotal Price : ₹"
                        + total);


                    passengerId.setText("");
                    trainNo.setText("");
                    journeyDate.setText("");
                    numberOfSeats.setText("");
                    seatNumbers.setText("");


                    refreshDashboard();

                }
                catch (Exception ex) {

                    showError(ex);
                }
            });


        return panel;
    }


    // =========================================================
    // RESERVATIONS
    // =========================================================

    private JPanel createReservationPanel() {

        JPanel panel =
            new JPanel(
                new BorderLayout(
                    15,
                    15));


        panel.setBorder(
            BorderFactory.createEmptyBorder(
                15,
                15,
                15,
                15));


        JLabel heading =
            new JLabel(
                "Reservation Details",
                SwingConstants.CENTER);


        heading.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                27));


        panel.add(
            heading,
            BorderLayout.NORTH);


        String[] columns = {

            "Reservation ID",
            "Booking ID",
            "Passenger",
            "Train No",
            "Train Name",
            "Journey Date",
            "Seat",
            "Price",
            "Status"
        };


        DefaultTableModel model =
            new DefaultTableModel(
                columns,
                0) {

                @Override
                public boolean isCellEditable(
                    int row,
                    int column) {

                    return false;
                }
            };


        reservationTable =
            new JTable(model);


        reservationTable.setRowHeight(
            28);


        reservationTable.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                12));


        reservationTable.getTableHeader()
            .setFont(
                new Font(
                    "Arial",
                    Font.BOLD,
                    12));


        DefaultTableCellRenderer center =
            new DefaultTableCellRenderer();

        center.setHorizontalAlignment(
            SwingConstants.CENTER);


        for (
            int i = 0;
            i < reservationTable.getColumnCount();
            i++) {

            reservationTable
                .getColumnModel()
                .getColumn(i)
                .setCellRenderer(center);
        }


        JScrollPane scroll =
            new JScrollPane(
                reservationTable);


        scroll.setBorder(
            BorderFactory.createTitledBorder(
                "All Reservations"));


        panel.add(
            scroll,
            BorderLayout.CENTER);


        JButton refresh =
            new JButton(
                "REFRESH RESERVATIONS");


        refresh.addActionListener(
            e -> refreshReservationTable());


        panel.add(
            refresh,
            BorderLayout.SOUTH);


        return panel;
    }


    // =========================================================
    // REFRESH RESERVATION TABLE
    // =========================================================

    private void refreshReservationTable() {

        try {

            String sql =

                "SELECT r.reservation_id, " +
                "r.booking_id, " +
                "p.name, " +
                "t.train_no, " +
                "t.train_name, " +
                "r.journey_date, " +
                "r.seat_no, " +
                "r.ticket_price, " +
                "r.status " +

                "FROM reservation r " +

                "JOIN passenger p " +
                "ON r.passenger_id = p.passenger_id " +

                "JOIN train t " +
                "ON r.train_no = t.train_no " +

                "ORDER BY r.booking_id, r.seat_no";


            DefaultTableModel model =
                (DefaultTableModel)
                    reservationTable.getModel();


            model.setRowCount(0);


            try (
                Connection con =
                    DBConnection.getConnection();

                PreparedStatement ps =
                    con.prepareStatement(sql);

                ResultSet rs =
                    ps.executeQuery()) {


                while (rs.next()) {

                    model.addRow(
                        new Object[] {

                            rs.getInt(
                                "reservation_id"),

                            rs.getInt(
                                "booking_id"),

                            rs.getString(
                                "name"),

                            rs.getInt(
                                "train_no"),

                            rs.getString(
                                "train_name"),

                            rs.getDate(
                                "journey_date"),

                            rs.getInt(
                                "seat_no"),

                            "₹" +
                            rs.getDouble(
                                "ticket_price"),

                            rs.getString(
                                "status")
                        });
                }
            }

        }
        catch (Exception e) {

            showError(e);
        }
    }


    // =========================================================
    // CANCEL SEATS
    // =========================================================

    private JPanel createCancelPanel() {

        JPanel panel =
            new JPanel(
                new BorderLayout(
                    15,
                    15));


        panel.setBorder(
            BorderFactory.createEmptyBorder(
                15,
                15,
                15,
                15));


        JLabel heading =
            new JLabel(
                "Cancel Seats",
                SwingConstants.CENTER);


        heading.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                27));


        panel.add(
            heading,
            BorderLayout.NORTH);


        JPanel form =
            new JPanel(
                new GridLayout(
                    3,
                    2,
                    10,
                    10));


        form.setBorder(
            BorderFactory.createTitledBorder(
                "Cancellation Details"));


        JTextField bookingId =
            new JTextField();


        JTextField seatNumbers =
            new JTextField();


        JButton cancel =
            new JButton(
                "CANCEL SEATS");


        form.add(
            new JLabel(
                "Booking ID"));

        form.add(
            bookingId);


        form.add(
            new JLabel(
                "Seat Numbers"));

        form.add(
            seatNumbers);


        form.add(
            new JLabel(
                "Example: 10,11,12"));

        form.add(cancel);


        panel.add(
            form,
            BorderLayout.CENTER);


        cancel.addActionListener(
            e -> {

                try {

                    int id =
                        Integer.parseInt(
                            bookingId.getText());


                    String[] values =
                        seatNumbers
                            .getText()
                            .split(",");


                    List<Integer> seats =
                        new ArrayList<>();


                    for (
                        String value :
                        values) {

                        seats.add(
                            Integer.parseInt(
                                value.trim()));
                    }


                    int confirm =
                        JOptionPane.showConfirmDialog(

                            this,

                            "Booking ID : "
                            + id

                            + "\nSeats : "
                            + seats

                            + "\n\nAre you sure?",

                            "Confirm Cancellation",

                            JOptionPane.YES_NO_OPTION);


                    if (
                        confirm !=
                        JOptionPane.YES_OPTION) {

                        return;
                    }


                    service.cancelSeats(
                        id,
                        seats);


                    JOptionPane.showMessageDialog(

                        this,

                        "Seats cancelled successfully!\n\n"

                        + "Booking ID : "
                        + id

                        + "\nCancelled Seats : "
                        + seats);


                    bookingId.setText("");
                    seatNumbers.setText("");


                    refreshDashboard();

                }
                catch (Exception ex) {

                    showError(ex);
                }
            });


        return panel;
    }


    // =========================================================
    // ERROR MESSAGE
    // =========================================================

    private void showError(
        Exception e) {

        JOptionPane.showMessageDialog(

            this,

            e.getMessage(),

            "Error",

            JOptionPane.ERROR_MESSAGE);
    }


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
        String[] args) {

        SwingUtilities.invokeLater(
            () -> {

                Railwayapp app =
                    new Railwayapp();

                app.setVisible(true);
            });
    }
}