package com.example.socialnet;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class User implements Serializable{
    private static final long serialVersionUID = 8539936152170847419L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String username;

    private String password;
    private boolean enabled;

    private String name;
    private String surname;

    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bornDate;

    private String street;
    private String number;
    private String city;
    private String postCode;

    @OneToMany(mappedBy = "user_initial")
    List<Relation> relations;


    @OneToMany(mappedBy = "user_invited")
    List<Relation> invitations;

    public User() {
    }

    public User(String username, String password, boolean enabled, String name, String surname, Date bornDate,
                String street, String number, String city, String postCode, List<Relation> relations,
                List<Relation> invitations) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.name = name;
        this.surname = surname;
        this.bornDate = bornDate;
        this.street = street;
        this.number = number;
        this.city = city;
        this.postCode = postCode;
        this.relations = relations;
        this.invitations = invitations;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBornDate() {
        return bornDate;
    }

    public void setBornDate(Date bornDate) {
        this.bornDate = bornDate;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public List<Relation> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<Relation> invitations) {
        this.invitations = invitations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                isEnabled() == user.isEnabled() &&
                Objects.equals(getUsername(), user.getUsername()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getSurname(), user.getSurname()) &&
                Objects.equals(getBornDate(), user.getBornDate()) &&
                Objects.equals(getStreet(), user.getStreet()) &&
                Objects.equals(getNumber(), user.getNumber()) &&
                Objects.equals(getCity(), user.getCity()) &&
                Objects.equals(getPostCode(), user.getPostCode()) &&
                Objects.equals(getRelations(), user.getRelations()) &&
                Objects.equals(getInvitations(), user.getInvitations());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getUsername(), getPassword(), isEnabled(), getName(), getSurname(), getBornDate(), getStreet(), getNumber(), getCity(), getPostCode(), getRelations(), getInvitations());
    }
}