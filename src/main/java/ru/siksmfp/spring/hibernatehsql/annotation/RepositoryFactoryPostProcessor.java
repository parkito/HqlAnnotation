package ru.siksmfp.spring.hibernatehsql.annotation;

import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import ru.siksmfp.spring.hibernatehsql.repository.api.IGenericRepository;

import java.lang.reflect.Proxy;
import java.util.Set;

/**
 * @author Artem Karnov @date 4/23/2018.
 * @email artem.karnov@t-systems.com
 */
public class RepositoryFactoryPostProcessor implements BeanFactoryPostProcessor {

    private HqlInvocationHandler hqlInvocationHandler;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Set<Class<? extends IGenericRepository>> repos = findAllRepositoriesInClassPath("ru");

        for (Class bean : repos) {
            Object newBean = Proxy.newProxyInstance(
                    this.getClass().getClassLoader(),
                    new Class[]{bean.getClass()},
                    hqlInvocationHandler);

            beanFactory.registerSingleton(bean.getName(), newBean);
        }
    }

    private Set<Class<? extends IGenericRepository>> findAllRepositoriesInClassPath(String rootPackage) {
        Reflections reflections = new Reflections(rootPackage);
        return reflections.getSubTypesOf(IGenericRepository.class);
    }

    public void setHqlInvocationHandler(HqlInvocationHandler hqlInvocationHandler) {
        this.hqlInvocationHandler = hqlInvocationHandler;
    }

    public HqlInvocationHandler getHqlInvocationHandler() {
        return hqlInvocationHandler;
    }
}
