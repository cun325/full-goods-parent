# 多阶段构建 - 后端服务
FROM maven:3.8.6-openjdk-8-slim AS backend-builder

# 设置工作目录
WORKDIR /app

# 复制pom文件
COPY pom.xml .
COPY full-goods-common/pom.xml full-goods-common/
COPY full-goods-admin/pom.xml full-goods-admin/
COPY full-goods-api/pom.xml full-goods-api/
COPY full-goods-image-service/pom.xml full-goods-image-service/

# 下载依赖（利用Docker缓存）
RUN mvn dependency:go-offline -B

# 复制源代码
COPY full-goods-common/src full-goods-common/src
COPY full-goods-admin/src full-goods-admin/src
COPY full-goods-api/src full-goods-api/src
COPY full-goods-image-service/src full-goods-image-service/src

# 构建项目
RUN mvn clean package -DskipTests -B

# 前端构建阶段
FROM node:16-alpine AS frontend-builder

# 设置工作目录
WORKDIR /app

# 复制前端项目
COPY front-end/shopapp-admin/package*.json ./shopapp-admin/
COPY fruitshop-uniapp/package*.json ./fruitshop-uniapp/

# 安装依赖
RUN cd shopapp-admin && npm ci --only=production
RUN cd fruitshop-uniapp && npm ci --only=production

# 复制源代码
COPY front-end/shopapp-admin ./shopapp-admin
COPY fruitshop-uniapp ./fruitshop-uniapp

# 构建前端
RUN cd shopapp-admin && npm run build
RUN cd fruitshop-uniapp && npm run build

# 生产环境镜像 - Admin服务
FROM openjdk:8-jre-alpine AS admin-service

# 安装必要工具
RUN apk add --no-cache curl tzdata

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 创建应用用户
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# 设置工作目录
WORKDIR /app

# 复制jar包
COPY --from=backend-builder /app/full-goods-admin/target/*.jar app.jar

# 复制前端静态文件
COPY --from=frontend-builder /app/shopapp-admin/dist ./static

# 设置文件权限
RUN chown -R appuser:appgroup /app

# 切换到非root用户
USER appuser

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8082/admin/health || exit 1

# 暴露端口
EXPOSE 8082

# JVM优化参数
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -Djava.security.egd=file:/dev/./urandom"

# 启动应用
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# 生产环境镜像 - API服务
FROM openjdk:8-jre-alpine AS api-service

# 安装必要工具
RUN apk add --no-cache curl tzdata

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 创建应用用户
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# 设置工作目录
WORKDIR /app

# 复制jar包
COPY --from=backend-builder /app/full-goods-api/target/*.jar app.jar

# 设置文件权限
RUN chown -R appuser:appgroup /app

# 切换到非root用户
USER appuser

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8081/api/health || exit 1

# 暴露端口
EXPOSE 8081

# JVM优化参数
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -Djava.security.egd=file:/dev/./urandom"

# 启动应用
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# 生产环境镜像 - 图片服务
FROM openjdk:8-jre-alpine AS image-service

# 安装必要工具
RUN apk add --no-cache curl tzdata

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 创建应用用户
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# 设置工作目录
WORKDIR /app

# 复制jar包
COPY --from=backend-builder /app/full-goods-image-service/target/*.jar app.jar

# 创建上传目录
RUN mkdir -p /app/uploads && chown -R appuser:appgroup /app

# 切换到非root用户
USER appuser

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8083/health || exit 1

# 暴露端口
EXPOSE 8083

# JVM优化参数
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:G1HeapRegionSize=8m -XX:+UseStringDeduplication -Djava.security.egd=file:/dev/./urandom"

# 启动应用
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]