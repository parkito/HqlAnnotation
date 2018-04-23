package ru.siksmfp.spring.hibernatehsql.annotation;

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
}
