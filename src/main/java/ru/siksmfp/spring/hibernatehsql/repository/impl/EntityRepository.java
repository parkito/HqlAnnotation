package ru.siksmfp.spring.hibernatehsql.repository.impl;

import org.springframework.stereotype.Repository;
import ru.siksmfp.spring.hibernatehsql.annotation.Hql;
import ru.siksmfp.spring.hibernatehsql.entity.Data;
import ru.siksmfp.spring.hibernatehsql.repository.api.IGenericRepository;

import java.util.List;

/**
 * @author Artem Karnov @date 4/17/2018.
 * @email artem.karnov@t-systems.com
 */
public interface EntityRepository extends IGenericRepository<Data, Long> {

    @Hql(value = "select d from Data d where d.dataString = '?1'")
    List<Data> findDataByString(String string);

}
