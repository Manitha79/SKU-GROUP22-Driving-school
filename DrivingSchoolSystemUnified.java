// DrivingSchoolSystemUnified.java
// Single-file fully unified Driving School Management & Scheduling System.
// Includes all OOP Domain Models, Custom FIFO Queue, Bubble Sort Algorithm,
// Single Plaintext FileHandler (NO Base64, directly human-readable .txt files),
// and a Zero-Dependency Embedded Web Server.
// Run directly: java DrivingSchoolSystemUnified.java

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executors;
import com.sun.net.httpserver.*;

public class DrivingSchoolSystemUnified {

    // =========================================================================
    // SECTION 1: OOP DOMAIN MODELS
    // =========================================================================

    /**
     * Abstract base class: Person
     * Demonstrates OOP Pillars: Abstraction & Encapsulation.
     */
    public static abstract class Person {
        private final String id;
        private String name;
        private String email;
        private String phone;

        protected Person(String id, String name, String email, String phone) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phone = phone;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public abstract String getRole();
        public abstract String getDisplayDetails();

        @Override
        public String toString() {
            return getRole() + " [" + id + "] " + name + " (" + email + ", " + phone + ")";
        }
    }

    /**
     * Student extends Person
     * Module 1: Dinushan R.K
     * Demonstrates OOP Pillars: Inheritance & Polymorphism.
     */
    public static class Student extends Person {
        private String licenceType;
        private String address;
        private String emergencyContact;

        public Student(String id, String name, String email, String phone, String licenceType, String address, String emergencyContact) {
            super(id, name, email, phone);
            this.licenceType = licenceType;
            this.address = address;
            this.emergencyContact = (emergencyContact != null && !emergencyContact.isBlank()) ? emergencyContact : phone;
        }

        public String getLicenceType() { return licenceType; }
        public void setLicenceType(String licenceType) { this.licenceType = licenceType; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getEmergencyContact() { return emergencyContact; }
        public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

        @Override
        public String getRole() { return "STUDENT"; }

        @Override
        public String getDisplayDetails() {
            return "Student: " + getName() + " | Licence: " + licenceType + " | Phone: " + getPhone();
        }
    }

    /**
     * Instructor extends Person
     * Module 3: Kumarawansha W.K.R.D
     * Demonstrates OOP Pillars: Inheritance & Polymorphism.
     */
    public static class Instructor extends Person implements Comparable<Instructor> {
        private String licenceCategory;
        private int experienceYears;
        private double hourlyRate;
        private String status;

        public Instructor(String id, String name, String email, String phone, String licenceCategory, int experienceYears, double hourlyRate, String status) {
            super(id, name, email, phone);
            this.licenceCategory = licenceCategory;
            this.experienceYears = experienceYears;
            this.hourlyRate = hourlyRate;
            this.status = (status != null && !status.isBlank()) ? status : "ACTIVE";
        }

        public String getLicenceCategory() { return licenceCategory; }
        public void setLicenceCategory(String licenceCategory) { this.licenceCategory = licenceCategory; }
        public int getExperienceYears() { return experienceYears; }
        public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
        public double getHourlyRate() { return hourlyRate; }
        public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        @Override
        public String getRole() { return "INSTRUCTOR"; }

        @Override
        public String getDisplayDetails() {
            return "Instructor: " + getName() + " | " + licenceCategory + " | " + experienceYears + " yrs exp.";
        }

        @Override
        public int compareTo(Instructor other) {
            return Integer.compare(other.experienceYears, this.experienceYears);
        }
    }

    /**
     * Vehicle fleet entity
     * Module 4: Senevirathna M.G.T.R
     * Demonstrates OOP Pillar: Encapsulation.
     */
    public static class Vehicle {
        private final String id;
        private String registrationNo;
        private String model;
        private String category;
        private String transmission;
        private String status;

        public Vehicle(String id, String registrationNo, String model, String category, String transmission, String status) {
            this.id = id;
            this.registrationNo = registrationNo;
            this.model = model;
            this.category = category;
            this.transmission = transmission;
            this.status = (status != null && !status.isBlank()) ? status : "AVAILABLE";
        }

