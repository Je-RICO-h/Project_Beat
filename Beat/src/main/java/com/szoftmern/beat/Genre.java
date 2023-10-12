package com.szoftmern.beat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Genres", schema = "beat-db")
public class Genre {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "genre")
    private String genre;

    @ManyToMany(mappedBy = "genres")
    private List<Track> tracks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Genre genre1 = (Genre) o;

        if (id != genre1.id) return false;
        if (genre != null ? !genre.equals(genre1.genre) : genre1.genre != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        return result;
    }
}
