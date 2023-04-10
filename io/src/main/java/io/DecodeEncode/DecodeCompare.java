package io.DecodeEncode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * 序列号的目的: (1) 便于网络传输 (2) 便于对象存储
 *
 * 在跨进程调用时, 要将 java对线 -> 字节数组或者 ByteBuffer对象; 接受后将其解码为 java对象; --- java对象编解码技术
 *
 * java序列化缺点: (1)无法跨语言 (2)序列化后的码流太大 (3)序列化性能低
 *
 * 编解码框架的优劣评判
 * (1)是否跨语言
 * (2)编码后的码流大小
 * (3)编解码的性能
 * (4)类库是否小巧, API使用是否方便
 * (5)使用者需要手工开发的工作量和难度
 */
public class DecodeCompare {
    public static byte[] serialize(User user) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(user);
        os.flush();
        os.close();
        byte[] bytes = bos.toByteArray();
        bos.close();
        return bytes;
    }

    public static void main(String[] args) throws IOException {
        User user = new User(1,"carson");
        System.out.println("JDK serializable length : " + serialize(user).length);
        System.out.println("The byte array serializable length is : " + user.decode().length);

        int loop = 100000;
        long st1 = System.currentTimeMillis();
        for(int i = 0; i < loop; i++){
            serialize(user);
        }
        System.out.println("JDK serializable time cost : " + (System.currentTimeMillis() - st1));

        long st2 = System.currentTimeMillis();
        for(int i = 0; i < loop; i++){
            user.decode();
        }
        System.out.println("The byte array serializable time cost : " + (System.currentTimeMillis() - st2));
    }
}

class User implements Serializable{
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // 二进制编解码
    public byte[] decode(){
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] value = this.name.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.id);
        buffer.flip();
        value = null;
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }
}
