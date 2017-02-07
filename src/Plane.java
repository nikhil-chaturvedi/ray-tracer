import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Kartikeya Sharma on 28-01-2017.
 */
public class Plane implements Entity {
    private Vector normal;
    private Vector point;
    private Material material;
    private Colour colour;
    private boolean isTransformed;
    private double[][] transformation;

    Plane(JSONObject conf) {
        this.normal = new Vector (conf.getJSONObject("normal"), "normal");
        this.point = new Vector(conf.getJSONObject("point"), "point");
        this.colour = new Colour(conf.getJSONObject("colour"), "col");
        this.material = new Material(conf.getJSONObject("material"));
        this.isTransformed = conf.getBoolean("isTransformed");
        if(isTransformed()) {
            this.transformation = new double[4][4];
            JSONArray transforms = conf.getJSONArray("transformation");
            for(int i=0; i<4; i++) {
                JSONArray row = transforms.getJSONArray(i);
                for(int j=0; j<4; j++) {
                    this.transformation[i][j] = row.getDouble(j);
                }
            }
        }
        this.transformation = null;

    }

    public Plane(Vector normal, Vector point) {
        this.normal = normal;
        this.point = point;
    }

    public Vector getNormal(Vector intersection) {

        if(isTransformed()) {
            return Vector.transform(normal, Vector.transpose(Vector.invert(getTransformation())));
        }
        return normal;
    }

    public Vector getPoint() {
        return point;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public Colour getColour() {
        return colour;
    }


    public double[][] getTransformation() {
        return transformation;
    }

    public boolean isTransformed() {
        return isTransformed;
    }

    public double getTimeIntersection (Ray ray){
        Vector origin = ray.getOrigin();
        Vector direction = ray.getDirection();
        double D = - (Vector.dot(normal, point));
        if(Math.abs(Vector.dot(normal, direction)) < 0.01)
            return 0;
        double t = - ((Vector.dot(normal, origin) + D)/(Vector.dot(normal, direction)));
        final double EPSILON = 0.01;
        if (t<EPSILON)
            return 0.0;
        return t;
    }

    public Vector getIntersection (Ray ray, double time) {
        Vector intersection = new Vector(ray.getOrigin().getX() + ray.getDirection().getX() * time,
                ray.getOrigin().getY() + ray.getDirection().getY() * time,
                ray.getOrigin().getZ() + ray.getDirection().getZ() * time);
       if(isTransformed()) {
            return Vector.transform(intersection, getTransformation());
        }
        return intersection;
    }

    public Ray getRefractedRay (Ray ray, Vector intersection, Vector normal) {
        return ray;
    }
}

