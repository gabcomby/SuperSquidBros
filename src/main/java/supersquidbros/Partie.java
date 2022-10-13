package supersquidbros;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Random;

public class Partie {
    private Camera camera;
    private Méduse méduse;
    private ArrayList<Plateforme> listePlateforme = new ArrayList<>();
    private ArrayList<Bulles> tabBulles = new ArrayList<>();
    private double tempsEcouleBulles = 0;
    private boolean gameOver;
    private int numeroPlateforme = 1;
    private boolean modeDebug;

    public Partie() {
        this.camera = new Camera();
        this.gameOver = false;
        this.méduse = new Méduse();
        this.modeDebug = false;
        genererBulles();
        for (int i = 0; i < 4; i++) {
            creerPlateforme();
            numeroPlateforme++;
        }
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isModeDebug() {
        return modeDebug;
    }

    public void setModeDebug(boolean modeDebug) {
        this.modeDebug = modeDebug;
    }

    /**
     * Méthode pour update la partie
     * @param deltaTime Le temps écoulé depuis la dernière update
     */
    public void update(double deltaTime) {
        //On vérifie si la partie est en mode debug ou non
        enModeDebug();
        //On reset les collisions de la méduse
        méduse.setEnCollision(false);
        //On update les plateformes pour vérifier si elles sont en collision avec la méduse
        for (int i = 0; i < listePlateforme.size(); i++) {
            listePlateforme.get(i).estEnCollision(méduse);
        }
        /*Pour chaque plateforme, on update les collisions de la méduse selon si elles sont en contact ou non*/
        for (int i = 0; i < listePlateforme.size(); i++) {
            if (listePlateforme.get(i).isEnCollision()) {
                //Si c'est une plateforme verte, alors on met enCollision à false
                if (listePlateforme.get(i) instanceof PlateformeVerte) {
                    méduse.setEnCollision(false);
                    méduse.setSurPlateformeVerte(true);
                }
                //Si c'est une plateforme normale, on met enCollision à true
                else {
                    méduse.setEnCollision(true);
                    méduse.setSurPlateformeVerte(false);
                    méduse.setHauteurPlateforme((listePlateforme.get(i).getY()) - méduse.getH());
                }
            }
        }
        méduse.update(deltaTime);
        //On update toutes les plateformes
        for (int i = 0; i < listePlateforme.size(); i++) {
            listePlateforme.get(i).update(deltaTime, méduse);
        }
        //On update toutes les bulles
        for (int i = 0; i < tabBulles.size(); i++) {
            tabBulles.get(i).update(deltaTime);
        }
        //On update la caméra
        camera.update(deltaTime, méduse);
        tempsEcouleBulles = tempsEcouleBulles + deltaTime;
        //On crée et efface les plateformes en temps réel pour ne pas surcharger le programme
        creerEtEffacerPlateformes();
        //On génère des nouvelles bulles toutes les 3 secondes
        if (tempsEcouleBulles > 3) {
            genererBulles();
            tempsEcouleBulles = 0;
        }
        //On vérifie si la partie est perdue ou non
        verifierGameOver();
    }

    /**
     * Méthode qui dessine tous les objets dans la fenêtre Javafx
     * @param context GraphicsContext du pane
     */
    public void draw(GraphicsContext context) {
        //Dessine les bulles
        for (int i = 0; i < tabBulles.size(); i++) {
            tabBulles.get(i).draw(context, camera);
        }
        //Dessine la méduse
        méduse.draw(context, camera);
        //Dessine les plateformes
        for (int i = 0; i < listePlateforme.size(); i++) {
            listePlateforme.get(i).draw(context, camera);
        }
    }

    /**
     * Fonction servant à effacer et créer des nouvelles plateformes en temps réel pour ne pas surcharger le programme
     */
    private void creerEtEffacerPlateformes() {
        //On update la position relative à la caméra de la plateforme la plus en haut
        double yCameraPlateformeEnHaut = camera.calculerYCamera(listePlateforme.get(3).getY());
        //Si il y a 100 pixels ou plus entre le haut de l'écran et la plateforme d'en haut, on en crée une nouvelle
        if (yCameraPlateformeEnHaut >= 100 && listePlateforme.size() == 4) {
            creerPlateforme();
            numeroPlateforme++;
        }
        //Si une plateforme n'est plus affichée à l'écran, on la delete et on en crée une nouvelle
        for (int i = 0; i < listePlateforme.size(); i++) {
            double yCamera = camera.calculerYCamera(listePlateforme.get(i).getY());
            if (yCamera > Main.HEIGHT) {
                listePlateforme.remove(i);
                creerPlateforme();
                numeroPlateforme++;
            }
        }
    }

    /**
     * Vérifie si la méduse tombe en bas de l'écran
     */
    private void verifierGameOver() {
        double yCameraMeduse = camera.calculerYCamera(méduse.getY());
        if (yCameraMeduse > Main.HEIGHT)
            gameOver = true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public double getScoreDeLaPartie() {
        return camera.getyCaméra();
    }

    /**
     * Méthode qui retourne un int qui représente un type de plateforme, ce chiffre est calculé en fonction des
     * règles qui spécifient la chance de tomber sur chacun des types de plateformes
     * @return Le ID d'un type de plateforme qui servira dans un switch
     */
    private int pourcentagePlateformes() {
        Random rnd = new Random();
        int chiffreAleatoire = rnd.nextInt(101);
        int idPlateforme = 0;
        if (chiffreAleatoire <= 50) {
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

    /**
     * Crée une plateforme en fonction du chiffre retourné par la méthode pourcentagePlateformes()
     */
    private void creerPlateforme() {
        Plateforme plateforme;
        switch (pourcentagePlateformes()) {
            case 1 -> plateforme = new Plateforme(numeroPlateforme);
            case 2 -> plateforme = new PlateformeRouge(numeroPlateforme);
            case 3 -> plateforme = new PlateformeVerte(numeroPlateforme);
            case 4 -> plateforme = new PlateformeNoire(numeroPlateforme);
            default -> plateforme = new Plateforme(numeroPlateforme);
        }
        plateforme.setEstEnModeDebug(modeDebug);
        listePlateforme.add(plateforme);
    }

    /**
     * Crée 15 bulles séparées en 3 groupes de 5 bulles et les mets dans le tableau tabBulles
     */
    private void genererBulles() {
        Random rnd = new Random();
        //Calcule le x de base des groupes de bulles
        double basex1 = rnd.nextInt((int) Main.WIDTH + 1);
        double basex2 = rnd.nextInt((int) Main.WIDTH + 1);
        double basex3 = rnd.nextInt((int) Main.WIDTH + 1);
        double facteurRand;
        //Rentre les bulles dans le tableau
        for (int i = 0; i < 5; i++) {
            facteurRand = genererPositifOuNegatif();
            tabBulles.add(new Bulles(basex1 + (facteurRand * rnd.nextInt(21)), camera.getyCaméra() + Main.HEIGHT));
        }
        for (int j = 0; j < 5; j++) {
            facteurRand = genererPositifOuNegatif();
            tabBulles.add(new Bulles(basex2 + (facteurRand * rnd.nextInt(21)), camera.getyCaméra() + Main.HEIGHT));
        }
        for (int k = 0; k < 5; k++) {
            facteurRand = genererPositifOuNegatif();
            tabBulles.add(new Bulles(basex3 + (facteurRand * rnd.nextInt(21)), camera.getyCaméra() + Main.HEIGHT));
        }
    }

    /**
     * Méthode qui retourne soit +1 soit -1
     * @return -1 ou +1
     */
    private double genererPositifOuNegatif() {
        Random rnd = new Random();
        double facteurRand;
        if (rnd.nextInt(3) == 1) {
            facteurRand = -1;
        } else {
            facteurRand = 1;
        }
        return facteurRand;
    }

    /**
     * Méthode qui update le mode debug de chaque objet présent dans le jeu
     */
    private void enModeDebug() {
        méduse.setEstEnModeDebug(modeDebug);
        for (int i = 0; i < listePlateforme.size(); i++) {
            listePlateforme.get(i).setEstEnModeDebug(modeDebug);
        }
        camera.setEnModeDebug(modeDebug);
    }

    public Méduse getMéduse() {
        return méduse;
    }
}
