import java.util.ArrayList;

/**
 * Created by Nikhil on 05/02/17.
 */
class Tracer {
    private ArrayList<Entity> entities;
    private ArrayList<Light> lights;
    private Vector eye;

    public Tracer(ArrayList<Entity> entities, ArrayList<Light> lights, Vector eye) {
        this.entities = entities;
        this.lights = lights;
        this.eye = eye;
    }

    Colour trace(Ray ray, int depth) {
        if (depth == 0)
            return null;

        Entity intersectingEntity = findIntersectingEntity(ray, entities);

        if (intersectingEntity == null)
            return null;

        double timeOfIntersection = intersectingEntity.getTimeIntersection(ray);
        Vector intersection = intersectingEntity.getIntersection(ray, timeOfIntersection);
        Vector normal = intersectingEntity.getNormal(intersection);

        Colour colour = null;
        for(Light light : lights) {
            if(depth==4) {
                Vector shadow_vector = Vector.unit(Vector.subtract(light.getPosition(), intersection));
                Ray shadow_ray = new Ray(intersection, shadow_vector);
                ArrayList<Entity> list_without_first_object = new ArrayList<Entity>(entities);
                list_without_first_object.remove(intersectingEntity);
                Entity obstruction = findIntersectingEntity(shadow_ray, list_without_first_object);
                if (obstruction != null) {
                    colour = Colour.add(colour, light.getAmbientColour(intersectingEntity));
                    //System.out.println("Shadow detected");
                }
            }

                colour = Colour.add(colour, light.getColour(intersectingEntity, intersection, normal, eye));
        }

        if (intersectingEntity.getMaterial().getReflectiveCoeff() > 0.01) {
            Vector reflectDir = Vector.scale(-2.0 * Vector.dot(ray.getDirection(), normal), normal);
            reflectDir = Vector.unit(Vector.add(reflectDir, ray.getDirection()));
            Ray reflectRay = new Ray(intersection, reflectDir);

            Colour reflectColour = trace(reflectRay, depth - 1);

            if (reflectColour == null)
                reflectColour = new Colour(0, 0, 0);

            reflectColour = Colour.scale(intersectingEntity.getMaterial().getReflectiveCoeff(), reflectColour);
            colour = Colour.add(colour, reflectColour);
        }

        if (intersectingEntity.getMaterial().getRefractiveCoeff() > 0.01) {
            Ray refractRay = intersectingEntity.getRefractedRay(ray, intersection, normal);

            Colour refractColour = trace(refractRay, depth - 1);

            if (refractColour == null)
                refractColour = new Colour(0, 0, 0);

            refractColour = Colour.scale(intersectingEntity.getMaterial().getRefractiveCoeff(), refractColour);
            colour = Colour.add(colour, refractColour);
        }

        return colour;
    }

    private Entity findIntersectingEntity(Ray ray, ArrayList<Entity> entities) {
        Entity intersectingEntity = null;
        double minTimeIntersection = Double.MAX_VALUE;

        for (Entity entity : entities) {
            double timeOfIntersection = entity.getTimeIntersection(ray);
            if (timeOfIntersection > 0.0 && timeOfIntersection < minTimeIntersection) {
                intersectingEntity = entity;
                minTimeIntersection = timeOfIntersection;
            }
        }

        return intersectingEntity;
    }
}