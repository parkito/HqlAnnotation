package ru.siksmfp.spring.hibernatehsql.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.siksmfp.spring.hibernatehsql.entity.Data;
import ru.siksmfp.spring.hibernatehsql.repository.impl.EntityRepository;

import java.util.List;

/**
 * @author Artem Karnov @date 4/22/2018.
 * @email artem.karnov@t-systems.com
 */
@Service
public class EntityService {
    
    private EntityRepository entityRepository;

    public void save(Data data) {
        entityRepository.save(data);
    }

    public void deleteAll() {
        entityRepository.deleteAll();
    }

    public List<Data> getAll() {
        return entityRepository.getAll();
    }

    public void delete(Data data) {
        entityRepository.delete(data);
    }

    public Data find(long id) {
        return entityRepository.find(id);
    }

    public void batchSave(List<Data> data) {
        entityRepository.batchSave(data);
    }

    public List<Data> findDataByString(String string) {
        return entityRepository.findDataByString(string);
    }

    public void setEntityRepository(EntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    public EntityRepository getEntityRepository() {
        return entityRepository;
    }
}
