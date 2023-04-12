package com.carson.beans.factory.event;

import com.carson.context.ApplicationListener;
import com.carson.context.event.ContextClosedEvent;

/**
 * @author carson_luo
 */
public class MyContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println(this.getClass().getName() + " listened ContextCloseEvent");

        // 附加记录: 监听'容器关闭事件'时, 如果产生异常, 会导致后面该事件的监听者无法处理 😂
        // throw new RuntimeException("listen ContextCloseEvent, threw exception");
    }
}
