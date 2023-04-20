package com.carson.context.event;

import com.carson.context.ApplicationEvent;
import com.carson.context.ApplicationListener;

/**
 * multicast : 多播
 * <p>
 * 该接口是注册监听器和发布事件的抽象接口
 *
 * @author carson_luo
 */
public interface ApplicationEventMulticaster {

    /**
     * 添加监听者
     */
    void addApplicationListener(ApplicationListener<?> listener);

    /**
     * 移除监听者
     */
    void removeApplicationListener(ApplicationListener<?> listener);

    /**
     * 多播事件
     */
    void multicastEvent(ApplicationEvent event);
}
