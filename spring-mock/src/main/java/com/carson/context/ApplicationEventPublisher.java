package com.carson.context;

/**
 * 事件发布者接口
 *
 * @author carson_luo
 */
public interface ApplicationEventPublisher {

    /**
     * 发布事件
     */
    void publishEvent(ApplicationEvent event);
}
