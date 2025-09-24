import java.util.Random;

public class ScrollArtV1 {
    // ANSI color codes
    static final String RESET = "\u001B[0m";
    static final String WHITE = "\u001B[37m";
    static final String CYAN = "\u001B[36m";
    static final String ORANGE = "\u001B[33m";
    static final String GREEN = "\u001B[32m";
    static final String BRIGHT_WHITE = "\u001B[97m";
    static final String BRIGHT_ORANGE = "\u001B[93m";
    
    // Inner class to hold character and color information
    static class ColoredChar {
        char character;
        String color;
        
        ColoredChar(char character, String color) {
            this.character = character;
            this.color = color;
        }
        
        @Override
        public String toString() {
            return color + character + RESET;
        }
    }
    
    static final int WIDTH = getTerminalWidth() - 1;
    static final int BUNNY_WIDTH = 8;
    static final int BUNNY_HEIGHT = 6;
    static final Random rand = new Random();

    public static void main(String[] args) throws InterruptedException {
        ColoredChar[][] nextRows = new ColoredChar[BUNNY_HEIGHT][WIDTH]; // store upcoming rows
        for (int i = 0; i < nextRows.length; i++) {
            nextRows[i] = emptyRow();
        }

        while (true) {
            for (int x = 0; x < WIDTH - BUNNY_WIDTH; x++) {
                if (rand.nextDouble() < 0.01) {
                    ColoredChar[][] img;
                    if (rand.nextDouble() < 0.5)
                        img = getBunny();
                    else {
                        img = getCarrot();
                    }
                    for (int iy = 0; iy < BUNNY_HEIGHT; iy++) {
                        for (int ix = 0; ix < BUNNY_WIDTH; ix++) {
                            nextRows[iy][x + ix] = img[iy][ix];
                        }
                    }
                }
            }

            // Print and remove the top row with colors
            printColoredRow(nextRows[0]);
            // Shift all rows up
            shiftRowsUp(nextRows);
            Thread.sleep(40); // Delay in ms
        }
    }

    static void shiftRowsUp(ColoredChar[][] nextRows) {
        for (int i = 1; i < nextRows.length; i++) {
            nextRows[i - 1] = nextRows[i];
        }
        nextRows[nextRows.length - 1] = emptyRow();
    }

    static ColoredChar[] emptyRow() {
        ColoredChar[] row = new ColoredChar[WIDTH];
        for (int i = 0; i < WIDTH; i++) {
            row[i] = new ColoredChar(' ', WHITE);
        }
        return row;
    }
    
    static void printColoredRow(ColoredChar[] row) {
        StringBuilder sb = new StringBuilder();
        for (ColoredChar coloredChar : row) {
            sb.append(coloredChar.toString());
        }
        System.out.println(sb.toString());
    }

    // rename your function here
    static char[][] getBunny() {
        char[][] img = new char[BUNNY_HEIGHT][BUNNY_WIDTH];
        // fill with empty space
        for (int y = 0; y < BUNNY_HEIGHT; y++) {
            for (int x = 0; x < BUNNY_WIDTH; x++) {
                img[y][x] = ' ';
            }
        }
        // then fill individual characters
        img[0][0] = '(';
        img[0][1] = '\\';
        img[0][2] = '(';
        img[0][3] = '\\';
        img[1][0] = '(';
        img[1][1] = '-';
        img[1][2] = '.';
        img[1][3] = '-';
        img[1][4] = ')';
        img[2][0] = 'o';
        img[2][1] = '_';
        img[2][2] = '(';
        img[2][3] = '"';
        img[2][4] = ')';
        img[2][5] = '(';
        img[2][6] = '"';
        img[2][7] = ')';
        img[3][1] = '/';
        img[3][4] = '\\';
        img[4][1] = '\\';
        img[4][2] = '\\';
        img[4][3] = '/';
        img[4][4] = '/';
        img[5][2] = '\\';
        img[5][3] = '/';

        return img;
    }

    static char[][] getCarrot() {
        char[][] img = new char[BUNNY_HEIGHT][BUNNY_WIDTH];
        // fill with empty space
        for (int y = 0; y < BUNNY_HEIGHT; y++) {
            for (int x = 0; x < BUNNY_WIDTH; x++) {
                img[y][x] = ' ';
            }
        }
        // then fill individual characters
        img[0][5] = '\\';
        img[0][6] = ')';
        img[0][7] = '/';
        img[1][4] = '-';
        img[1][5] = '-';
        img[1][6] = 'v';

        img[2][3] = '/';
        img[2][4] = 'r';
        img[2][6] = ')';

        img[3][2] = '>';
        img[3][3] = 'r';
        img[3][4] = '.';
        img[3][5] = '/';

        img[4][1] = '/';
        img[4][3] = '\'';

        img[5][0] = 'c';
        img[5][1] = '_';
        img[5][2] = '/';

        return img;
    }

    public static int getTerminalWidth() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return getUnixTerminalWidth();
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

}