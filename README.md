# link-rest-cms-demo
A simple JAX RS project demonstrating the use of [LinkRest](https://github.com/nhl/link-rest) framework

* Requires Java 1.8 or newer
* Requires a servlet 3.1 container (though will probably work on 3.0) 

How a "real app" would be different from this example:

* Instead of using an in-memory Derby database, you may want to connect your app to a real DB and scrub this code of all the refrences to Derby.
* Instead of Jersey injection provider, you may be using Spring, CDI, and such. So you might want to convert Cayenne and LinkRest bootstrap code to integrate with your own injection.
