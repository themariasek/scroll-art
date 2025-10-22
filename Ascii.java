import java.util.Random;

class Ascii {
    private static final Random rand = new Random();

    public static void main(String[] args) {
        char[][] llama = buildLlama();
        final int canvasRows = 24;
        final int canvasCols = Math.max(llama[0].length, Math.min(120, currentTerminalWidth()));

        while (true) {
            char[][] canvas = createEmptyCanvas(canvasRows, canvasCols);

            int llamaCount = 3 + rand.nextInt(4);
            for (int n = 0; n < llamaCount; n++) {
                dropLlama(canvas, llama);
            }

            printCanvas(canvas);

            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private static char[][] buildLlama() {
        char[][] llama = new char[9][19];
        for (int i = 0; i < llama.length; i++) {
            for (int j = 0; j < llama[i].length; j++) {
                llama[i][j] = ' ';
            }
        }

        llama[0][1] = '/';
        llama[0][2] = '^';
        llama[0][3] = '-';
        llama[0][4] = '-';
        llama[0][5] = '-';
        llama[0][6] = '^';
        llama[0][7] = '\\';
        llama[1][1] = '|';
        llama[1][3] = '.';
        llama[1][5] = '.';
        llama[1][7] = '|';
        llama[2][1] = '\\';
        llama[2][4] = '`';
        llama[2][7] = '/';
        llama[2][12] = 'M';
        llama[2][13] = 'S';
        llama[3][1] = '/';
        llama[3][2] = '=';
        llama[3][3] = '=';
        llama[3][4] = '=';
        llama[3][5] = '=';
        llama[3][6] = '=';
        llama[3][7] = '\\';
        llama[3][8] = '_';
        llama[3][9] = '_';
        llama[3][10] = '_';
        llama[3][11] = '_';
        llama[3][12] = '_';
        llama[3][13] = '_';
        llama[3][14] = '_';
        llama[3][15] = '_';
        llama[3][16] = '_';
        llama[4][0] = '/';
        llama[4][17] = '\\';
        llama[4][18] = '[';
        llama[4][19] = ']';
        llama[5][0] = '\\';
        llama[5][17] = '/';
        llama[6][1] = '-';
        llama[6][2] = '-';
        llama[6][3] = '-';
        llama[6][4] = '-';
        llama[6][5] = '-';
        llama[6][6] = '-';
        llama[6][7] = '-';
        llama[6][8] = '-';
        llama[6][9] = '-';
        llama[6][10] = '-';
        llama[6][11] = '-';
        llama[6][12] = '-';
        llama[6][13] = '-';
        llama[6][14] = '-';
        llama[6][15] = '-';
        llama[6][16] = '-';
        llama[7][1] = '|';
        llama[7][3] = '|';
        llama[7][5] = '|';
        llama[7][7] = '|';
        llama[7][10] = '|';
        llama[7][12] = '|';
        llama[7][14] = '|';
        llama[7][16] = '|';
        llama[8][1] = '[';
        llama[8][2] = '|';
        llama[8][3] = ']';
        llama[8][5] = '[';
        llama[8][6] = '|';
        llama[8][7] = ']';
        llama[8][10] = '[';
        llama[8][11] = '|';
        llama[8][12] = ']';
        llama[8][14] = '[';
        llama[8][15] = '|';
        llama[8][16] = ']';
        return llama;
    }

    private static char[][] createEmptyCanvas(int rows, int cols) {
        char[][] canvas = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                canvas[r][c] = ' ';
            }
        }
        return canvas;
    }

    private static void dropLlama(char[][] canvas, char[][] llama) {
        int attempts = 0;
        while (attempts < 10) {
            int top = rand.nextInt(Math.max(1, canvas.length - llama.length + 1));
            int left = rand.nextInt(Math.max(1, canvas[0].length - llama[0].length + 1));
            if (canPlace(canvas, llama, top, left)) {
                drawLlama(canvas, llama, top, left);
                return;
            }
            attempts++;
        }
    }

    private static boolean canPlace(char[][] canvas, char[][] llama, int top, int left) {
        for (int i = 0; i < llama.length; i++) {
            for (int j = 0; j < llama[i].length; j++) {
                char ch = llama[i][j];
                if (ch != ' ' && canvas[top + i][left + j] != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private static void drawLlama(char[][] canvas, char[][] llama, int top, int left) {
        for (int i = 0; i < llama.length; i++) {
            for (int j = 0; j < llama[i].length; j++) {
                char ch = llama[i][j];
                if (ch != ' ') {
                    canvas[top + i][left + j] = ch;
                }
            }
        }
    }

    private static void printCanvas(char[][] canvas) {
        for (int r = 0; r < canvas.length; r++) {
            System.out.println(new String(canvas[r]));
        }
    }

    private static int currentTerminalWidth() {
        String columns = System.getenv("COLUMNS");
        if (columns != null) {
            try {
                return Integer.parseInt(columns.trim());
            } catch (NumberFormatException ignored) {
            }
        }
        return 80;
    }
}

/* /^---^\
   | . . | 
   \  `  /   MS 
   /=====\________
  /               \[]
  \               / 
   --------------- 
   | || |   | || |
   [|][|]   [|][|] */
