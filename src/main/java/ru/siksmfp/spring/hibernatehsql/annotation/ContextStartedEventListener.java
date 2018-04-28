package ru.siksmfp.spring.hibernatehsql.annotation;

import org.reflections.Reflections;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import ru.siksmfp.spring.hibernatehsql.repository.api.IGenericRepository;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Artem Karnov @date 4/26/2018.
 * @email artem.karnov@t-systems.com
 */
public class ContextStartedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final String ROOT_PACKAGE = "";

    private HqlInvocationHandler hqlInvocationHandler;
    private TypeInfoContainer typeContainer;
    private String scanPackageName;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String rootPackage = scanPackageName == null ? ROOT_PACKAGE : scanPackageName;

        Set<Class<? extends IGenericRepository>> repositories = findAllRepositoriesInClassPath(rootPackage);

        for (Class beanClass : repositories) {
            Object beanInstance = Proxy.newProxyInstance(
                    this.getClass().getClassLoader(),
                    new Class[]{beanClass},
                    hqlInvocationHandler);
            typeContainer.addInstance(beanClass, beanInstance);

            Type[] genericInterfaces = beanClass.getGenericInterfaces();
            for (Type genericInterface : genericInterfaces) {
                if (genericInterface instanceof ParameterizedType) {
                    Type genericType = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
                    typeContainer.addObjectType(beanClass, genericType.getTypeName());
                }
            }
        }

        String[] beanNames = event.getApplicationContext().getBeanDefinitionNames();

        for (String beanName : beanNames) {
            Object bean = event.getApplicationContext().getBean(beanName);
            Field[] beanFields = bean.getClass().getDeclaredFields();
            for (Field beanField : beanFields) {
                List<Class> clss = Arrays.asList(beanField.getType().getInterfaces());
                if (clss.contains(IGenericRepository.class)) {
                    beanField.setAccessible(true);
                    try {
                        if (beanField.get(bean) == null) {
                            beanField.set(bean, beanField.getType().cast(typeContainer.getInstance(beanField.getType())));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
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

    public void setTypeContainer(TypeInfoContainer typeContainer) {
        this.typeContainer = typeContainer;
    }

    public TypeInfoContainer getTypeContainer() {
        return typeContainer;
    }

    public String getScanPackageName() {
        return scanPackageName;
    }

    public void setScanPackageName(String scanPackageName) {
        this.scanPackageName = scanPackageName;
    }
}
