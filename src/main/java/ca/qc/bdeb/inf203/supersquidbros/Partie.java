package ca.qc.bdeb.inf203.supersquidbros;

import javafx.scene.canvas.GraphicsContext;

public class Partie {
    private Méduse méduse;
    private Plateforme plateformeTest = new Plateforme(1);
    private Plateforme plateformeTest2 = new Plateforme(2);
    private Plateforme[] plateformes = {plateformeTest, plateformeTest2};
    private boolean partieEnPause = false;
    private boolean gameOver = false;

    public Partie() {
        this.méduse = new Méduse();
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
            plateformeTest.estEnCollision(méduse); //On update les plateformes pour vérifier si elles sont en collision avec la méduse
            plateformeTest2.estEnCollision(méduse);
            for (int i = 0; i < plateformes.length; i++) {
                if (plateformes[i].isEnCollision()) { //On vérifie quelle est la plateforme la plus élevée qui est en contact avec la méduse
                    méduse.setEnCollision(true); //On indique que la méduse est en collision
                    méduse.setHauteurPlateforme(plateformes[i].getY() - méduse.getH());
                } //On donne la position Y de la plateforme à la méduse
            }
            méduse.update(deltaTime);
        }
    }

    public void draw(GraphicsContext context) {
        méduse.draw(context);
        plateformeTest.draw(context);
        plateformeTest2.draw(context);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
