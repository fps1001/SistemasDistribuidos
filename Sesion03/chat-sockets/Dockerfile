FROM openjdk:1.8
LABEL authors="fpisot1001@alu.ubu.es"
COPY . /src/main/java/es/ubu/lsi
WORKDIR /src//main/java/es/ubu/lsi
RUN javac ./server/ChatServerImpl
CMD ["java","ChatServerImpl"]
ENTRYPOINT ["top", "-b"]

#$ docker build -t my-java-app .
#$ docker run -it --rm --name my-running-app my-java-app
#$ docker run --rm -v "$PWD":/usr/src/myapp -w /usr/src/myapp openjdk:11 javac Main.java