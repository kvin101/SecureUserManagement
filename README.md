![img.png](img.png)
![img_1.png](img_1.png)
![img_3.png](img_3.png)
------------------------------------------------------------------

## Objectives

* To develop a robust REST API ensuring data security and reliability.
* To implement JWT token-based authentication for enhanced security.
* To maintain data integrity and reliability using H2 database.
* Robust error handling mechanisms to enhance user experience.

------------------------------------------------------------------

## Proposed Work

### User Registration:

* Client sends a POST request with user details to the registration endpoint. Server validates and
  processes the request, encrypts the password using BCrypt, and stores user data in the database

### User Login:

* Client sends a POST request with credentials to the login endpoint. Server verifies credentials,
  generates a JWT token upon successful authentication, and returns it to the client.

### User Details Retrieval:

* Authenticated client includes the JWT token in the Authorization header of a GET request to
  retrieve
  user details. Server validates the token, fetches user data from the database, and sends it back
  to
  the client.

------------------------------------------------------------------

## Explore Rest APIs

The app defines following CRUD APIs.

### Auth

| Method | Url              | Decription | Sample Valid Request Body | 
|--------|------------------|------------|---------------------------|
| POST   | /api/auth/signup | Sign up    | [JSON](#signup)           |
| POST   | /api/auth/signin | Log in     | [JSON](#signin)           |

### Users

| Method | Url                                  | Description                                                                   | Sample Valid Request Body |
|--------|--------------------------------------|-------------------------------------------------------------------------------|---------------------------|
| GET    | /api/users/me                        | Get logged in user profile                                                    |                           |
| GET    | /api/users/{username}/profile        | Get user profile by username                                                  |                           |
| GET    | /api/users/checkUsernameAvailability | Check if username is available to register                                    |                           |
| GET    | /api/users/checkEmailAvailability    | Check if email is available to register                                       |                           |
| PUT    | /api/users/{username}                | Update user (If profile belongs to logged in user or logged in user is admin) | [JSON](#userupdate)       |
| DELETE | /api/users/{username}                | Delete user (For logged in user or admin)                                     |                           |

## End points

## Swagger UI End-point : [swagger](http://localhost:8085/swagger-ui/index.html)
