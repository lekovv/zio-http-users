# ZIO-HTTP-USERS

This is a test project in which I practice my skills in ZIO

### At the moment the project has implemented:

* Docker, Docker compose
* HTTP endpoints (ZIO-HTTP)
* Working with PostgreSQL, Quill
* Flyway for migrations
* Logging (ZIO-logging, SLF4J2)
* External http requests (ZIO-HTTP)
* Service Pattern and DI
* Error handling logic

### Endpoints description:

1) _GET /api/user/get-all_ - method to get all users in "user" table
2) _GET /api/user/get(id: UUID)_ - method to get user by id
3) _POST /api/user/set_ - method to create a new user (uuid is generated automatically).
   Body:

```json
  {
  "first_name": "string",
  "last_name": "string",
  "is_active": "boolean (required)"
}
```

4) _PUT /api/user/update_ - method to update user.
   Body:

```json
{
  "id": "UUID (required)",
  "first_name": "string",
  "last_name": "string",
  "is_active": "boolean (required)"
}
```

5) _DELETE /api/user/delete(id: UUID)_ - method to delete user from user table by id
6) _GET /endpoint/cat_ - proxi method of https://catfact.ninja/fact

### To run the application locally:

1) Please install [Docker](https://www.docker.com/) if you don't have it already
2) Make sure you are in the **root directory** of the project and run:

```bash
sbt pack
```

3) Set up your environment variables for the database in the file `docker-compose.yml`
4) Go to the `/docker` directory. You can do this by running the command:

```bash
cd docker
```

5) Run:

```bash
docker-compose up
```

---

# ZIO-HTTP-USERS

Это тестовый проект, в котором я тренирую свои навыки в ZIO

### На текущий момент в проекте реализовано:

* Docker, Docker compose
* HTTP endpoints (ZIO-HTTP)
* Работа с PostgreSQL, Quill
* Flyway для миграций
* Логирование (ZIO-logging, SLF4J2)
* Внешние http запросы (ZIO-HTTP)
* Service Pattern и DI
* Логика обработки ошибок

### Описание эндпойнтов:

1) _GET /api/user/get-all_ - метод для получения всех пользователей в таблице "users"
2) _GET /api/user/get(id: UUID)_ - метод для получения пользователя по id
3) _POST /api/user/set_ - метод для создания нового пользователя (uuid генерируется автоматически).
   Тело запроса:

```json
  {
  "first_name": "string",
  "last_name": "string",
  "is_active": "boolean (обязательное)"
}
```

4) _PUT /api/user/update_ - метод для обновления пользователя.
   Тело запроса:

```json
{
  "id": "UUID (обязательное)",
  "first_name": "string",
  "last_name": "string",
  "is_active": "boolean (обязательное)"
}
```

5) _DELETE /api/user/delete(id: UUID)_ - метод для удаления пользователя по id
6) _GET /endpoint/cat_ - прокси метод https://catfact.ninja/fact

### Чтобы запустить приложение локально:

1) Установите [Docker](https://www.docker.com/) если еще этого не сделали
2) Убедитесь, что вы находитесь в **корневой директории** проекта и выполните команду:

```bash
sbt pack
```

3) Установите ваши параметры окружения для базы данных в файле `docker-compose.yml`
4) Перейдите в директорию `/docker`. Вы можете это сделать выполнив команду:

```bash
cd docker
```

5) Выполните команду:

```bash
docker-compose up
```