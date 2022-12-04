package org.example;

public enum Colors {
    /**
     * Для цветного вывода в консоль
     */
    ANSI_YELLOW,
    ANSI_CYAN,
    ANSI_PURPLE,
    ANSI_RESET;

    @Override
    public java.lang.String toString() {
        switch (this) {
            case ANSI_YELLOW: return "\u001B[33m";
            case ANSI_CYAN: return "\u001B[36m";
            case ANSI_PURPLE: return "\u001B[35m";
            case ANSI_RESET: return "\u001B[0m";
        }
        return "";
    }
}
