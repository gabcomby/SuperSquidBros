package ca.qc.bdeb.inf203.supersquidbros;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;


public class Plateforme extends GameObject{

    private boolean enCollision;
    private Color couleur;
    private Random rnd = new Random();
    public Plateforme(int posY) {
        this.y = posY;
        this.h = 10;
        this.w = rnd.nextInt(95)+80;
        this.x = setX();
        this.couleur = Color.rgb(230,134,58);
        this.enCollision = false;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(couleur);
        context.fillRect(x,y,w,h);
    }

    public void estEnCollision(Méduse m){
        if (m.getY()-50 <= this.y && m.getVy()>0 && m.getX()+50>=this.x && m.getX()<=this.x + this.w){
            m.setHauteurPlateforme(this.y-50);
            m.setEnCollision(true);
            m.alreadyInTheAir = false;
        }
        else{
            m.setEnCollision(false);
            m.alreadyInTheAir = true;
        }
    }

    private int setX (){
        double limite =Main.WIDTH - w;
        if (limite < 0)
            limite=0;
        int limite1 = (int)limite;
        int posX = rnd.nextInt(limite1);
        return posX;
    }
}
