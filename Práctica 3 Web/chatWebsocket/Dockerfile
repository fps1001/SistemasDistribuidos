# Usar una imagen base con Java
FROM openjdk:17-oracle

# Directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR y todos los recursos estáticos necesarios
COPY target/*.jar app.jar

# Exponer el puerto en el que corre la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java","-jar","app.jar"]