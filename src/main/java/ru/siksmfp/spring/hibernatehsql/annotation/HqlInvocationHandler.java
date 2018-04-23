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

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    public GenericRepository getGenericRepository() {
        return genericRepository;
    }
}
