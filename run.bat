setx DATABASE_URL "postgres://kskeem:test123@localhost:5432/helloworld"
call mvn clean package
java -jar target/dependency/jetty-runner.jar --port 8081 target/*.war
