/**
 * Created by Nikhil on 27/01/17.
 */
interface Entity {
    double getTimeIntersection(Ray ray);
    Vector getIntersection(Ray ray, double time);
    Vector getNormal(Vector intersection);
    Ray getRefractedRay(Ray ray, Vector intersection, Vector normal);
    Colour getColour();
    Material getMaterial();
}
