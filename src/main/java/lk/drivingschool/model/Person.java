package lk.drivingschool.model;

/**
 * Abstract class representing a Person in the system.
 * OOP Concepts:
 * 1. Abstraction: Declares abstract method getRole().
 * 2. Encapsulation: Private variables accessed via public getters and setters.
 */
public abstract class Person {
    private String id;
    private String name;
    private String email;
    private String phone;

    public Person(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Abstract method: every subclass must specify its role
    public abstract String getRole();

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
