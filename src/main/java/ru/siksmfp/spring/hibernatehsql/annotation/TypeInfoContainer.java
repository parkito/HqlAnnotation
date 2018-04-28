package ru.siksmfp.spring.hibernatehsql.annotation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Artem Karnov @date 4/27/2018.
 * @email artem.karnov@t-systems.com
 */
public class TypeInfoContainer {

    private final static Map<Class, RepositoryInfo> TYPE_CONTAINER_MAP = new HashMap<>();

    public void addInstance(Class clazz, Object object) {
        RepositoryInfo repositoryInfo = new RepositoryInfo();
        repositoryInfo.setInstance(object);
        TYPE_CONTAINER_MAP.put(clazz, repositoryInfo);
    }

    public void addObjectType(Class clazz, String objectType) {
        RepositoryInfo repositoryInfo = TYPE_CONTAINER_MAP.get(clazz);
        if (repositoryInfo != null) {
            repositoryInfo.setTypeName(objectType);
        } else {
            throw new IllegalStateException("Context was initialized with violations. Bean " + clazz + " wasn't initialized. ");
        }
    }

    public Object getInstance(Class clazz) {
        RepositoryInfo repositoryInfo = TYPE_CONTAINER_MAP.get(clazz);
        if (repositoryInfo != null) {
            return repositoryInfo.getInstance();
        } else {
            throw new IllegalStateException("Context was initialized with violations. Can't find an instance of " + clazz);
        }
    }


    public String getFullName(Object object) {
        for (Map.Entry<Class, RepositoryInfo> next : TYPE_CONTAINER_MAP.entrySet()) {
            if (next.getValue().getInstance() == object) {
                return next.getValue().getTypeName();
            }
        }
        throw new IllegalStateException("Context was initialized with violations. Can't find an instance of " + object);
    }

    public String getShortName(Object object) {
        String fullName = getFullName(object);
        String[] split = fullName.split("\\.");
        return split[split.length - 1];

    }
}
