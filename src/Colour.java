import org.json.JSONObject;

/**
 * Created by Nikhil on 28/01/17.
 */
class Colour {
    private int r;
    private int g;
    private int b;
    private int numColours;

    public Colour(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.numColours = 1;
    }

    public Colour(int r, int g, int b, int numColours) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.numColours = numColours;
    }

    Colour(Colour c) {
        this.r = c.r;
        this.g = c.g;
        this.b = c.b;
        this.numColours = 1;
    }

    Colour(JSONObject obj, String key) {
        this.r = obj.getInt(key + "_r");
        this.g = obj.getInt(key + "_g");
        this.b = obj.getInt(key + "_b");
        this.numColours = 1;
    }

    static Colour add(Colour c1, Colour c2) {
        if (c1 == null)
            return c2;
        if (c2 == null)
            return c1;
        return new Colour (c1.r + c2.r, c1.g + c2.g, c1.b + c2.b, c1.numColours + c2.numColours);
    }

    static Colour multiply(Colour c1, Colour c2) {
        return new Colour ( ((c1.r/c1.numColours) * (c2.r/c2.numColours))/255,
                            ((c1.g/c1.numColours) * (c2.g/c2.numColours))/255,
                            ((c1.b/c1.numColours) * (c2.b/c2.numColours))/255 );
    }

    static Colour scale(double scalar, Colour c) {
        return new Colour(  (int)(scalar * (double)(c.r/c.numColours)),
                            (int)(scalar * (double)(c.g/c.numColours)),
                            (int)(scalar * (double)(c.b/c.numColours)) );
    }

    int getColourCode() {
        return ((r/numColours << 16) + (g/numColours << 8) + b/numColours);
    }
}