        public String getId() { return id; }
        public String getRegistrationNo() { return registrationNo; }
        public void setRegistrationNo(String registrationNo) { this.registrationNo = registrationNo; }
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getTransmission() { return transmission; }
        public void setTransmission(String transmission) { this.transmission = transmission; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    /**
     * Lesson Booking entity
     * Module 2: Thanneliyadda T.L.K.S
     * Queued and scheduled in FIFO order.
     */
    public static class Lesson {
        private final String id;
        private String studentId;
        private String instructorId;
        private String lessonDate;
        private String lessonTime;
        private String notes;
        private String status;
        private long requestTimestamp;

        public Lesson(String id, String studentId, String instructorId, String lessonDate, String lessonTime, String notes, String status, long requestTimestamp) {
            this.id = id;
            this.studentId = studentId;
            this.instructorId = (instructorId != null && !instructorId.isBlank()) ? instructorId : "UNASSIGNED";
            this.lessonDate = lessonDate;
            this.lessonTime = lessonTime;
            this.notes = notes;
            this.status = (status != null && !status.isBlank()) ? status : "PENDING_QUEUE";
            this.requestTimestamp = requestTimestamp > 0 ? requestTimestamp : System.currentTimeMillis();
        }

        public String getId() { return id; }
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        public String getInstructorId() { return instructorId; }
        public void setInstructorId(String instructorId) { this.instructorId = instructorId; }
        public String getLessonDate() { return lessonDate; }
        public void setLessonDate(String lessonDate) { this.lessonDate = lessonDate; }
        public String getLessonTime() { return lessonTime; }
        public void setLessonTime(String lessonTime) { this.lessonTime = lessonTime; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getRequestTimestamp() { return requestTimestamp; }
    }

    /**
     * Payment entity
     * Module 5: Weerakkody M.R.B
     */
    public static class Payment {
        private final String id;
        private String studentId;
        private double amount;
        private String paymentDate;
        private String method;
        private String status;

        public Payment(String id, String studentId, double amount, String paymentDate, String method, String status) {
            this.id = id;
            this.studentId = studentId;
            this.amount = amount;
            this.paymentDate = paymentDate;
            this.method = method;
            this.status = (status != null && !status.isBlank()) ? status : "PAID";
        }

        public String getId() { return id; }
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public String getPaymentDate() { return paymentDate; }
        public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    /**
     * Feedback entity
     * Module 6: Sajan P. (Admin & Feedback)
     */
    public static class Feedback {
        private final String id;
        private String studentId;
        private int rating;
        private String comments;
        private String status;
        private String submissionDate;

        public Feedback(String id, String studentId, int rating, String comments, String status, String submissionDate) {
            this.id = id;
            this.studentId = studentId;
            this.rating = Math.max(1, Math.min(5, rating));
            this.comments = comments;
            this.status = (status != null && !status.isBlank()) ? status : "NEW";
            this.submissionDate = (submissionDate != null && !submissionDate.isBlank()) ? submissionDate : LocalDate.now().toString();
        }

        public String getId() { return id; }
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        public int getRating() { return rating; }
        public void setRating(int rating) { this.rating = Math.max(1, Math.min(5, rating)); }
        public String getComments() { return comments; }
        public void setComments(String comments) { this.comments = comments; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getSubmissionDate() { return submissionDate; }

        public String getRatingStars() {
            return "★".repeat(Math.max(0, rating)) + "☆".repeat(Math.max(0, 5 - rating));
        }
    }

    // =========================================================================
    // SECTION 2: DATA STRUCTURES & ALGORITHMS
    // =========================================================================

    /**
     * Custom FIFO Queue for Lesson Requests.
     * Module 2: Thanneliyadda T.L.K.S
     */
    public static class LessonQueue {
        private final LinkedList<Lesson> list = new LinkedList<>();

        public synchronized void enqueue(Lesson lesson) {
            if (lesson != null) list.addLast(lesson);
        }

        public synchronized Lesson dequeue() {
            return list.isEmpty() ? null : list.removeFirst();
        }

        public synchronized Lesson peek() {
            return list.isEmpty() ? null : list.getFirst();
        }

        public synchronized boolean isEmpty() {
            return list.isEmpty();
        }

        public synchronized int size() {
            return list.size();
        }

        public synchronized List<Lesson> asList() {
            return new ArrayList<>(list);
        }
    }

    /**
     * Custom Bubble Sort with Performance Telemetry.
     * Module 3: Kumarawansha W.K.R.D
     */
    public static class BubbleSort {
        public static class SortMetrics {
            public final int comparisons;
            public final int swaps;
            public final long durationNanos;

            public SortMetrics(int comparisons, int swaps, long durationNanos) {
                this.comparisons = comparisons;
                this.swaps = swaps;
                this.durationNanos = durationNanos;
            }

            public double getDurationMillis() { return durationNanos / 1_000_000.0; }
        }

        public static SortMetrics sortByExperienceDescending(List<Instructor> list) {
            if (list == null || list.size() <= 1) return new SortMetrics(0, 0, 0);
            long start = System.nanoTime();
            int comparisons = 0, swaps = 0, n = list.size();

            for (int i = 0; i < n - 1; i++) {
                boolean swapped = false;
                for (int j = 0; j < n - i - 1; j++) {
                    comparisons++;
                    if (list.get(j).getExperienceYears() < list.get(j + 1).getExperienceYears()) {
                        Collections.swap(list, j, j + 1);
                        swaps++;
                        swapped = true;
                    }
                }
                if (!swapped) break;
            }
            return new SortMetrics(comparisons, swaps, System.nanoTime() - start);
        }

        public static SortMetrics sortByExperienceAscending(List<Instructor> list) {
            if (list == null || list.size() <= 1) return new SortMetrics(0, 0, 0);
            long start = System.nanoTime();
            int comparisons = 0, swaps = 0, n = list.size();

            for (int i = 0; i < n - 1; i++) {
                boolean swapped = false;
                for (int j = 0; j < n - i - 1; j++) {
                    comparisons++;
                    if (list.get(j).getExperienceYears() > list.get(j + 1).getExperienceYears()) {
                        Collections.swap(list, j, j + 1);
                        swaps++;
                        swapped = true;
                    }
                }
                if (!swapped) break;
            }
            return new SortMetrics(comparisons, swaps, System.nanoTime() - start);
        }
    }

    // =========================================================================
    // SECTION 3: SINGLE FILEHANDLER - PLAINTEXT PERSISTENCE (NO BASE64)
    // =========================================================================

    /**
     * Single FileHandler for all file load and save operations.
     * Records are written directly as human-readable plaintext with '|' separator.
     */
    public static class FileHandler {
        private static final String DATA_DIR = "data";
        private static final String DELIM = "|";
        private static final String SPLIT = "\\|";

        static {
            new File(DATA_DIR).mkdirs();
            initializeSampleData();
        }

        private static File getFile(String fileName) {
            new File(DATA_DIR).mkdirs();
            return new File(DATA_DIR, fileName);
        }

        /**
         * LOAD METHOD: Reads all records from a file into a List of String rows.
         */
        public static synchronized List<List<String>> loadRecords(String fileName) {
            List<List<String>> rows = new ArrayList<>();
            File file = getFile(fileName);
            if (!file.exists()) return rows;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        String[] parts = line.split(SPLIT, -1);
                        rows.add(new ArrayList<>(Arrays.asList(parts)));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading " + fileName + ": " + e.getMessage());
            }
            return rows;
        }

        /**
         * SAVE METHOD: Overwrites the file with the given list of records.
         */
        public static synchronized void saveRecords(String fileName, List<List<String>> rows) {
            File file = getFile(fileName);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8))) {
                for (List<String> row : rows) {
                    writer.write(String.join(DELIM, row));
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Error writing " + fileName + ": " + e.getMessage());
            }
        }

        /**
         * APPEND METHOD: Adds a single record to the end of the file.
         */
        public static synchronized void appendRecord(String fileName, List<String> row) {
            File file = getFile(fileName);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
                writer.write(String.join(DELIM, row));
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Error appending to " + fileName + ": " + e.getMessage());
            }
        }

        /**
         * UPDATE METHOD: Updates a record matching id (index 0).
         */
        public static synchronized boolean updateRecord(String fileName, String id, List<String> row) {
            List<List<String>> rows = loadRecords(fileName);
            for (int i = 0; i < rows.size(); i++) {
                if (!rows.get(i).isEmpty() && rows.get(i).get(0).equalsIgnoreCase(id)) {
                    rows.set(i, new ArrayList<>(row));
                    saveRecords(fileName, rows);
                    return true;
                }
            }
            return false;
        }

        /**
         * DELETE METHOD: Deletes a record matching id (index 0).
         */
        public static synchronized boolean deleteRecord(String fileName, String id) {
            List<List<String>> rows = loadRecords(fileName);
            boolean removed = rows.removeIf(r -> !r.isEmpty() && r.get(0).equalsIgnoreCase(id));
            if (removed) {
                saveRecords(fileName, rows);
            }
            return removed;
        }

        /**
         * INITIAL SAMPLE DATA SEEDER: Plain, human-readable text.
         */
        public static void initializeSampleData() {
            if (loadRecords("students.txt").isEmpty() || isOldBase64("students.txt")) {
                saveRecords("students.txt", List.of(
                    List.of("STU-1001", "Kamal Perera", "kamal.p@gmail.com", "0771234567", "Auto Car (Class B)", "12/A Flower Rd, Colombo 07", "0719876543"),
                    List.of("STU-1002", "Nimal Silva", "nimal.silva@yahoo.com", "0712345678", "Manual Car (Class B1)", "45 Galle Rd, Dehiwala", "0778899001"),
                    List.of("STU-1003", "Anusha Fernando", "anusha.f@outlook.com", "0763456789", "Motorcycle (Class A)", "89 Kandy Rd, Kiribathgoda", "0723344556"),
                    List.of("STU-1004", "Dinushan R.K", "dinushan.rk@drivingschool.lk", "0784567890", "Heavy Vehicle (Class C)", "21 Station Rd, Gampaha", "0751122334"),
                    List.of("STU-1005", "Saman Jayasuriya", "saman.j@gmail.com", "0705678901", "Dual (Auto & Manual)", "104 High Level Rd, Nugegoda", "0776655443")
                ));
            }
            if (loadRecords("instructors.txt").isEmpty() || isOldBase64("instructors.txt")) {
                saveRecords("instructors.txt", List.of(
                    List.of("INS-2001", "Sunil Bandara", "0777112233", "Car & Van", "15", "sunil.b@drivingschool.lk", "ACTIVE"),
                    List.of("INS-2002", "Ruwan Jayawardena", "0711223344", "Motorcycle", "4", "ruwan.j@drivingschool.lk", "ACTIVE"),
                    List.of("INS-2003", "Mahesh Wickramasinghe", "0788334455", "Heavy Vehicles", "12", "mahesh.w@drivingschool.lk", "ACTIVE"),
                    List.of("INS-2004", "Pradeep Kumara", "0766445566", "Dual Purpose", "8", "pradeep.k@drivingschool.lk", "ACTIVE"),
                    List.of("INS-2005", "Chaminda Vaas", "0722556677", "Car (Auto)", "2", "chaminda.v@drivingschool.lk", "ON_LEAVE")
                ));
            }
            if (loadRecords("vehicles.txt").isEmpty() || isOldBase64("vehicles.txt")) {
                saveRecords("vehicles.txt", List.of(
                    List.of("VEH-3001", "WP CAB-1234", "Toyota Vitz 2021", "Car (Auto)", "AUTOMATIC", "AVAILABLE"),
                    List.of("VEH-3002", "WP CAC-5678", "Suzuki Swift 2020", "Car (Manual)", "MANUAL", "IN_LESSON"),
                    List.of("VEH-3003", "WP BIKE-8899", "Honda CB Hornet", "Motorcycle", "MANUAL", "AVAILABLE"),
                    List.of("VEH-3004", "WP NA-9900", "Isuzu Elf Truck", "Heavy Vehicle", "MANUAL", "MAINTENANCE")
                ));
            }
            if (loadRecords("lessons.txt").isEmpty() || isOldBase64("lessons.txt")) {
                long now = System.currentTimeMillis();
                saveRecords("lessons.txt", List.of(
                    List.of("LES-4001", "STU-1001", "INS-2001", "2026-11-08", "08:30 AM", "Steering and lane controls", "CONFIRMED", String.valueOf(now - 100000)),
                    List.of("LES-4002", "STU-1002", "INS-2004", "2026-11-08", "10:30 AM", "Three-point turn and parking", "PENDING_QUEUE", String.valueOf(now - 80000)),
                    List.of("LES-4003", "STU-1003", "INS-2002", "2026-11-09", "02:00 PM", "Highway road signs practice", "PENDING_QUEUE", String.valueOf(now - 50000)),
                    List.of("LES-4004", "STU-1004", "INS-2003", "2026-11-09", "04:00 PM", "Clutch control on slopes", "PENDING_QUEUE", String.valueOf(now - 20000))
                ));
            }
            if (loadRecords("payments.txt").isEmpty() || isOldBase64("payments.txt")) {
                saveRecords("payments.txt", List.of(
                    List.of("PAY-5001", "STU-1001", "35000.00", "2026-11-01", "CARD", "PAID"),
                    List.of("PAY-5002", "STU-1002", "15000.00", "2026-11-02", "CASH", "PAID"),
                    List.of("PAY-5003", "STU-1003", "12500.00", "2026-11-03", "BANK_TRANSFER", "PAID"),
                    List.of("PAY-5004", "STU-1004", "20000.00", "2026-11-04", "ONLINE", "PENDING")
                ));
            }
            if (loadRecords("feedback.txt").isEmpty() || isOldBase64("feedback.txt")) {
                saveRecords("feedback.txt", List.of(
                    List.of("FDB-6001", "STU-1001", "5", "Mr. Sunil is very patient and explained junction safety clearly!", "REVIEWED", "2026-11-02"),
                    List.of("FDB-6002", "STU-1002", "4", "Great vehicle condition. Looking forward to parallel parking next.", "REVIEWED", "2026-11-03"),
                    List.of("FDB-6003", "STU-1003", "5", "Friendly atmosphere and clear instructions on motorbike balance.", "NEW", "2026-11-04"),
                    List.of("FDB-6004", "STU-1005", "3", "Session was postponed by 15 minutes but overall lesson was good.", "NEW", "2026-11-05")
                ));
            }
        }

        private static boolean isOldBase64(String fileName) {
            File file = getFile(fileName);
            if (!file.exists()) return false;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                String line = reader.readLine();
                return line != null && line.contains("U1RVL");
            } catch (Exception ignored) { return false; }
        }
    }

