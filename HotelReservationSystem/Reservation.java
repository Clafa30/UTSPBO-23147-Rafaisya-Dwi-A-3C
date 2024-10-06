package HotelReservationSystem;

import java.time.LocalDate;

public class Reservation {
    private String customerName;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public Reservation(String customerName, Room room, LocalDate checkInDate, LocalDate checkOutDate){
        this.customerName = customerName;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public String getCustomerName(){
        return customerName;
    }

    public Room getRoom(){
        return room;
    }

    public LocalDate getCheckInDate(){
        return checkInDate;
    }

    public LocalDate getCheckOutDate(){
        return checkOutDate;
    }
}
