package com.szoftmern.beat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Playlist playlist = (Playlist) o;

        if (id != playlist.id) return false;
        if (isPublic != playlist.isPublic) return false;
        if (creator != playlist.creator) return false;
        if (name != null ? !name.equals(playlist.name) : playlist.name != null) return false;
        if (imageUrl != null ? !imageUrl.equals(playlist.imageUrl) : playlist.imageUrl != null) return false;
        if (creationDate != null ? !creationDate.equals(playlist.creationDate) : playlist.creationDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (isPublic ? 1 : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (int) (creator ^ (creator >>> 32));
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        return result;
    }
}
