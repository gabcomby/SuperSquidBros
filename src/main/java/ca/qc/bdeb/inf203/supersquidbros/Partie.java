package ca.qc.bdeb.inf203.supersquidbros;

import javafx.scene.canvas.GraphicsContext;

public class Partie {
    private Méduse méduse;
    private Plateforme plateformeTest = new Plateforme(380);
    //private [] plateformes
    private boolean partieEnPause = false;
    private boolean gameOver = false;

    public Partie() {
        this.méduse = new Méduse();
    }

    public void update(double deltaTime) {
        if(!partieEnPause) {
            plateformeTest.estEnCollision(méduse);
            méduse.update(deltaTime);
        }
        //Tester le collisions
        //Bouger la caméra
        //Vérifier si on a gagné ou perdu
    }

    public void draw(GraphicsContext context) {
        méduse.draw(context);
        plateformeTest.draw(context);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
