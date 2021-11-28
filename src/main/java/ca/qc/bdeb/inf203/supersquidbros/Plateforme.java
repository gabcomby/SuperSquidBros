package ca.qc.bdeb.inf203.supersquidbros;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.Random;


public class Plateforme extends GameObject {

    private boolean enCollision;
    private Color couleur;
    private Random rnd = new Random();

    public Plateforme(int numPlateforme) {
        genererPlateforme(numPlateforme);
        this.couleur = Color.rgb(230, 134, 58);
        this.enCollision = false;
    }

    public boolean isEnCollision() {
        return enCollision;
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        double yCamera = camera.calculerYCamera(y);
        context.setFill(couleur);
        context.fillRect(x, yCamera, w, h);
    }

    /**
     * Fonction pour vérifier si une plateforme est en collision avec la méduse
     *
     * @param m On passe la méduse en paramètre afin d'avoir accès à sa position ainsi que sa largeur & hauteur
     */
    public void estEnCollision(Méduse m) {
        if (m.getY() + m.getH() >= this.y - 10 && m.getY() + m.getH() <= this.y + 10 && m.getVy() > 0 && m.getX() + 50 >= this.x && m.getX() <= this.x + this.w && !m.isEnCollision()) {
            this.enCollision = true;
        } else {
            this.enCollision = false;
        }
    }

    /**
     * Cette fonction génère la plateforme
     *
     * @param numPlateforme Le numéro de la plateforme permet de s'assurer que les plateformes sont toujours à 100
     *                      pixels les unes au dessus des autres
     */
    private void genererPlateforme(int numPlateforme) {
        this.w = rnd.nextInt(95) + 80;
        this.h = 10;
        this.x = setX();
        this.y = Main.HEIGHT - (numPlateforme * 100);
    }

    /**
     * Cette fonction permet de générer une position x aléatoire pour notre plateforme en tenant compte de sa largeur
     *
     * @return La position en x de la plateforme
     */
    private int setX() {
        double limite = Main.WIDTH - w;
        if (limite < 0)
            limite = 0;
        int limite1 = (int) limite;
        int posX = rnd.nextInt(limite1);
        return posX;
    }
}
