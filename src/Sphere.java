import org.json.JSONObject;
import org.json.JSONArray;
import org.ejml.simple.SimpleMatrix;

/**
 * Created by Nikhil on 27/01/17.
 */
class Sphere implements Entity {
    private Vector centre;
    private double radius;

    private Colour colour;
    private Material material;
    private boolean isTransformed;
    private SimpleMatrix transformation_matrix;

    Sphere(JSONObject conf) {
        this.centre = new Vector(conf.getJSONObject("centre"), "centre");
        this.radius = conf.getDouble("radius");
        this.colour = new Colour(conf.getJSONObject("colour"), "col");
        this.material = new Material(conf.getJSONObject("material"));
        this.isTransformed = conf.getBoolean("isTransformed");
        if(isTransformed()) {

            double[][] transformation = new double[4][4];
            JSONArray transforms = conf.getJSONArray("transformation");
            for(int i=0; i<4; i++) {
                JSONArray row = transforms.getJSONArray(i);
                for(int j=0; j<4; j++) {
                    transformation[i][j] = row.getDouble(j);
                  //  System.out.print(transformation[i][j] + " ");
                }
                //System.out.println();
            }
            this.transformation_matrix = new SimpleMatrix(transformation);

        }
        else {
            this.transformation_matrix = null;
        }
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
        Vector intersection = new Vector(ray.getOrigin().getX() + ray.getDirection().getX() * time,
                ray.getOrigin().getY() + ray.getDirection().getY() * time,
                ray.getOrigin().getZ() + ray.getDirection().getZ() * time);
        if(isTransformed()) {
            return Vector.transform(intersection, transformation_matrix);
        }
        return intersection;
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
       Vector normal =  Vector.unit(Vector.subtract(intersection, centre));
       if(isTransformed()) {
           return Vector.transform(normal, transformation_matrix.invert().transpose());
       }
       return normal;
    }


    public Colour getColour() {
        return colour;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean isTransformed() {
        return isTransformed;
    }
}


