## sbt project compiled with Scala 3

### Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

For more information on the sbt-dotty plugin, see the
[scala3-example-project](https://github.com/scala/scala3-example-project/blob/main/README.md).


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

docker-compose up -d

docker-compose down

