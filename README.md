# Spring MVC and Hibernate template application 

This is a template for a 3rd-Party Smartsheet (www.smartsheet.com) web application that uses Spring MVC and Hibernate. Use this as baseline when starting new 3rd party webapps for the Cloudspokes challenges put on by Smartsheet.


## Running the application locally

First copy smartsheet.properties.sample -> smartsheet.properties and add your API key and secret.
Make sure you have a System property named DATABASE_URL configured to point to your local db (postgres)
Then build with:

    $mvn clean install

Then run it with:

    $java -jar target/dependency/jetty-runner.jar target/*.war


## Coding Guidelines
* Adhere to the style of the original code, including naming conventions for classes and packages, variables, etc; 
* Use good object-oriented designs. 
    *  Follow the MVC pattern laid out.
    *  Use appropriate separation of concerns for services, API calls, util classes, etc.
* This project is Spring enabled. Use dependency injection as appropriate. 
* Provide descriptive comments as necessary. Use Javadocs style comments where appropriate.
* Log as needed. The project currently uses java.util.logging to standard out. This is adequate for now.
* Test your code, please.