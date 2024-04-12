## Aplicación ChatApp ahora con RMI.

### Aplicación Chat 1.0 de la asignatura Sistemas Distribuidos ahora con RMI.

En el pdf de la sesión05 viene explicada la tarea. En este documento se verá los pormenores del proyecto.
- Se genera un build.xml con el objetivo de documentar el proyecto aunque también permite compilarlo y limpiarlo,
para ello en terminal escribimos:

*ant javadoc*

- Se genera un archivo pom.xml para compilar el proyecto en maven. Para limpiar y compilar el proyecto usamos:

*mvn clean compile*

(Lo hacemos tanto en la carpeta Practica2ChatRMI como en Practica2ChatRMI-Web)

Con ello generaremos un .jar y un .war respectivamente.

- A continuación se *despliega un servidor glassfish 5.1.0* en localhost:8080 donde se añade como deployment el .war generado anteriormente.
- Después se ejecutan los .bat: registro, servidor y tantos clientes como se quieran en ese orden.
- El cliente debe pasar como argumento su nombre.

Una vez ejecutados los procesos el cliente tiene la opción de enviar mensajes al resto de clientes, también puede usar los comandos:
- logout: cierra su conexión con el servidor.
- drop + nombre_usuario: cerrará la conexión de nombre_usuario.

