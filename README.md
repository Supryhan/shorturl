## Application to make URL shorter

This application is aimed to make URLs shorter

## Usage
### Launching application
1. Run Postgres in docker
2. Run Main.scala class

### 1. Run Postgres in docker using docker-compose.yml
Type command in console:

`docker-compose up -d`

this downloads and starts postgres and admin/page in docker containers.

To login admin page:```http://localhost:5050/login?next=/browser/```, use ```login:password``` -- ```admin@admin.com:admin```

On Dashboard click: `Create New Server`. As Name put: `UrlShortnerDbServer`. In Connection tab in field Host name/address put: `postgres`, port: `5432`, Username: `postgres`, Password: `password` and press `Save`.

Please, then create two tables. To do that follow: Servers -> UrlShortnerDbServer -> Databases -> url-shortener-database -> Schemas -> public -> Tables.

On Tables right-click -> Query Tool and copy-paste SQL script from file `tables.sql` so that it will create tables `locators` and `ref` in DB named `url-shortener-database` and then insert init record into table `ref` (see scripts). 

To stop DB run command:

`docker-compose down`

### 2. To run BE user need to execut run method in Main.scala class

After starting application, user must get logs that:
(1) application configured and started;
(2) Application connected to Postgres;
(3) Ember-Server service bound to address: [::]:8080

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
