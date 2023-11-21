package com.szoftmern.beat;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "users")
@EqualsAndHashCode
@Entity
@Table(name = "Countries", schema = "beat-db")
public class Country {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "total_play_count")
    private int totalPlayCount;

    @OneToMany(mappedBy = "country")
    private List<User> users;
}
