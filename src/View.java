import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
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
        this.eye = new Vector(0.0, 0.0, -eyeDist);

        this.screenHeight = root.getInt("rows");
        this.screenWidth = root.getInt("columns");

        JSONArray entities = root.getJSONArray("entities");
        for (int i = 0; i < entities.length(); i++) {
            JSONObject entity = entities.getJSONObject(i);
            if (entity.has("sphere")) {
                this.entities.add(new Sphere(entity));
            }
        }
    }
}
