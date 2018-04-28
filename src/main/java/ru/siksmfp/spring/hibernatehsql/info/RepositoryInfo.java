package ru.siksmfp.spring.hibernatehsql.info;

/**
 * @author Artem Karnov @date 4/27/2018.
 * @email artem.karnov@t-systems.com
 */
public class RepositoryInfo {
    private Object instance;
    private String typeName;

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