    // =========================================================================
    // SECTION 4: COORDINATION SERVICE
    // =========================================================================

    public static class DataService {
        public static List<Student> getStudents() {
            List<Student> list = new ArrayList<>();
            for (List<String> r : FileHandler.loadRecords("students.txt")) {
                if (r.size() >= 6) {
                    list.add(new Student(r.get(0), r.get(1), r.get(2), r.get(3), r.get(4), r.get(5),
                            r.size() > 6 ? r.get(6) : r.get(3)));
                }
            }
            return list;
        }

        public static List<Instructor> getInstructors() {
            List<Instructor> list = new ArrayList<>();
            for (List<String> r : FileHandler.loadRecords("instructors.txt")) {
                if (r.size() >= 5) {
                    int exp = 0; try { exp = Integer.parseInt(r.get(4)); } catch (Exception ignored) {}
                    String email = r.size() > 5 ? r.get(5) : "";
                    String status = r.size() > 6 ? r.get(6) : "ACTIVE";
                    list.add(new Instructor(r.get(0), r.get(1), email, r.get(2), r.get(3), exp, 2500.0, status));
                }
            }
            return list;
        }

        public static List<Vehicle> getVehicles() {
            List<Vehicle> list = new ArrayList<>();
            for (List<String> r : FileHandler.loadRecords("vehicles.txt")) {
                if (r.size() >= 5) {
                    String trans = r.size() > 5 ? r.get(4) : "MANUAL";
                    String status = r.size() > 5 ? r.get(5) : r.get(4);
                    list.add(new Vehicle(r.get(0), r.get(1), r.get(2), r.get(3), trans, status));
                }
            }
            return list;
        }

        public static List<Lesson> getLessons() {
            List<Lesson> list = new ArrayList<>();
            for (List<String> r : FileHandler.loadRecords("lessons.txt")) {
                if (r.size() >= 5) {
                    String ins = r.size() > 5 ? r.get(2) : "UNASSIGNED";
                    String date = r.size() > 5 ? r.get(3) : r.get(2);
                    String time = r.size() > 5 ? r.get(4) : r.get(3);
                    String notes = r.size() > 5 ? r.get(5) : r.get(4);
                    String status = r.size() > 6 ? r.get(6) : "PENDING_QUEUE";
                    long ts = 0; if (r.size() > 7) try { ts = Long.parseLong(r.get(7)); } catch (Exception ignored) {}
                    list.add(new Lesson(r.get(0), r.get(1), ins, date, time, notes, status, ts));
                }
            }
            return list;
        }

        public static LessonQueue getLessonQueue() {
            LessonQueue q = new LessonQueue();
            List<Lesson> all = getLessons();
            all.sort(Comparator.comparingLong(Lesson::getRequestTimestamp));
            for (Lesson l : all) if ("PENDING_QUEUE".equalsIgnoreCase(l.getStatus())) q.enqueue(l);
            return q;
        }

        public static List<Payment> getPayments() {
            List<Payment> list = new ArrayList<>();
            for (List<String> r : FileHandler.loadRecords("payments.txt")) {
                if (r.size() >= 5) {
                    double amt = 0.0; try { amt = Double.parseDouble(r.get(2)); } catch (Exception ignored) {}
                    String status = r.size() > 5 ? r.get(5) : (r.size() > 4 ? r.get(4) : "PAID");
                    String method = r.size() > 5 ? r.get(4) : (r.size() > 3 ? r.get(3) : "CASH");
                    list.add(new Payment(r.get(0), r.get(1), amt, r.get(3), method, status));
                }
            }
            return list;
        }

        public static double getTotalRevenue() {
            return getPayments().stream().filter(p -> "PAID".equalsIgnoreCase(p.getStatus())).mapToDouble(Payment::getAmount).sum();
        }

        public static List<Feedback> getFeedbacks() {
            List<Feedback> list = new ArrayList<>();
            for (List<String> r : FileHandler.loadRecords("feedback.txt")) {
                if (r.size() >= 4) {
                    int rating = 5; try { rating = Integer.parseInt(r.get(2)); } catch (Exception ignored) {}
                    String status = r.size() > 4 ? r.get(4) : "NEW";
                    String date = r.size() > 5 ? r.get(5) : "2026-11-06";
                    list.add(new Feedback(r.get(0), r.get(1), rating, r.get(3), status, date));
                }
            }
            return list;
        }

        public static double getAverageRating() {
            List<Feedback> f = getFeedbacks();
            return f.isEmpty() ? 5.0 : f.stream().mapToInt(Feedback::getRating).average().orElse(5.0);
        }
    }

    // =========================================================================
    // SECTION 5: EMBEDDED HTTP WEB SERVER & UI RENDERER (NO TOMCAT NEEDED!)
    // =========================================================================

    public static class WebApp {
        private static volatile String activeRole = "ADMIN";
        private static volatile String activeUserName = "Sajan P. (Admin Lead)";

        public static void startServer(int port) throws IOException {
            HttpServer server;
            try {
                server = HttpServer.create(new InetSocketAddress(port), 0);
            } catch (BindException e) {
                port = port + 1;
                server = HttpServer.create(new InetSocketAddress(port), 0);
            }

            server.setExecutor(Executors.newCachedThreadPool());

            // Routes & Controllers
            server.createContext("/", exchange -> handleRoot(exchange));
            server.createContext("/dashboard", exchange -> handleDashboard(exchange));
            server.createContext("/students", exchange -> handleStudents(exchange));
            server.createContext("/lessons", exchange -> handleLessons(exchange));
            server.createContext("/instructors", exchange -> handleInstructors(exchange));
            server.createContext("/vehicles", exchange -> handleVehicles(exchange));
            server.createContext("/payments", exchange -> handlePayments(exchange));
            server.createContext("/feedback", exchange -> handleFeedback(exchange));
            server.createContext("/switch-role", exchange -> handleSwitchRole(exchange));
            server.createContext("/login", exchange -> handleLogin(exchange));
            server.createContext("/logout", exchange -> {
                activeRole = "STUDENT";
                activeUserName = "Kamal Perera (Student)";
                redirect(exchange, "/login");
            });

            server.start();
            System.out.println("==================================================================");
            System.out.println(" APEX DRIVING SCHOOL SYSTEM - STANDALONE UNIFIED SERVER RUNNING ");
            System.out.println("==================================================================");
            System.out.println(">> Web Interface URL: http://localhost:" + port);
            System.out.println(">> Active Role: " + activeRole + " (" + activeUserName + ")");
            System.out.println(">> Data format: Human-readable plaintext in data/*.txt (No Base64)");
            System.out.println(">> Ready for Lecturer Demo!");
            System.out.println(">> Press Ctrl+C in terminal to stop server.");
            System.out.println("==================================================================");
        }

        private static void handleRoot(HttpExchange ex) throws IOException {
            if ("STUDENT".equals(activeRole) || "INSTRUCTOR".equals(activeRole)) {
                redirect(ex, "/lessons");
            } else {
                handleDashboard(ex);
            }
        }

        private static void handleSwitchRole(HttpExchange ex) throws IOException {
            String query = ex.getRequestURI().getQuery();
            String role = "ADMIN";
            String redir = "/dashboard";
            if (query != null) {
                for (String p : query.split("&")) {
                    if (p.startsWith("role=")) role = p.substring(5).toUpperCase();
                    if (p.startsWith("redirect=")) redir = URLDecoder.decode(p.substring(9), StandardCharsets.UTF_8);
                }
            }
            activeRole = role;
            if ("STUDENT".equals(role)) {
                activeUserName = "Kamal Perera (Student)";
                if ("/dashboard".equals(redir)) redir = "/lessons";
            } else if ("INSTRUCTOR".equals(role)) {
                activeUserName = "Sunil Bandara (Instructor)";
                if ("/dashboard".equals(redir)) redir = "/lessons";
            } else {
                activeRole = "ADMIN";
                activeUserName = "Sajan P. (Admin Lead)";
            }
            redirect(ex, redir);
        }

