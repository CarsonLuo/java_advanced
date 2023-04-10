package com.carson.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 资源的抽象和访问的接口
 *
 * @author carson_luo
 */
public interface Resource {

    InputStream getInputStream() throws IOException;
}
