package ca.qc.bdeb.inf203.supersquidbros;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
        Button retourMenu = new Button("Retour au menu");
        retourMenu.setLayoutX(120);
        retourMenu.setLayoutY(450);
        rootClassement.getChildren().add(retourMenu);

        Pane rootPartie = genererMenuBackground();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext context = canvas.getGraphicsContext2D();
        rootPartie.getChildren().add(canvas);

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
                } else {
                    //CHANGER DE SCÈNE VERS LA SCÈNE DE GAME OVER
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
}
