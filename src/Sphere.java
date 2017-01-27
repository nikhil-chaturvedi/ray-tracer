import org.json.JSONObject;

/**
 * Created by Nikhil on 27/01/17.
 */
class Sphere implements Entity {
    private Vector centre;
    private double radius;

    Sphere(JSONObject conf) {
        this.centre = new Vector(conf, "centre");
        this.radius = conf.getDouble("radius");
    }

    public double getMin(int x, int y) {
        if (x<y)
            return x;
        return y;
    }

    public Vector getIntersectionPoint(Ray ray) {
        double a = 1.0;
        double b = 2( ray.getDirection().getX()*(ray.getOrigin().getX() - center.getX()) +
                ray.getDirection().getY()*(ray.getOrigin().getY() - center.getY()) +
                ray.getDirection().getZ()*(ray.getOrigin().getZ() - center.getZ()) );
        double c = (ray.getOrigin().getX() - center.getX())*(ray.getOrigin().getX() - center.getX())
                + (ray.getOrigin().getY() - center.getY())*(ray.getOrigin().getY() - center.getY())
                + (ray.getOrigin().getZ() - center.getZ())*(ray.getOrigin().getZ() - center.getZ()) ;
        double t0 = (-b + Math.sqrt(b*b - 4*a*c))/(2*a);
        double t1 = (-b - Math.sqrt(b*b - 4*a*c))/(2*a);

        double t = getMin(t0,t1);

        Vector intersection = new Vector(ray.getOrigin().getX() + ray.getDirection().getX()*t,
                ray.getOrigin().getY() + ray.getDirection().getY()*t,
                ray.getOrigin().getZ() + ray.getDirection().getZ()*t);
        return intersection;
    }

    public Vector getNormal(Ray ray) {
        return new Vector ( (getIntersection(ray) - center.getX())/radius,
                (getIntersection(ray) - center.getY())/radius,
                (getIntersection(ray) - center.getZ())/radius );
    },
}

