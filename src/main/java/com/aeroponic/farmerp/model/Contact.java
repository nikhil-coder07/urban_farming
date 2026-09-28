package com.aeroponic.farmerp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(length = 150, nullable = false)
    private String name;

    @NotNull(message = "Contact type is required (CUSTOMER or VENDOR)")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContactType type;

    @Column(length = 300)
    private String address;

    @Pattern(regexp = "^$|^[0-9]{10}$", message = "Phone must be 10 digits or empty")
    @Column(length = 20)
    private String phone;

    public Contact() {}

    public Contact(Long id, String name, ContactType type, String address, String phone) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.address = address;
        this.phone = phone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ContactType getType() { return type; }
    public void setType(ContactType type) { this.type = type; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}