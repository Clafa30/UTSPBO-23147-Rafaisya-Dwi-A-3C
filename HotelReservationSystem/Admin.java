package HotelReservationSystem;

import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

public class Admin extends User {

    public Admin(String username, String password){
        super(username, password);
    }

    public void checkAvailability(Hotel hotel){
        boolean availableRoomsFound = false;
        System.out.println("\n--- Ketersediaan Kamar ---");
        for(Room room : hotel.getRooms()){
            if(room.isAvailable()){
                availableRoomsFound = true;
                System.out.println("Kamar " + room.getRoomNumber() + " - " + room.getRoomType() + " - Rp." + room.getPrice());
            }
        }
        if(!availableRoomsFound){
            System.out.println("Tidak ada kamar yang tersedia.");
        }
        System.out.println("---------------------------\n");
    }

    public void viewReservations(Hotel hotel){
        ArrayList<Reservation> reservations = hotel.getReservations();
        if(reservations.isEmpty()){
            System.out.println("Tidak ada reservasi yang ditemukan.");
            return;
        }
        System.out.println("\n--- Daftar Reservasi ---");
        for(Reservation reservation : reservations){
            printReservationDetails(reservation);
        }
        System.out.println("-------------------------\n");
    }

    public void cancelReservation(Hotel hotel, String customerName, int roomNumber){
        ArrayList<Reservation> reservations = hotel.getReservations();
        boolean found = false;
        Reservation reservationToCancel = null;

        for(Reservation res : reservations){
            if(res.getCustomerName().equalsIgnoreCase(customerName) && res.getRoom().getRoomNumber() == roomNumber){
                reservationToCancel = res;
                found = true;
                break;
            }
        }

        if(found && reservationToCancel != null){
            reservations.remove(reservationToCancel);
            System.out.println("Reservasi berhasil dibatalkan.");
        } else{
            System.out.println("Reservasi tidak ditemukan.");
        }
    }

    private void printReservationDetails(Reservation reservation){
        if(reservation != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            System.out.println("\n--- DETAIL RESERVASI ---");
            System.out.println("Nama Customer: " + reservation.getCustomerName());
            Room room = reservation.getRoom();
            if(room != null){
                System.out.printf(
                    "Nomor Kamar: %d\nJenis Kamar: %s\nHarga Kamar: Rp.%.1f\n",
                    room.getRoomNumber(), room.getRoomType(), room.getPrice()
                );
            }
            System.out.printf(
                "Tanggal Check-In: %s\nTanggal Check-Out: %s\n",
                reservation.getCheckInDate().format(formatter), 
                reservation.getCheckOutDate().format(formatter)
            );
            System.out.println("-----------------------\n");
        } else{
            System.out.println("Reservasi tidak ditemukan.");
        }
    }
}
