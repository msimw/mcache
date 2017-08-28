package com.msimw.mcache.handler.bean;


import java.io.Serializable;

/**
 * Created by 胡明 on 17-7-14.
 * 代表参数
 */
public class Parameter implements Serializable {

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数类型
     */
    private Class<?> type;

    /**
     * 参数值
     */
    private Object value;




    public Parameter() {
    }

    public Parameter(String name, Class<?> type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
