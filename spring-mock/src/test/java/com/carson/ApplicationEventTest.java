package com.carson;

import com.carson.beans.factory.event.CustomEvent;
import com.carson.context.support.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class ApplicationEventTest {

    @Test
    public void TestApplicationEvent() {
        var applicationContext = new ClassPathXmlApplicationContext("classpath:spring-event.xml");

        applicationContext.publishEvent(new CustomEvent(applicationContext));

        applicationContext.registerShutdownHook();
    }
}
