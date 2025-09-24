import java.util.Random;

public class ScrollArtV1Pattern {
    static final int WIDTH = getTerminalWidth() - 1;
    static final Random rand = new Random();
    // Makes NUM_PATTERNS patterns of straight lines moving either left or right 
    // Adds a new line at random times that branches off an existing line
    static final int NUM_PATTERNS = 6;
    static Integer[] xCols = new Integer[NUM_PATTERNS];
    static int[] dirs = new int[NUM_PATTERNS]; // 1 for going right or -1 for going left
    static final String[] LIST_SYMBOLS = new String[]{"*", "x", "o", "@", "#"};
    static String[] symbols = new String[NUM_PATTERNS]; // could be different symbols


    public static void main(String[] args) throws InterruptedException {

        xCols[0] = 0; // start first pattern at left edge
        dirs[0] = 1; // start moving right
        symbols[0] = "*"; // default symbol
        for (int i = 1; i < NUM_PATTERNS; i++) {
            xCols[i] = null;
        }
        int timeSinceLast = 0;

        while (true) {
            timeSinceLast++;
            // maybe add a new pattern
            if (rand.nextDouble() < .1 * timeSinceLast) { // probability increases with time
                // find any null spot
                int i = getNullIndex(xCols);
                if (i != -1) {
                    startPattern(i);
                    timeSinceLast = 0;
                }
            }

            // move existing patterns
            movePatterns();
            // map the patterns to characters
            printRow();
            Thread.sleep(20); // Delay in ms
        }
    }

    public static void movePatterns() {
        for (int i = 0; i < NUM_PATTERNS; i++) {
            if (xCols[i] != null) {
                // move the pattern
                xCols[i] += dirs[i];
                // check for edge
                if (xCols[i] <= 0 || xCols[i] >= WIDTH) {
                    xCols[i] = null; // remove the pattern
                }
            }
        }
    }

    public static void printRow() {
        // map the patterns to characters
        String[] row = new String[WIDTH];
        for (int i = 0; i < WIDTH; i++) {
            row[i] = " ";
        }
        for (int i = 0; i < NUM_PATTERNS; i++) {
            if (xCols[i] != null) {
                row[xCols[i]] = symbols[i];
            }
        }
        System.out.println(String.join("", row));
    }

    public static int getNullIndex(Integer[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                return i;
            }
        }
        return -1; // no null found
    }

    public static void startPattern(int i) {
        int pattern = rand.nextInt(NUM_PATTERNS); // pick a random pattern to branch off
        if (xCols[pattern] != null) {
            xCols[i] = xCols[pattern];
            dirs[i] = dirs[pattern] * -1; // opposite direction
            symbols[i] = LIST_SYMBOLS[rand.nextInt(LIST_SYMBOLS.length)]; // random symbol
        }
    }

    public static int getTerminalWidth() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return getUnixTerminalWidth();
        } else if (os.contains("win")) {
            return getWindowsTerminalWidth();
        } else {
            return 80; // fallback for unknown OS
        }
    }

    private static int getUnixTerminalWidth() {
        try {
            // Try to get terminal size from environment variables first
            String columns = System.getenv("COLUMNS");
            if (columns != null && !columns.isEmpty()) {
                return Integer.parseInt(columns);
            }

            // Fallback to stty command
            ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", "stty size </dev/tty");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream()));
            String output = reader.readLine();
            if (output != null && !output.isEmpty()) {
                String[] parts = output.trim().split(" ");
                return Integer.parseInt(parts[1]); // columns
            }
        } catch (Exception ignored) {
            // Silently ignore errors and fall back to default
        }
        return 80; // fallback
    }

    private static int getWindowsTerminalWidth() {
        // Github Copilot (Claude Sonnet 4) attempted to get columns.
        // Did not work on parallels.
        try {
            // Try to get terminal size from environment variables first
            String columns = System.getenv("COLUMNS");
            if (columns != null && !columns.isEmpty()) {
                return Integer.parseInt(columns);
            }

            // Try using PowerShell with different approaches
            // Method 1: Try to get console window size directly
            ProcessBuilder pb = new ProcessBuilder("powershell", "-Command",
                    "[Console]::WindowWidth");
            // also (Get-Host).UI.RawUI.WindowSize.Width didn't work
            pb.redirectErrorStream(true);
            Process process = pb.start();
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream()));
            String output = reader.readLine();
            if (output != null && !output.isEmpty()) {
                try {
                    return Integer.parseInt(output.trim());
                } catch (NumberFormatException ignored) {
                    // Continue to next method if parsing fails
                }
            }

        } catch (Exception e) {
            // If PowerShell fails, try using cmd with mode command
            try {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "mode con");
                pb.redirectErrorStream(true);
                Process process = pb.start();
                java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("Columns:")) {
                        String[] parts = line.split(":");
                        if (parts.length > 1) {
                            try {
                                return Integer.parseInt(parts[1].trim());
                            } catch (NumberFormatException ignored) {
                                // Continue to fallback
                            }
                        }
                    }
                }
            } catch (Exception ignored) {
                // Silently ignore errors and fall back to default
            }
        }
        return 80; // fallback
    }

}