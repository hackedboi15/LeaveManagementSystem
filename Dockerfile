FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests   # jar banega
CMD ["java", "-jar", "target/myapp-0.0.1-SNAPSHOT.jar"]
