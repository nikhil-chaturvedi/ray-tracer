import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
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
        }
    }

    void render(String filename) throws IOException {
        BufferedImage img = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        int red = 255 << 16;
        for(int i = 0; i < screenWidth; i++) {
            for(int j = 0; j < screenHeight; j++) {
                Vector rayDir = vcs.convertVCStoWCS(new Vector((double)(i-screenWidth/2), (double)(j-screenHeight/2), 0.0));
                rayDir = Vector.unit(Vector.subtract(rayDir, eye));
                if (i == 0 && j == 0)
                    rayDir.print();
                Ray ray = new Ray(eye, rayDir, 1.0);

                for(Entity entity : entities) {
                    if (entity.getIntersection(ray) != null) {
                        img.setRGB(i, j, red);
                    }
                }
            }
        }
        ImageIO.write(img, "png", new File(filename));
    }
}
