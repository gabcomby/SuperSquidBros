package ca.qc.bdeb.inf203.supersquidbros;

import javafx.scene.paint.Color;

public class PlateformeVerte extends Plateforme {
    private boolean dejaSauté = false;

    public PlateformeVerte(int numPlateforme) {
        super(numPlateforme);
        this.couleur = Color.LIGHTGREEN;
    }

    /**
     * Méthode pour update la position d'une plateforme verte
     * @param deltaTime Le temps écoulé depuis la dernière update
     * @param m La méduse du jeu
     */
    @Override
    public void update(double deltaTime, Méduse m) {
        //Si la méduse est en collision avec cette plateforme, alors on la fait rebondir
        if (this.enCollision) {
            //Si la vitesse de la méduse est inférieure à 100, alors on met sa vitesse de rebond à 100
            if (Math.abs(m.getVy()) < 100 && !this.dejaSauté) {
                m.setEnCollision(false);
                m.setVy(-100);
                this.dejaSauté = true;
            }
            //Sinon, on la fait rebondir 1.5X plus vite
            else {
                m.setVy(m.getVy() * -1.5);
                this.dejaSauté = false;
            }
        }
    }
}
