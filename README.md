# The Smartsheet Spring MVC and Hibernate template application

This is a template for a 3rd-Party [Smartsheet](http://www.smartsheet.com) web application that uses Spring MVC and Hibernate. Use this as baseline when starting new 3rd party webapps for the Cloudspokes challenges put on by Smartsheet.


## Running the application locally

First copy smartsheet.properties.sample -> [USERNAME].smartsheet.properties, where [USERNAME] is the value returned by System.getProperty("user.name"), and add your API key and secret and redirect Url. 
To deploy to Heroku, you must have a smartsheet.properties file present with your "production" configuration.

Alternatively, you may configure the three properties more securely in system environment variables instead of in a properties file. For Heroku use these commands to do so:

	$ heroku config:set smartsheet.client_id=xxx
	$ heroku config:set smartsheet.client_secret=yyy
	$ heroku config:set smartsheet.redirect_uri=http://zzz.herokuapp.com/target

Whether locally or on Heroku, environment variables take precedence over any properties file.

Finally make sure you have a System property named DATABASE_URL configured to point to your local db (postgres)
Then build with:

    $ mvn clean install

Then run it with:

    $ java -jar target/dependency/jetty-runner.jar target/*.war

To build without running tests use:

    $ mvn clean install -DskipTests

## Coding Guidelines
* Adhere to the style of the original code, including naming conventions for classes and packages, variables, etc; 
* Use good object-oriented designs. 
    *  Follow the MVC pattern laid out.
    *  Use appropriate separation of concerns for services, API calls, util classes, etc.
* This project is Spring enabled. Use dependency injection as appropriate. 
* Provide descriptive comments as necessary. Use Javadocs style comments where appropriate.
* Log as needed. The project currently uses java.util.logging to standard out. This is adequate for now.