        private static void handleLogin(HttpExchange ex) throws IOException {
            if ("POST".equalsIgnoreCase(ex.getRequestMethod())) {
                var form = parseForm(ex);
                String role = form.getOrDefault("role", "ADMIN");
                activeRole = role;
                if ("STUDENT".equals(role)) {
                    activeUserName = "Kamal Perera (Student)";
                    redirect(ex, "/lessons");
                } else if ("INSTRUCTOR".equals(role)) {
                    activeUserName = "Sunil Bandara (Instructor)";
                    redirect(ex, "/lessons");
                } else {
                    activeRole = "ADMIN";
                    activeUserName = "Sajan P. (Admin Lead)";
                    redirect(ex, "/dashboard");
                }
                return;
            }

            String html = "<!doctype html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1'>" +
                "<title>Sign In - Apex Driving School</title>" +
                "<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css' rel='stylesheet'>" +
                "<link href='https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css' rel='stylesheet'>" +
                "<style>body{background:#f1f5f9;min-height:100vh;display:flex;align-items:center;justify-content:center;font-family:sans-serif;}" +
                ".login-card{background:#fff;border-radius:12px;box-shadow:0 4px 15px rgba(0,0,0,0.06);border:1px solid #e2e8f0;max-width:440px;width:100%;padding:2.25rem;}" +
                ".role-box{border:1.5px solid #e2e8f0;border-radius:10px;padding:1rem;margin-bottom:0.75rem;cursor:pointer;display:flex;align-items:center;gap:0.75rem;}" +
                ".role-box:hover{border-color:#2563eb;background:#eff6ff;}</style></head><body>" +
                "<div class='login-card'><div class='text-center mb-4'>" +
                "<div class='d-inline-flex align-items-center justify-content-center bg-primary text-white rounded-circle mb-2' style='width:52px;height:52px;'>" +
                "<i class='bi bi-car-front-fill fs-4'></i></div><h4 class='fw-bold text-dark mb-1'>Apex Driving School</h4>" +
                "<p class='text-muted small mb-0'>Select role to enter tailored portal</p></div>" +
                "<form method='post' action='/login'>" +
                "<label class='role-box w-100'><input type='radio' name='role' value='ADMIN' checked><div class='flex-grow-1'>" +
                "<div class='fw-bold text-dark d-flex justify-content-between'><span><i class='bi bi-shield-lock-fill text-danger me-1'></i> Admin Portal</span><span class='badge bg-danger-subtle text-danger'>Full Access</span></div>" +
                "<small class='text-muted d-block'>Access all 6 modules, revenue, and queue</small></div></label>" +
                "<label class='role-box w-100'><input type='radio' name='role' value='STUDENT'><div class='flex-grow-1'>" +
                "<div class='fw-bold text-dark d-flex justify-content-between'><span><i class='bi bi-person-fill text-primary me-1'></i> Student Portal</span><span class='badge bg-primary-subtle text-primary'>Customer</span></div>" +
                "<small class='text-muted d-block'>Book lessons, view receipts, and submit reviews</small></div></label>" +
                "<label class='role-box w-100'><input type='radio' name='role' value='INSTRUCTOR'><div class='flex-grow-1'>" +
                "<div class='fw-bold text-dark d-flex justify-content-between'><span><i class='bi bi-person-badge-fill text-info me-1'></i> Instructor Portal</span><span class='badge bg-info-subtle text-info'>Staff</span></div>" +
                "<small class='text-muted d-block'>View training schedule, fleet vehicles, and rank</small></div></label>" +
                "<button type='submit' class='btn btn-primary w-100 py-2 fw-semibold mt-3'><i class='bi bi-box-arrow-in-right me-1'></i> Enter Selected Portal</button></form>" +
                "<div class='text-center mt-4 pt-3 border-top'><small class='text-muted'>OOP Coursework RBAC Module</small></div></div></body></html>";
            sendHtml(ex, html);
        }

        private static void sendHtml(HttpExchange ex, String html) throws IOException {
            byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
            ex.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            ex.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
        }

        private static void redirect(HttpExchange ex, String path) throws IOException {
            ex.getResponseHeaders().set("Location", path);
            ex.sendResponseHeaders(302, -1);
        }

        private static Map<String, String> parseForm(HttpExchange ex) throws IOException {
            Map<String, String> params = new HashMap<>();
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            for (String pair : body.split("&")) {
                int eq = pair.indexOf('=');
                if (eq > 0) {
                    String k = URLDecoder.decode(pair.substring(0, eq), StandardCharsets.UTF_8);
                    String v = pair.length() > eq + 1 ? URLDecoder.decode(pair.substring(eq + 1), StandardCharsets.UTF_8) : "";
                    params.put(k, v);
                }
            }
            return params;
        }

        private static String layout(String title, String activeTab, String content) {
            String navItems = "";
            String[][] allTabs = {
                {"/dashboard", "dashboard", "bi-speedometer2", "Dashboard", "ADMIN"},
                {"/students", "students", "bi-people", "Students", "ADMIN"},
                {"/lessons", "lessons", "bi-calendar-event", "STUDENT".equals(activeRole) ? "My Lessons" : ("INSTRUCTOR".equals(activeRole) ? "Teaching Schedule" : "Lessons & Queue"), "ALL"},
                {"/instructors", "instructors", "bi-person-badge", "Instructors", "ADMIN_INSTRUCTOR"},
                {"/vehicles", "vehicles", "bi-truck-front", "Vehicles", "ADMIN_INSTRUCTOR"},
                {"/payments", "payments", "bi-credit-card", "STUDENT".equals(activeRole) ? "My Payments" : "Payments", "ADMIN_STUDENT"},
                {"/feedback", "feedback", "bi-chat-heart", "Feedback", "ADMIN_STUDENT"}
            };

            for (String[] t : allTabs) {
                String allowed = t[4];
                boolean isAllowed = "ALL".equals(allowed)
                    || ("ADMIN".equals(allowed) && "ADMIN".equals(activeRole))
                    || ("ADMIN_INSTRUCTOR".equals(allowed) && ("ADMIN".equals(activeRole) || "INSTRUCTOR".equals(activeRole)))
                    || ("ADMIN_STUDENT".equals(allowed) && ("ADMIN".equals(activeRole) || "STUDENT".equals(activeRole)));

                if (!isAllowed) continue;

                boolean act = t[1].equals(activeTab);
                navItems += "<li class='nav-item'><a class='nav-link " + (act ? "active fw-bold text-primary" : "text-secondary") + "' href='" + t[0] + "'><i class='bi " + t[2] + " me-1'></i> " + t[3] + "</a></li>";
            }

            String roleBadgeClass = "STUDENT".equals(activeRole) ? "bg-primary" : ("INSTRUCTOR".equals(activeRole) ? "bg-info text-dark" : "bg-danger");
            String roleSwitcherHtml = "<div class='dropdown ms-lg-3'>" +
                "<button class='btn btn-sm btn-outline-secondary dropdown-toggle d-flex align-items-center gap-2 py-1 px-3' type='button' data-bs-toggle='dropdown' style='border-radius:20px; font-size:0.82rem;'>" +
                "<i class='bi " + ("STUDENT".equals(activeRole) ? "bi-person-fill text-primary" : ("INSTRUCTOR".equals(activeRole) ? "bi-person-badge-fill text-info" : "bi-shield-lock-fill text-danger")) + "'></i>" +
                "<span>" + activeUserName + "</span>" +
                "<span class='badge " + roleBadgeClass + "' style='font-size:0.65rem;'>" + activeRole + "</span>" +
                "</button>" +
                "<ul class='dropdown-menu dropdown-menu-end shadow-sm border-0 py-2' style='font-size:0.85rem; border-radius:10px;'>" +
                "<li><h6 class='dropdown-header text-uppercase text-muted' style='font-size:0.7rem;'>Switch Portal (Live Viva Demo)</h6></li>" +
                "<li><a class='dropdown-item py-2' href='/switch-role?role=ADMIN&redirect=/dashboard'><i class='bi bi-shield-lock-fill text-danger me-2'></i>Administrator Portal</a></li>" +
                "<li><a class='dropdown-item py-2' href='/switch-role?role=STUDENT&redirect=/lessons'><i class='bi bi-person-fill text-primary me-2'></i>Customer / Student Portal</a></li>" +
                "<li><a class='dropdown-item py-2' href='/switch-role?role=INSTRUCTOR&redirect=/lessons'><i class='bi bi-person-badge-fill text-info me-2'></i>Instructor Staff Portal</a></li>" +
                "<li><hr class='dropdown-divider'></li>" +
                "<li><a class='dropdown-item text-danger' href='/logout'><i class='bi bi-box-arrow-right me-2'></i>Sign Out</a></li>" +
                "</ul></div>";

            return "<!doctype html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1'>" +
                   "<title>" + title + " - Apex Driving School</title>" +
                   "<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css' rel='stylesheet'>" +
                   "<link href='https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css' rel='stylesheet'>" +
                   "<style>" +
                   "body{background:#f8fafc;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;color:#334155;min-height:100vh;display:flex;flex-direction:column;}" +
                   ".navbar-clean{background:#ffffff;border-bottom:1px solid #e2e8f0;padding:0.75rem 0;}" +
                   ".card-clean{background:#ffffff;border:1px solid #e2e8f0;border-radius:10px;box-shadow:0 1px 3px rgba(0,0,0,0.05);overflow:hidden;margin-bottom:1.5rem;}" +
                   ".card-clean-header{padding:0.85rem 1.25rem;border-bottom:1px solid #e2e8f0;font-weight:600;font-size:0.95rem;background:#fafafa;display:flex;justify-content:space-between;align-items:center;}" +
                   ".card-clean-body{padding:1.25rem;}" +
                   ".viva-badge-banner{background:#ffffff;border:1px solid #e2e8f0;border-left:4px solid #2563eb;border-radius:10px;padding:0.75rem 1.25rem;margin-bottom:1.5rem;box-shadow:0 1px 3px rgba(0,0,0,0.05);display:flex;align-items:center;justify-content:space-between;flex-wrap:wrap;gap:0.75rem;}" +
                   ".viva-badge-title{font-size:0.95rem;font-weight:700;color:#0f172a;}" +
                   ".viva-concept-pill{font-size:0.75rem;font-weight:600;padding:0.25rem 0.65rem;border-radius:999px;background:#f1f5f9;color:#475569;border:1px solid #e2e8f0;}" +
                   ".kpi-card{background:#ffffff;border:1px solid #e2e8f0;border-radius:10px;padding:1rem;box-shadow:0 1px 3px rgba(0,0,0,0.05);text-align:center;}" +
                   ".kpi-label{font-size:0.75rem;font-weight:600;color:#64748b;text-transform:uppercase;letter-spacing:0.04em;margin-bottom:0.25rem;}" +
                   ".kpi-value{font-size:1.6rem;font-weight:700;line-height:1.2;}" +
                   ".kpi-member{font-size:0.7rem;color:#94a3b8;font-weight:500;display:block;margin-top:0.25rem;}" +
                   ".table-clean th{background:#f8fafc;color:#64748b;font-size:0.78rem;font-weight:600;text-transform:uppercase;letter-spacing:0.04em;padding:0.75rem 1rem;border-bottom:1px solid #e2e8f0;}" +
                   ".table-clean td{padding:0.75rem 1rem;vertical-align:middle;border-bottom:1px solid #f1f5f9;font-size:0.875rem;}" +
                   ".badge-subtle-success{background:#dcfce7;color:#166534;font-weight:500;padding:0.35em 0.65em;border-radius:6px;}" +
                   ".badge-subtle-warning{background:#fef9c3;color:#854d0e;font-weight:500;padding:0.35em 0.65em;border-radius:6px;}" +
                   ".badge-subtle-danger{background:#fee2e2;color:#991b1b;font-weight:500;padding:0.35em 0.65em;border-radius:6px;}" +
                   ".badge-subtle-primary{background:#dbeafe;color:#1e40af;font-weight:500;padding:0.35em 0.65em;border-radius:6px;}" +
                   ".badge-subtle-info{background:#cffafe;color:#155e75;font-weight:500;padding:0.35em 0.65em;border-radius:6px;}" +
                   "</style></head><body>" +
                   "<nav class='navbar navbar-expand-lg navbar-clean sticky-top'>" +
                   "<div class='container'>" +
                   "<a class='navbar-brand fw-bold text-dark d-flex align-items-center gap-2' href='" + ("STUDENT".equals(activeRole) || "INSTRUCTOR".equals(activeRole) ? "/lessons" : "/dashboard") + "'>" +
                   "<i class='bi bi-car-front-fill text-primary fs-4'></i><span>Apex Driving School</span></a>" +
                   "<div class='collapse navbar-collapse show' id='navMenu'>" +
                   "<ul class='navbar-nav me-auto ms-lg-3 gap-1'>" + navItems + "</ul>" +
                   roleSwitcherHtml +
                   "</div></div></nav>" +
                   "<div class='container py-4 flex-grow-1'>" + content + "</div>" +
                   "<footer class='bg-white border-top py-3 text-center text-muted small mt-auto'>" +
                   "Apex Driving School Management System &bull; Unified Coursework System &bull; 6-Member Viva Ready" +
                   "</footer>" +
                   "<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js'></script>" +
                   "</body></html>";
        }

