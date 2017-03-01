package com.ex.async;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xc on 17-3-1.
 */
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();
}
