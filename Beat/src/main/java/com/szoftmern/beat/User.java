package com.szoftmern.beat;

import lombok.*;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@ToString(exclude = {"favorites"})
@EqualsAndHashCode
@Entity
@Table(name = "Users", schema = "beat-db")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "password_hash")
    private byte[] passHash;

    @Basic
    @Column(name = "gender")
    private Byte gender;

    @Basic
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Basic
    @Column(name = "registration_date")
    private Date registrationDate;

    @ManyToMany(mappedBy = "usersWhoFavorited")
    private List<Track> favorites;

    public User(String name, String email, byte[] passHash, Byte gender, Date dateOfBirth, Country country, Date registrationDate) {
        this.name = name;
        this.email = email;
        this.passHash = passHash;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.registrationDate = registrationDate;
    }
}
