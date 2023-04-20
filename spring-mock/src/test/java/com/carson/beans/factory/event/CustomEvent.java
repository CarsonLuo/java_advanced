package com.carson.beans.factory.event;

import com.carson.context.ApplicationContext;
import com.carson.context.event.ApplicationContextEvent;

/**
 * @author carson_luo
 */
public class CustomEvent extends ApplicationContextEvent {

    public CustomEvent(ApplicationContext source) {
        super(source);
    }
}
