# link-rest-cms-demo
A simple JAX RS project demonstrating the use of [LinkRest](https://github.com/nhl/link-rest) framework

* Requires Java 1.8 or newer
* Requires a servlet 3.1 container (though will probably work on 3.0) 

How a "real app" would be different from this example:

* Instead of using an in-memory Derby database, you may want to connect Cayenne stack to a real DB and scrub the code of all the references to Derby.

Operations:

    curl -i -X POST 'http://127.0.0.1:8080/link-rest-cms-demo-1.0-SNAPSHOT/rest/domain' \
         -d '{"vhost":"mysite1.example.org","name":"My Site #1"}'

    curl -i -X GET 'http://127.0.0.1:8080/link-rest-cms-demo-1.0-SNAPSHOT/rest/domain/1/articles'
    
    curl -i -X POST 'http://127.0.0.1:8080/link-rest-cms-demo-1.0-SNAPSHOT/rest/domain/1/articles' \
         -d '[{"body":"This is an article showing how to use LinkRest","title":"LinkRest Presentation"},{"body":"This is an article about Apache Cayenne","title":"Cayenne Goodies"}]'
