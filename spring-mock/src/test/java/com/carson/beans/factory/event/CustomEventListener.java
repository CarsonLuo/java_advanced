package com.carson.beans.factory.event;

import com.carson.context.ApplicationListener;

/**
 * @author carson_luo
 */
public class CustomEventListener implements ApplicationListener<CustomEvent> {

    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println(this.getClass().getName() + " listened CustomEvent");
    }
}
