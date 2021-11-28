package ca.qc.bdeb.inf203.supersquidbros;

import javafx.scene.paint.Color;

public class PlateformeRouge extends Plateforme {
    private double tempsEcoulé;
    public PlateformeRouge(int numPlateforme) {
        super(numPlateforme);
        this.couleur = Color.rgb(184, 15, 36);
        this.tempsEcoulé = 0;
    }

    @Override
    public void update(double deltaTime, Méduse m) {
        tempsEcoulé = tempsEcoulé + deltaTime;
        this.x = x + Math.sin(5 * tempsEcoulé) * 25;
        super.update(deltaTime);
    }
}
