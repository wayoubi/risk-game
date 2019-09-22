#./mvnw clean install
#./mvnw package
./mvnw compile
#./mvnw test
clear
./mvnw -q -pl . spring-boot:run