package com.example.socialnet;

import javax.persistence.*;

@Entity
public class Relation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user_initial;

    @ManyToOne
    private User user_invited;


    private boolean confirmed;

    public Relation() {
    }

    public Relation(User user_initial, User user_invited, boolean confirmed) {
        this.user_initial = user_initial;
        this.user_invited = user_invited;
        this.confirmed = confirmed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser_initial() {
        return user_initial;
    }

    public void setUser_initial(User user_initial) {
        this.user_initial = user_initial;
    }

    public User getUser_invited() {
        return user_invited;
    }

    public void setUser_invited(User user_invited) {
        this.user_invited = user_invited;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
