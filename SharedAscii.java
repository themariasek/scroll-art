import java.util.Random;

public class SharedAscii {
    static final int WIDTH = getTerminalWidth() - 1;
    static final int BUNNY_WIDTH = 8;
    static final int BUNNY_HEIGHT = 6;
    static final Random rand = new Random();

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        int iterations = 0;
        char[][] nextRows = new char[44][WIDTH];
        for (int i = 0; i < nextRows.length; i++) {
            nextRows[i] = emptyRow();
        }

        char[] prevRow = nextRows[0];
        int spawnCooldown = 0;
        while (true) {
            if (spawnCooldown > 0) {
                spawnCooldown--;
            }
            int offset = rand.nextInt(20) + 10;
            for (int x = offset; x < WIDTH - 34; x += 34) {
                if (spawnCooldown == 0 && isBlank(prevRow, x) && rand.nextDouble() < 0.2) {
                    if (canPlace(nextRows, x)) {
                        loadNextRowsWithImage(nextRows, x);
                        spawnCooldown = 3;
                    }
                }
            }
            System.out.println(new String(nextRows[0]));
            prevRow = nextRows[0];
            shiftRowsUp(nextRows);
            Thread.sleep(100);
            long time = System.currentTimeMillis() - startTime;
            iterations++;
        }
    }

    private static boolean isBlank(char[] prevRow, int x) {
        for (int i = x; i < x + 34 && i < prevRow.length; i++) {
            if (prevRow[i] != ' ') {
                return false;
            }
        }
        return true;
    }

    private static boolean canPlace(char[][] nextRows, int x) {
        int maxHeight = 8;
        int maxWidth = 12;
        if (x + maxWidth > WIDTH) {
            return false;
        }
        for (int y = 0; y < maxHeight && y < nextRows.length; y++) {
            for (int ix = x; ix < x + maxWidth; ix++) {
                if (nextRows[y][ix] != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private static void loadNextRowsWithImage(char[][] nextRows, int x) {
        String llama = """
                /^---^\\
                | . . |
                \\  `  /   MS
                /=====\\________
                /               \\[]
                \\               /
                ---------------
                | || |   | || |
                [|][|]   [|][|]
                """;
        AsciiArt bearArt = new AsciiArt(getBear());
        AsciiArt kumaArt = new AsciiArt(getKuma());
        AsciiArt llamaArt = new AsciiArt(llama);
        AsciiArt yeppiArt = new AsciiArt(getYeppi());

        double r = rand.nextDouble();
        AsciiArt art;
        if (r < 0.27) {
            art = llamaArt;
        } else if (r < 0.54) {
            art = bearArt;
        } else if (r < 0.8) {
            art = kumaArt;
        } else {
            art = yeppiArt;
        }

        int xOffset = x + rand.nextInt(10) - 5;
        if (xOffset < 0) xOffset = 0;
        if (xOffset + art.width > WIDTH) xOffset = WIDTH - art.width;

        if (art.img == getYeppi()) {
            int yeppiWidth = art.width;
            int yeppiHeight = art.height;
            if (xOffset + yeppiWidth > WIDTH) return;
            for (int y = 0; y < yeppiHeight && y < nextRows.length; y++) {
                for (int ix = xOffset; ix < xOffset + yeppiWidth; ix++) {
                    if (nextRows[y][ix] != ' ') return;
                }
            }
        }

        for (int iy = 0; iy < art.height && iy < nextRows.length; iy++) {
            for (int ix = 0; ix < art.width && (xOffset + ix) < WIDTH; ix++) {
                nextRows[iy][xOffset + ix] = art.img[iy][ix];
            }
        }
    }

    static void shiftRowsUp(char[][] nextRows) {
        for (int i = 1; i < nextRows.length; i++) {
            nextRows[i - 1] = nextRows[i];
        }
        nextRows[nextRows.length - 1] = emptyRow();
    }

    static char[] emptyRow() {
        char[] row = new char[WIDTH];
        for (int i = 0; i < WIDTH; i++) {
            row[i] = ' ';
        }
        return row;
    }

    static char[][] getYeppi() {
        char[][] img = new char[32][34];
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 34; x++) {
                img[y][x] = ' ';
            }
        }
        img[0][1] = '.';
        img[0][2] = '_';
        img[0][3] = '_';
        img[0][25] = '.';
        img[0][26] = '^';
        img[0][27] = '^';

        img[1][1] = '/';
        img[1][2] = '"';
        img[1][5] = '"';
        img[1][6] = ';';
        img[1][25] = '|';
        img[1][29] = '"';
        img[1][30] = ')';

        img[2][0] = '.';
        img[2][7] = '\\';
        img[2][24] = '/';
        img[2][25] = '"';
        img[2][28] = 'X';
        img[2][30] = '?';

        img[3][0] = '|';
        img[3][1] = '"';
        img[3][3] = 'X';
        img[3][7] = '\\';
        img[3][22] = '/';
        img[3][26] = 'X';
        img[3][27] = 'X';
        img[3][30] = '|';
        img[4][0] = '\\';
        img[4][2] = 'X';
        img[4][3] = 'X';
        img[4][4] = 'X';
        img[4][5] = '.';
        img[4][8] = '\\';
        img[4][9] = '+';
        img[4][10] = '-';
        img[4][11] = '_';
        img[4][12] = '.';
        img[4][13] = '_';
        img[4][14] = '.';
        img[4][15] = '+';
        img[4][16] = '-';
        img[4][17] = '.';
        img[4][18] = '.';
        img[4][19] = '-';
        img[4][20] = '#';
        img[4][21] = '/';
        img[4][22] = '-';
        img[4][25] = 'X';
        img[4][26] = 'X';
        img[4][27] = 'X';
        img[4][28] = 'X';
        img[4][31] = ')';

        img[5][0] = '\'';
        img[5][3] = '%';
        img[5][4] = '$';
        img[5][25] = '*';
        img[5][26] = '-';
        img[5][27] = '-';
        img[5][28] = '_';
        img[5][31] = '.';

        img[6][0] = '\\';
        img[6][32] = '|';

        img[7][0] = '.';
        img[7][1] = '|';
        img[7][6] = '_';
        img[7][7] = '_';
        img[7][23] = '_';
        img[7][24] = '_';
        img[7][25] = '_';
        img[7][32] = '\\';

        img[8][0] = '|';
        img[8][1] = '"';
        img[8][5] = '*';
        img[8][6] = '#';
        img[8][7] = '#';
        img[8][8] = '#';
        img[8][9] = 'x';
        img[8][10] = '*';
        img[8][22] = '#';
        img[8][23] = 'x';
        img[8][24] = '#';
        img[8][25] = '#';
        img[8][27] = '\\';
        img[8][33] = ':';

        img[9][0] = '\\';
        img[9][4] = '(';
        img[9][6] = '#';
        img[9][7] = '#';
        img[9][8] = '#';
        img[9][9] = 'x';
        img[9][10] = '@';
        img[9][21] = 'x';
        img[9][22] = '#';
        img[9][23] = '#';
        img[9][24] = '#';
        img[9][25] = '#';
        img[9][33] = '|';

        img[10][0] = '\'';
        img[10][1] = '.';
        img[10][15] = '_';
        img[10][16] = '.';
        img[10][17] = '.';
        img[10][18] = '_';
        img[10][32] = ')';
        img[10][33] = ':';

        img[11][1] = '\'';
        img[11][2] = '.';
        img[11][14] = '\\';
        img[11][15] = '=';
        img[11][18] = '=';
        img[11][19] = '/';
        img[11][32] = '/';

        img[12][2] = '\\';
        img[12][16] = '^';
        img[12][17] = '^';
        img[12][31] = '.';

        img[13][3] = '+';
        img[13][16] = '/';
        img[13][17] = '\\';
        img[13][30] = '?';
        img[13][31] = '"';

        img[14][4] = '"';
        img[14][13] = '*';
        img[14][14] = '-';
        img[14][15] = '"';
        img[14][18] = '"';
        img[14][19] = '-';
        img[14][20] = '*';
        img[14][28] = '/';
        img[14][29] = '"';

        img[15][8] = '"';
        img[15][9] = '+';
        img[15][10] = '=';
        img[15][11] = '.';
        img[15][12] = '_';
        img[15][13] = '_';
        img[15][14] = '_';
        img[15][15] = '\\';
        img[15][16] = '_';
        img[15][17] = '_';
        img[15][18] = '/';
        img[15][19] = '.';
        img[15][20] = '_';
        img[15][21] = '_';
        img[15][22] = '.';
        img[15][23] = '-';
        img[15][24] = '-';
        img[15][25] = '=';
        img[15][26] = '=';
        img[15][27] = '"';
        return img;
    }

    static char[][] getBear() {
        char[][] img = new char[8][12];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 12; x++) {
                img[y][x] = ' ';
            }
        }
        img[0][2] = '\u26E7';
        img[1][2] = '\u028C';
        img[2][1] = '/';
        img[2][3] = '\\';
        img[3][0] = '@';
        img[3][1] = '_';
        img[3][2] = '_';
        img[3][3] = '_';
        img[3][4] = '@';
        img[4][0] = '(';
        img[4][1] = '-';
        img[4][2] = '_';
        img[4][3] = '-';
        img[4][4] = ')';
        img[5][1] = '-';
        img[5][2] = '-';
        img[5][3] = '-';
        img[5][0] = '<';
        img[5][4] = '>';
        img[6][0] = '/';
        img[6][1] = '|';
        img[6][3] = '|';
        img[6][4] = '\\';
        img[6][5] = '/';
        img[7][0] = '~';
        img[7][1] = '~';
        img[7][2] = '~';
        img[7][3] = '~';
        img[7][4] = '~';
        img[7][5] = '~';
        img[7][7] = 'A';
        img[7][8] = 'G';

        img[4][7] = '<';
        img[4][9] = 'h';
        img[4][10] = 'i';
        img[4][11] = ')';
        img[5][9] = '\uFE36';
        return img;
    }

    static char[][] getKuma() {
        int kuma_height = 6;
        int kuma_width = 9;
        char[][] img = new char[kuma_height][kuma_width];
        for (int y = 0; y < kuma_height; y++) {
            for (int x = 0; x < kuma_width; x++) {
                img[y][x] = ' ';
            }
        }
        img[0][0] = ' ';
        img[0][1] = ' ';
        img[0][2] = 'p';
        img[0][3] = ' ';
        img[0][4] = '◞';
        img[0][5] = '◟';
        img[0][6] = 'p';

        img[1][0] = ' ';
        img[1][1] = '(';
        img[1][2] = '˶';
        img[1][3] = '˃';
        img[1][4] = 'ᵕ';
        img[1][5] = '˂';
        img[1][6] = '˶';
        img[1][7] = ')';

        img[2][0] = '૮';
        img[2][1] = '(';
        img[2][2] = ' ';
        img[2][3] = ' ';
        img[2][4] = ' ';
        img[2][5] = ' ';
        img[2][6] = ' ';
        img[2][7] = ')';
        img[2][8] = 'ა';

        img[3][0] = ' ';
        img[3][1] = ' ';
        img[3][2] = ' ';
        img[3][3] = 'ᵕ';
        img[3][4] = ' ';
        img[3][5] = 'ᵕ';

        img[4][0] = ' ';
        img[4][1] = '\\';
        img[4][2] = ' ';
        img[4][3] = '|';
        img[4][4] = ' ';
        img[4][5] = '|';
        img[4][6] = ' ';
        img[4][7] = '/';

        img[5][0] = ' ';
        img[5][1] = '✶';
        img[5][2] = '⋆';
        img[5][3] = '˚';
        img[5][4] = '모';
        img[5][5] = '✧';
        img[5][6] = '˖';
        img[5][7] = '°';
        img[5][8] = '.';

        return img;
    }

    public static int getTerminalWidth() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return getUnixTerminalWidth();
        } else {
            return 80;
        }
    }

    private static int getUnixTerminalWidth() {
        try {
            String columns = System.getenv("COLUMNS");
            if (columns != null && !columns.isEmpty()) {
                return Integer.parseInt(columns);
            }

            ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", "stty size </dev/tty");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream()));
            String output = reader.readLine();
            if (output != null && !output.isEmpty()) {
                String[] parts = output.trim().split(" ");
                return Integer.parseInt(parts[1]);
            }
        } catch (Exception ignored) {
        }
        return 80;
    }
}