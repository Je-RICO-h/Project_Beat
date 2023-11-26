package com.szoftmern.beat;

import lombok.*;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"tracks"})
@EqualsAndHashCode
@Entity
@Table(name = "Playlists", schema = "beat-db")
public class Playlist {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "creator")
    private User creator;

    @Basic
    @Column(name = "creation_date")
    private Date creationDate;

    @ManyToMany(mappedBy = "playlists")
    private List<Track> tracks = new ArrayList<>();

    public Playlist(String name, User creator, Date creationDate) {
        this.name = name;
        this.creator = creator;
        this.creationDate = creationDate;
    }
}
