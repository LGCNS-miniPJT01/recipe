# 기본 이미지 선택 (Gradle + JDK 포함)
FROM eclipse-temurin:21-jdk AS build

# 작업 디렉토리 설정
WORKDIR /app

# JAR 파일 복사
COPY build/libs/*.jar app.jar

# 환경 변수 설정 (docker-compose에서 설정)
ENV DB_HOST=db
ENV DB_PORT=3306
ENV DB_NAME=receipe_db
ENV DB_USER=receipe
ENV DB_PASSWORD=p@ssw0rd
ENV JWT_SECRET=vmfhaltmskdlstkfkdgodyroqkfwkdbalroqkfwkdbalaaaaaaaaaaaaaaaabbbbb
ENV JWT_EXPIRATION=3600000

# 실행 명령어 설정
ENTRYPOINT ["java", "-jar", "app.jar"]
