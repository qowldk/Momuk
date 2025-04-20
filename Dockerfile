# 1. Maven 빌드용 이미지 (1단계 빌드)
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# pom.xml과 src 폴더 복사
COPY pom.xml .
COPY src ./src

# Maven으로 패키지 빌드
RUN mvn clean package -DskipTests

# 2. 실제 실행용 이미지 (2단계 실행)
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# 위에서 빌드한 JAR 파일 복사 (버전은 실제로 만들어지는 이름 확인!)
COPY --from=build /app/target/*.jar app.jar

# 실행
CMD ["java", "-jar", "app.jar"]
