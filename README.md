# News Management
## Overview
This project is a user management system and news module implemented using Spring Boot. It provides functionalities for user registration, authentication, and news article management.

## Table of Contents
    Introduction
    
    Setup Instructions
    
    API Endpoints
    
    Token Management
    
    Additional Notes

This documentation provides an overview of the user management system and news module implemented in the application. It includes setup instructions, available API endpoints, and additional information necessary for maintenance and further development.
Setup Instructions
Prerequisites

    Java Development Kit (JDK) 17 or higher

    Maven

    Spring Boot framework

    MySQL

    Integrated Development Environment (IDE) (e.g., IntelliJ IDEA, Eclipse)

Steps to Set Up the Project

Clone the Repository:

    git clone https://github.com/your-username/newsManagement.git
    cd newsManagement

Configure Database:
    Update the application.properties file with your database connection details.

    spring.datasource.url=jdbc:mysql://localhost:3306/your_db
    spring.datasource.username=your_username
    spring.datasource.password=your_password

Build the Project:

    mvn clean install

Run the Application:

    mvn spring-boot:run
    
## API Endpoints
## User Management API

 ### User Signup
  
    Endpoint: POST /auth/signup
    Description: Registers a new user.

  ### User Login
  
    Endpoint: POST /auth/login
    Description: Authenticates a user and issues access and refresh tokens.

  ### Refresh Token
  
    Endpoint: POST /auth/refresh
    Description: Issues a new access token using a valid refresh token.

  ### Promote User
  
    Endpoint: POST /auth/promote/{userId}?role=CONTENT_WRITER
    Description: Promotes a user to a higher role (e.g., from CONTENT_WRITER to ADMIN).

## News API

  ### Create News Article
  
    Endpoint: POST /news/add
    Description: Adds a new news article.

  ### Delete News Article

    Endpoint: DELETE /news/{id}
    Description: Deletes a news article by ID.
    Response: Returns HTTP 204 No Content.

  ### Get All Approved News

    Endpoint: GET /news/all
    Description: Retrieves a list of all approved news articles.

  ### Approve News

    Endpoint: PUT /news/approve/{id}
    Description: Approves a news article by ID.
    Response: Returns HTTP 204 No Content.

  ### Reject News

    Endpoint: POST /news/{id}/reject
    Description: Rejects a news article by ID.
    Response: Returns HTTP 204 No Content.

## Additional Technical API Documentation
For detailed information on all available API endpoints, including request/response examples, please refer to the following link:
        
    https://documenter.getpostman.com/view/26185546/2sAXqy3KDb


## Token Management

  Access and refresh tokens are validated during user authentication.

  If a token is expired, a TokenExpiredException is thrown, and the API returns a 401 Unauthorized response.

## Additional Notes
  Configure MySQL with Docker

  To run MySQL using Docker, ensure you have Docker and Docker Compose installed. Start the MySQL server by navigating to the directory containing the docker-compose.yml file 'src/main/resources/static' and running:

    docker-compose up -d
