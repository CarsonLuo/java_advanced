package com.carson.util;

import com.moandjiezana.toml.Toml;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 * @author carson_luo
 */
public class TomlReadTest {

    public static final String BEAN_ELEMENT = "bean";
    public static final String PROPERTY_ELEMENT = "property";

    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String REF_ATTRIBUTE = "ref";

    @Test
    public void TestTomlDocRead(){
        Toml toml = new Toml();
        Toml doc = toml.read(new File("src/test/resources/spring.toml"));
        Map<String, Object> map = doc.toMap();
        System.out.println(map);

        Set<Map.Entry<String, Object>> entries = doc.entrySet();
        System.out.println(entries);
    }
}
