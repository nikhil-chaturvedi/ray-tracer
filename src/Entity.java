/**
 * Created by Nikhil on 27/01/17.
 */
interface Entity {
    Vector getIntersection(Ray ray);
    Vector getNormal(Ray ray, Vector intersection);
    Colour getColour();
    Material getMaterial();
}
