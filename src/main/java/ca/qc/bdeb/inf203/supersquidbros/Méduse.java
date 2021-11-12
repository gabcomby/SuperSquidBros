package ca.qc.bdeb.inf203.supersquidbros;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Méduse {
    protected double vx = 0, vy = 0;
    protected double ax = 0, ay = 200;
    protected double w = 80, h = 80;
    protected double y = 200;
    protected double x = 175;
    protected KeyCode left = KeyCode.LEFT, right = KeyCode.RIGHT, up = KeyCode.UP;
    protected Image imageSquid = new Image("meduse1.png");

    public Méduse() {
    }
    public void draw(GraphicsContext context) {
        context.drawImage(imageSquid, x, y, w, h);
    }
    public void update(double deltaTime) {
        boolean left = Input.isKeyPressed(this.left);
        boolean right = Input.isKeyPressed(this.right);
        boolean up = Input.isKeyPressed(this.up);
        boolean alreadyInTheAir = false;
        if(left)
            vx=-50;
        else if(right)
            vx=50;
        else
            vx=0;
        double newX = x + (vx * deltaTime);
        if (newX > Main.WIDTH - w) {
            x=x + (-vx * deltaTime);
        } else if (newX < 0) {
            x=x + (-vx * deltaTime);
        } else
            x = newX;

        if(vy!=0)
            alreadyInTheAir = true;
        if(up && !alreadyInTheAir)
            vy = -150;
        vy = vy+(ay*deltaTime);
        double newY = y+(vy*deltaTime);
        if(newY>Main.HEIGHT - h) {
            vy = 0;
        }
        else
            y = newY;
    }
}
