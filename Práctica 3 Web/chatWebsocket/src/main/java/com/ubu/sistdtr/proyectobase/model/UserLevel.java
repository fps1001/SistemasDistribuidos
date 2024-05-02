package com.ubu.sistdtr.proyectobase.model;

public enum UserLevel {
    GUEST(0), // Nivel más bajo
    USER(1),
    ADMIN(2); // Nivel más alto

    private final int levelValue;

    // Constructor privado para asociar un valor numérico a cada constante de la enumeración
    UserLevel(int level) {
        this.levelValue = level;
    }

    // Getter para obtener el valor numérico del nivel
    public int getLevelValue() {
        return this.levelValue;
    }


    public static UserLevel fromString(String levelStr) {
        for (UserLevel level : UserLevel.values()) {
            if (level.name().equalsIgnoreCase(levelStr)) {
                return level;
            }
        }
        throw new IllegalArgumentException("No level found with name: " + levelStr);
    }

}
