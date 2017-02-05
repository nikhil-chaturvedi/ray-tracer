import java.util.Arrays;

/**
 * Created by Kartikeya Sharma on 28-01-2017.
 */
public class Polygon implements Entity{
    private int npoints;
    private Vector points[];
    private static final int MIN_LENGTH = 4;
    private static final double EPSILON = 0.0000001;
    private Material material;
    private Colour colour;

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

    public void setNpoints(int npoints) {
        this.npoints = npoints;
    }

    public Vector[] getPoints() {
        return points;
    }

    public void setPoints(Vector[] points) {
        this.points = points;
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

        for(i=0; i<vertexList.length; i++) {
            v1.setX(vertexList[i].getX() - q.getX());
            v1.setY(vertexList[i].getY() - q.getY());
            v1.setZ(vertexList[i].getZ() - q.getZ());
            v1.setX(vertexList[(i+1)%P.getNpoints()].getX() - q.getX());
            v1.setY(vertexList[(i+1)%P.getNpoints()].getY() - q.getY());
            v1.setZ(vertexList[(i+1)%P.getNpoints()].getZ() - q.getZ());

            m1 = Vector.norm(v1);
            m2 = Vector.norm(v2);

            if(m1*m2 <= EPSILON)
                return true;
            else
                costheta = (Vector.dot(v1,v2)) / (m1*m2);
            anglesum+= Math.acos(costheta);
        }

        if(anglesum - 2*Math.PI <= EPSILON)
            return true;
        return false;
    }

    public double getTimeIntersection(Ray ray) {
        return 0.0;
    }

    public Vector getIntersection(Ray ray, double time) {
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
        double t = - ((Vector.dot(normal, origin) + D)/(Vector.dot(normal, direction)));

        return new Vector(origin.getX() + direction.getX()*t,
                            origin.getY() + direction.getY()*t,
                            origin.getZ() + direction.getZ()*t);
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
        Vector normal = Vector.scale(1/(Vector.norm(Vector.cross(v1,v2))), Vector.cross(v1,v2));
        return normal;
    }

    public Ray getRefractedRay (Ray ray, Vector intersection, Vector normal) {
        return ray;
    }
}
