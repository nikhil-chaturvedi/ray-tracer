import org.ejml.simple.SimpleMatrix;
import org.json.JSONObject;

/**
 * Created by Nikhil on 27/01/17.
 */
class Vector {
    private double x;
    private double y;
    private double z;

    public Vector() {
        this.x=0;
        this.y=0;
        this.z=0;
    }

    Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vector(Vector v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    Vector(JSONObject obj, String key) {
        this.x = obj.getDouble(key + "_x");
        this.y = obj.getDouble(key + "_y");
        this.z = obj.getDouble(key + "_z");
    }

    static double norm(Vector v) {
        return Math.sqrt((v.x * v.x) + (v.y * v.y) + (v.z * v.z));
    }

    static Vector scale(double scalar, Vector v) {
        return new Vector(scalar * v.x, scalar * v.y, scalar * v.z);
    }

    static Vector unit(Vector v) {
        return scale(1.0/norm(v), v);
    }

    static Vector add(Vector v1, Vector v2) {
        return new Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    static Vector subtract(Vector v1, Vector v2) {
        return new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    static double dot(Vector v1, Vector v2) {
        return ((v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z));
    }

    static Vector cross(Vector v1, Vector v2) {
        double x = (v1.y * v2.z) - (v1.z * v2.y);
        double y = (v1.z * v2.x) - (v1.x * v2.z);
        double z = (v1.x * v2.y) - (v1.y * v2.x);
        return new Vector(x, y, z);
    }

    static Vector transform(Vector v, SimpleMatrix transformation_matrix){
       SimpleMatrix vector1 = new SimpleMatrix(1,4);
       vector1.set(0,0,v.getX());
       vector1.set(0,1,v.getY());
       vector1.set(0,2,v.getZ());
       vector1.set(0,3,1.0);
       SimpleMatrix vector2 = vector1.mult(transformation_matrix);
       if(Math.abs(vector2.get(0,3) - 1.0) < 0.01){
           vector2.scale(1/vector2.get(0,3));
       }
       return new Vector(vector2.get(0,0), vector2.get(0, 1), vector2.get(0,2));
    }

    void print() {
        System.out.println(x + " " + y + " " + z);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

    double getZ() {
        return z;
    }
}


