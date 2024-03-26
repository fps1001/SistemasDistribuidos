package es.ubu.lsi;

import java.applet.Applet;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


/**
 *
 * Cliente gráfico del servidor remoto.
 */ 
public class CallbackApplet extends Applet implements Cliente {

	/**
	 * Serial id.
	 */
	private static final long serialVersionUID = 5687676355246482888L;

	/**
	 * Mensaje.
	 */
   	private String message = "-n/a-";
   
   /**
    * Ventana principal.
    */
   	private Frame ventana =new Frame();
   
   /**
    * Etiqueta.
    */
   	private Label label= new Label("                   ");
   
    /**
     * Inicia el applet conectando con el servidor remoto.
     */
   	public void init() {
    	ventana.add(label); // añade etiqueta en la ventana principal
      	try {
      		System.setSecurityManager(new SecurityManager());
        	// Exporta el objeto 
      		// Dado que no hay herencia múltiple en Java y que un applet
      		// hereda de Applet, no queda otra opción.
         	UnicastRemoteObject.exportObject(this, 10000);
         	// construye cadena de conexión
         	String host = "rmi://" + getCodeBase().getHost()+ "/Servidor";
         	// resuelve
         	Hola obj = (Hola) Naming.lookup(host);
         	// invoca el método del servidor remoto
         	message = obj.decirHola((Cliente) this);
      	} 
      	catch (Exception e) {
         	System.err.println("Excepcion en el applet: " + e.toString());
      	}
   	}

   /**
    * Muestra el mensaje en el applet.
    * 
    * @param g lienzo
    */
   public void paint(Graphics g) {
      g.drawString(message, 25, 50);
   }
   
   /**
   * Este método es llamado por el servidor remoto para lanzar
   * una ventana popup en el cliente gr�fico.
   *   
   * @param txt texto a mostrar
   * @throws RemoteException errores remotos
   */
   public void popup(String txt) throws RemoteException{
      label.setText(txt);
      ventana.setSize(100,100);
      ventana.show();
   }
}
