package com.carson.beans.factory.event;

import com.carson.context.ApplicationListener;
import com.carson.context.event.ContextRefreshedEvent;

/**
 * @author carson_luo
 */
public class MyContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println(this.getClass().getName() + " listened ContextRefreshedEvent");
    }
}
