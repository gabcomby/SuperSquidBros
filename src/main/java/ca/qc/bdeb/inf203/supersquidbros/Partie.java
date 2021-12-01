package ca.qc.bdeb.inf203.supersquidbros;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.Random;

public class Partie {
    private Camera camera = new Camera();
    private Méduse méduse;
    private ArrayList<Plateforme> listePlateforme = new ArrayList<>();
    private boolean partieEnPause = false;
    private boolean gameOver = false;
    private int numeroPlateforme = 1;
    private double scoreDeLaPartie = 0;

    public Partie() {
        this.méduse = new Méduse();
        camera.setyCaméra(0);
        camera.setVy(0);
        for(int i = 0; i<4; i++) {
            creerPlateforme();
            numeroPlateforme++;
            System.out.println(numeroPlateforme);
        }
    }

    /*TODO :
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
                    if(listePlateforme.get(i) instanceof PlateformeVerte) {
                        méduse.setEnCollision(false);
                        méduse.setSurPlateformeVerte(true);
                    }
                    else {
                        méduse.setEnCollision(true);
                        méduse.setSurPlateformeVerte(false);
                        méduse.setHauteurPlateforme((listePlateforme.get(i).getY()) - méduse.getH());
                    }
                }
            }
            méduse.update(deltaTime);
            for(int i = 0; i<listePlateforme.size(); i++) {
                listePlateforme.get(i).update(deltaTime, méduse);
            }
            double positionNégative = méduse.getY() - 430;
            if(Math.abs(positionNégative) > scoreDeLaPartie) {
                scoreDeLaPartie = Math.abs(positionNégative);
            }
            camera.update(deltaTime, méduse);
            creerEtEffacerPlateformes();
            verifierGameOver();
        }
    }

    public void draw(GraphicsContext context) {
        méduse.draw(context, camera);
        for(int i = 0; i<listePlateforme.size(); i++) {
            listePlateforme.get(i).draw(context, camera);
        }
    }

    private void creerEtEffacerPlateformes () {
        double yCameraPlateformeEnHaut = camera.calculerYCamera(listePlateforme.get(3).getY());
        if(yCameraPlateformeEnHaut >= 100 && listePlateforme.size() == 4) {
            creerPlateforme();
            numeroPlateforme++;
        }
        for(int i = 0; i<listePlateforme.size(); i++) {
            double yCamera = camera.calculerYCamera(listePlateforme.get(i).getY());
            if(yCamera > Main.HEIGHT) {
                listePlateforme.remove(i);
                creerPlateforme();
                numeroPlateforme++;
            }
        }
    }

    private void verifierGameOver() {
        double yCameraMeduse = camera.calculerYCamera(méduse.getY());
        if(yCameraMeduse > Main.HEIGHT)
            gameOver = true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public double getScoreDeLaPartie() {
        return scoreDeLaPartie;
    }

    private int pourcentagePlateformes() {
        Random rnd = new Random();
        int chiffreAleatoire = rnd.nextInt(101);
        int idPlateforme = 0;
        if(chiffreAleatoire <= 50) {
            idPlateforme = 1;
        } else if (chiffreAleatoire <= 70) {
            idPlateforme = 2;
        } else if (chiffreAleatoire <= 85) {
            idPlateforme = 3;
        } else {
            idPlateforme = 4;
        }
        return idPlateforme;
    }

    private void creerPlateforme () {
        Plateforme plateforme;
        switch (pourcentagePlateformes()) {
            case 1 -> plateforme = new Plateforme(numeroPlateforme);
            case 2 -> plateforme = new PlateformeRouge(numeroPlateforme);
            case 3 -> plateforme = new PlateformeVerte(numeroPlateforme);
            case 4 -> plateforme = new PlateformeNoire(numeroPlateforme);
            default -> plateforme = new Plateforme(numeroPlateforme);
        }
        listePlateforme.add(plateforme);
    }

    private void genererBulles(){
        Random rnd = new Random();
    }
}
