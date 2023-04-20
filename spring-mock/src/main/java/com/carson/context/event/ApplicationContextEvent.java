package com.carson.context.event;

import com.carson.context.ApplicationContext;
import com.carson.context.ApplicationEvent;

/**
 * @author carson_luo
 */
public abstract class ApplicationContextEvent extends ApplicationEvent {

    public ApplicationContextEvent(ApplicationContext source) {
        super(source);
    }

    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }
}
