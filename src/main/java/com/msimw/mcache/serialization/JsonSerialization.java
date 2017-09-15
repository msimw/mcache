package com.msimw.mcache.serialization;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by msimw on 17-9-15.
 *
 * json序列化
 */
public class JsonSerialization implements ISerialization {


    @Override
    public <T> T serialize(Object obj, Class<T> returnType) {
        return (T) JSONObject.toJSONString(obj);
    }

    @Override
    public <T> T deserialize(Object obj, Class<T> returnType) {
        return JSONObject.parseObject(String.valueOf(obj),returnType);
    }
}
