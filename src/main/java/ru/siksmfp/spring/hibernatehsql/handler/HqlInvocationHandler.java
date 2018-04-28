package ru.siksmfp.spring.hibernatehsql.handler;

import ru.siksmfp.spring.hibernatehsql.api.Hql;
import ru.siksmfp.spring.hibernatehsql.info.TypeInfoContainer;
import ru.siksmfp.spring.hibernatehsql.repository.GenericRepositoryImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Artem Karnov @date 4/23/2018.
 * @email artem.karnov@t-systems.com
 */
public class HqlInvocationHandler implements InvocationHandler {

    private GenericRepositoryImpl genericRepositoryImpl;
    private TypeInfoContainer typeContainer;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Hql annotation = method.getAnnotation(Hql.class);
        if (annotation != null) {
            int parameterCount = args.length;
            String[] parameters = new String[parameterCount];
            for (int i = 0; i < parameterCount; i++) {
                parameters[i] = (String) args[i];
            }

            String queryValue = annotation.value();
            String query = wiveParametersToQuery(queryValue, parameters);
            Class<?> returnType = method.getReturnType();
            return genericRepositoryImpl.performQuery(query, returnType);
        }
        String fullName, shortName;

        switch (method.getName()) {
            case "save":
                genericRepositoryImpl.save(args[0]);
                return null;
            case "batchSave":
                shortName = typeContainer.getShortName(proxy);
                genericRepositoryImpl.batchSave((List) args[0], shortName);
                return null;
            case "find":
                fullName = typeContainer.getFullName(proxy);
                return genericRepositoryImpl.find(args[0], fullName);
            case "update":
                genericRepositoryImpl.update(args[0]);
                return null;
            case "delete":
                genericRepositoryImpl.delete(args[0]);
                return null;
            case "getAll":
                shortName = typeContainer.getShortName(proxy);
                return genericRepositoryImpl.getAll(shortName);
            case "deleteAll":
                shortName = typeContainer.getShortName(proxy);
                genericRepositoryImpl.deleteAll(shortName);
            case "countElements":
                shortName = typeContainer.getShortName(proxy);
                return genericRepositoryImpl.countElements(shortName);
            default:
                return null;

        }
    }

    private String wiveParametersToQuery(String query, String[] parameters) {
        // TODO: 4/28/2018 Get rid of ''
        for (int i = 0; i < parameters.length; i++) {
            String replacePattern = "\\?" + (i + 1);
            query = query.replaceFirst(replacePattern, parameters[i]);
        }
        return query;
    }

    public void setGenericRepositoryImpl(GenericRepositoryImpl genericRepositoryImpl) {
        this.genericRepositoryImpl = genericRepositoryImpl;
    }

    public GenericRepositoryImpl getGenericRepositoryImpl() {
        return genericRepositoryImpl;
    }

    public void setTypeContainer(TypeInfoContainer typeContainer) {
        this.typeContainer = typeContainer;
    }

    public TypeInfoContainer getTypeContainer() {
        return typeContainer;
    }
}
