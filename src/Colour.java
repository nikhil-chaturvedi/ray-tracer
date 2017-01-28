import org.json.JSONObject;

/**
 * Created by Nikhil on 28/01/17.
 */
class Colour {
    private int r;
    private int g;
    private int b;

    public Colour(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    Colour(Colour c) {
        this.r = c.r;
        this.g = c.g;
        this.b = c.b;
    }

    Colour(JSONObject obj, String key) {
        this.r = obj.getInt(key + "_r");
        this.g = obj.getInt(key + "_g");
        this.b = obj.getInt(key + "_b");
    }

    static Colour add(Colour c1, Colour c2) {
        return new Colour ((c1.r + c2.r)/2, (c1.g + c2.g)/2, (c1.b + c2.b)/2);
    }

    static Colour multiply(Colour c1, Colour c2) {
        return new Colour ((c1.r * c2.r)/255, (c1.g * c2.g)/255, (c1.b * c2.b)/255);
    }

    static Colour scale(double scalar, Colour c) {
        return new Colour((int)(scalar * (double)c.r), (int)(scalar * (double)c.g), (int)(scalar * (double)c.b));
    }

    int getColourCode() {
        return ((r << 16) + (g << 8) + b);
    }
}
