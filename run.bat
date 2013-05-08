setx DATABASE_URL "postgres://kskeem:test123@localhost:5432/helloworld"
call mvn package
java -jar target/dependency/jetty-runner.jar target/*.war