        private static void handleDashboard(HttpExchange ex) throws IOException {
            if (!"ADMIN".equals(activeRole)) {
                redirect(ex, "/lessons");
                return;
            }
            var students = DataService.getStudents();
            var instructors = DataService.getInstructors();
            var vehicles = DataService.getVehicles();
            var queue = DataService.getLessonQueue();
            var rev = DataService.getTotalRevenue();
            var rating = DataService.getAverageRating();

            String html = "<div class='d-flex justify-content-between align-items-center mb-4'>" +
                "<div><h3 class='fw-bold text-dark mb-1'>Executive Dashboard</h3><p class='text-muted small mb-0'>System overview connecting all 6 coursework modules.</p></div>" +
                "<a href='/lessons' class='btn btn-primary btn-sm'><i class='bi bi-plus-lg me-1'></i> Book New Lesson</a></div>" +
                "<div class='row g-3 mb-4'>" +
                "<div class='col-6 col-md-4 col-lg-2'><div class='kpi-card' style='border-top:3px solid #2563eb;'><div class='kpi-label'>Students</div><div class='kpi-value text-primary'>" + students.size() + "</div><span class='kpi-member'>Dinushan R.K</span></div></div>" +
                "<div class='col-6 col-md-4 col-lg-2'><div class='kpi-card' style='border-top:3px solid #d97706;'><div class='kpi-label'>Queued</div><div class='kpi-value text-warning'>" + queue.size() + "</div><span class='kpi-member'>Thanneliyadda</span></div></div>" +
                "<div class='col-6 col-md-4 col-lg-2'><div class='kpi-card' style='border-top:3px solid #0891b2;'><div class='kpi-label'>Instructors</div><div class='kpi-value text-info'>" + instructors.size() + "</div><span class='kpi-member'>Kumarawansha</span></div></div>" +
                "<div class='col-6 col-md-4 col-lg-2'><div class='kpi-card' style='border-top:3px solid #64748b;'><div class='kpi-label'>Vehicles</div><div class='kpi-value text-secondary'>" + vehicles.size() + "</div><span class='kpi-member'>Senevirathna</span></div></div>" +
                "<div class='col-6 col-md-4 col-lg-2'><div class='kpi-card' style='border-top:3px solid #16a34a;'><div class='kpi-label'>Revenue</div><div class='kpi-value text-success fs-5'>LKR " + String.format(Locale.US, "%,.0f", rev) + "</div><span class='kpi-member'>Weerakkody</span></div></div>" +
                "<div class='col-6 col-md-4 col-lg-2'><div class='kpi-card' style='border-top:3px solid #dc2626;'><div class='kpi-label'>Rating</div><div class='kpi-value text-danger'>" + String.format(Locale.US, "%.1f", rating) + " ★</div><span class='kpi-member'>Sajan P.</span></div></div>" +
                "</div>" +
                "<div class='row g-4'>" +
                "<div class='col-lg-7'><div class='card-clean'><div class='card-clean-header'><span><i class='bi bi-diagram-3-fill me-2 text-warning'></i>Active FIFO Lesson Queue (Thanneliyadda)</span></div>" +
                "<div class='table-responsive'><table class='table table-clean mb-0'><thead><tr><th>Lesson ID</th><th>Student</th><th>Date/Time</th><th>Status</th></tr></thead><tbody>";

            for (Lesson l : DataService.getLessons()) {
                html += "<tr><td><span class='badge bg-light text-dark font-monospace'>" + l.getId() + "</span></td>" +
                        "<td class='fw-semibold'>" + l.getStudentId() + "</td><td>" + l.getLessonDate() + " (" + l.getLessonTime() + ")</td>" +
                        "<td><span class='badge " + ("CONFIRMED".equals(l.getStatus()) ? "badge-subtle-success" : "badge-subtle-warning") + "'>" + l.getStatus() + "</span></td></tr>";
            }
            html += "</tbody></table></div></div></div>" +
                "<div class='col-lg-5'><div class='card-clean'><div class='card-clean-header'><span><i class='bi bi-award-fill me-2 text-info'></i>Instructors Ranked by Experience (Kumarawansha)</span></div>" +
                "<ul class='list-group list-group-flush'>";

            List<Instructor> sortedIns = new ArrayList<>(instructors);
            BubbleSort.sortByExperienceDescending(sortedIns);
            for (Instructor ins : sortedIns) {
                html += "<li class='list-group-item d-flex justify-content-between align-items-center py-2 px-3 border-bottom'>" +
                        "<div><div class='fw-bold'>" + ins.getName() + "</div><small class='text-muted'>" + ins.getLicenceCategory() + "</small></div>" +
                        "<span class='badge badge-subtle-info fs-6 px-3 py-1'>" + ins.getExperienceYears() + " Yrs</span></li>";
            }
            html += "</ul></div></div></div>";

            sendHtml(ex, layout("Executive Dashboard", "dashboard", html));
        }

