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
@Table(name = "Artists", schema = "beat-db")
public class Artist {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "artists")
    private List<Track> tracks;
}
