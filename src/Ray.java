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
}
