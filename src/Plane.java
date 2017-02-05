/**
 * Created by Kartikeya Sharma on 28-01-2017.
 */
public class Plane implements Entity {
    private Vector normal;
    private Vector point;
    private Material material;
    private Colour colour;

    public Plane(Vector normal, Vector point) {
        this.normal = normal;
        this.point = point;
    }

    public Vector getNormal(Vector intersection) {
        return normal;
    }

    public Vector getPoint() {
        return point;
    }

    public void setNormal(Vector normal) {
        this.normal = normal;
    }

    public void setPoint(Vector point) {
        this.point = point;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public Vector getNormal (Ray ray, Vector intersection) {
        return normal;
    }

    public double getTimeIntersection (Ray ray) {
        return 0.0;
    }

    public Vector getIntersection (Ray ray, double time) {
        Vector origin = ray.getOrigin();
        Vector direction = ray.getDirection();
        double D = - (Vector.dot(normal, point));
        double t = - ((Vector.dot(normal, origin) + D)/(Vector.dot(normal, direction)));

        return new Vector(origin.getX() + direction.getX()*t,
                origin.getY() + direction.getY()*t,
                origin.getZ() + direction.getZ()*t);
    }

    public Ray getRefractedRay (Ray ray, Vector intersection, Vector normal) {
        return ray;
    }
}

