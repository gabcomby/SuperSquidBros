package ca.qc.bdeb.inf203.supersquidbros;

import javafx.scene.paint.Color;

public class PlateformeVerte extends Plateforme{
    public PlateformeVerte(int numPlateforme) {
        super(numPlateforme);
        this.couleur = Color.LIGHTGREEN;
    }

    @Override
    public void update(double deltaTime,Méduse m) {
        if (this.enCollision){
            if (Math.abs(m.getVy()) < 100){
                m.setVy(-100);
            }
            else{
                m.setVx(m.getVy()*-1.5);
            }
        }
    }



}
