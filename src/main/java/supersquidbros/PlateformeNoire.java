package supersquidbros;

import javafx.scene.paint.Color;

public class PlateformeNoire extends Plateforme {
    private boolean dejaCollision;

    public PlateformeNoire(int numPlateforme) {
        super(numPlateforme);
        this.couleur = Color.BLACK;
        this.dejaCollision = false;
    }

    /**
     * Override la méthode vide de la classe mère plateforme pour pouvoir tomber lors d'une collision
     * @param deltaTime le temps écoulé
     * @param m la méduse
     */
    @Override
    public void update(double deltaTime, Méduse m) {
        if (this.enCollision) {
            this.dejaCollision = true;
        }
        if (!this.enCollision && this.dejaCollision) {
            this.vy = 200;
        }
        this.y = y + vy * deltaTime;
    }
}
