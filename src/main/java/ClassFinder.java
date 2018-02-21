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

        if (args.length != 2) {
            out.printf("Wrong number of input parameters: expected 2, got %d\n", args.length);
            return;
        }

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

    enum State {
        lookup,
        check,
        found
    }

    private static State FSM;

    // while process wildcard, there are three states:
    // - when we've found substr which matches the chars following wc - we keep checking it (State.check)
    // - when we've not found such one - we try to find it until reach the end of the line (State.lookup)
    // - when we've found the whole piece of substr and came to the new wc (State.found)
    //

    private static boolean matchesByCapitalOrSmallLetters(String line, String pattern) {

        int line_length = line.length();
        int pattern_length = pattern.length();

        int offset = 0;
        int line_index = 0;
        int pattern_index = 0;

        FSM = State.lookup;

        while (pattern_index < pattern_length && line_index < line_length) {

            if (FSM == State.lookup) {
                if (line.charAt(line_index) == pattern.charAt(pattern_index)) {

                    if (line_index + 1 < line_length) {
                        FSM = State.check;
                        offset++;
                    } else {
                        return false;
                    }
                }
                line_index++;
                continue;
            }

            if (FSM == State.check) {
                if (pattern.charAt(pattern_index + offset) == '*') {
                    if (pattern_length >= pattern_index + offset + 1) {
                        FSM = State.lookup;
                        pattern_index += offset + 1;
                    } else  {
                        return true;
                    }
                }
                if (line_index + 1 < line_length) {
                    if (line.charAt(line_index + 1) == pattern.charAt(pattern_index + offset)) {
                        offset++;
                    } else {
                        FSM = State.lookup;
                    }
                    line_index++;
                } else {
                    return false;
                }
            }
        }

        return false;
    }
}
