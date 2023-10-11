package com.szoftmern.beat;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Track {
    private long id;
    private String title;
    private boolean explicit;
    private String resourceURL;
    private int playCount;
    private List<String> artists;
    private List<String> genres;
}
