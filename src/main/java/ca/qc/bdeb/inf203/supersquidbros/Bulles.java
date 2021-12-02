package ca.qc.bdeb.inf203.supersquidbros;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bulles extends GameObject{
    private Color couleur;
    public Bulles(double posX, double posY) {
        this.couleur = Color.rgb(0, 0, 255, 0.4);
        this.w = rnd.nextInt(31) + 10.0;
        this.h = w;
        this.vy = -(rnd.nextInt(101) + 350.0);
        this.x = posX;
        this.y = posY;
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        double yCamera = camera.calculerYCamera(y);
        context.setFill(couleur);
        context.fillOval(x, yCamera, w, h);
    }

    @Override
    public void update(double deltaTime) {
        this.y = (this.vy * deltaTime) + y;
    }
}
