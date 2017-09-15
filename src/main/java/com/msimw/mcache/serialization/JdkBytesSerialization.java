package com.msimw.mcache.serialization;

import org.springframework.util.SerializationUtils;

/**
 * Created by msimw on 17-9-15.
 *
 * java 序列化
 */
public class JdkBytesSerialization implements ISerialization {


    @Override
    public <T> T serialize(Object obj, Class<T> returnType) {
        return (T) SerializationUtils.serialize(obj);
    }

    @Override
    public <T> T deserialize(Object obj, Class<T> returnType) {
        return (T) SerializationUtils.deserialize((byte[]) obj);
    }
}
