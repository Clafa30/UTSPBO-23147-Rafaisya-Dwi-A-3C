package HotelReservationSystem;

import java.util.ArrayList;
import java.time.LocalDate;

public class Hotel {
    private ArrayList<Room> rooms;
    private ArrayList<Reservation> reservations;

    public Hotel(){
        rooms = new ArrayList<>();
        reservations = new ArrayList<>();
    }

    public void addRoom(Room room){
        rooms.add(room);
    }

    public ArrayList<Room> getRooms(){
        return rooms;
    }

    public ArrayList<Reservation> getReservations(){
        return reservations;
    }

    public void addReservation(Reservation reservation){
        reservations.add(reservation);
    }

    public boolean isRoomAvailable(int roomNumber, LocalDate checkIn, LocalDate checkOut){
        for(Reservation res : reservations){
            if(res.getRoom().getRoomNumber() == roomNumber){
                if(!(checkOut.isBefore(res.getCheckInDate()) || checkIn.isAfter(res.getCheckOutDate()))){
                    return false;
                }
            }
        }
        return true;
    }

    public void cancelReservation(String customerName, int roomNumber){
        Reservation reservationToCancel = null;
        for(Reservation res : reservations){
            if(res.getCustomerName().equalsIgnoreCase(customerName) && res.getRoom().getRoomNumber() == roomNumber){
                reservationToCancel = res;
                break;
            }
        }

        if(reservationToCancel != null){
            reservations.remove(reservationToCancel);
            System.out.println("Reservasi berhasil dibatalkan.");
        } else{
            System.out.println("Reservasi tidak ditemukan.");
        }
    }
}
