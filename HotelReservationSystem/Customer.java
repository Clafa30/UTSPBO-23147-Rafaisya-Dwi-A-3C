package HotelReservationSystem;

import java.time.LocalDate;

public class Customer extends User {

    public Customer(String username, String password){
        super(username, password);
    }

    public Reservation makeReservation(Hotel hotel, Room room, LocalDate checkInDate, LocalDate checkOutDate){
        if(hotel.isRoomAvailable(room.getRoomNumber(), checkInDate, checkOutDate)){
            Reservation reservation = new Reservation(getUsername(), room, checkInDate, checkOutDate);
            hotel.addReservation(reservation);
            System.out.println("Reservasi berhasil dibuat.");
            return reservation;
        } else{
            System.out.println("Kamar tidak tersedia untuk tanggal yang dipilih.");
            return null;
        }
    }
}
