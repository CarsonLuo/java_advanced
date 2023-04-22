package com.carson.context.annotation;

import cn.hutool.core.util.ClassUtil;
import com.carson.beans.factory.config.BeanDefinition;
import com.carson.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * candidate 候选者, 申请人
 *
 * @author carson_luo
 */
public class ClassPathScanningCandidateComponentProvider {

    /**
     * 扫描 -> 类 -> BeanDefinition
     */
    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        LinkedHashSet<BeanDefinition> candidates = new LinkedHashSet<>();
        // 扫描带有 '@Component' 注解的类
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
        for (Class<?> clazz : classes) {
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            candidates.add(beanDefinition);
        }
        return candidates;
    }
}
