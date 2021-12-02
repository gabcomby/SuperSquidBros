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
    private Text scoreDeLaPartie = new Text("0");
    private Text gameOverText = new Text("Game Over!");
    private double scoreDeLaPartiePourFichier = 0;
    private ListView listeScore = new ListView();
    private double compteurTempsGameOver = 0;
    private boolean dejaEnregistréScore;
    private Text positionMeduse = new Text("Position = (0,0)");
    private Text vitesseMeduse = new Text("Vitesse = (0,0)");
    private Text accelerationMeduse = new Text("Acceleration = (0,0)");
    private Text toucheLeSol = new Text("Touche le sol = oui");

    @Override
    public void start(Stage stage) throws Exception {
        Pane rootMenu = genererMenuBackgroundPane();
        Image imageMenu = new Image("accueil.png");
        ImageView imageViewMenu = new ImageView(imageMenu);
        imageViewMenu.setLayoutY(50);
        Button jouer = new Button("Jouer!");
        jouer.setLayoutX(150);
        jouer.setLayoutY(350);
        Button classement = new Button("Meilleurs scores");
        classement.setLayoutX(123);
        classement.setLayoutY(400);
        Button fermerLeJeu = new Button("Quitter");
        fermerLeJeu.setLayoutY(450);
        fermerLeJeu.setLayoutX(147);
        rootMenu.getChildren().addAll(imageViewMenu, jouer, classement, fermerLeJeu);

        VBox rootClassement = new VBox();
        Text meilleursScore = new Text("Meilleurs score!");
        meilleursScore.setFont(Font.font(50));
        HBox entrerNom = new HBox();
        entrerNom.setAlignment(Pos.CENTER);
        Text nom = new Text("Nom : ");
        rootClassement.setAlignment(Pos.CENTER);
        TextField textField = new TextField();
        textField.setMaxWidth(320);
        textField.setMaxHeight(300);
        Button boutonEnregistrer = new Button("Sauvegarder");
        entrerNom.getChildren().addAll(nom, textField, boutonEnregistrer);
        Button retourMenu = new Button("Retour au menu");
        rootClassement.getChildren().addAll(meilleursScore, listeScore, entrerNom, retourMenu);

        StackPane rootPartie = genererMenuBackgroundStackPane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext context = canvas.getGraphicsContext2D();
        rootPartie.getChildren().add(canvas);
        scoreDeLaPartie.setFont(Font.font(50));
        scoreDeLaPartie.setFill(Color.WHITE);
        gameOverText.setFill(Color.WHITE);
        rootPartie.setAlignment(scoreDeLaPartie, Pos.TOP_CENTER);
        rootPartie.setAlignment(gameOverText, Pos.CENTER);
        gameOverText.setOpacity(0);
        gameOverText.setFont(Font.font(60));
        gameOverText.setFill(Color.RED);
        rootPartie.getChildren().add(gameOverText);
        rootPartie.getChildren().add(scoreDeLaPartie);
        VBox debugModeAffichage = new VBox();
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

        Scene scenePartie = new Scene(rootPartie, WIDTH, HEIGHT);
        Scene sceneClassement = new Scene(rootClassement, WIDTH, HEIGHT);
        Scene sceneMenu = new Scene(rootMenu, WIDTH, HEIGHT);

        boutonEnregistrer.setOnAction((e) -> {
            if (!dejaEnregistréScore) {
                String nomHighScore = textField.getText();
                écrireFichier(nomHighScore, (int) scoreDeLaPartiePourFichier);
                lireFichier();
                dejaEnregistréScore = true;
            }
        });

        fermerLeJeu.setOnAction((e) -> {
            Platform.exit();
        });

        classement.setOnAction((e) -> {
            lireFichier();
            stage.setScene(sceneClassement);
        });

        jouer.setOnAction((e) -> {
            partie = new Partie();
            scoreDeLaPartie.setText("0 px");
            scoreDeLaPartiePourFichier = 0;
            dejaEnregistréScore = false;
            stage.setScene(scenePartie);
            timer.start();
        });

        retourMenu.setOnAction((e) -> {
            stage.setScene(sceneMenu);
        });

        scenePartie.setOnKeyReleased((e) -> {
            Input.setKeyPressed(e.getCode(), false);
        });

        scenePartie.setOnKeyPressed((e) -> {
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
                if (!partie.isGameOver()) {
                    partie.update(deltaTime);
                    if (partie.isModeDebug()) {
                        modeDebugAffichage();
                    } else {
                        positionMeduse.setOpacity(0);
                        vitesseMeduse.setOpacity(0);
                        accelerationMeduse.setOpacity(0);
                        toucheLeSol.setOpacity(0);
                        scoreDeLaPartie.setFont(Font.font(50));
                    }
                    scoreDeLaPartiePourFichier = Math.abs(Math.round(partie.getScoreDeLaPartie()));
                    scoreDeLaPartie.setText(String.valueOf(Math.abs(Math.round(partie.getScoreDeLaPartie()))) + " px");
                } else {
                    compteurTempsGameOver = compteurTempsGameOver + deltaTime;
                    gameOverText.setOpacity(100);
                    if (compteurTempsGameOver >= 3) {
                        lireFichier();
                        stage.setScene(sceneClassement);
                        timer.stop();
                        compteurTempsGameOver = 0;
                        gameOverText.setOpacity(0);
                    }
                }
                context.clearRect(0, 0, WIDTH, HEIGHT);
                partie.draw(context);
                lastTime = now;
            }
        };
        stage.setResizable(false);
        stage.getIcons().add(new Image("meduse1.png"));
        stage.setTitle("Super Squid Bros");
        stage.setScene(sceneMenu);
        stage.show();
    }

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

    private void écrireFichier(String nom, int score) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter("highscores.txt", true));
            printWriter.write(nom + ";" + score);
            printWriter.write("\n");
            printWriter.close();
        } catch (java.io.IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier de score!");
            System.exit(1);
        }
    }

    private void lireFichier() {
        try {
            listeScore.getItems().clear();
            ArrayList<Integer> scoresNumériques = new ArrayList<>();
            ArrayList<String> listeDeNoms = new ArrayList<>();
            String[] tab;
            BufferedReader highscoreReader = new BufferedReader(new FileReader("highscores.txt"));
            String ligne = highscoreReader.readLine();
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
            for (int i = 0; i < scoresNumériques.size(); i++) {
                int position = i + 1;
                listeScore.getItems().add("#" + position + " -- " + listeDeNoms.get(i) + " -- " + scoresNumériques.get(i));
            }
        } catch (java.io.IOException e) {
            System.out.println("Erreur lors de la lecture du fichier de score!");
            System.exit(1);
        }
    }

    private void modeDebugAffichage() {
        positionMeduse.setText("Position = (" + String.valueOf(Math.round(partie.getMéduse().getX()))
                + " , " + String.valueOf(Math.round(partie.getMéduse().getY())) + ")");
        vitesseMeduse.setText("Vitesse = (" + String.valueOf(Math.round(partie.getMéduse().getVx())
                + " , " + String.valueOf(Math.round(partie.getMéduse().getVy())) + ")"));
        accelerationMeduse.setText("Accélération = (" + String.valueOf(Math.round(
                partie.getMéduse().getAx())) + " , " + String.valueOf(Math.round(
                partie.getMéduse().getAy())) + ")");
        toucheLeSol.setText("Touche le sol = " + partie.getMéduse().isEnCollision());
        positionMeduse.setOpacity(100);
        vitesseMeduse.setOpacity(100);
        accelerationMeduse.setOpacity(100);
        toucheLeSol.setOpacity(100);
        scoreDeLaPartie.setFont(Font.font(35));
    }
}
