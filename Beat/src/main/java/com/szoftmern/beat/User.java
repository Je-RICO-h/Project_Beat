package com.szoftmern.beat;

import lombok.*;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString(exclude = {"favorites", "playlists"})
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

    @Basic
    @Column(name = "country_id")
    private long countryId;

    @Basic
    @Column(name = "registration_date")
    private Date registrationDate;

    @Basic
    @Column(name = "is_logged_in")
    private boolean isLoggedIn;

    @Basic
    @Column(name = "is_filtering_explicit_lyrics")
    private boolean isFilteringExplicitLyrics;

    @ManyToMany(mappedBy = "usersWhoFavorited")
    private List<Track> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "creator")
    private List<Playlist> playlists = new ArrayList<>();

    public User(String name, String email, byte[] passHash, Byte gender, Date dateOfBirth, long countryId, Date registrationDate, boolean isFilteringExplicitLyrics) {
        this.name = name;
        this.email = email;
        this.passHash = passHash;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.countryId = countryId;
        this.registrationDate = registrationDate;
        this.isFilteringExplicitLyrics = isFilteringExplicitLyrics;
    }
}
