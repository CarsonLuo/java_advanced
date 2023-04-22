package com.carson.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author carson_luo
 */
public class PropertyValues {

    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    public void addPropertyValue(PropertyValue pv) {
        for (int i = 0; i < propertyValueList.size(); i++) {
            PropertyValue propertyValue = this.propertyValueList.get(i);
            if (propertyValue.getName().equals(pv.getName())) {
                this.propertyValueList.set(i, pv);
                return;
            }
        }
        this.propertyValueList.add(pv);
    }

    public PropertyValue[] getPropertyValues() {
        return this.propertyValueList.toArray(new PropertyValue[0]);
    }

    public PropertyValue getPropertyValue(String propertyName) {
        for (int i = 0; i < this.propertyValueList.size(); i++) {
            PropertyValue pv = this.propertyValueList.get(i);
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }
}
