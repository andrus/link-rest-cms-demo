# link-rest-cms-demo
A simple JAX RS project demonstrating the use of [LinkRest](https://github.com/nhl/link-rest) framework

* Requires Java 1.8 or newer
* Requires a servlet 3.1 container (though will probably work on 3.0) 

How a "real app" would be different from this example:

* Instead of using an in-memory Derby database, you may want to connect Cayenne stack to a real DB and scrub the code of all the references to Derby.

Operations:

    curl -i -X POST 'http://127.0.0.1:8080/link-rest-cms-demo-1.0-SNAPSHOT/rest/domain' \
         -d '{"vhost":"mysite1.example.org","name":"My Site #1"}'
         
    curl -i -X GET 'http://127.0.0.1:8080/link-rest-cms-demo-1.0-SNAPSHOT/rest/domain'
    
    # query String is a URL encoded form of 'include={"path":"articles","sort":"publishedOn"}&exclude=articles.body'
    curl -i -X GET  'http://127.0.0.1:8080/link-rest-cms-demo-1.0-SNAPSHOT/rest/domain?include=%7B%22path%22%3A%22articles%22%2C%22sort%22%3A%22publishedOn%22%7D&exclude=articles.body'
         
    curl -i -X PUT 'http://127.0.0.1:8080/link-rest-cms-demo-1.0-SNAPSHOT/rest/domain' \
         -d '{"id":1, "name":"My Site about LinkRest"}'

    curl -i -X GET 'http://127.0.0.1:8080/link-rest-cms-demo-1.0-SNAPSHOT/rest/domain/1/articles'
    curl -i -X GET 'http://127.0.0.1:8080/link-rest-cms-demo-1.0-SNAPSHOT/rest/domain/1/articles?include=domain'
    
    curl -i -X POST 'http://127.0.0.1:8080/link-rest-cms-demo-1.0-SNAPSHOT/rest/domain/1/articles' \
         -d '[
              {"title":"LinkRest Presentation","body":"Here is how to use LinkRest"},
              {"title":"Cayenne Goodies", "body":"This is an article about Apache Cayenne"}
             ]'
             
    curl -i -X PUT 'http://127.0.0.1:8080/link-rest-cms-demo-1.0-SNAPSHOT/rest/domain/1/articles' \
         -d '{"id":1,"title":"LinkRest latest Presentation"}'
         
    curl -i -X DELETE 'http://127.0.0.1:8080/link-rest-cms-demo-1.0-SNAPSHOT/rest/domain/1/articles/1'
