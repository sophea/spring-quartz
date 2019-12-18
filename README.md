# Getting Started
This project is about Springboot with Quartz Integration

# Technologies
    - Java version 1.8
    - Maven 3.x
    - Database Mysql
# Docker
````
    docker run --name mysql -p 3306:3306 -d -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=demo mysql:5.7
````

# Features:

- create new job running with bashscript behind
- create new job with java code and cron job expression
- create new job with Simple repeating time
- manage public holiday


# Demo
````
CRUD actions : http://localhost:8081/holiday.html

````
 