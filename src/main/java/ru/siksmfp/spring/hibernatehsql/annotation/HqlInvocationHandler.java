package ru.siksmfp.spring.hibernatehsql.annotation;

import ru.siksmfp.spring.hibernatehsql.repository.api.GenericRepository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Artem Karnov @date 4/23/2018.
 * @email artem.karnov@t-systems.com
 */
public class HqlInvocationHandler implements InvocationHandler {

    private GenericRepository genericRepository;
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
            return genericRepository.performQuery(query, returnType);
        }

        String objectTypeName = typeContainer.getObjectTypeName(proxy);

        switch (method.getName()) {
            case "save":
                genericRepository.save(args[0]);
                return null;
            case "batchSave":
                genericRepository.batchSave((List) args[0], objectTypeName);
                return null;
            case "find":
                return genericRepository.find(args[0], objectTypeName);
            case "update":
                genericRepository.update(args[0]);
                return null;
            case "delete":
                genericRepository.delete(args[0]);
                return null;
            case "getAll":
                return genericRepository.getAll(objectTypeName);
            case "deleteAll":
                genericRepository.deleteAll(objectTypeName);
            case "countElements":
                return genericRepository.countElements(objectTypeName);
            default:
                return null;

        }
    }

    private String wiveParametersToQuery(String query, String[] parameters) {
        for (int i = 0; i < parameters.length; i++) {
            String replacePattern = "\\?" + (i + 1);
            query = query.replaceFirst(replacePattern, parameters[i]);
        }
        return query;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    public GenericRepository getGenericRepository() {
        return genericRepository;
    }

    public void setTypeContainer(TypeInfoContainer typeContainer) {
        this.typeContainer = typeContainer;
    }

    public TypeInfoContainer getTypeContainer() {
        return typeContainer;
    }
}
