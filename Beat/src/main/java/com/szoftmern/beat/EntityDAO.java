package com.szoftmern.beat;

import java.util.List;

public interface EntityDAO extends AutoCloseable {
    public <T> void saveEntity(T a);
    public List<?> getEntities();
    public <T> void updateEntity(T a);
    public <T> void deleteEntity(T a);
}
