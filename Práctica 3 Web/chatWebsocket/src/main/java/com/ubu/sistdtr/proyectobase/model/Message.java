package com.ubu.sistdtr.proyectobase.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Mensaje del chat y su información pertinente.
 *
 * @author Fernando Pisot
 *
 */
public class Message {
    private String from;
    private String from_id;
    private UserLevel from_level;
    private String text;

    @JsonCreator    // Soluciona el problema de deserialización de docker con esta clase.
    public Message(@JsonProperty("from") String from,
                   @JsonProperty("from_id") String from_id,
                   @JsonProperty("from_level") UserLevel from_level,
                   @JsonProperty("text") String text) {
        this.from = from;
        this.from_id = from_id;
        this.from_level = from_level;
        this.text = text;
    }

    // Getters y setters
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public UserLevel getFrom_level() {
        return from_level;
    }

    public void setFrom_level(UserLevel from_level) {
        this.from_level = from_level;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
