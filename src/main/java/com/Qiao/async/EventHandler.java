package com.Qiao.async;

import java.util.List;

/**
 * Created by white and black on 2016/8/28.
 */
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupporEventType();
}