        private static void handleStudents(HttpExchange ex) throws IOException {
            if (!"ADMIN".equals(activeRole)) {
                redirect(ex, "/lessons");
                return;
            }
            if ("POST".equalsIgnoreCase(ex.getRequestMethod())) {
                var form = parseForm(ex);
                String act = form.getOrDefault("action", "create");
                if ("delete".equals(act)) {
                    FileHandler.deleteRecord("students.txt", form.get("id"));
                } else {
                    String id = "STU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                    FileHandler.appendRecord("students.txt", List.of(id, form.get("name"), form.get("email"), form.get("phone"), form.get("licenceType"), form.get("address"), form.get("phone")));
                }
                redirect(ex, "/students");
                return;
            }
            var list = DataService.getStudents();
            String html = "<div class='viva-badge-banner' style='border-left-color: #2563eb;'>" +
                "<div><div class='viva-badge-title'><i class='bi bi-people-fill text-primary me-1'></i> Module 1: Student Management</div>" +
                "<small class='text-muted'>Lead Developer: <strong>Dinushan R.K</strong> &bull; Persistence: <code>data/students.txt</code></small></div>" +
                "<div class='d-flex gap-2 flex-wrap'><span class='viva-concept-pill'>Inheritance (Student extends Person)</span><span class='viva-concept-pill'>Polymorphism (getRole)</span><span class='viva-concept-pill'>Encapsulation</span></div></div>" +
                "<div class='row g-4'><div class='col-lg-4'><div class='card-clean'><div class='card-clean-header'><span><i class='bi bi-person-plus me-1 text-primary'></i> Enroll New Student</span></div>" +
                "<div class='card-clean-body'><form method='post' action='/students'><input type='hidden' name='action' value='create'>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Full Name</label><input class='form-control form-control-sm' name='name' required placeholder='Kamal Perera'></div>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Email</label><input type='email' class='form-control form-control-sm' name='email' required placeholder='name@gmail.com'></div>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Phone</label><input class='form-control form-control-sm' name='phone' required placeholder='0771234567'></div>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Licence Class</label><select class='form-select form-select-sm' name='licenceType'>" +
                "<option>Auto Car (Class B)</option><option>Manual Car (Class B1)</option><option>Motorcycle (Class A)</option><option>Heavy Vehicle (Class C)</option></select></div>" +
                "<div class='mb-3'><label class='form-label small fw-semibold'>Address</label><textarea class='form-control form-control-sm' name='address' rows='2' required></textarea></div>" +
                "<button class='btn btn-primary btn-sm w-100'>Register Student</button></form></div></div></div>" +
                "<div class='col-lg-8'><div class='card-clean'><div class='card-clean-header'><span><i class='bi bi-list-ul me-1 text-secondary'></i> Registered Students (" + list.size() + ")</span></div>" +
                "<div class='table-responsive'><table class='table table-clean mb-0'>" +
                "<thead><tr><th>ID</th><th>Name & Contact</th><th>Licence</th><th>Actions</th></tr></thead><tbody>";
            for (Student s : list) {
                html += "<tr><td><span class='badge bg-light text-dark font-monospace'>" + s.getId() + "</span></td>" +
                        "<td><div class='fw-bold'>" + s.getName() + "</div><small class='text-muted'>" + s.getPhone() + " &bull; " + s.getEmail() + "</small></td>" +
                        "<td><span class='badge badge-subtle-primary'>" + s.getLicenceType() + "</span></td>" +
                        "<td><form method='post' action='/students' onsubmit='return confirm(\"Delete student?\")'><input type='hidden' name='action' value='delete'><input type='hidden' name='id' value='" + s.getId() + "'><button class='btn btn-sm btn-outline-danger py-0 px-2'><i class='bi bi-trash'></i></button></form></td></tr>";
            }
            html += "</tbody></table></div></div></div></div>";
            sendHtml(ex, layout("Students", "students", html));
        }

        private static void handleLessons(HttpExchange ex) throws IOException {
            if ("POST".equalsIgnoreCase(ex.getRequestMethod())) {
                var form = parseForm(ex);
                String act = form.getOrDefault("action", "create");
                if ("process".equals(act)) {
                    var q = DataService.getLessonQueue();
                    Lesson next = q.dequeue();
                    if (next != null) {
                        next.setStatus("CONFIRMED");
                        FileHandler.updateRecord("lessons.txt", next.getId(), List.of(
                            next.getId(), next.getStudentId(), next.getInstructorId(),
                            next.getLessonDate(), next.getLessonTime(), next.getNotes(),
                            next.getStatus(), String.valueOf(next.getRequestTimestamp())
                        ));
                    }
                } else if ("delete".equals(act)) {
                    FileHandler.deleteRecord("lessons.txt", form.get("id"));
                } else {
                    String id = "LES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                    FileHandler.appendRecord("lessons.txt", List.of(id, form.get("studentId"), form.get("instructorId"), form.get("lessonDate"), form.get("lessonTime"), form.get("notes"), "PENDING_QUEUE", String.valueOf(System.currentTimeMillis())));
                }
                redirect(ex, "/lessons");
                return;
            }
            var list = DataService.getLessons();
            var q = DataService.getLessonQueue();
            String html = "<div class='viva-badge-banner' style='border-left-color: #d97706;'>" +
                "<div><div class='viva-badge-title'><i class='bi bi-calendar-event text-warning me-1'></i> Module 2: Lesson Scheduling & FIFO Queue</div>" +
                "<small class='text-muted'>Lead Developer: <strong>Thanneliyadda T.L.K.S</strong> &bull; Persistence: <code>data/lessons.txt</code></small></div>" +
                "<div class='d-flex gap-2 flex-wrap'><span class='viva-concept-pill'>Custom LessonQueue (FIFO)</span><span class='viva-concept-pill'>enqueue() / dequeue() / peek()</span></div></div>" +
                "<div class='card-clean p-3 mb-4 d-flex flex-row justify-content-between align-items-center' style='border-left:4px solid #d97706;'>" +
                "<div><h5 class='fw-bold mb-1'>" + q.size() + " Requests Currently in FIFO Queue</h5><small class='text-muted'>Processed strictly First-In-First-Out based on booking timestamp.</small></div>" +
                "<form method='post' action='/lessons'><input type='hidden' name='action' value='process'><button class='btn btn-warning fw-bold btn-sm " + (q.isEmpty() ? "disabled" : "") + "'><i class='bi bi-play-fill me-1'></i> Process Next in Queue</button></form></div>" +
                "<div class='row g-4'><div class='col-lg-4'><div class='card-clean'><div class='card-clean-header'><span><i class='bi bi-calendar-plus me-1 text-primary'></i> Enqueue Lesson</span></div>" +
                "<div class='card-clean-body'><form method='post' action='/lessons'><input type='hidden' name='action' value='create'>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Student ID / Name</label><input class='form-control form-control-sm' name='studentId' required placeholder='STU-1001'></div>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Assigned Instructor</label><input class='form-control form-control-sm' name='instructorId' placeholder='INS-2001'></div>" +
                "<div class='row g-2 mb-2'><div class='col-6'><label class='form-label small fw-semibold'>Date</label><input type='date' class='form-control form-control-sm' name='lessonDate' required value='2026-11-10'></div>" +
                "<div class='col-6'><label class='form-label small fw-semibold'>Time</label><input class='form-control form-control-sm' name='lessonTime' required value='09:00 AM'></div></div>" +
                "<div class='mb-3'><label class='form-label small fw-semibold'>Notes</label><textarea class='form-control form-control-sm' name='notes' rows='2' placeholder='Reverse parking'></textarea></div>" +
                "<button class='btn btn-primary btn-sm w-100'>Enqueue Lesson</button></form></div></div></div>" +
                "<div class='col-lg-8'><div class='card-clean'><div class='card-clean-header'><span><i class='bi bi-calendar-week me-1 text-secondary'></i> Scheduled Lessons (" + list.size() + ")</span></div>" +
                "<div class='table-responsive'><table class='table table-clean mb-0'>" +
                "<thead><tr><th>ID</th><th>Student</th><th>Instructor</th><th>Schedule</th><th>Status</th><th>Action</th></tr></thead><tbody>";
            for (Lesson l : list) {
                html += "<tr><td><span class='badge bg-light text-dark font-monospace'>" + l.getId() + "</span></td>" +
                        "<td class='fw-bold'>" + l.getStudentId() + "</td><td>" + l.getInstructorId() + "</td>" +
                        "<td>" + l.getLessonDate() + " <small class='text-muted'>(" + l.getLessonTime() + ")</small></td>" +
                        "<td><span class='badge " + ("CONFIRMED".equals(l.getStatus()) ? "badge-subtle-success" : "badge-subtle-warning") + "'>" + l.getStatus() + "</span></td>" +
                        "<td><form method='post' action='/lessons' onsubmit='return confirm(\"Delete?\")'><input type='hidden' name='action' value='delete'><input type='hidden' name='id' value='" + l.getId() + "'><button class='btn btn-sm btn-outline-danger py-0 px-2'><i class='bi bi-trash'></i></button></form></td></tr>";
            }
            html += "</tbody></table></div></div></div></div>";
            sendHtml(ex, layout("Lessons & Queue", "lessons", html));
        }

