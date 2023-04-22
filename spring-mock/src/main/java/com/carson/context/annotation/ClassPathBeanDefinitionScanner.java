package com.carson.context.annotation;

import cn.hutool.core.util.StrUtil;
import com.carson.beans.factory.config.BeanDefinition;
import com.carson.beans.factory.support.BeanDefinitionRegistry;
import com.carson.stereotype.Component;

import java.util.Set;

/**
 * @author carson_luo
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            // 扫描 -> 类 -> BeanDefinition
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);

            // 完善BeanDefinition属性 -> 注册到容器中
            for (BeanDefinition candidate : candidates) {
                // 解析Bean的作用域
                String beanScope = resolveBeanScope(candidate);
                if (StrUtil.isNotEmpty(beanScope)) {
                    candidate.setScope(beanScope);
                }
                // 生成Bean的名称
                String beanName = determineBeanName(candidate);
                // 注册 BeanDefinition
                registry.registerBeanDefinition(beanName, candidate);
            }
        }
    }

    /**
     * 获取bean的作用域
     */
    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (scope != null) {
            return scope.value();
        }
        return StrUtil.EMPTY;
    }

    /**
     * 生成Bean名称
     */
    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component component = beanClass.getAnnotation(Component.class);
        String value = component.value();
        if (StrUtil.isEmpty(value)) {
            value = StrUtil.lowerFirst(beanClass.getSimpleName());
        }
        return value;
    }
}
