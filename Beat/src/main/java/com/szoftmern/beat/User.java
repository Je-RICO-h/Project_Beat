package com.szoftmern.beat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
    private String name;
    private String email;
    private String passHash;
    private byte gender;
    private Date dateOfBirth;
    private String Country;
    private List<Track> favorites;

}