        private static void handleInstructors(HttpExchange ex) throws IOException {
            if ("STUDENT".equals(activeRole)) {
                redirect(ex, "/lessons");
                return;
            }
            if ("POST".equalsIgnoreCase(ex.getRequestMethod())) {
                var form = parseForm(ex);
                String act = form.getOrDefault("action", "create");
                if ("delete".equals(act)) {
                    FileHandler.deleteRecord("instructors.txt", form.get("id"));
                } else {
                    String id = "INS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                    FileHandler.appendRecord("instructors.txt", List.of(id, form.get("name"), form.get("phone"), form.get("licenceCategory"), form.get("experienceYears"), form.get("email"), "ACTIVE"));
                }
                redirect(ex, "/instructors");
                return;
            }
            var list = DataService.getInstructors();
            String query = ex.getRequestURI().getQuery();
            boolean asc = query != null && query.contains("sort=asc");
            var metrics = asc ? BubbleSort.sortByExperienceAscending(list) : BubbleSort.sortByExperienceDescending(list);

            String html = "<div class='viva-badge-banner' style='border-left-color: #0891b2;'>" +
                "<div><div class='viva-badge-title'><i class='bi bi-person-badge text-info me-1'></i> Module 3: Instructor Management & Bubble Sort</div>" +
                "<small class='text-muted'>Lead Developer: <strong>Kumarawansha W.K.R.D</strong> &bull; Persistence: <code>data/instructors.txt</code></small></div>" +
                "<div class='d-flex gap-2 flex-wrap'><span class='viva-concept-pill'>Inheritance (Instructor extends Person)</span><span class='viva-concept-pill'>BubbleSort (O(n²) with Early Exit)</span></div></div>" +
                "<div class='card-clean p-3 mb-4 d-flex flex-row justify-content-between align-items-center' style='border-left:4px solid #0891b2;'>" +
                "<div><h6 class='fw-bold mb-1'>Bubble Sort Telemetry: " + metrics.comparisons + " Comparisons, " + metrics.swaps + " Swaps (" + String.format(Locale.US, "%.3f", metrics.getDurationMillis()) + " ms)</h6>" +
                "<small class='text-muted'>Sorted by Experience Years " + (asc ? "Ascending" : "Descending") + " (O(n²) with early exit flag).</small></div>" +
                "<div class='d-flex gap-2'><a href='/instructors?sort=desc' class='btn btn-sm btn-info text-dark fw-bold'>Desc</a><a href='/instructors?sort=asc' class='btn btn-sm btn-outline-secondary'>Asc</a></div></div>" +
                "<div class='row g-4'><div class='col-lg-4'><div class='card-clean'><div class='card-clean-header'><span><i class='bi bi-person-plus me-1 text-primary'></i> Add Instructor</span></div>" +
                "<div class='card-clean-body'><form method='post' action='/instructors'><input type='hidden' name='action' value='create'>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Instructor Name</label><input class='form-control form-control-sm' name='name' required placeholder='Sunil Bandara'></div>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Phone</label><input class='form-control form-control-sm' name='phone' required placeholder='0777112233'></div>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Category</label><input class='form-control form-control-sm' name='licenceCategory' required value='Car & Van'></div>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Experience (Years)</label><input type='number' class='form-control form-control-sm' name='experienceYears' required value='10'></div>" +
                "<div class='mb-3'><label class='form-label small fw-semibold'>Email</label><input type='email' class='form-control form-control-sm' name='email' placeholder='sunil@drivingschool.lk'></div>" +
                "<button class='btn btn-primary btn-sm w-100'>Save Instructor</button></form></div></div></div>" +
                "<div class='col-lg-8'><div class='card-clean'><div class='card-clean-header'><span><i class='bi bi-award me-1 text-secondary'></i> Ranked Instructors (" + list.size() + ")</span></div>" +
                "<div class='table-responsive'><table class='table table-clean mb-0'>" +
                "<thead><tr><th>Rank</th><th>Name & Phone</th><th>Category</th><th>Experience</th><th>Action</th></tr></thead><tbody>";
            int rank = 1;
            for (Instructor ins : list) {
                html += "<tr><td><span class='badge " + (rank == 1 ? "bg-warning text-dark" : "bg-light text-dark") + " rounded-circle p-2'>#" + (rank++) + "</span></td>" +
                        "<td><div class='fw-bold'>" + ins.getName() + "</div><small class='text-muted'>" + ins.getPhone() + "</small></td>" +
                        "<td><span class='badge badge-subtle-primary'>" + ins.getLicenceCategory() + "</span></td>" +
                        "<td><span class='badge badge-subtle-info fs-6 px-3 py-1'>" + ins.getExperienceYears() + " Yrs</span></td>" +
                        "<td><form method='post' action='/instructors' onsubmit='return confirm(\"Delete?\")'><input type='hidden' name='action' value='delete'><input type='hidden' name='id' value='" + ins.getId() + "'><button class='btn btn-sm btn-outline-danger py-0 px-2'><i class='bi bi-trash'></i></button></form></td></tr>";
            }
            html += "</tbody></table></div></div></div></div>";
            sendHtml(ex, layout("Instructors & BubbleSort", "instructors", html));
        }

        private static void handleVehicles(HttpExchange ex) throws IOException {
            if ("STUDENT".equals(activeRole)) {
                redirect(ex, "/lessons");
                return;
            }
            if ("POST".equalsIgnoreCase(ex.getRequestMethod())) {
                var form = parseForm(ex);
                String act = form.getOrDefault("action", "create");
                if ("delete".equals(act)) {
                    FileHandler.deleteRecord("vehicles.txt", form.get("id"));
                } else {
                    String id = "VEH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                    FileHandler.appendRecord("vehicles.txt", List.of(id, form.get("registrationNo"), form.get("model"), form.get("category"), form.get("transmission"), form.get("status")));
                }
                redirect(ex, "/vehicles");
                return;
            }
            var list = DataService.getVehicles();
            String html = "<div class='viva-badge-banner' style='border-left-color: #64748b;'>" +
                "<div><div class='viva-badge-title'><i class='bi bi-truck-front text-secondary me-1'></i> Module 4: Vehicle Fleet Management</div>" +
                "<small class='text-muted'>Lead Developer: <strong>Senevirathna M.G.T.R</strong> &bull; Persistence: <code>data/vehicles.txt</code></small></div>" +
                "<div class='d-flex gap-2 flex-wrap'><span class='viva-concept-pill'>Fleet Entity Encapsulation</span><span class='viva-concept-pill'>Lifecycle Status State Tracking</span></div></div>" +
                "<div class='row g-4'><div class='col-lg-4'><div class='card-clean'><div class='card-clean-header'><span><i class='bi bi-plus-circle me-1 text-primary'></i> Add Vehicle</span></div>" +
                "<div class='card-clean-body'><form method='post' action='/vehicles'><input type='hidden' name='action' value='create'>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Plate Number</label><input class='form-control form-control-sm text-uppercase' name='registrationNo' required placeholder='WP CAB-1234'></div>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Make & Model</label><input class='form-control form-control-sm' name='model' required placeholder='Toyota Vitz 2021'></div>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Category</label><input class='form-control form-control-sm' name='category' required value='Car (Auto)'></div>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Transmission</label><select class='form-select form-select-sm' name='transmission'><option>AUTOMATIC</option><option>MANUAL</option></select></div>" +
                "<div class='mb-3'><label class='form-label small fw-semibold'>Status</label><select class='form-select form-select-sm' name='status'><option>AVAILABLE</option><option>IN_LESSON</option><option>MAINTENANCE</option></select></div>" +
                "<button class='btn btn-primary btn-sm w-100'>Save Vehicle</button></form></div></div></div>" +
                "<div class='col-lg-8'><div class='card-clean'><div class='card-clean-header'><span><i class='bi bi-car-front me-1 text-secondary'></i> Active Fleet (" + list.size() + ")</span></div>" +
                "<div class='table-responsive'><table class='table table-clean mb-0'>" +
                "<thead><tr><th>Plate No</th><th>Model</th><th>Category</th><th>Transmission</th><th>Status</th><th>Action</th></tr></thead><tbody>";
            for (Vehicle v : list) {
                html += "<tr><td><span class='badge bg-dark text-warning font-monospace fs-6'>" + v.getRegistrationNo() + "</span></td>" +
                        "<td>" + v.getModel() + "</td><td>" + v.getCategory() + "</td><td>" + v.getTransmission() + "</td>" +
                        "<td><span class='badge " + ("AVAILABLE".equals(v.getStatus()) ? "badge-subtle-success" : ("IN_LESSON".equals(v.getStatus()) ? "badge-subtle-warning" : "badge-subtle-danger")) + "'>" + v.getStatus() + "</span></td>" +
                        "<td><form method='post' action='/vehicles' onsubmit='return confirm(\"Delete?\")'><input type='hidden' name='action' value='delete'><input type='hidden' name='id' value='" + v.getId() + "'><button class='btn btn-sm btn-outline-danger py-0 px-2'><i class='bi bi-trash'></i></button></form></td></tr>";
            }
            html += "</tbody></table></div></div></div></div>";
            sendHtml(ex, layout("Vehicle Fleet", "vehicles", html));
        }

