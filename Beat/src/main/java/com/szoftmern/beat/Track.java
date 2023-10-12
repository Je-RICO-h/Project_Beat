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
    private long playCount;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Track track = (Track) o;

        if (id != track.id) return false;
        if (playCount != track.playCount) return false;
        if (title != null ? !title.equals(track.title) : track.title != null) return false;
        if (isLyricsExplicit != null ? !isLyricsExplicit.equals(track.isLyricsExplicit) : track.isLyricsExplicit != null)
            return false;
        if (resourceUrl != null ? !resourceUrl.equals(track.resourceUrl) : track.resourceUrl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (isLyricsExplicit != null ? isLyricsExplicit.hashCode() : 0);
        result = 31 * result + (resourceUrl != null ? resourceUrl.hashCode() : 0);
        result = 31 * result + (int) (playCount ^ (playCount >>> 32));
        return result;
    }
}
