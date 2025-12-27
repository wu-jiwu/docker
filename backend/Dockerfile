# ========================== 第一阶段：构建阶段 ==========================
FROM maven:3.9-eclipse-temurin-17 AS builder

# 设置编码（构建阶段也需要）
ENV LANG C.UTF-8
ENV LC_ALL C.UTF-8
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8

# ========================== 第二阶段：运行时阶段 ==========================
FROM eclipse-temurin:17-jre-alpine

# 标签
LABEL maintainer="GZU Backend Team"
LABEL version="1.0.0"
LABEL description="Backend CRUD API Service"

# 字符编码设置（关键）
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
ENV LC_CTYPE=C.UTF-8

# 安装工具
RUN apk add --no-cache curl tzdata \
    && ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && rm -rf /var/cache/apk/*

# 创建非root用户
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# 从构建阶段复制jar文件
COPY --from=builder /app/target/*.jar app.jar

# 创建日志目录
RUN mkdir -p /app/logs

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# 启动应用（注意：JVM参数通过环境变量传递）
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} ${JAVA_TOOL_OPTIONS} -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar"]