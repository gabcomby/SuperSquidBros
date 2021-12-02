package ca.qc.bdeb.inf203.supersquidbros;

import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public abstract class GameObject {
    protected double vx, vy;
    protected double ax, ay;
    protected double w, h;
    protected double y;
    protected double x;
    protected Random rnd = new Random();
    protected boolean estEnModeDebug = false;

    public void setEstEnModeDebug(boolean estEnModeDebug) {
        this.estEnModeDebug = estEnModeDebug;
    }

    /**
     * Méthode générale a tous les gameObject qui met a jour les variables physiques de l'objet en fonction du temps
     * @param deltaTime Le temps écoulé depuis la dernière update
     */
    public void update(double deltaTime) {
    }

    /**
     * Méthode générale a tous les gameObject qui permet de les afficher a l'écran
     * @param context GraphicsContext sur lequel on dessine
     * @param camera
     */
    public void draw(GraphicsContext context, Camera camera) {
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getAx() {
        return ax;
    }

    public double getAy() {
        return ay;
    }

    public double getH() {
        return h;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

}
