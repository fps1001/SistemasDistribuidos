# Chat Web 2.0

**He elegido la versión del trabajo 2.0 que usa spring, stomp etc.**

Parto del proyecto de partida que se ve en la [imagen](../proyecto%20base%20websocket.png) que usa Spring initializr.

El proyecto usa Spring Security con una pantalla de login por defecto, los usuarios y sus contraseñas están en un fichero [users.csv](./src/main/resources/users.csv) donde están creados los usuarios, sus id's, sus contraseñas y el nivel de autorización que tienen y si son inclusivos o no.
Si son inclusivos permitirán mensajes de usuarios con menor nivel.<p>
El resto funciona como una pantalla de chat normal con cuantos usuarios estén creados.<p>
Una de las mayores dificultades en esta práctica fue la adaptación de los niveles y mensajes inclusivos: 
al hacerlo no sirve la **clase provista por Spring UserDetails** necesitaba ampliarla con las nuevas variables y
muchas de las funciones de configuración y seguridad ya trabajan por defecto con la clase, así que hubo que cambiar manualmente
aquellas funciones automáticas.<p>
Para los niveles cree una enumeración con equivalencia numérica: ADMIN, USER, GUEST, pero se podrían ampliar sin fallos en el sistema
al comparar numéricamente los resultados.<p>
Como window.currentUser.level devolvía un valor numérico para el cliente tuve que adaptar con una función en javascript dentro del html.

Como mejora

--------
Forma de uso en local:<p>
- En un navegador se pone la dirección web: [localohost:8080](localhost/8080)
- En la pantalla de login de spring security por defecto se pone uno de los usuarios del csv, sin modificar serían:
  aroca, fpisot y victorero (ver [users.csv](./src/main/resources/users.csv) para ver inclusividad y niveles)
- ¡Chatea con tus amigos!
--------
Forma de uso con docker:<p>
- Se puede hacer una compilación completa con maven para garantizar los archivos jar con: *'mvn clean package'*
- Se construye la imagen con *'docker build -t chatwebsocket .'*
- Se ejecuta el contenedor para cada cliente con: *'docker run -p 8080:8080 chatwebsocket'*
- Se puede loguear entonces en tres navegadores distintos con los usuarios fpisot/aroca/victorero y ver como se chatea entre ellos. <p>
Observando como fpisot que no es inclusivo ignora los mensajes de victorero que es guest (perfil más bajo).
----------
Formas de mejora del proyecto:
- Permitir dar de alta usuarios grabando la información en el csv: habría que crear una pantalla de sign-up y persistir los datos
- Función de logout.
- Cambiar csv por una base de datos usando JPA/Hibernate.