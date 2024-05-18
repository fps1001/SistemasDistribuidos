# SistemasDistribuidos
En este repositorio se incluirá en trabajo realizado en la asignatura Sistemas Distribuidos 
del Grado en Informática de la Universidad de Burgos.
Curso 2023-2024.

### https://github.com/fps1001/SistemasDistribuidos

## Sesión 01
- Creación de script con ant y maven: en un proyecto maven se ha generado un archivo build.xml para ejecutar el comando 'ant'
- Se crea un pom.xml equivalente al usado para ant. 
- El proyecto base usado es el que usa por defecto intellij.
## Sesión 02
- Prueba de los distintos servidores y clientes dados.
  - Echo
  - Quote
  - Multicast
  - Echo-NIO
- Ejecución multi-proyecto.
- Sobre el ejemplo de *Cliente de Socket (Eco)* y *Servidor Multi-hilo*, implementar una lista negra en el servidor que permita bloquear conexiones desde diferentes orígenes (Puerto de Origen).
- El código consiste en modificar y crear una lista negra en el servidor y para ayudar al testeo asigné al socket de conexión del lado del cliente el puerto asignandolo como el siguiente parámetro:
Ejemplo de uso:
- java -jar .\target\EchoClientSocket-0.0.1-SNAPSHOT.jar localhost 8080 1234 
- Donde 8080 sería el puerto del servidor y 1234 el puerto del origen que es el bloqueado.

## Sesión 03 - Chat Sockets - (Práctica evaluable - 10%)
- Chat creado con sockets de tipo TCP donde un cliente puede enviar mensajes al resto de clientes.
- Existen varios tipos de mensajes:
  - texto libre.
  - logout
  - shutdown
  - drop usuarioaeliminar
## Sesión 04 - Hola Mundo RMI
- Modificar la aplicación "Hola mundo" de RMI para exponer al menos 3 objetos y listar estos objetos desde el cliente RMI.

## Sesión 05 - Chat RMI
- Consiste básicamente en el proyecto de la sesión 3 de un chat entre clientes y servidor pero usando como medio RMI de contrucción de clases dinámicas.

## Sesión 08 - Validación de formulario
- Crear un formulario HTML y enviar contenido a un servlet y a un JSP.

## Práctica 3 Web
- Se trata de la versión Chat Web 2.0 con websocket: varios usuarios pueden hablar entre sí desde su navegador.

## Taller
- Consiste en hacer una pequeña web app usando las tecnologías Spring boot, JPA, Hibernate y Thymeleaf.