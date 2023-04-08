package com.carson.core.io;

/**
 * 资源加载接口
 * 资源查找定位策略的抽象
 *
 * @author carson_luo
 */
public interface ResourceLoader {

    Resource getResource(String location);
}
