import static java.lang.System.out;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;

public class ClassFinder {

    enum Type {
        isCapital,
        isSmall,
        isMixed
    }

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

    private static Type getType(String pattern) {
        if (pattern.equals(pattern.toLowerCase())) {
            return Type.isSmall;
        }
        if (pattern.equals(pattern.toUpperCase())) {
            return Type.isCapital;
        }
        return Type.isMixed;
    }

    private static boolean isSuitable(String line, String pattern) {

        int lastDotIndex = line.lastIndexOf('.');
        String className = lastDotIndex == -1 ? line : line.substring(lastDotIndex + 1);

        lastDotIndex = pattern.lastIndexOf('.');
        String patternClassName = lastDotIndex == -1 ? pattern : pattern.substring(lastDotIndex + 1);

        switch (getType(patternClassName)) {
            case isMixed:
                out.println("Implement me!");
            case isSmall:
                return matchesByCapitalOrSmallLetters(className.toLowerCase(), patternClassName);
            case isCapital: {
                return matchesByCapitalOrSmallLetters(className, patternClassName);
            }
            default:
                return true;
        }
    }

    private static boolean matchesByCapitalOrSmallLetters(String line, String pattern) {

        int j = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == pattern.charAt(j)) {
                if (j == pattern.length()-1) {
                    return true;
                }
                j++;
            }
        }
        return false;
    }
}
