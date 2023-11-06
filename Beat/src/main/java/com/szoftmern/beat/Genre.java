package com.szoftmern.beat;

import lombok.*;

import jakarta.persistence.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"tracks"})
@EqualsAndHashCode
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
}