        private static void handlePayments(HttpExchange ex) throws IOException {
            if ("INSTRUCTOR".equals(activeRole)) {
                redirect(ex, "/lessons");
                return;
            }
            if ("POST".equalsIgnoreCase(ex.getRequestMethod())) {
                var form = parseForm(ex);
                String act = form.getOrDefault("action", "create");
                if ("delete".equals(act)) {
                    FileHandler.deleteRecord("payments.txt", form.get("id"));
                } else {
                    String id = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                    FileHandler.appendRecord("payments.txt", List.of(id, form.get("studentId"), form.get("amount"), LocalDate.now().toString(), form.get("method"), "PAID"));
                }
                redirect(ex, "/payments");
                return;
            }
            var list = DataService.getPayments();
            double total = DataService.getTotalRevenue();
            String html = "<div class='viva-badge-banner' style='border-left-color: #16a34a;'>" +
                "<div><div class='viva-badge-title'><i class='bi bi-credit-card text-success me-1'></i> Module 5: Payment & Billing Management</div>" +
                "<small class='text-muted'>Lead Developer: <strong>Weerakkody M.R.B</strong> &bull; Persistence: <code>data/payments.txt</code></small></div>" +
                "<div class='d-flex gap-2 flex-wrap'><span class='viva-concept-pill'>Financial Encapsulation</span><span class='viva-concept-pill'>Revenue Aggregation Algorithm</span></div></div>" +
                "<div class='card-clean p-3 mb-4' style='border-left:4px solid #16a34a;'>" +
                "<h4 class='fw-bold mb-0 text-success'>Total Paid Revenue: LKR " + String.format(Locale.US, "%,.2f", total) + "</h4></div>" +
                "<div class='row g-4'><div class='col-lg-4'><div class='card-clean'><div class='card-clean-header'><span><i class='bi bi-receipt me-1 text-primary'></i> Issue Payment Receipt</span></div>" +
                "<div class='card-clean-body'><form method='post' action='/payments'><input type='hidden' name='action' value='create'>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Student ID</label><input class='form-control form-control-sm' name='studentId' required placeholder='STU-1001'></div>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Amount (LKR)</label><input type='number' step='0.50' class='form-control form-control-sm' name='amount' required value='25000'></div>" +
                "<div class='mb-3'><label class='form-label small fw-semibold'>Method</label><select class='form-select form-select-sm' name='method'><option>CARD</option><option>CASH</option><option>BANK_TRANSFER</option><option>ONLINE</option></select></div>" +
                "<button class='btn btn-primary btn-sm w-100'>Issue Receipt</button></form></div></div></div>" +
                "<div class='col-lg-8'><div class='card-clean'><div class='card-clean-header'><span><i class='bi bi-clock-history me-1 text-secondary'></i> Payment Records (" + list.size() + ")</span></div>" +
                "<div class='table-responsive'><table class='table table-clean mb-0'>" +
                "<thead><tr><th>Receipt No</th><th>Student</th><th>Amount (LKR)</th><th>Method</th><th>Status</th><th>Action</th></tr></thead><tbody>";
            for (Payment p : list) {
                html += "<tr><td><span class='badge bg-light text-dark font-monospace'>" + p.getId() + "</span></td>" +
                        "<td>" + p.getStudentId() + "</td><td class='fw-bold text-success'>LKR " + String.format(Locale.US, "%,.2f", p.getAmount()) + "</td>" +
                        "<td><span class='badge badge-subtle-info'>" + p.getMethod() + "</span></td>" +
                        "<td><span class='badge badge-subtle-success'>" + p.getStatus() + "</span></td>" +
                        "<td><form method='post' action='/payments' onsubmit='return confirm(\"Delete?\")'><input type='hidden' name='action' value='delete'><input type='hidden' name='id' value='" + p.getId() + "'><button class='btn btn-sm btn-outline-danger py-0 px-2'><i class='bi bi-trash'></i></button></form></td></tr>";
            }
            html += "</tbody></table></div></div></div></div>";
            sendHtml(ex, layout("Payments", "payments", html));
        }

        private static void handleFeedback(HttpExchange ex) throws IOException {
            if ("INSTRUCTOR".equals(activeRole)) {
                redirect(ex, "/lessons");
                return;
            }
            if ("POST".equalsIgnoreCase(ex.getRequestMethod())) {
                var form = parseForm(ex);
                String act = form.getOrDefault("action", "create");
                if ("delete".equals(act)) {
                    FileHandler.deleteRecord("feedback.txt", form.get("id"));
                } else {
                    String id = "FDB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                    FileHandler.appendRecord("feedback.txt", List.of(id, form.get("studentId"), form.get("rating"), form.get("comments"), "NEW", LocalDate.now().toString()));
                }
                redirect(ex, "/feedback");
                return;
            }
            var list = DataService.getFeedbacks();
            double avg = DataService.getAverageRating();
            String html = "<div class='viva-badge-banner' style='border-left-color: #dc2626;'>" +
                "<div><div class='viva-badge-title'><i class='bi bi-chat-heart text-danger me-1'></i> Module 6: Admin Dashboard & Feedback</div>" +
                "<small class='text-muted'>Lead Developer: <strong>Sajan P.</strong> &bull; Persistence: <code>data/feedback.txt</code></small></div>" +
                "<div class='d-flex gap-2 flex-wrap'><span class='viva-concept-pill'>Rating Computation (1-5 Stars)</span><span class='viva-concept-pill'>Quality Audit & Administration</span></div></div>" +
                "<div class='card-clean p-3 mb-4' style='border-left:4px solid #dc2626;'>" +
                "<h4 class='fw-bold mb-1 text-danger'>Average Satisfaction: " + String.format(Locale.US, "%.1f", avg) + " / 5.0 Stars</h4>" +
                "<small class='text-muted'>Supervised by Admin Sajan P. for quality control.</small></div>" +
                "<div class='row g-4'><div class='col-lg-4'><div class='card-clean'><div class='card-clean-header'><span><i class='bi bi-pencil-square me-1 text-primary'></i> Submit Feedback</span></div>" +
                "<div class='card-clean-body'><form method='post' action='/feedback'><input type='hidden' name='action' value='create'>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Student ID</label><input class='form-control form-control-sm' name='studentId' required placeholder='STU-1001'></div>" +
                "<div class='mb-2'><label class='form-label small fw-semibold'>Rating</label><select class='form-select form-select-sm text-warning fw-bold' name='rating'><option value='5'>★★★★★ (5 Stars)</option><option value='4'>★★★★☆ (4 Stars)</option><option value='3'>★★★☆☆ (3 Stars)</option></select></div>" +
                "<div class='mb-3'><label class='form-label small fw-semibold'>Comments</label><textarea class='form-control form-control-sm' name='comments' rows='3' required placeholder='Feedback message...'></textarea></div>" +
                "<button class='btn btn-primary btn-sm w-100'>Submit Review</button></form></div></div></div>" +
                "<div class='col-lg-8'><div class='card-clean'><div class='card-clean-header'><span><i class='bi bi-chat-quote me-1 text-secondary'></i> Customer Reviews (" + list.size() + ")</span></div>" +
                "<div class='table-responsive'><table class='table table-clean mb-0'>" +
                "<thead><tr><th>Feedback ID</th><th>Student</th><th>Rating</th><th>Comments</th><th>Status</th><th>Action</th></tr></thead><tbody>";
            for (Feedback fb : list) {
                html += "<tr><td><span class='badge bg-light text-dark font-monospace'>" + fb.getId() + "</span></td>" +
                        "<td>" + fb.getStudentId() + "</td><td class='text-warning fw-bold'>" + fb.getRatingStars() + "</td>" +
                        "<td class='small text-muted'>&ldquo;" + fb.getComments() + "&rdquo;</td>" +
                        "<td><span class='badge badge-subtle-success'>" + fb.getStatus() + "</span></td>" +
                        "<td><form method='post' action='/feedback' onsubmit='return confirm(\"Delete?\")'><input type='hidden' name='action' value='delete'><input type='hidden' name='id' value='" + fb.getId() + "'><button class='btn btn-sm btn-outline-danger py-0 px-2'><i class='bi bi-trash'></i></button></form></td></tr>";
            }
            html += "</tbody></table></div></div></div></div>";
            sendHtml(ex, layout("Feedback & Admin", "feedback", html));
        }
    }

    // =========================================================================
    // SECTION 6: APPLICATION ENTRY POINT (MAIN METHOD)
    // =========================================================================

    public static void main(String[] args) {
        System.out.println("Starting Unified Apex Driving School Management System...");
        try {
            WebApp.startServer(8080);
        } catch (Exception e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
