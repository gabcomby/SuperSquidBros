package supersquidbros;

import javafx.scene.paint.Color;

public class PlateformeRouge extends Plateforme {
    private double tempsEcoulé;
    private double facteurRand1;
    private double facteurRand2;

    public PlateformeRouge(int numPlateforme) {
        super(numPlateforme);
        this.couleur = Color.rgb(184, 15, 36);
        this.tempsEcoulé = 0;
        if (rnd.nextInt(3) == 1) {
            this.facteurRand1 = -1;
        } else {
            this.facteurRand1 = 1;
        }
        facteurRand2 = rnd.nextDouble() + 0.5;
    }

    /**
     * Fonction pour update la position d'une plateforme rouge
     * @param deltaTime Le temps écoulé depuis la dernière update
     * @param m La méduse du jeu
     */
    @Override
    public void update(double deltaTime, Méduse m) {
        tempsEcoulé = tempsEcoulé + deltaTime;
        //On se sert d'une fonction SINUS pour randomiser le déplacement de la plateforme
        this.x = x + facteurRand1 * Math.sin(facteurRand2 * tempsEcoulé);
        //On s'assure que la plateforme ne puisse pas sortir de l'écran
        if (this.x < 0) {
            this.x = 0;
            this.facteurRand1 = facteurRand1 * -1;
        } else if (this.x + this.w > Main.WIDTH) {
            this.x = Main.WIDTH - this.w;
            this.facteurRand1 = facteurRand1 * -1;
        }

    }
}
