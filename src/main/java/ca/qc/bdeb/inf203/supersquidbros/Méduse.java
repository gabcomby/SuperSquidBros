package ca.qc.bdeb.inf203.supersquidbros;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Méduse extends GameObject {
    protected KeyCode left = KeyCode.LEFT, right = KeyCode.RIGHT, up = KeyCode.UP;
    protected Image imageSquid1Droite = new Image("meduse1.png");
    protected Image imageSquid1Gauche = new Image("meduse1-g.png");
    protected boolean vaADroite = true;
    protected boolean alreadyInTheAir = false;

    public Méduse() {
        this.vx = 0;
        this.vy = 0;
        this.ax = 0;
        this.ay = 1200;
        this.w = 50;
        this.h = 50;
        this.y = 200;
        this.x = 175;
    }

    @Override
    public void draw(GraphicsContext context) {
        verifieDirection();
        if (vaADroite) {
            context.drawImage(imageSquid1Droite, x, y, w, h);
        }
        if (!vaADroite) {
            context.drawImage(imageSquid1Gauche, x, y, w, h);
        }
    }

    @Override
    public void update(double deltaTime) {
        boolean left = Input.isKeyPressed(this.left);
        boolean right = Input.isKeyPressed(this.right);
        boolean up = Input.isKeyPressed(this.up);

        if (left) {
            ax = -1200;
        } else if (right) {
            ax = 1200;
        } else {
            ax = 0;
            int signeVitesse = vx > 0 ? 1 : -1;
            double vitesseAmortissementX = -signeVitesse * 300;
            vx += deltaTime * vitesseAmortissementX;
            int nouveauSigneVitesse = vx > 0 ? 1 : -1;
            if (nouveauSigneVitesse != signeVitesse) {
                vx = 0;
            }
        }

        this.vx = vx + (ax * deltaTime);
        if (Math.abs(this.vx) > 175) {
            if (this.vx < 0) {
                this.vx = -175;
            }
            if (this.vx > 0) {
                this.vx = 175;
            }
        }


        double newX = x + (vx * deltaTime);
        if (newX > Main.WIDTH - w) {
            x = Main.WIDTH - w;
            this.vx = -vx;
        } else if (newX < 0) {
            x = 0;
            this.vx = -vx;
        } else
            x = newX;

        if (vy != 0) {
            alreadyInTheAir = true;
        }
        else if (vy == 0)
            alreadyInTheAir = false;

        if (up && !alreadyInTheAir) {
            System.out.println("Yes");
            vy = -600;
        }
        vy = vy + (ay * deltaTime);


        double newY = y + (vy * deltaTime);
        if (newY > Main.HEIGHT - h) {
            vy = 0;
        } else
            y = newY;
    }

    private void verifieDirection() {
        if (this.vx > 0) {
            this.vaADroite = true;
        } else if (this.vx < 0) {
            this.vaADroite = false;
        }
    }

    public void setAlreadyInTheAir(boolean alreadyInTheAir) {
        this.alreadyInTheAir = alreadyInTheAir;
    }
}
