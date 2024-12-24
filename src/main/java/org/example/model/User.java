package org.example.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String registrationCode;

    private boolean isCoordinator;

    private String phoneNumber;
    private String address;
    private String profilePicture;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToMany(mappedBy = "creator")
    private Set<Event> createdEvents = new HashSet<>();

    @ManyToMany(mappedBy = "participants")
    private Set<Event> attendingEvents = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Post> posts = new HashSet<>();

    public User() {
        this.createdAt = new Date();
    }

    public User(String username, String email, String registrationCode) {
        this.username = username;
        this.email = email;
        setRegistrationCode(registrationCode);
        this.createdAt = new Date();
    }

    public void setRegistrationCode(String code) {
        this.registrationCode = code;
        this.isCoordinator = "COORD123".equals(code);
    }

    // Standard getters and setters
    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        // הגיון לבדיקה אם המשתמש הוא אדמין
        // למשל, בדיקה של דגל או תפקיד במשתמש
        return this.isCoordinator;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Set<Event> getCreatedEvents() {
        return createdEvents;
    }

    public Set<Event> getAttendingEvents() {
        return attendingEvents;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}