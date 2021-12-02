package ca.qc.bdeb.inf203.supersquidbros;

public class Camera {
    private double yCaméra = 0;
    private double vy = 0;
    private double vyTemp = 0;
    private double ay = -2;
    private boolean enModeDebug;

    public void setEnModeDebug(boolean enModeDebug) {
        this.enModeDebug = enModeDebug;
    }

    public Camera() {
        this.enModeDebug = false;
        this.yCaméra = 0;
        this.vy = 0;
    }

    public void update(double deltaTime, Méduse méduse) {
        if(enModeDebug) {
            ay = 0;
            vy=0;
        }
        else {
            ay = -2;
            vy = vyTemp;
            vy += ay * deltaTime;
            vyTemp = vy;
        }
        yCaméra += deltaTime * vy;
        if (calculerYCamera(méduse.getY()) < (Main.HEIGHT / 4)) {
            if (méduse.getVy() < 0)
                vy = méduse.getVy();
            yCaméra += deltaTime * vy;
        }
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public void setyCaméra(double yCaméra) {
        this.yCaméra = yCaméra;
    }

    public double calculerYCamera(double yPhysique) {
        return yPhysique - yCaméra;
    }

    public double getyCaméra() {
        return yCaméra;
    }
}
