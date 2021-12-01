package ca.qc.bdeb.inf203.supersquidbros;

import javafx.scene.paint.Color;

public class PlateformeVerte extends Plateforme{
    private boolean dejaSauté = false;
    public PlateformeVerte(int numPlateforme) {
        super(numPlateforme);
        this.couleur = Color.LIGHTGREEN;
    }

    @Override
    public void update(double deltaTime,Méduse m) {
        if (this.enCollision){
            if (Math.abs(m.getVy()) < 100 && !this.dejaSauté){
                m.setEnCollision(false);
                m.setVy(-100);
                this.dejaSauté = true;
            }
            else{
                m.setVy(m.getVy()*-1.5);
                this.dejaSauté = false;
            }
        }
    }



}
