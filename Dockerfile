# ===== 第一阶段：构建阶段 =====
# 用自带 maven 和 JDK 21 的镜像，负责把项目打包成 jar
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# 使用阿里云 Maven 镜像，加速依赖下载（国内访问 Maven 中央仓库不稳定）
COPY maven-settings.xml /root/.m2/settings.xml

# 拷贝源码和配置，直接打包成 jar（跳过测试，跑得快）
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests clean package

# ===== 第二阶段：运行阶段 =====
# 只要 JDK 21 运行环境，不要 maven（这样镜像小很多）
FROM eclipse-temurin:21-jre
WORKDIR /app

# 从第一阶段把打好的 jar 复制过来
COPY --from=build /app/target/kaifangyuanzi-0.0.1-SNAPSHOT.jar app.jar

# 声明容器里监听 8080 端口（只是说明，真正映射端口要靠运行时的 -p 参数）
EXPOSE 8080

# 容器启动时执行的命令，等价于在命令行跑：java -jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
