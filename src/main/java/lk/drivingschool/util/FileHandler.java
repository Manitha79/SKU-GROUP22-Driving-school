package lk.drivingschool.util;

import java.io.*;
import java.util.*;

/**
 * Single, simple FileHandler for reading and writing text files.
 * Uses ONLY standard pure Java BufferedReader and BufferedWriter.
 * Saves plain text lines with the pipe '|' delimiter into the 'data' folder.
 */
public class FileHandler {

    private static final String FOLDER = "data";
    private static final String DELIMITER = "|";

    static {
        // Automatically make sure data directory exists and has sample data
        File dir = new File(FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        seedInitialData();
    }

    /**
     * Helper to get the File object inside the data directory.
     */
    private static File getFile(String fileName) {
        File dir = new File(FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, fileName);
    }

    // =========================================================================
    // 1. LOAD: Read all lines from text file using BufferedReader
    // =========================================================================
    public static synchronized List<String[]> load(String fileName) {
        List<String[]> list = new ArrayList<>();
        File file = getFile(fileName);

        if (!file.exists()) {
            return list;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\\|", -1);
                    list.add(parts);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return list;
    }

    // =========================================================================
    // 2. SAVE: Write all lines to text file using BufferedWriter
    // =========================================================================
    public static synchronized void save(String fileName, List<String[]> rows) {
        File file = getFile(fileName);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            for (String[] row : rows) {
                String line = String.join(DELIMITER, row);
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    // =========================================================================
    // 3. APPEND: Add one new line to the end of the text file
    // =========================================================================
    public static synchronized void append(String fileName, String[] row) {
        File file = getFile(fileName);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            String line = String.join(DELIMITER, row);
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error appending to file: " + e.getMessage());
        }
    }

    // =========================================================================
    // 4. UPDATE: Find a record by its ID (index 0) and update it
    // =========================================================================
    public static synchronized boolean update(String fileName, String id, String[] newRow) {
        List<String[]> rows = load(fileName);
        boolean found = false;

        for (int i = 0; i < rows.size(); i++) {
            String[] r = rows.get(i);
            if (r.length > 0 && r[0].equalsIgnoreCase(id)) {
                rows.set(i, newRow);
                found = true;
                break;
            }
        }

        if (found) {
            save(fileName, rows);
        }
        return found;
    }

    // =========================================================================
    // 5. DELETE: Find a record by its ID (index 0) and remove it
    // =========================================================================
    public static synchronized boolean delete(String fileName, String id) {
        List<String[]> rows = load(fileName);
        boolean removed = rows.removeIf(r -> r.length > 0 && r[0].equalsIgnoreCase(id));

        if (removed) {
            save(fileName, rows);
        }
        return removed;
    }

    // =========================================================================
    // 6. INITIAL SAMPLE DATA SEEDER
    // =========================================================================
    public static void seedInitialData() {
        if (load("students.txt").isEmpty()) {
            List<String[]> students = new ArrayList<>();
            students.add(new String[]{"STU-1001", "Kamal Perera", "kamal.p@gmail.com", "0771234567", "Auto Car (Class B)", "12/A Flower Rd, Colombo 07"});
            students.add(new String[]{"STU-1002", "Nimal Silva", "nimal.silva@yahoo.com", "0712345678", "Manual Car (Class B1)", "45 Galle Rd, Dehiwala"});
            students.add(new String[]{"STU-1003", "Anusha Fernando", "anusha.f@outlook.com", "0763456789", "Motorcycle (Class A)", "89 Kandy Rd, Kiribathgoda"});
            students.add(new String[]{"STU-1004", "Dinushan R.K", "dinushan.rk@drivingschool.lk", "0784567890", "Heavy Vehicle (Class C)", "21 Station Rd, Gampaha"});
            students.add(new String[]{"STU-1005", "Saman Jayasuriya", "saman.j@gmail.com", "0705678901", "Dual (Auto & Manual)", "104 High Level Rd, Nugegoda"});
            save("students.txt", students);
        }

        if (load("instructors.txt").isEmpty()) {
            List<String[]> instructors = new ArrayList<>();
            instructors.add(new String[]{"INS-2001", "Sunil Bandara", "sunil.b@drivingschool.lk", "0777112233", "Car & Van", "15", "ACTIVE"});
            instructors.add(new String[]{"INS-2002", "Ruwan Jayawardena", "ruwan.j@drivingschool.lk", "0711223344", "Motorcycle", "4", "ACTIVE"});
            instructors.add(new String[]{"INS-2003", "Mahesh Wickramasinghe", "mahesh.w@drivingschool.lk", "0788334455", "Heavy Vehicles", "12", "ACTIVE"});
            instructors.add(new String[]{"INS-2004", "Pradeep Kumara", "pradeep.k@drivingschool.lk", "0766445566", "Dual Purpose", "8", "ACTIVE"});
            instructors.add(new String[]{"INS-2005", "Chaminda Vaas", "chaminda.v@drivingschool.lk", "0722556677", "Car (Auto)", "2", "ON_LEAVE"});
            save("instructors.txt", instructors);
        }

        if (load("vehicles.txt").isEmpty()) {
            List<String[]> vehicles = new ArrayList<>();
            vehicles.add(new String[]{"VEH-3001", "WP CAB-1234", "Toyota Vitz 2021", "Car (Auto)", "AUTOMATIC", "AVAILABLE"});
            vehicles.add(new String[]{"VEH-3002", "WP CAC-5678", "Suzuki Swift 2020", "Car (Manual)", "MANUAL", "IN_LESSON"});
            vehicles.add(new String[]{"VEH-3003", "WP BIKE-8899", "Honda CB Hornet", "Motorcycle", "MANUAL", "AVAILABLE"});
            vehicles.add(new String[]{"VEH-3004", "WP NA-9900", "Isuzu Elf Truck", "Heavy Vehicle", "MANUAL", "MAINTENANCE"});
            save("vehicles.txt", vehicles);
        }

        if (load("lessons.txt").isEmpty()) {
            List<String[]> lessons = new ArrayList<>();
            lessons.add(new String[]{"LES-4001", "STU-1001", "INS-2001", "2026-11-08", "08:30 AM", "Steering and lane controls", "CONFIRMED"});
            lessons.add(new String[]{"LES-4002", "STU-1002", "INS-2004", "2026-11-08", "10:30 AM", "Three-point turn and parking", "PENDING_QUEUE"});
            lessons.add(new String[]{"LES-4003", "STU-1003", "INS-2002", "2026-11-09", "02:00 PM", "Highway road signs practice", "PENDING_QUEUE"});
            lessons.add(new String[]{"LES-4004", "STU-1004", "INS-2003", "2026-11-09", "04:00 PM", "Clutch control on slopes", "PENDING_QUEUE"});
            save("lessons.txt", lessons);
        }

        if (load("payments.txt").isEmpty()) {
            List<String[]> payments = new ArrayList<>();
            payments.add(new String[]{"PAY-5001", "STU-1001", "35000.00", "2026-11-01", "CARD", "PAID"});
            payments.add(new String[]{"PAY-5002", "STU-1002", "15000.00", "2026-11-02", "CASH", "PAID"});
            payments.add(new String[]{"PAY-5003", "STU-1003", "12500.00", "2026-11-03", "BANK_TRANSFER", "PAID"});
            payments.add(new String[]{"PAY-5004", "STU-1004", "20000.00", "2026-11-04", "ONLINE", "PENDING"});
            save("payments.txt", payments);
        }

        if (load("feedback.txt").isEmpty()) {
            List<String[]> feedback = new ArrayList<>();
            feedback.add(new String[]{"FDB-6001", "STU-1001", "5", "Mr. Sunil is very patient and explained junction safety clearly!", "REVIEWED"});
            feedback.add(new String[]{"FDB-6002", "STU-1002", "4", "Great vehicle condition. Looking forward to parallel parking next.", "REVIEWED"});
            feedback.add(new String[]{"FDB-6003", "STU-1003", "5", "Friendly atmosphere and clear instructions on motorbike balance.", "NEW"});
            feedback.add(new String[]{"FDB-6004", "STU-1005", "3", "Session was postponed by 15 minutes but overall lesson was good.", "NEW"});
            save("feedback.txt", feedback);
        }
    }
}
