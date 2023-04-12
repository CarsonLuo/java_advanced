package com.carson.context;

import java.util.EventObject;

/**
 * @author carson_luo
 */
public abstract class ApplicationEvent extends EventObject {

    public ApplicationEvent(Object source) {
        super(source);
    }
}
