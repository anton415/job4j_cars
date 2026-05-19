# job4j_cars

Учебное приложение для работы с объявлениями о продаже автомобилей.

## Стек

- Java 17+
- Maven
- Spring Boot
- Thymeleaf
- Liquibase
- H2 для локального запуска

## Запуск

```bash
mvn spring-boot:run
```

После запуска главная страница будет доступна по адресу:

```text
http://localhost:8080
```

Если порт `8080` занят, можно указать другой порт:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

Консоль H2 доступна по адресу:

```text
http://localhost:8080/h2-console
```

Параметры подключения к локальной базе:

```text
JDBC URL: jdbc:h2:mem:job4j_cars
User Name: sa
Password:
```

## Проверка

```bash
mvn test
```

Liquibase changelog находится в `src/main/resources/db/changelog/db.changelog-master.yaml`.
