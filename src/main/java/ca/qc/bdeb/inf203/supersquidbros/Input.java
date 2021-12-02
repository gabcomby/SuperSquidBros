package ca.qc.bdeb.inf203.supersquidbros;

import javafx.scene.input.KeyCode;

import java.util.HashMap;

public class Input {
    private static HashMap<KeyCode, Boolean>touches = new HashMap<>();

    public static boolean isKeyPressed (KeyCode code) {
        return touches.getOrDefault(code, false);
    }

    /**
     * Méthode pour indiquer qu'une touche est appuyée
     * @param code Le KeyCode de la touche
     * @param isPressed True or false
     */
    public static void setKeyPressed(KeyCode code, boolean isPressed) {
        touches.put(code, isPressed);
    }
}
