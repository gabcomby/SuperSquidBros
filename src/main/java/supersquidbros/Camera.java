package supersquidbros;

public class Camera {
    private double yCaméra;
    private double vy;
    private double vyTemp = 0;
    private double ay = -2;
    private boolean enModeDebug;

    /**
     * Setter qui met la caméra en mode déboguage
     * @param enModeDebug
     */
    public void setEnModeDebug(boolean enModeDebug) {
        this.enModeDebug = enModeDebug;
    }

    /**
     * Constructeur de la caméra
     */
    public Camera() {
        this.enModeDebug = false;
        this.yCaméra = 0;
        this.vy = 0;
    }

    /**
     * Calcule la position de la caméra. A partir de celle-ci on calcule la position des objets relative a la caméra
     * @param deltaTime Le temps écoulé depuis la dernière update
     * @param méduse Caméra du jeu
     */
    public void update(double deltaTime, Méduse méduse) {
        //Vérifie si est en mode déboguage
        if (enModeDebug) {
            ay = 0;
            vy = 0;
        } else {
            ay = -2;
            vy = vyTemp;
            vy += ay * deltaTime;
            vyTemp = vy;
        }
        yCaméra += deltaTime * vy;
        //Monte si la méduse est trop haut dans l'écran
        if (calculerYCamera(méduse.getY()) < (Main.HEIGHT / 4)) {
            if (méduse.getVy() < 0)
                vy = méduse.getVy();
            yCaméra += deltaTime * vy;
        }
    }

    /**
     * Retourne la position en y des objets en fonction de la position de la caméra
     * @param yPhysique position verticale réelle
     * @return
     */
    public double calculerYCamera(double yPhysique) {
        return yPhysique - yCaméra;
    }

    public double getyCaméra() {
        return yCaméra;
    }
}
