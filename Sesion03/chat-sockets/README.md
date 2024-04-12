Aplicación ChatApp
Aplicación Chat 1.0 de la asignatura Sistemas Distribuidos.
En el pdf de la sesión03 viene explicada la tarea. En este read.me se verá los pormenores del proyecto.
- Se genera un build.xml con el objetivo de documentar el proyecto aunque también permite compilarlo y limpiarlo,
para ello en terminal escribimos:

ant javadoc

- Se genera un archivo pom.xml para compilar el proyecto en maven. Para limpiar y compilar el proyecto usamos:

mvn clean compile

- He dockerizado el proyecto para ello he creado dos archivos dockerfile: uno para cliente y otro para el servidor.
Intenté hacerlo con docker-compose y con variables (entrypoint.sh) pero al tener interfaz interactiva no me fue posible,
y me resultó más fácil crear los contenedores individualmente. Para ello seguimos los pasos:
- Creamos las imágenes:

docker build -f Dockerfile.server -t chat-server .
docker build -f Dockerfile.client -t chat-client .

- Primero el servidor que si puede ser detached:

docker run -d --name chat-server-instance -p 1500:1500 chat-server

- Después los clientes, para pruebas por ejemplo se pueden crear dos contenedores clientes con interfaz interactiva:

docker run -it --name chat-client-aroca --network="host" chat-client java es.ubu.lsi.client.ChatClientImpl aroca
docker run -it --name chat-client-fpisot --network="host" chat-client java es.ubu.lsi.client.ChatClientImpl fpisot

Con ello ya tendríamos el proyecto dockerizado y funcionando.

