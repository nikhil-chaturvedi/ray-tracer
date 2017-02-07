import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Nikhil on 27/01/17.
 */
class View {
    private VCS vcs;
    private Vector eye;

    private int screenHeight;
    private int screenWidth;

    private ArrayList<Entity> entities;
    private ArrayList<Light> lights;
    private Colour ambient;

    View(String configFileName) throws FileNotFoundException {
        String jsonConfig = new Scanner(new File(configFileName)).useDelimiter("\\Z").next();
        JSONObject root = new JSONObject(jsonConfig);

        JSONObject viewRef = root.getJSONObject("view_reference");
        Vector viewReference = new Vector(viewRef, "ref");
        JSONObject viewPlane = root.getJSONObject("view_plane");
        Vector normal = new Vector(viewPlane, "normal");

        JSONObject upVector = root.getJSONObject("up_vector");
        Vector up = new Vector(upVector, "up");

        this.vcs = new VCS(viewReference, normal, up);

        double eyeDist = root.getDouble("eye");
        this.eye = this.vcs.convertVCStoWCS(new Vector(0.0, 0.0, -eyeDist));

        this.screenHeight = root.getInt("rows");
        this.screenWidth = root.getInt("columns");

        this.entities = new ArrayList<>();
        JSONArray entities = root.getJSONArray("entities");
        for (int i = 0; i < entities.length(); i++) {
            JSONObject entity = entities.getJSONObject(i);
            if (entity.has("sphere")) {
                this.entities.add(new Sphere(entity.getJSONObject("sphere")));
            }
            if (entity.has("polygon")) {
                this.entities.add(new Polygon(entity.getJSONObject("polygon")));
            }
            if(entity.has("plane")) {
               this.entities.add(new Plane(entity.getJSONObject("plane")));
            }
        }

        this.ambient = new Colour(root.getJSONObject("ambient"), "col");

        this.lights = new ArrayList<>();
        JSONArray lights = root.getJSONArray("lights");
        for (int i = 0; i < lights.length(); i++) {
            JSONObject light = lights.getJSONObject(i);
            this.lights.add(new Light(light));
        }
    }

    void render(String filename, int sampleSize) throws IOException {
        int screenWidth = sampleSize * this.screenWidth;
        int screenHeight = sampleSize * this.screenHeight;

        BufferedImage img = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);

        double wl = -this.screenWidth/2, wr = this.screenWidth/2;
        double wt = this.screenHeight/2, wb = -this.screenHeight/2;
        double delU = (wr - wl)/(double)screenWidth;
        double delV = (wt - wb)/(double)screenHeight;

        for(int i = 0; i < screenWidth; i++) {
            for(int j = 0; j < screenHeight; j++) {
                Vector rayDir = vcs.convertVCStoWCS(new Vector(wl + (delU * i), wt - (delV * j), 0.0));
                rayDir = Vector.unit(Vector.subtract(rayDir, eye));
                Ray ray = new Ray(eye, rayDir);

                Tracer tracer = new Tracer(entities, lights, ambient, eye);
                Colour colour = tracer.trace(ray, 4);

                if (colour == null)
                    continue;

                img.setRGB(i, j, colour.getColourCode());
            }
        }

        img = antiAlias(img, sampleSize);

        ImageIO.write(img, "png", new File(filename));
    }

    private BufferedImage antiAlias(BufferedImage img, int sampleSize) {
        BufferedImage newImg = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < screenWidth; i++) {
            for (int j = 0; j < screenHeight; j++) {
                Colour colour = null;
                for (int p = 0; p < sampleSize; p++) {
                    for (int q = 0; q < sampleSize; q++) {
                        int x = sampleSize * i + p;
                        int y = sampleSize * j + q;
                        int newColour = img.getRGB(x, y);
                        int blue = newColour & 255;
                        int green = (newColour >> 8) & 255;
                        int red = (newColour >> 16) & 255;
                        colour = Colour.add(colour, new Colour(red, green, blue));
                    }
                }
                newImg.setRGB(i, j, colour.getColourCode());
            }
        }
        return newImg;
    }
}
