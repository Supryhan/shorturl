## Application to make URL shorter

### Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

This application is aimed to make URLs shorter

This BE application is planned to interact with FE application on these contracts:
### 1. To make URL shorter
  - Request:
  
      Method: POST

      URL: http://localhost:8080/v1/api/encode/
      
      Body: { 'name': 'type here URL from user input to make it short'}

  - Response:
  
      Body: { 'short': 'here will be short URL code'}

This template will help to request BE (use it in console in browser) :

```js
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
```

### 2. To reveal initial URL from short URL code:
  - Request:
  
      Method: POST
  
      URL: http://localhost:8080/v1/api/decode/
  
      Body: { 'name': 'type here short URL code'}
  
  - Responce:
   
      Status: 200 OK
  
      Body: { 'initial': 'here will be initial URL address>'}
  
      or
  
      Status: 404 Not Found

This template will help to request BE (use it in console in browser) :

```js
fetch('http://localhost:8080/v1/api/decode/', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({name: '23jf3a'})
})
.then(response => response.text())
.then(text => console.log(text))
.catch(error => console.error('Error:', error));
```

## Launching application
### Run command in console:

`docker-compose up -d`

this downloads and starts postgres and admin/page docker containers

To login admin page:```http://localhost:5050/login```, use ```login:password``` -- ```admin@admin.com:admin```

Please, create two tables `locators` and `ref` in DB named `mydatabase` (use scripts from file `tables.sql`) and then insert init record into table `ref` (see scripts). 

### To stop DB run command:

`docker-compose down`

