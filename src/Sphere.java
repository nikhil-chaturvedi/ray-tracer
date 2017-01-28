import org.json.JSONObject;

/**
 * Created by Nikhil on 27/01/17.
 */
class Sphere implements Entity {
    private Vector centre;
    private double radius;

    private Colour colour;
    private Material material;

    Sphere(JSONObject conf) {
        this.centre = new Vector(conf.getJSONObject("centre"), "centre");
        this.radius = conf.getDouble("radius");
        this.colour = new Colour(conf.getJSONObject("colour"), "col");
        this.material = new Material(conf.getJSONObject("material"));
    }

    private double getMin(double x, double y) {
        if (x<y)
            return x;
        return y;
    }

    public Vector getIntersection(Ray ray) {
        double a = 1.0;
        double b = 2.0 * ( ray.getDirection().getX()*(ray.getOrigin().getX() - centre.getX()) +
                ray.getDirection().getY()*(ray.getOrigin().getY() - centre.getY()) +
                ray.getDirection().getZ()*(ray.getOrigin().getZ() - centre.getZ()));
        double c = (ray.getOrigin().getX() - centre.getX())*(ray.getOrigin().getX() - centre.getX())
                + (ray.getOrigin().getY() - centre.getY())*(ray.getOrigin().getY() - centre.getY())
                + (ray.getOrigin().getZ() - centre.getZ())*(ray.getOrigin().getZ() - centre.getZ());
        c -= radius * radius;

        if ((b*b - 4*a*c) < 0.0)
            return null;

        double t0 = (-b + Math.sqrt(b*b - 4*a*c))/(2*a);
        double t1 = (-b - Math.sqrt(b*b - 4*a*c))/(2*a);

        double t = getMin(t0,t1);

        return new Vector(ray.getOrigin().getX() + ray.getDirection().getX()*t,
                ray.getOrigin().getY() + ray.getDirection().getY()*t,
                ray.getOrigin().getZ() + ray.getDirection().getZ()*t);
    }

    public Vector getNormal(Vector intersection) {
        return Vector.unit(Vector.subtract(intersection, centre));
    }

    public Colour getColour() {
        return colour;
    }

    public Material getMaterial() {
        return material;
    }
}

