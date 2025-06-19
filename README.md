Платформа для размещения объявлений (Avito Clone)

Описание

Это веб-приложение, реализующее функционал платформы для продажи вещей и общения между пользователями. Позволяет регистрироваться, добавлять объявления, комментарии к ним, загружать аватарки и контент-картинки.

Технологии:
- Java 17
- Spring Boot 3.2
- Spring Security
- Spring Data JPA
- PostgreSQL
- Liquibase
- MapStruct
- Swagger (SpringDoc OpenAPI)
- Docker
- JUnit 5


Структура проекта

| Путь                                | Описание                                      |
|-------------------------------------|-----------------------------------------------|
| `src/main/java/.../controller`      | REST-контроллеры                              |
| `src/main/java/.../service`         | Интерфейсы сервисов                           |
| `src/main/java/.../service/impl`    | Реализации сервисов                           |
| `src/main/java/.../model`           | JPA-сущности (Entity)                         |
| `src/main/java/.../dto`             | DTO-классы                                    |
| `src/main/java/.../mapper`          | MapStruct-интерфейсы для маппинга             |
| `src/main/java/.../repository`      | Spring Data JPA репозитории                   |
| `src/main/java/.../config`          | Конфигурация Spring Security, Swagger и CORS  |
| `src/main/resources`                | Ресурсы приложения                            |
| `src/main/resources/application.properties` | Настройки Spring Boot                       |
| `src/main/resources/db/changelog/liquibase-changeLog.xml` | Миграции Liquibase              |


  Функционал:
Просмотр объявлений (GET /ads, /ads/{id})
Создание/удаление/редактирование объявлений
Регистрация и авторизация
Смена пароля
Загрузка картинок к объявлению
Загрузка аватарок пользователя
Комментарии к объявлению
Роли: USER, ADMIN

  Как запустить?
Удостоверьтесь, что POSTGRES работает и соединения есть
Собери проект:  ./mvnw clean install
Запусти сервер:  ./mvnw spring-boot:run
Запусти frontend:  docker run -p 3000:3000 --rm ghcr.io/dmitry-bizin/front-react-avito:v1.21
Открой: http://localhost:3000

Участник:
Dinara Allanazarova — https://github.com/Dinara0113


