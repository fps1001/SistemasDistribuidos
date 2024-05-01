package com.ubu.sistdtr.proyectobase;
/**
 * Mensaje del chat y su información pertinente.
 *
 * @author Fernando Pisot
 *
 */
public class Message {
    /** Indica nombre del remitente del mensaje*/
    private String from;
    /** Indica Id del remitente del mensaje*/
    private String from_id;
    /** Indica nivel del remitente del mensaje*/
    private String from_level;
    /** Mensaje a enviar*/
    private String text;
    /**
     * Constructor del mensaje y la información del remitente
     */
    public Message(String from, String from_id, String from_level, String text) {
        this.from = from;
        this.from_id = from_id;
        this.from_level = from_level;
        this.text = text;
    }


    //Getters y setters de cada atributo de la clase:
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getFrom_level() {
        return from_level;
    }

    public void setFrom_level(String from_level) {
        this.from_level = from_level;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
