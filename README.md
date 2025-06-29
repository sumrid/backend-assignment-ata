## GET /api/jobs

### Query Parameters

| param          | optional | description                                          | example                  |
|----------------|----------|------------------------------------------------------|--------------------------|
| fields         | yes      | selected field (supported job_title, salary, gender) | ?fields=job_title,salary |
| sortBy         | yes      | sort by field  (supported job_title, salary, gender) | ?sort=salary             |
| sortDescending | yes      | default is false                                     | ?sortDescending=true     |
| size           | yes      | default is 20                                        | ?size=50                 |
| field[gte]     | yes      | supported operator gte, lte, eq, like                | ?salary[gte]=10000       |

### Response Body

```json
[
  {
    "gender": "Male",
    "salary": 720000,
    "job_title": "Java Software Developer"
  },
  {
    "gender": "Male",
    "salary": 170000,
    "job_title": "java contractor"
  },
  {
    "gender": "Male",
    "salary": 156000,
    "job_title": "Javascript Engineer"
  }
]
```

### cURL

```shell
curl --request GET \
  --url 'http://localhost:8080/api/jobs?fields=job_title%2Csalary%2Cgender&sortBy=salary&size=50&sortDescending=true&job_title%5Blike%5D=java&salary%5Bgte%5D=50000'
```

---

## Run Application

### Using Maven

```shell
mvn spring-boot:run
```

### Using docker

```shell
# build image
docker build -t assignment-ata .

# start application
docker run --rm -p 8080:8080 assignment-ata:latest
```

---

## Reference

- https://www.baeldung.com/database-migrations-with-flyway
- https://www.baeldung.com/opencsv
- [Dynamic query](https://zengcode.medium.com/ep5-%E0%B9%80%E0%B8%82%E0%B8%B5%E0%B8%A2%E0%B8%99-dynamic-query-%E0%B8%94%E0%B9%89%E0%B8%A7%E0%B8%A2-criteria-api-%E0%B9%83%E0%B8%99-spring-data-jpa-d70aae5bb182)
- https://www.baeldung.com/jpa-hibernate-projections
- https://www.baeldung.com/hibernate-criteria-queries
- https://www.baeldung.com/spring-boot-fix-the-no-main-manifest-attribute
- https://dev.to/dzifahodey/the-java-nosuchfileexception-how-to-avoid-it-when-adding-resources-to-a-spring-boot-application-2bd6
