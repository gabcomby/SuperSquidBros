package ca.qc.bdeb.inf203.supersquidbros;

public class Camera {
    private double yCaméra = 0;
    private double vy = 0;
    private double vyTemp = 0;
    private double ay = -2;

    public void update(double deltaTime, Méduse méduse) {
        vy = vyTemp;
        vy += ay * deltaTime;
        vyTemp = vy;
        yCaméra += deltaTime * vy;
        if (calculerYCamera(méduse.getY()) < (Main.HEIGHT / 4)) {
            if (méduse.getVy() < 0)
                vy = méduse.getVy();
            yCaméra += deltaTime * vy;
        }
    }

    public double calculerYCamera(double yPhysique) {
        return yPhysique - yCaméra;
    }

    public double getyCaméra() {
        return yCaméra;
    }
}
