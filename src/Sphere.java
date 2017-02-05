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

    public double getTimeIntersection(Ray ray) {
        double a = 1.0;
        double b = 2.0 * ( ray.getDirection().getX()*(ray.getOrigin().getX() - centre.getX()) +
                ray.getDirection().getY()*(ray.getOrigin().getY() - centre.getY()) +
                ray.getDirection().getZ()*(ray.getOrigin().getZ() - centre.getZ()));
        double c = (ray.getOrigin().getX() - centre.getX())*(ray.getOrigin().getX() - centre.getX())
                + (ray.getOrigin().getY() - centre.getY())*(ray.getOrigin().getY() - centre.getY())
                + (ray.getOrigin().getZ() - centre.getZ())*(ray.getOrigin().getZ() - centre.getZ());
        c -= radius * radius;

        if ((b*b - 4*a*c) < 0.0)
            return 0.0;

        double t0 = (-b + Math.sqrt(b*b - 4*a*c))/(2*a);
        double t1 = (-b - Math.sqrt(b*b - 4*a*c))/(2*a);

        final double EPSILON = 0.01;
        if (t0 < EPSILON && t1 < EPSILON)
            return 0.0;
        else if (t0 > EPSILON && t1 < EPSILON)
            return t0;
        else if (t0 < EPSILON && t1 > EPSILON)
            return t1;
        return Math.min(t0, t1);
    }

    public Vector getIntersection(Ray ray, double time) {
        return new Vector(ray.getOrigin().getX() + ray.getDirection().getX()*time,
                ray.getOrigin().getY() + ray.getDirection().getY()*time,
                ray.getOrigin().getZ() + ray.getDirection().getZ()*time);
    }

    public Ray getRefractedRay (Ray ray, Vector intersection, Vector normal) {
        Ray refractRay = refractAtSurface(ray, intersection, normal, material.getRefractiveIndex());

        double timeOfLeave = getTimeIntersection(refractRay);
        Vector pointOfLeave = getIntersection(refractRay, timeOfLeave);
        Vector normalAtLeave = Vector.scale(-1.0, getNormal(pointOfLeave));

        return refractAtSurface(refractRay, pointOfLeave, normalAtLeave, 1.0/material.getRefractiveIndex());
    }

    private Ray refractAtSurface (Ray incidentRay, Vector intersection, Vector normal, double refIndex) {
        double incidentAngle = Math.acos(-1.0 * Vector.dot(incidentRay.getDirection(), normal));
        double refractAngle = Math.asin(Math.sin(incidentAngle)/refIndex);

        Vector tempDir1 = Vector.add(incidentRay.getDirection(), Vector.scale(Math.cos(incidentAngle), normal));

        Vector tempDir2 = Vector.scale(1.0/refIndex, tempDir1);
        Vector tempDir3 = Vector.scale(Math.cos(refractAngle), normal);
        Vector refractDir = Vector.unit(Vector.subtract(tempDir2, tempDir3));

        return new Ray(intersection, refractDir);
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

