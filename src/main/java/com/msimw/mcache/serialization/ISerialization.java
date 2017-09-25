package com.msimw.mcache.serialization;

/**
 * Created by msimw on 17-9-15.
 *
 * 序列化 接口
 */
public interface ISerialization {

    /**
     * 序列化
     * @param obj
     * @param returnType
     * @return
     */
    public <T> T serialize(Object obj,Class<T> returnType);


    /**
     * 反序列化
     * @param obj
     * @return
     */
    public <T> T deserialize(Object obj,Class<T> returnType);
}
