package ca.qc.bdeb.inf203.supersquidbros;

public class Camera {
    private double yCaméra = 0;
    private double vy = 0;
    private double ay = -2;

    public void update(double deltaTime) {
        vy += ay*deltaTime;
        yCaméra+= deltaTime*vy;
    }

    public double calculerYCamera(double yPhysique) {
        return yPhysique - yCaméra;
    }
}
