package HotelReservationSystem;

import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Main {
    private static ArrayList<User> users = new ArrayList<>(); 

    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        hotel.addRoom(new Room(101, "Deluxe", 150000));
        hotel.addRoom(new Room(102, "Standard", 100000));

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nApp Reservasi Hotel ResV!\n");
            System.out.println("Pilih opsi berikut:");
            System.out.println("1. Daftar sebagai Admin");
            System.out.println("2. Daftar sebagai Customer");
            System.out.println("3. Masuk");
            System.out.println("4. Keluar");

            System.out.print("\nMasukan Pilihan Kamu : ");
            int choice = 0;
            try {
                String inputChoice = scanner.nextLine().trim();
                choice = Integer.parseInt(inputChoice);
            } catch (NumberFormatException e) {
                System.out.println("\nInput harus berupa angka.");
                continue;
            }

            switch (choice) {
                case 1:
                    registerAdmin(scanner);
                    break;
                case 2:
                    registerCustomer(scanner);
                    break;
                case 3:
                    login(scanner, hotel);
                    break;
                case 4:
                    exit = true; 
                    System.out.println("Salam Hangat ResV Hotel!");
                    break;
                default:
                    System.out.println("\nPilihan tidak valid");
            }
        }

        scanner.close();
    }

    private static boolean isUsernameTaken(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    private static void registerAdmin(Scanner scanner) {
        System.out.print("Masukkan username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Masukkan password: ");
        String password = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("\nUsername dan password tidak boleh kosong");
            return;
        }

        if (isUsernameTaken(username)) {
            System.out.println("\nUsername sudah digunakan. Silakan pilih username lain.");
            return;
        }

        Admin admin = new Admin(username, password);
        users.add(admin); 
        System.out.println("\nAkun Admin berhasil dibuat!");
    }

    private static void registerCustomer(Scanner scanner) {
        System.out.print("Masukkan username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Masukkan password: ");
        String password = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("\nUsername dan password tidak boleh kosong");
            return;
        }

        if (isUsernameTaken(username)) {
            System.out.println("\nUsername sudah digunakan. Silakan pilih username lain.");
            return;
        }

        Customer customer = new Customer(username, password);
        users.add(customer); 
        System.out.println("\nAkun Customer berhasil dibuat!");
    }

    private static void login(Scanner scanner, Hotel hotel) {
        System.out.print("Masukkan username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Masukkan password: ");
        String password = scanner.nextLine().trim();

        User user = getUserInstance(username, password);

        if (user != null) {
            if (user instanceof Customer) {
                Customer customer = (Customer) user;
                handleCustomerActions(scanner, hotel, customer);
            } else if (user instanceof Admin) {
                Admin admin = (Admin) user;
                handleAdminActions(scanner, hotel, admin);
            }
        } else {
            System.out.println("\nUsername atau password salah");
        }
    }

    private static User getUserInstance(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
                return user; 
            }
        }
        return null; 
    }

    private static void handleCustomerActions(Scanner scanner, Hotel hotel, Customer customer) {
        boolean continueBooking = true; 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/mm/yyyy"); 

        while (continueBooking) {
            LocalDate checkInDate = null;
            LocalDate checkOutDate = null;

            while (checkInDate == null) {
                System.out.print("\nMasukkan tanggal check-in (dd/mm/yyyy): ");
                String inputCheckIn = scanner.nextLine().trim();
                try {
                    checkInDate = LocalDate.parse(inputCheckIn, formatter);
                } catch (DateTimeParseException e) {
                    System.out.println("Format tanggal tidak valid, silakan coba lagi.");
                }
            }

            while (checkOutDate == null) {
                System.out.print("Masukkan tanggal check-out (dd/mm/yyyy): ");
                String inputCheckOut = scanner.nextLine().trim();
                try {
                    checkOutDate = LocalDate.parse(inputCheckOut, formatter);
                    if (!checkOutDate.isAfter(checkInDate)) {
                        System.out.println("Tanggal check-out harus setelah tanggal check-in.");
                        checkOutDate = null; 
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Format tanggal tidak valid, silakan coba lagi.");
                }
            }

            ArrayList<Room> availableRooms = getAvailableRooms(hotel, checkInDate, checkOutDate);
            if(availableRooms.isEmpty()){
                System.out.println("\nTidak ada kamar yang tersedia untuk tanggal tersebut.");
            } else{
                System.out.println("\nDaftar Kamar yang Tersedia:");
                for(int i = 0; i < availableRooms.size(); i++){
                    Room room = availableRooms.get(i);
                    System.out.println((i + 1) + ". Kamar " + room.getRoomNumber() + " - " + room.getRoomType() + " - Rp." + room.getPrice());
                }

                System.out.print("Pilih nomor kamar untuk di pesan : ");
                int choice = 0;
                try {
                    String inputChoice = scanner.nextLine().trim();
                    choice = Integer.parseInt(inputChoice);
                } catch (NumberFormatException e) {
                    System.out.println("Input harus berupa angka.");
                    continue; 
                }

                if (choice >= 1 && choice <= availableRooms.size()) {
                    Room selectedRoom = availableRooms.get(choice - 1);

                    Reservation reservation = customer.makeReservation(hotel, selectedRoom, checkInDate, checkOutDate);
                    if (reservation != null) {
                        printReceipt(reservation);
                    }

                } else {
                    System.out.println("Pilihan tidak valid");
                }
            }

            boolean validOption = false;
            while (!validOption) {
                System.out.println("\nApakah Anda ingin memesan kamar lagi atau kembali ke menu utama?");
                System.out.println("1. Pesan kamar lagi");
                System.out.println("2. Kembali ke menu utama");
                System.out.print("Masukkan pilihan Kamu (1/2): ");
                String option = scanner.nextLine().trim();

                if (option.equals("1")) {
                    validOption = true;
                } else if (option.equals("2")) {
                    validOption = true;
                    continueBooking = false; 
                } else {
                    System.out.println("Pilihan tidak valid. Silakan masukkan 1 atau 2.");
                }
            }

        }
    }

    private static ArrayList<Room> getAvailableRooms(Hotel hotel, LocalDate checkIn, LocalDate checkOut){
        ArrayList<Room> availableRooms = new ArrayList<>();
        for(Room room : hotel.getRooms()){
            if(hotel.isRoomAvailable(room.getRoomNumber(), checkIn, checkOut)){
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    private static void handleAdminActions(Scanner scanner, Hotel hotel, Admin admin) {
        boolean adminExit = false;

        while (!adminExit) {
            System.out.println("\n----- MENU ADMIN -----");
            System.out.println("1. Cek Ketersediaan Kamar");
            System.out.println("2. Lihat Detail Reservasi");
            System.out.println("3. Batalkan Reservasi");
            System.out.println("4. Keluar dari Menu Admin");
            System.out.println("\n-----------------------");
            
            System.out.print("\nMasukan Pilihan Kamu : ");
            int choice = 0;
            try {
                String inputChoice = scanner.nextLine().trim();
                choice = Integer.parseInt(inputChoice);
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka.");
                continue;
            }
            
            switch (choice) {
                case 1:
                    admin.checkAvailability(hotel); 
                    break;
                case 2:
                    admin.viewReservations(hotel); 
                    break;
                case 3:
                    System.out.print("Masukkan nama customer untuk membatalkan reservasi: ");
                    String customerName = scanner.nextLine().trim();
                    System.out.print("Masukkan nomor kamar yang ingin dibatalkan: ");
                    int roomNumber = 0;
                    try {
                        roomNumber = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Nomor kamar harus berupa angka.");
                        break;
                    }
                    admin.cancelReservation(hotel, customerName, roomNumber);
                    break;
                case 4:
                    adminExit = true; 
                    break;
                default:
                    System.out.println("Pilihan tidak valid");
            }
        }
    }

    private static void printReceipt(Reservation reservation) {
        if (reservation != null) { 
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/mm/yyyy");
            System.out.println("\n--- STRUK RESERVASI ---");
            System.out.println("Nama Customer: " + reservation.getCustomerName());
            System.out.println("Nomor Kamar: " + reservation.getRoom().getRoomNumber());
            System.out.println("Jenis Kamar: " + reservation.getRoom().getRoomType());
            System.out.println("Harga Kamar: Rp." + reservation.getRoom().getPrice());
            System.out.println("Tanggal Check-In: " + reservation.getCheckInDate().format(formatter));
            System.out.println("Tanggal Check-Out: " + reservation.getCheckOutDate().format(formatter));
            System.out.println("-----------------------\n");
        } else {
            System.out.println("Reservasi tidak ditemukan.");
        }
    }
}
