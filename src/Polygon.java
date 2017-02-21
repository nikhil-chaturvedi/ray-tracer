import java.util.Arrays;

import org.ejml.simple.SimpleMatrix;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Created by Kartikeya Sharma on 28-01-2017.
 */
public class Polygon implements Entity{
    private int npoints;
    private Vector points[];
    private static final int MIN_LENGTH = 4;
    private static final double EPSILON = 0.01;
    private Material material;
    private Colour colour;
    private Vector normal;
    private boolean isTransformed;
    private SimpleMatrix transformation_matrix;

    @Override
    public Colour getColour() {
        return colour;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    public int getNpoints() {
        return npoints;
    }

    public Vector[] getPoints() {
        return points;
    }

    public boolean isTransformed() {
        return isTransformed;
    }


    Polygon (JSONObject conf) {
        this.npoints = conf.getInt("npoints");
        this.points = new Vector[this.npoints];
        JSONObject pointslist = conf.getJSONObject("points");
        for(int i=1; i<= npoints ; i++) {
            JSONObject point = pointslist.getJSONObject("points_" + Integer.toString(i));
            Vector temp = new Vector(point, "");
            this.points[i-1] = temp;
        }
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
                    //System.out.print(transformation[i][j] + " ");
                }
               // System.out.println();
            }
            this.transformation_matrix = new SimpleMatrix(transformation);
        }
        else
            this.transformation_matrix = null;
    }

    public Polygon() {
        points = new Vector[MIN_LENGTH];
    }

    public Polygon(Vector points[], int npoints) {

        if (npoints > points.length ) {
            throw new IndexOutOfBoundsException("npoints > npoints.length ");
        }

        if (npoints < 0) {
            throw new NegativeArraySizeException("npoints < 0");
        }

        this.npoints = npoints;
        this.points = Arrays.copyOf(points, npoints);
    }
    //might come in handy?
    public void reset() {
        npoints = 0;
    }

    public void translate(int deltaX, int deltaY, int deltaZ) {
        for (int i = 0; i < npoints; i++) {
            points[i].setX(points[i].getX() + deltaX*1.0);
            points[i].setY(points[i].getY() + deltaY*1.0);
            points[i].setZ(points[i].getZ() + deltaZ*1.0);
        }
    }

    public boolean isInside(Vector q, Polygon P) {
        int i;
        double m1, m2;
        double anglesum =0;
        double costheta;
        Vector v1 = new Vector();
        Vector v2 = new Vector();
        Vector vertexList[] = P.getPoints();
        int num_points = P.getNpoints();

        for(i=0; i<vertexList.length; i++) {
            v1.setX(vertexList[i].getX() - q.getX());
            v1.setY(vertexList[i].getY() - q.getY());
            v1.setZ(vertexList[i].getZ() - q.getZ());
            v2.setX(vertexList[(i+1)%num_points].getX() - q.getX());
            v2.setY(vertexList[(i+1)%num_points].getY() - q.getY());
            v2.setZ(vertexList[(i+1)%num_points].getZ() - q.getZ());

            m1 = Vector.norm(v1);
            m2 = Vector.norm(v2);

            if(m1*m2 <= EPSILON) //IN CASE POINT COINCIDES WITH VERTEX
                return true;
            else
                costheta = (Vector.dot(v1,v2)) / (m1*m2);
            anglesum+= Math.acos(costheta);
        }

        if(Math.abs(anglesum - 2*Math.PI) <= EPSILON)
            return true;
        return false;
    }

    public double getTimeIntersection(Ray ray) {
        Vector vertexlist[] = this.getPoints();

        Vector v1 = new Vector(vertexlist[1].getX() - vertexlist[0].getX(),
                vertexlist[1].getY() - vertexlist[0].getY(),
                vertexlist[1].getZ() - vertexlist[0].getZ());
        Vector v2 = new Vector(vertexlist[2].getX() - vertexlist[1].getX(),
                vertexlist[2].getY() - vertexlist[1].getY(),
                vertexlist[2].getZ() - vertexlist[1].getZ());

        Vector normal = Vector.scale(1/(Vector.norm(Vector.cross(v1,v2))), Vector.cross(v1,v2));

        Vector origin = ray.getOrigin();
        Vector direction = ray.getDirection();
        double D = - (Vector.dot(normal, vertexlist[0]));
        if(Math.abs(Vector.dot(normal, direction)) < 0.01)
            return 0;
        double t = - ((Vector.dot(normal, origin) + D)/(Vector.dot(normal, direction)));
        final double EPSILON = 0.01;
        if (t<EPSILON)
            return 0.0;
        Vector intersection = new Vector(origin.getX() + direction.getX() * t,
                origin.getY() + direction.getY() * t,
                origin.getZ() + direction.getZ() * t);

        if(isInside(intersection, this))
            return t;
        return 0.0;

    }

    public Vector getIntersection(Ray ray, double time) {
        return new Vector(ray.getOrigin().getX() + ray.getDirection().getX() * time,
                ray.getOrigin().getY() + ray.getDirection().getY() * time,
                ray.getOrigin().getZ() + ray.getDirection().getZ() * time);
    }

    //Normal at every point is same? (We only care for direction, this shouldn't matter, in any case check)
    public Vector getNormal (Vector intersection) {
        Vector vertexlist[] = this.getPoints();
        Vector v1 = new Vector(vertexlist[1].getX() - vertexlist[0].getX(),
                vertexlist[1].getY() - vertexlist[0].getY(),
                vertexlist[1].getZ() - vertexlist[0].getZ());
        Vector v2 = new Vector(vertexlist[2].getX() - vertexlist[1].getX(),
                vertexlist[2].getY() - vertexlist[1].getY(),
                vertexlist[2].getZ() - vertexlist[1].getZ());
        return Vector.scale(1/(Vector.norm(Vector.cross(v1,v2))), Vector.cross(v1,v2));
    }

    public Ray getRefractedRay (Ray ray, Vector intersection, Vector normal) {
        return ray;
    }

    public SimpleMatrix getTransformation() {
        return transformation_matrix;
    }
}
