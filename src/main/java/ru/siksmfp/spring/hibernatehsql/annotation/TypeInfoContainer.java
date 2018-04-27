package ru.siksmfp.spring.hibernatehsql.annotation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Artem Karnov @date 4/27/2018.
 * @email artem.karnov@t-systems.com
 */
public class TypeInfoContainer {

    private final static Map<Class, ObjectInfo> TYPE_CONTAINER_MAP = new HashMap<>();

    public void addInstance(Class clazz, Object object) {
        ObjectInfo objectInfo = new ObjectInfo();
        objectInfo.setInstance(object);
        TYPE_CONTAINER_MAP.put(clazz, objectInfo);
    }

    public void addObjectType(Class clazz, String objectType) {
        ObjectInfo objectInfo = TYPE_CONTAINER_MAP.get(clazz);
        objectInfo.setTypeName(objectType);
    }

    public Object getInstance(Class clazz) {
        ObjectInfo objectInfo = TYPE_CONTAINER_MAP.get(clazz);
        if (objectInfo != null) {
            return objectInfo.getInstance();
        } else {
            throw new IllegalStateException("Can't find an instance of " + clazz);
        }
    }

    public String getObjectTypeName(Object object) {
        for (Map.Entry<Class, ObjectInfo> next : TYPE_CONTAINER_MAP.entrySet()) {
            if (next.getValue().getInstance() == object) {
                return next.getValue().getTypeName();
            }
        }
        throw new IllegalStateException("Can't find an instance of " + object);
    }
}
