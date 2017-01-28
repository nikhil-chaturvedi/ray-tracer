/**
 * Created by Nikhil on 27/01/17.
 */
interface Entity {
    Vector getIntersection(Ray ray);
    Vector getNormal(Vector intersection);
    Colour getColour();
    Material getMaterial();
}
