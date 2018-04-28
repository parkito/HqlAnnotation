package ru.siksmfp.spring.hibernatehsql.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Artem Karnov @date 4/20/2018.
 * @email artem.karnov@t-systems.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Hql {

    String value() default "";

    boolean nativeQuery() default false;

    // TODO: 4/28/2018 Add native query implementation
    // TODO: 4/28/2018 Add logging
}
