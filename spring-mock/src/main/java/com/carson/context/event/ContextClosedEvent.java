package com.carson.context.event;

import com.carson.context.ApplicationContext;

/**
 * @author carson_luo
 */
public class ContextClosedEvent extends ApplicationContextEvent {

    public ContextClosedEvent(ApplicationContext source) {
        super(source);
    }
}
