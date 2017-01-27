/**
 * Created by Nikhil on 27/01/17.
 */
class Ray {
    private Vector origin;
    private Vector direction;
    private double intensity;

    public Ray(Vector origin, Vector direction, double intensity) {
        this.origin = origin;
        this.direction = direction;
        this.intensity = intensity;
    }

    public Vector getOrigin() {
        return origin;
    }

    public void setOrigin(Vector origin) {
        this.origin = origin;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }
}
