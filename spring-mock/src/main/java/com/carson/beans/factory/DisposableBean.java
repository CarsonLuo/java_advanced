package com.carson.beans.factory;

/**
 * Disposable: 一次性的, 用完即丢弃的, (人, 观点) 可有可无的, 可轻易放弃的
 *
 * @author carson_luo
 */
public interface DisposableBean {

    void destroy() throws Exception;
}
