set CLASSPATH=.\target\RMI-Dinamico-0.0.1-SNAPSHOT.jar

java -Djava.security.policy=registerit.policy ^
-Djava.rmi.server.codebase=http://localhost:8080/RMI-Dinamico-Web/ ^
es.ubu.lsi.ServidorDinamico