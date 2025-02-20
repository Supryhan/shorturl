## Application to make URL shorter

### Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

This application is aimed to make URLs shorter

This BE application is planned to interact with FE application on these contracts:
### 1. To make URL shorter
  - Request:
      Method: POST
      URL: http://localhost:8080/v1/api/encode/
      Body: { 'name': '<here URL from user input>'}
  - Response: 
      Body: { 'short': '<here short URL code>'}

This template will help to request BE :

fetch('http://localhost:8080/v1/api/encode/', {
method: 'POST',
headers: {
'Content-Type': 'application/json'
},
body: JSON.stringify({name: 'google.com/maps'})
})
.then(response => response.text())
.then(text => console.log(text))
.catch(error => console.error('Error:', error));


### 2. To reveal initial URL from short URL code:
  - Request:
      Method: POST
      URL: http://localhost:8080/v1/api/decode/
      Body: { 'name': '<here short URL code>'}
  - Responce:
      Status: 200 OK
      Body: { 'initial': '<here short URL code>'}
      or
      Status: 404 Not Found

This template will help to request BE :

fetch('http://localhost:8080/v1/api/decode/', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({name: 'short.name/word'})
})
.then(response => response.text())
.then(text => console.log(text))
.catch(error => console.error('Error:', error));


## Launching application
### Run command in console:

docker-compose up -d

this download and starts postgres and admin/page
Login admin page using login:password - admin@admin.com:admin
Create two tables "locators" and "ref" (scripts in file "tables.sql") and insert init record into ref. 

### To stop DB run command:

docker-compose down

