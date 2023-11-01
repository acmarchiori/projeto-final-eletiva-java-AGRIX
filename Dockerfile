# 1º estágio
# Chamado `build-image`, imagem JDK temporária para compilar o código
FROM maven:3-openjdk-17 as build-image
# Define o diretório de trabalho
WORKDIR /to-build-app
# Copia apenas o arquivo pom.xml (para aproveitar o cache)
COPY pom.xml .
# Executa o comando para baixar as dependências do Maven
RUN mvn dependency:go-offline
# Copia o restante do código-fonte
COPY src src
# Executa o comando de empacotamento do Maven
RUN mvn package

# 2º estágio
# Com imagem JRE, limpa e mais leve
FROM eclipse-temurin:17-jre-alpine
# Define o diretório de trabalho
WORKDIR /app
# Copia o jar gerado no primeiro estágio para o diretório de trabalho
COPY --from=build-image /to-build-app/target/*.jar /app/app.jar
# Expõe a porta 8080
EXPOSE 8080
# Define o comando de execução da aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
