package ca.qc.bdeb.inf203.supersquidbros;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Partie {
    private Camera camera = new Camera();
    private Méduse méduse;
    private ArrayList<Plateforme> listePlateforme = new ArrayList<>();
    private boolean partieEnPause = false;
    private boolean gameOver = false;
    private int numeroPlateforme = 1;

    public Partie() {
        this.méduse = new Méduse();
        for(int i = 0; i<4; i++) {
            Plateforme plateforme = new Plateforme(numeroPlateforme);
            listePlateforme.add(plateforme);
            numeroPlateforme++;
        }
    }

    /*TODO :
     *  Créer la caméra
     *  Déplacer la caméra
     *  Créer un compteur de score en fonction de la caméra
     *  Vérifier si la tête de la méduse est en dessous de la caméra
     *  Créer une méthode pour créer et effacer des plateformes en temps réel*/

    public void update(double deltaTime) {
        if (!partieEnPause) {
            méduse.setEnCollision(false); //On reset les collisions de la méduse

            //On update les plateformes pour vérifier si elles sont en collision avec la méduse
            for (int i = 0; i< listePlateforme.size(); i++) {
                listePlateforme.get(i).estEnCollision(méduse);
            }

            for(int i = 0; i<listePlateforme.size(); i++) {
                if(listePlateforme.get(i).isEnCollision()) {
                    méduse.setEnCollision(true);
                    méduse.setHauteurPlateforme((listePlateforme.get(i).getY()) - méduse.getH());
                }
            }
            méduse.update(deltaTime);
            camera.update(deltaTime);
            creerEtEffacerPlateformes();
        }
    }

    public void draw(GraphicsContext context) {
        méduse.draw(context, camera);
        for(int i = 0; i<listePlateforme.size(); i++) {
            listePlateforme.get(i).draw(context, camera);
        }
    }

    private void creerEtEffacerPlateformes () {
        for(int i = 0; i<listePlateforme.size(); i++) {
            double yCamera = camera.calculerYCamera(listePlateforme.get(i).getY());
            if(yCamera > Main.HEIGHT) {
                listePlateforme.remove(i);
                Plateforme plateforme = new Plateforme(numeroPlateforme);
                listePlateforme.add(plateforme);
                numeroPlateforme++;
            }
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
