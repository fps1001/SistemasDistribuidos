package es.ubu.lsi.echoserver;

/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

import java.net.*;
import java.io.*;
import java.util.Arrays;

public class EchoServerMultihilo {

    //FPS Lista negra de puertos.
    private static final int[] BLACKLISTED_PORTS = {1234, 5678}; 
    
    public static void main(String[] args) throws IOException {
        
        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }
        
        int portNumber = Integer.parseInt(args[0]);
        System.out.println("Escuchando por puerto: " + portNumber);
        
        try  (
            	//FPS Cambio por claridad la variable, en vez de volver a hacer el parse 
                //ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
                ServerSocket serverSocket = new ServerSocket(portNumber);
   		)
        {
            while (true){
                Socket clientSocket = serverSocket.accept();
                //FPS Comprobación de lista negra.
                if (isPortBlacklisted(clientSocket.getPort())) { // Utilizo un método que devuelve true si está en blacklist
                    System.out.println("Conexión rechazada desde el puerto bloqueado: " + clientSocket.getPort());
                    clientSocket.close(); // Cierra la conexión si está en la lista negra.
                    continue; // Vuelve a la siguiente iteración de bucle.
                }
                
                // Si pasa el corte establece la conexión.
                System.out.println("Nuevo Cliente: " + clientSocket.getInetAddress() + "/" + clientSocket.getPort());
            	Thread hilonuevocliente = new ThreadServerHandler(clientSocket);
            	hilonuevocliente.start();
            }
        	
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    //FPS Método auxiliar para verificar si un puerto está en la lista negra
    private static boolean isPortBlacklisted(int port) {
        return Arrays.stream(BLACKLISTED_PORTS).anyMatch(blacklistedPort -> blacklistedPort == port);
    }
    
}
    
class ThreadServerHandler extends Thread {
	
	private final Socket clientSocket;
	
	public ThreadServerHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public void run() {
		try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        	BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String inputLine;
            
			while ((inputLine = in.readLine()) != null) {
            	System.out.println(clientSocket.getPort() + ":" + inputLine);
                out.println(inputLine);
            }
        }
        catch (IOException e) {
            System.out.println("Exception caught on thread");
            System.out.println(e.getMessage());
        }
      }
}