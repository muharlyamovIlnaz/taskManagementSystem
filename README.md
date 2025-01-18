# Менеджер задач

### Основной функциона

- Регистрация пользователей по email/password
- Получение JWT по email/password
- JWT аутентификация
- Разделение доступов по ролям
- Создание, редактирование, удаление задач
- Создание, удаление комментариев

### Для локального запуска:

- Небходимо запустить docker-compose.yml

- В docker-compose.yml файле можно отредактировать переменые среды перед запуском

```
      PG_URL: jdbc:postgresql://postgres:5432/postgres
      PG_USERNAME: postgres
      PG_PASSWORD: postgres
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      SPRING_PROFILES_ACTIVE: dev
      PG_SCHEMA: task
      JWT_SECRET: PRh7vkFBAn53ZI2YK5/Y9xaXgGR6sQ3uCUMwO0/x/5Y=
      JWT_EXPIRATION_TIME: 86400000
```

- В противном случае будут использованы значения по умолчанию

#### После локального запуска документация SWAGGER доступна по адресу

- http://localhost:8080/api/v1/swagger-ui/index.html





