package ca.qc.bdeb.inf203.supersquidbros;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static final double HEIGHT = 480;
    public static final double WIDTH = 350;
    private boolean jeuEnPause = false;
    private AnimationTimer timer;
    private Partie partie;
    protected Text scoreDeLaPartie = new Text("0");

    @Override
    public void start(Stage stage) throws Exception {
        Pane rootMenu = genererMenuBackground();
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

        Pane rootClassement = genererMenuBackground();
        Text textGameOver = new Text("Game Over");
        rootClassement.getChildren().add(textGameOver);
        Button retourMenu = new Button("Retour au menu");
        retourMenu.setLayoutX(120);
        retourMenu.setLayoutY(450);
        rootClassement.getChildren().add(retourMenu);

        StackPane rootPartie = genererMenuBackground2();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext context = canvas.getGraphicsContext2D();
        rootPartie.getChildren().add(canvas);
        scoreDeLaPartie.setFont(Font.font(50));
        rootPartie.setAlignment(scoreDeLaPartie, Pos.TOP_CENTER);
        rootPartie.getChildren().add(scoreDeLaPartie);

        Scene scenePartie = new Scene(rootPartie, WIDTH, HEIGHT);
        Scene sceneClassement = new Scene(rootClassement, WIDTH, HEIGHT);
        Scene sceneMenu = new Scene(rootMenu, WIDTH, HEIGHT);

        fermerLeJeu.setOnAction((e) -> {
            Platform.exit();
        });

        classement.setOnAction((e) -> {
            stage.setScene(sceneClassement);
        });

        jouer.setOnAction((e) -> {
            stage.setScene(scenePartie);
            partie = new Partie();
            scoreDeLaPartie.setText(String.valueOf(partie.getScoreDeLaPartie()) + " px");
            timer.start();
        });

        retourMenu.setOnAction((e) -> {
            stage.setScene(sceneMenu);
        });

        scenePartie.setOnKeyReleased((e) -> {
            Input.setKeyPressed(e.getCode(), false);
        });

        scenePartie.setOnKeyPressed((e) -> {
            Input.setKeyPressed(e.getCode(), true);
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
                if(!partie.isGameOver()) {
                    partie.update(deltaTime);
                    scoreDeLaPartie.setText(String.valueOf(Math.abs(Math.round(partie.getScoreDeLaPartie()))) + " px");
                } else {
                    stage.setScene(sceneClassement);
                    timer.stop();
                }
                context.clearRect(0, 0, WIDTH, HEIGHT);
                partie.draw(context);
                lastTime = now;
            }
        };

        stage.setTitle("Super Squid Bros");
        stage.setScene(sceneMenu);
        stage.show();
    }

    private Pane genererMenuBackground() {
        Pane rootMenu = new Pane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext contextMenu = canvas.getGraphicsContext2D();
        Color bleu = Color.rgb(0, 0, 139);
        contextMenu.setFill(bleu);
        contextMenu.fillRect(0, 0, WIDTH, HEIGHT);
        rootMenu.getChildren().add(canvas);
        return rootMenu;
    }

    private StackPane genererMenuBackground2() {
        StackPane rootMenu = new StackPane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext contextMenu = canvas.getGraphicsContext2D();
        Color bleu = Color.rgb(0, 0, 139);
        contextMenu.setFill(bleu);
        contextMenu.fillRect(0, 0, WIDTH, HEIGHT);
        rootMenu.getChildren().add(canvas);
        return rootMenu;
    }

    public Text getScoreDeLaPartie() {
        return scoreDeLaPartie;
    }

    public void setScoreDeLaPartie(Text scoreDeLaPartie) {
        this.scoreDeLaPartie = scoreDeLaPartie;
    }
}
