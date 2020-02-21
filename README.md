# Getting Started
This project is about backend template Springboot

# Technologies
    - Java version 1.8
    - Maven 3.x
    - Database Mysql
# Docker
````
    docker run --name mysql -p 3306:3306 -d -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=demo mysql:5.7
````

# Demo
````
TEST API : with access_token

curl -X GET \
  http://localhost:8082/secrets/test \
  -H 'authorization: Bearer xxxxxxx'
  
  
TEST2 API   no token requires

curl -X GET http://localhost:8082/secrets/test2 
````