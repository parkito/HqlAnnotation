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

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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

        switch (method.getName()) {
            case "save":
                genericRepository.save(args[0]);
                return null;
            case "batchSave":
                genericRepository.batchSave((List) args[0]);
                return null;
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
}
