import org.ejml.simple.SimpleMatrix;

/**
 * Created by Nikhil on 27/01/17.
 */
class Ray {
    private Vector origin;
    private Vector direction;

    public Ray(Vector origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;
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

    public void transform(SimpleMatrix transformation_matrix) {
        origin = Vector.transform(origin, transformation_matrix);
        direction = Vector.transform(direction, transformation_matrix);
    }
}
