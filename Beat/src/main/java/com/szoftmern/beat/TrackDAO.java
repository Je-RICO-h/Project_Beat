package com.szoftmern.beat;

import java.util.List;

public interface TrackDAO extends AutoCloseable {
    public void saveEntity(Track a);
    public List<Track> getEntities();
    public void updateEntity(Track a);
    public void deleteEntity(Track a);
}
