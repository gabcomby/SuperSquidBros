package ca.qc.bdeb.inf203.supersquidbros;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static final double HEIGHT = 480;
    public static final double WIDTH = 350;
    private AnimationTimer timer;
    private Partie partie;
    //L'affichage du score
    private Text scoreDeLaPartie = new Text("0");
    private Text gameOverText = new Text("Game Over!");
    //Le score pour le fichier
    private double scoreDeLaPartiePourFichier = 0;
    private ListView listeScore = new ListView();
    //Le compteur de temps pour les 3 secondes de Game Over
    private double compteurTempsGameOver = 0;
    //Éviter d'enregistrer 2 fois son score
    private boolean dejaEnregistréScore;
    //Tous les textes du mode débug
    private Text positionMeduse = new Text("Position = (0,0)");
    private Text vitesseMeduse = new Text("Vitesse = (0,0)");
    private Text accelerationMeduse = new Text("Acceleration = (0,0)");
    private Text toucheLeSol = new Text("Touche le sol = oui");

    @Override
    public void start(Stage stage) throws Exception {
        //On crée le Pane pour le menu principal
        Pane rootMenu = genererMenuBackgroundPane();
        //On met l'image du jeu dans le menu
        Image imageMenu = new Image("accueil.png");
        ImageView imageViewMenu = new ImageView(imageMenu);
        imageViewMenu.setLayoutY(50);
        //On place le bouton Jouer sur le menu
        Button jouer = new Button("Jouer!");
        jouer.setLayoutX(150);
        jouer.setLayoutY(350);
        //On place le bouton Classement sur le menu
        Button classement = new Button("Meilleurs scores");
        classement.setLayoutX(123);
        classement.setLayoutY(400);
        //On place le bouton Quitter sur le menu
        Button fermerLeJeu = new Button("Quitter");
        fermerLeJeu.setLayoutY(450);
        fermerLeJeu.setLayoutX(147);
        rootMenu.getChildren().addAll(imageViewMenu, jouer, classement, fermerLeJeu);

        //On crée la VBox pour l'écran des meilleurs scores
        VBox rootClassement = new VBox();
        Text meilleursScore = new Text("Meilleurs score!");
        meilleursScore.setFont(Font.font(50));
        //La HBox contenant les éléments pour enregistrer notre score
        HBox entrerNom = new HBox();
        entrerNom.setAlignment(Pos.CENTER);
        Text nom = new Text("Nom : ");
        rootClassement.setAlignment(Pos.CENTER);
        //Un tableau qui affiche les scores ligne par ligne
        TextField textField = new TextField();
        textField.setMaxWidth(320);
        textField.setMaxHeight(300);
        //Le bouton pour enregistrer le score du joueur
        Button boutonEnregistrer = new Button("Sauvegarder");
        entrerNom.getChildren().addAll(nom, textField, boutonEnregistrer);
        Button retourMenu = new Button("Retour au menu");
        rootClassement.getChildren().addAll(meilleursScore, listeScore, entrerNom, retourMenu);

        //On crée le StackPane pour l'écran de jeu
        StackPane rootPartie = genererMenuBackgroundStackPane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext context = canvas.getGraphicsContext2D();
        rootPartie.getChildren().add(canvas);
        //On règle la taille et la couleur du score et du texte de Game Over
        scoreDeLaPartie.setFont(Font.font(50));
        scoreDeLaPartie.setFill(Color.WHITE);
        gameOverText.setFill(Color.RED);
        rootPartie.setAlignment(scoreDeLaPartie, Pos.TOP_CENTER);
        rootPartie.setAlignment(gameOverText, Pos.CENTER);
        //On met l'opacité du texte Game Over à 0 pour ne pas le voir
        gameOverText.setOpacity(0);
        gameOverText.setFont(Font.font(60));
        rootPartie.getChildren().add(gameOverText);
        rootPartie.getChildren().add(scoreDeLaPartie);
        VBox debugModeAffichage = new VBox();
        //On set la couleur et l'opacité des textes du debug mode
        positionMeduse.setOpacity(0);
        positionMeduse.setFill(Color.WHITE);
        vitesseMeduse.setOpacity(0);
        vitesseMeduse.setFill(Color.WHITE);
        accelerationMeduse.setOpacity(0);
        accelerationMeduse.setFill(Color.WHITE);
        toucheLeSol.setOpacity(0);
        toucheLeSol.setFill(Color.WHITE);
        debugModeAffichage.getChildren().addAll(positionMeduse, vitesseMeduse, accelerationMeduse, toucheLeSol);
        rootPartie.setAlignment(debugModeAffichage, Pos.TOP_LEFT);
        rootPartie.getChildren().add(debugModeAffichage);

        //On crée les 3 scènes du jeu
        Scene scenePartie = new Scene(rootPartie, WIDTH, HEIGHT);
        Scene sceneClassement = new Scene(rootClassement, WIDTH, HEIGHT);
        Scene sceneMenu = new Scene(rootMenu, WIDTH, HEIGHT);

        //Actions du bouton pour enregistrer son score
        boutonEnregistrer.setOnAction((e) -> {
            if (!dejaEnregistréScore) {
                String nomHighScore = textField.getText();
                écrireFichier(nomHighScore, (int) scoreDeLaPartiePourFichier);
                lireFichier();
                dejaEnregistréScore = true;
            }
        });

        //Bouton pour quitter le jeu
        fermerLeJeu.setOnAction((e) -> {
            Platform.exit();
        });

        //Bouton pour aller au menu des High Score
        classement.setOnAction((e) -> {
            lireFichier();
            stage.setScene(sceneClassement);
        });

        //Bouton pour commencer une nouvelle partie
        jouer.setOnAction((e) -> {
            partie = new Partie();
            scoreDeLaPartie.setText("0 px");
            scoreDeLaPartiePourFichier = 0;
            dejaEnregistréScore = false;
            stage.setScene(scenePartie);
            timer.start();
        });

        //Bouton pour retourner au menu
        retourMenu.setOnAction((e) -> {
            stage.setScene(sceneMenu);
        });

        //Quand on lâche une touche on la retire de la liste des touches appuyées
        scenePartie.setOnKeyReleased((e) -> {
            Input.setKeyPressed(e.getCode(), false);
        });

        //On rajoute les touches appuyées à la liste des touches
        scenePartie.setOnKeyPressed((e) -> {
            //Si la touche est T, alors on active le debug mode et on consume l'event pour éviter la redondance
            if (e.getCode() == KeyCode.T) {
                partie.setModeDebug(!partie.isModeDebug());
                e.consume();
            } else {
                Input.setKeyPressed(e.getCode(), true);
            }
        });

        timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                double deltaTime = (now - lastTime) * 1e-9;
                //Tant que la partie n'est pas finie, on l'update
                if (!partie.isGameOver()) {
                    partie.update(deltaTime);
                    //Si la partie est en mode debug, on affiche les données du mode debug en haut à gauche
                    if (partie.isModeDebug()) {
                        modeDebugAffichage();
                    } else
                    //Si la partie n'est pas en mode debug, on cache les données du mode debug
                    {
                        positionMeduse.setOpacity(0);
                        vitesseMeduse.setOpacity(0);
                        accelerationMeduse.setOpacity(0);
                        toucheLeSol.setOpacity(0);
                        scoreDeLaPartie.setFont(Font.font(50));
                    }
                    //On update le texte du score de la partie et la valeur qui sera enregistrée dans le fichier
                    scoreDeLaPartiePourFichier = Math.abs(Math.round(partie.getScoreDeLaPartie()));
                    scoreDeLaPartie.setText(String.valueOf(Math.abs(Math.round(partie.getScoreDeLaPartie()))) + " px");
                } else {
                    //Quand la partie se termine, on commence un countdown de 3 secondes
                    compteurTempsGameOver = compteurTempsGameOver + deltaTime;
                    //On affiche le texte Game Over
                    gameOverText.setOpacity(100);
                    //Quand le 3 secondes est atteint, on change vers la scène des meilleurs score et on stop le timer
                    if (compteurTempsGameOver >= 3) {
                        lireFichier();
                        stage.setScene(sceneClassement);
                        timer.stop();
                        compteurTempsGameOver = 0;
                        gameOverText.setOpacity(0);
                    }
                }
                //On draw la partie
                context.clearRect(0, 0, WIDTH, HEIGHT);
                partie.draw(context);
                lastTime = now;
            }
        };
        //On empêche l'utilisateur de resize la fenêtre et on set le logo de l'application à l'image de la méduse
        stage.setResizable(false);
        stage.getIcons().add(new Image("meduse1.png"));
        stage.setTitle("Super Squid Bros");
        stage.setScene(sceneMenu);
        stage.show();
    }

    /**
     * Génère un Pane avec un background bleu pour le menu principal
     * @return Un Pane avec un background bleu
     */
    private Pane genererMenuBackgroundPane() {
        Pane rootMenu = new Pane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext contextMenu = canvas.getGraphicsContext2D();
        Color bleu = Color.rgb(0, 0, 139);
        contextMenu.setFill(bleu);
        contextMenu.fillRect(0, 0, WIDTH, HEIGHT);
        rootMenu.getChildren().add(canvas);
        return rootMenu;
    }

    /**
     * Génère un StackPane avec un background bleu pour la partie
     * @return Un StackPane avec un background bleu
     */
    private StackPane genererMenuBackgroundStackPane() {
        StackPane rootMenu = new StackPane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext contextMenu = canvas.getGraphicsContext2D();
        Color bleu = Color.rgb(0, 0, 139);
        contextMenu.setFill(bleu);
        contextMenu.fillRect(0, 0, WIDTH, HEIGHT);
        rootMenu.getChildren().add(canvas);
        return rootMenu;
    }

    /**
     * Méthode qui sert à écrire un score et un nom d'utilisateur associé dans un fichier de sauvegarde
     * @param nom
     * @param score
     */
    private void écrireFichier(String nom, int score) {
        try {
            //On écrit le nom et le score dans le fichier TXT spécifié
            PrintWriter printWriter = new PrintWriter(new FileWriter("highscores.txt", true));
            printWriter.write(nom + ";" + score);
            printWriter.write("\n");
            printWriter.close();
        }
        //On catch les erreurs d'écriture
        catch (java.io.IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier de score!");
            System.exit(1);
        }
    }

    /**
     * Méthode qui sert à lire le fichier de score pour en extraire les données
     */
    private void lireFichier() {
        try {
            listeScore.getItems().clear();
            ArrayList<Integer> scoresNumériques = new ArrayList<>();
            ArrayList<String> listeDeNoms = new ArrayList<>();
            String[] tab;
            BufferedReader highscoreReader = new BufferedReader(new FileReader("highscores.txt"));
            String ligne = highscoreReader.readLine();
            //On lit les lignes, puis on classe les scores du plus élevé au plus faible, puis on score les noms selon
            //le classement des scores
            while (ligne != null) {
                tab = ligne.split(";");
                String nom = tab[0];
                String score = tab[1];
                int scoreLu = Integer.parseInt(score);
                scoresNumériques.add(scoreLu);
                Collections.sort(scoresNumériques);
                Collections.reverse(scoresNumériques);
                listeDeNoms.add(scoresNumériques.indexOf(scoreLu), nom);
                ligne = highscoreReader.readLine();
            }
            highscoreReader.close();
            //On écrit les scores et noms dans le ListView
            for (int i = 0; i < scoresNumériques.size(); i++) {
                int position = i + 1;
                listeScore.getItems().add("#" + position + " -- " + listeDeNoms.get(i) +
                        " -- " + scoresNumériques.get(i));
            }
        } catch (java.io.IOException e) {
            System.out.println("Erreur lors de la lecture du fichier de score!");
            System.exit(1);
        }
    }

    /***
     * Affiche une série de plusieurs informations sur la méduse et sur ses collisions
     */
    private void modeDebugAffichage() {
        //Position de la méduse
        positionMeduse.setText("Position = (" + String.valueOf(Math.round(partie.getMéduse().getX()))
                + " , " + String.valueOf(Math.round(partie.getMéduse().getY())) + ")");
        //Vitesse de la méduse
        vitesseMeduse.setText("Vitesse = (" + String.valueOf(Math.round(partie.getMéduse().getVx())
                + " , " + String.valueOf(Math.round(partie.getMéduse().getVy())) + ")"));
        //Accélération de la méduse
        accelerationMeduse.setText("Accélération = (" + String.valueOf(Math.round(
                partie.getMéduse().getAx())) + " , " + String.valueOf(Math.round(
                partie.getMéduse().getAy())) + ")");
        //Contacts de la méduse
        toucheLeSol.setText("Touche le sol = " + partie.getMéduse().isEnCollision());
        positionMeduse.setOpacity(100);
        vitesseMeduse.setOpacity(100);
        accelerationMeduse.setOpacity(100);
        toucheLeSol.setOpacity(100);
        scoreDeLaPartie.setFont(Font.font(35));
    }
}
