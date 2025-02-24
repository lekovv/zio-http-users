# ZIO-HTTP-USERS

This is a test project in which I practice my skills in ZIO. 

### At the moment the project has implemented:
* HTTP endpoints (ZIO-HTTP)
* Working with PostgreSQL, Quill
* Flyway for migrations
* Logging (ZIO-logging, SLF4J2)
* External http requests (ZIO-HTTP)
* Service Pattern and DI
* Error handling logic

### Endpoints description:
1) _GET /api/user/get-all_ - method to get all users in user table
2) _GET /api/user/get_ - method to get user by id (query param "id")
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
5) _DELETE /api/user/delete_ - method to delete user from user table by id (query param "id")
6) _GET /endpoint/cat_ - proxi method of https://catfact.ninja/fact