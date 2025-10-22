import java.util.Random;

class Symbols {
    private static final Random rand = new Random();
    private static final int MAX_LINES = 8;
    private static final String[] SYMBOL_OPTIONS = { "*", "x", "o", "+", "#" };

    private static final Integer[] lineCols = new Integer[MAX_LINES];
    private static final int[] directions = new int[MAX_LINES];
    private static final String[] symbols = new String[MAX_LINES];

    public static void main(String[] args) throws InterruptedException {
        final int width = Math.max(40, getTerminalWidth() - 1);

        lineCols[0] = 0;
        directions[0] = 1;
        symbols[0] = "*";
        for (int i = 1; i < MAX_LINES; i++) {
            lineCols[i] = null;
        }

        int framesSinceNew = 0;

        while (true) {
            framesSinceNew++;
            if (rand.nextDouble() < 0.12 * framesSinceNew) {
                int slot = findEmptySlot();
                if (slot != -1) {
                    startLine(slot, width);
                    framesSinceNew = 0;
                }
            }

            moveLines(width);
            printRow(width);

            Thread.sleep(60);
        }
    }

    private static void moveLines(int width) {
        for (int i = 0; i < MAX_LINES; i++) {
            if (lineCols[i] != null) {
                lineCols[i] += directions[i];
                if (lineCols[i] <= 0 || lineCols[i] >= width - 1) {
                    lineCols[i] = null;
                }
            }
        }
    }

    private static void printRow(int width) {
        StringBuilder row = new StringBuilder();
        for (int i = 0; i < width; i++) {
            row.append(' ');
        }

        for (int i = 0; i < MAX_LINES; i++) {
            if (lineCols[i] != null) {
                row.setCharAt(lineCols[i], symbols[i].charAt(0));
            }
        }

        System.out.println(row.toString());
    }

    private static int findEmptySlot() {
        for (int i = 0; i < MAX_LINES; i++) {
            if (lineCols[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private static void startLine(int slot, int width) {
        int base = rand.nextInt(MAX_LINES);
        if (lineCols[base] != null) {
            lineCols[slot] = lineCols[base];
            directions[slot] = -directions[base];
        } else {
            lineCols[slot] = rand.nextInt(width);
            directions[slot] = rand.nextBoolean() ? 1 : -1;
        }
        symbols[slot] = SYMBOL_OPTIONS[rand.nextInt(SYMBOL_OPTIONS.length)];
    }

    private static int getTerminalWidth() {
        String columns = System.getenv("COLUMNS");
        if (columns != null && !columns.isEmpty()) {
            try {
                return Integer.parseInt(columns.trim());
            } catch (NumberFormatException ignored) {
            }
        }
        return 80;
    }
}
