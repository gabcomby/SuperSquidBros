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
        if (m.getY() == this.y && this.x<m.getX() && m.getX()<this.x + this.w){
            enCollision = true;
            m.alreadyInTheAir = false;
        }
        else{
            enCollision = false;
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
