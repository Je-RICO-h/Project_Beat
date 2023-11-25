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
@IdClass(PlaylistTracks.class)
@Table(name = "Playlist_Tracks", schema = "beat-db")
public class PlaylistTracks implements Serializable {
    @Id
    @Column(name = "playlist_id")
    private long playlist_id;

    @Id
    @Column(name = "track_id")
    private long track_id;
}
