# SistemasDistribuidos
En este repositorio se incluirá en trabajo realizado en la asignatura Sistemas Distribuidos 
del Grado en Informática de la Universidad de Burgos.
Curso 2023-2024.
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

