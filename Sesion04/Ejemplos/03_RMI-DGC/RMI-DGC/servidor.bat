Set CLASSPATH=.\target\classes;%CLASSPATH%
java -Dsun.rmi.dgc.checkInterval=500 -Djava.rmi.dgc.leaseValue=1000 -Djava.security.policy=registerit.policy es.ubu.lsi.ServidorImpl