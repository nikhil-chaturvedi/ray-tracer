import java.io.IOException;

/**
 * Created by Nikhil on 27/01/17.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        View view = new View(args[1]);
        view.render(args[2], 3);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
}
