import java.io.IOException;

/**
 * Created by Nikhil on 27/01/17.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        View view = new View("config.json");
        view.render("view.png", 1);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
}
