# @Hql
  <br>
 <strong>@Hql</strong> is an annotation for executing hql and sql queries 
</h3>

```java
@Hql(value = "select u from Data u where u.email = '?1'")
List<User> findDataByEmail(String email);
```
