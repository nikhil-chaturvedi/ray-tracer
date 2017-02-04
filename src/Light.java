import org.json.JSONObject;

/**
 * Created by Nikhil on 28/01/17.
 */
class Light {
    private Colour colour;
    private Vector position;

    Light(JSONObject light) {
        this.colour = new Colour(light.getJSONObject("colour"), "col");
        this.position = new Vector(light.getJSONObject("position"), "pos");
    }

    Colour getColour(Entity entity, Vector intersection, Vector normal, Vector eye) {
        Colour ambientColour = getAmbientColour(entity);
        Colour diffuseColour = getDiffuseColour(entity, intersection, normal);
        Colour specularColour = getSpecularColour(entity, intersection, normal, eye);
        //return specularColour;
        return Colour.add(ambientColour, Colour.add(diffuseColour, specularColour));
    }

    private Colour getAmbientColour(Entity entity) {
        Colour multColour = Colour.multiply(this.colour, entity.getColour());
        return Colour.scale(entity.getMaterial().getAmbientCoeff(), multColour);
    }

    private Colour getDiffuseColour(Entity entity, Vector intersection, Vector normal) {
        Vector lightVector = Vector.unit(Vector.subtract(this.position, intersection));

        double coeff = Vector.dot(normal, lightVector);
        if (coeff < 0.0)
            return new Colour(0,0,0);
        coeff *= entity.getMaterial().getDiffuseCoeff();

        Colour multColour = Colour.multiply(this.colour, entity.getColour());
        return Colour.scale(coeff, multColour);
    }

    private Colour getSpecularColour(Entity entity, Vector intersection, Vector normal, Vector eye) {
        Vector lightVector = Vector.unit(Vector.subtract(this.position, intersection));

        Vector reflectVector = Vector.scale(2.0 * Vector.dot(lightVector, normal), normal);
        reflectVector = Vector.unit(Vector.subtract(reflectVector, lightVector));

        Vector viewVector = Vector.unit(Vector.subtract(eye, intersection));

        double coeff = Vector.dot(reflectVector, viewVector);
        if (coeff < 0.0)
            return new Colour(0,0,0);
        coeff = Math.pow(coeff, entity.getMaterial().getRoughness());
        coeff *= entity.getMaterial().getSpecularCoeff();

        Colour multColour = Colour.multiply(this.colour, entity.getColour());
        return Colour.scale(coeff, multColour);
    }
}
