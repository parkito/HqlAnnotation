# Hql executor annotation

Annotation for executing Hql/SQL-native queries 

## Description

**@Hql** makes easy structure of repository level of applications.

Annotation uses Hibernate as JPA provider.  


## How to use

1) You have to create repository interface by extending IGenericRepository
with generic parameters <Entity, TypeOfPrimaryKey>

2) Write query in annotation under method with parameters

```java
public interface EntityRepository extends IGenericRepository<Data, Long> {

    @Hql(value = "select d from Data d where d.dataString = '?1'")
    List<Data> findDataByString(String string);

}

@Service
public class EntityService {

    private EntityRepository entityRepository;

    public List<Data> findDataByString(String string) {
        return entityRepository.findDataByString(string);
    }
}
```

## License

[MIT](https://github.com/parkito/HqlAnnotation/blob/master/LICENSE)
