/**
 * Created by Nikhil on 27/01/17.
 */
class VCS {
    private Vector viewReference;

    private Vector u;
    private Vector v;
    private Vector n;

    VCS(Vector viewReference, Vector normal, Vector up) {
        this.viewReference = viewReference;

        this.n = Vector.unit(normal);
        up = Vector.subtract(up, Vector.scale(Vector.dot(up, this.n), this.n));
        this.v = Vector.unit(up);
        this.u = Vector.cross(this.v, this.n);
    }

    Vector convertVCStoWCS(Vector p) {
        double newX = (p.getX() * u.getX()) + (p.getY() * v.getX()) + (p.getZ() * n.getX());
        double newY = (p.getX() * u.getY()) + (p.getY() * v.getY()) + (p.getZ() * n.getY());
        double newZ = (p.getX() * u.getZ()) + (p.getY() * v.getZ()) + (p.getZ() * n.getZ());

        newX += viewReference.getX();
        newY += viewReference.getY();
        newZ += viewReference.getZ();

        return new Vector(newX, newY, newZ);
    }

    Vector convertWCStoVCS(Vector p) {
        Vector diffVector = Vector.subtract(p, viewReference);

        double newX = Vector.dot(diffVector, u);
        double newY = Vector.dot(diffVector, v);
        double newZ = Vector.dot(diffVector, n);

        return new Vector(newX, newY, newZ);
    }
}
