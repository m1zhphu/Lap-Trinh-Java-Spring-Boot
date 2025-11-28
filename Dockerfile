# GIAI ĐOẠN 1: BUILD (Sử dụng Maven và JDK để biên dịch)
FROM maven:3.8.7-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src /app/src

# Lệnh Build: Biên dịch dự án thành file .jar
RUN mvn clean package -DskipTests

# GIAI ĐOẠN 2: RUNTIME (Sử dụng JRE nhẹ hơn để chạy)
# Chọn một JRE an toàn và nhẹ (OpenJDK 17 là phổ biến)
FROM openjdk:17-jre-slim
WORKDIR /app

# Sao chép file .jar đã tạo ở giai đoạn BUILD
# Cần thay thế 'Lap-Trinh-Java-Spring-Boot-0.0.1-SNAPSHOT.jar' bằng tên file JAR của bạn
# Tên file JAR thường là: [artifactId]-[version].jar
COPY --from=build /app/target/Lap-Trinh-Java-Spring-Boot-0.0.1-SNAPSHOT.jar app.jar

# Cấu hình cổng và chạy ứng dụng
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]