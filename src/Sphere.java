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

    public double getIntersection(Ray ray) {
        return 0.0;
    }
}
