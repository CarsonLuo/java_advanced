package com.carson.context.event;

import com.carson.context.ApplicationContext;

/**
 * @author carson_luo
 */
public class ContextRefreshedEvent extends ApplicationContextEvent {

    public ContextRefreshedEvent(ApplicationContext source) {
        super(source);
    }
}
