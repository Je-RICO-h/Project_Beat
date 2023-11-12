package com.szoftmern.beat;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@IdClass(FavoriteTracks.class)
@Table(name = "Favorite_Tracks", schema = "beat-db")
public class FavoriteTracks implements Serializable {
    @Id
    @Column(name = "user_id")
    private long user_id;

    @Id
    @Column(name = "track_id")
    private long track_id;
}
