import static java.lang.System.out;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;

public class ClassFinder {

    public static void main(String[] args) {
        try {
            readAllLines(get(args[0])).stream()
                    .map(String::trim)
                    .filter(l -> isSuitable(l, args[1]))
                    .forEach(out::println);
        } catch (Exception ex) {
            out.println(ex.getMessage());
        }
    }

    private static boolean isSuitable(String line, String pattern) {


        int lastDotIndex = line.lastIndexOf('.');
        String className = lastDotIndex == -1 ? line : line.substring(lastDotIndex + 1);
        return true;
    }
}
