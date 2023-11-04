package com.szoftmern.beat;

import jakarta.persistence.Entity;
import lombok.*;

import jakarta.persistence.*;

import java.util.Comparator;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"artists", "playlists", "genres", "usersWhoFavorited"})
@EqualsAndHashCode
@Entity
@Table(name = "Tracks", schema = "beat-db")
public class Track {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "explicit")
    private Boolean isLyricsExplicit;

    @Basic
    @Column(name = "resource_url")
    private String resourceUrl;

    @Basic
    @Column(name = "play_count")
    private int playCount;

    @ManyToMany
    @JoinTable(name = "Track_Artists", schema = "beat-db", joinColumns = @JoinColumn(name = "track_id", referencedColumnName = "id", nullable = false), inverseJoinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "id", nullable = false))
    private List<Artist> artists;

    @ManyToMany
    @JoinTable(name = "Playlist_Tracks", schema = "beat-db", joinColumns = @JoinColumn(name = "track_id", referencedColumnName = "id", nullable = false), inverseJoinColumns = @JoinColumn(name = "playlist_id", referencedColumnName = "id", nullable = false))
    private List<Playlist> playlists;

    @ManyToMany
    @JoinTable(name = "Track_Genres", schema = "beat-db", joinColumns = @JoinColumn(name = "track_id", referencedColumnName = "id", nullable = false), inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id", nullable = false))
    private List<Genre> genres;

    @ManyToMany
    @JoinTable(name = "Favorite_Tracks", schema = "beat-db", joinColumns = @JoinColumn(name = "track_id", referencedColumnName = "id", nullable = false), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false))
    private List<User> usersWhoFavorited;


    public static Comparator<Track> playCountComparator = new Comparator<Track>() {
        @Override
        public int compare(Track o1, Track o2) {
            return o1.playCount - o2.playCount;
        }
    };

    public static Comparator<Track> titleComparator = new Comparator<Track>() {
        @Override
        public int compare(Track o1, Track o2) {
            return o1.title.compareTo(o2.title);
        }
    };
}
