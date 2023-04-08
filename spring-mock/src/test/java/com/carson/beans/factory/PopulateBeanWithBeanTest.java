package com.carson.beans.factory;

import com.carson.beans.PropertyValue;
import com.carson.beans.PropertyValues;
import com.carson.beans.factory.bean.Car;
import com.carson.beans.factory.bean.Person;
import com.carson.beans.factory.config.BeanDefinition;
import com.carson.beans.factory.config.BeanReference;
import com.carson.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class PopulateBeanWithBeanTest {

    @Test
    public void TestPopulateBeanWithBean() {
        var carPv = new PropertyValues();
        carPv.addPropertyValue(new PropertyValue("brand", "BMW"));
        carPv.addPropertyValue(new PropertyValue("speed", 90.88));

        var personPv = new PropertyValues();
        personPv.addPropertyValue(new PropertyValue("name", "carson"));
        personPv.addPropertyValue(new PropertyValue("age", 26));
        personPv.addPropertyValue(new PropertyValue("car", new BeanReference("car")));

        var personBeanDefinition = new BeanDefinition(Person.class, personPv);
        var carBeanDefinition = new BeanDefinition(Car.class, carPv);

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("person", personBeanDefinition);
        beanFactory.registerBeanDefinition("car", carBeanDefinition);

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);

        Assertions.assertEquals(person.getName(), "carson");
        Assertions.assertEquals(person.getAge(), 26);
        Assertions.assertEquals(person.getCar().getBrand(), "BMW");
        Assertions.assertEquals(person.getCar().getSpeed(), 90.88);
    }
}
