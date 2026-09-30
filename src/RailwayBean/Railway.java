package RailwayBean;

import java.sql.Date;

public class Railway {

    private int trainNo;
    private String trainName;
    private String source;
    private String destination;
    private int totalSeats;
    private int availableSeats;
    private double ticketPrice;

    private int passengerId;
    private String passengerName;
    private int age;
    private String gender;
    private String phone;

    private int reservationId;
    private int bookingId;
    private Date journeyDate;
    private int seatNo;
    private String status;

    public Railway() {
    }

    // Train constructor
    public Railway(int trainNo, String trainName,
                   String source, String destination,
                   int totalSeats, int availableSeats,
                   double ticketPrice) {

        this.trainNo = trainNo;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.ticketPrice = ticketPrice;
    }

    // Passenger constructor
    public Railway(String passengerName, int age,
                   String gender, String phone) {

        this.passengerName = passengerName;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
    }

    // Reservation constructor
    public Railway(int passengerId, int trainNo,
                   Date journeyDate, int seatNo,
                   double ticketPrice, String status) {

        this.passengerId = passengerId;
        this.trainNo = trainNo;
        this.journeyDate = journeyDate;
        this.seatNo = seatNo;
        this.ticketPrice = ticketPrice;
        this.status = status;
    }

    public int getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(int trainNo) {
        this.trainNo = trainNo;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public Date getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(Date journeyDate) {
        this.journeyDate = journeyDate;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {

        return "Train No: " + trainNo +
               ", Train Name: " + trainName +
               ", From: " + source +
               ", To: " + destination +
               ", Available Seats: " + availableSeats +
               ", Ticket Price: ₹" + ticketPrice;
    }
}