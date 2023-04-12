package com.carson.context;

import java.util.EventListener;

/**
 * 事件监听者接口
 *
 * @author carson_luo
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    void onApplicationEvent(E event);
}
