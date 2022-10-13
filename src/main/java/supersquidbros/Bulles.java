package supersquidbros;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bulles extends GameObject {
    private Color couleur;

    public Bulles(double posX, double posY) {
        this.couleur = Color.rgb(0, 0, 255, 0.4);
        this.w = rnd.nextInt(31) + 10.0;
        this.h = w;
        this.vy = -(rnd.nextInt(101) + 350.0);
        this.x = posX;
        this.y = posY;
    }

    /**
     * Méthode qui dessine les bulles a l'écran
     * @param context GraphicsContext sur lequel on dessine
     * @param camera Caméra du jeu
     */
    @Override
    public void draw(GraphicsContext context, Camera camera) {
        double yCamera = camera.calculerYCamera(y);
        context.setFill(couleur);
        context.fillOval(x, yCamera, w, h);
    }

    /**
     * Méthode qui met a jour la position des bulles en fonction du temps
     * @param deltaTime Le temps écoulé depuis la dernière update
     */
    @Override
    public void update(double deltaTime) {
        this.y = (this.vy * deltaTime) + y;
    }
}
