import java.io.IOException;

/**
 * Created by Nikhil on 27/01/17.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        View view = new View("config.json");
        view.render("view.png");
    }
}
