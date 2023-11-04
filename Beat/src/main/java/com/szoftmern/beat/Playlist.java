package com.szoftmern.beat;

import lombok.*;

import jakarta.persistence.*;

import java.sql.Date;
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

    @Basic
    @Column(name = "public")
    private boolean isPublic;

    @Basic
    @Column(name = "image_url")
    private String imageUrl;

    @Basic
    @Column(name = "creator")
    private long creator;

    @Basic
    @Column(name = "creation_date")
    private Date creationDate;

    @ManyToMany(mappedBy = "playlists")
    private List<Track> tracks;
}
