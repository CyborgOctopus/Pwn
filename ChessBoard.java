import java.math.BigInteger;

// Inspired by https://alexanderameye.github.io/notes/chess-engine/
// I learned about how magic bitboards work from here: 
// https://essays.jwatzman.org/essays/chess-move-generation-with-magic-bitboards.html
// Also got more useful info about magic bitboards from here: 
// https://rhysre.net/fast-chess-move-generation-with-magic-bitboards.html
// And useful github repo: https://github.com/GunshipPenguin/shallow-blue/


public class ChessBoard {

    private static int BOARD_SIZE = 8;
    // The letter A is used for pawns because lowercase and capital Ps look too similar.
    private static String[] PIECE_LETTERS = {"R", "N", "B", "Q", "K", "P", "r", "n", "b", "q", "k", "p"};
    private static String BLANK_SQUARE = "_";
    private static long LEFT = 0x8080808080808080L;
    private static long RIGHT = 0x0101010101010101L;
    private static long TOP = 0xff00000000000000L;
    private static long BOTTOM = 0x00000000000000ffL;

    private static long BLACK_ROOKS = 0x8100000000000000L;
    private static long BLACK_KNIGHTS = 0x4200000000000000L;
    private static long BLACK_BISHOPS = 0x2400000000000000L;
    private static long BLACK_QUEEN = 0x1000000000000000L;
    private static long BLACK_KING = 0x0800000000000000L;
    private static long BLACK_PAWNS = 0x00ff000000000000L;

    private static long WHITE_ROOKS = 0x0000000000000081L;
    private static long WHITE_KNIGHTS = 0x0000000000000042L;
    private static long WHITE_BISHOPS = 0x0000000000000024L;
    private static long WHITE_QUEEN = 0x0000000000000010L;
    private static long WHITE_KING = 0x0000000000000008L;
    private static long WHITE_PAWNS = 0x000000000000ff00L;

    // For now, magic numbers and shifts will just be hard-coded based on tables from Dr. Pradyumna Kannan. Eventually,
    // I may switch to a different implementation.
    // The numbers below were provided by Dr. Kannan and can be found on his website:
    // http://pradu.us/old/Nov27_2008/Buzz/
    // I have changed the ordering of the numbers to fit my program.
    private static int[] ROOK_SHIFTS =
    {
        53, 53, 53, 53, 53, 54, 54, 53,
        53, 54, 54, 54, 54, 54, 54, 53,
        53, 54, 54, 54, 54, 54, 54, 53,
        53, 54, 54, 54, 54, 54, 54, 53,
        53, 54, 54, 54, 54, 54, 54, 53,
        53, 54, 54, 54, 54, 54, 54, 53,
        53, 54, 54, 54, 54, 54, 54, 53,
        52, 53, 53, 53, 53, 53, 53, 52
    };

    private static long[] ROOK_MAGICS =
    {
        0x0001FFFAABFAD1A2L, 0x0001000082000401L, 0x0001000204000801L, 0x0001000204080011L,
        0x0000040810002101L, 0x003FFFCDFFD88096L, 0x007FFCDDFCED714AL, 0x00FFFCDDFCED714AL,
        0x0000800041000080L, 0x0000800100020080L, 0x0000020004008080L, 0x0000040008008080L,
        0x0000080010008080L, 0x0000100020008080L, 0x0000200040008080L, 0x0000204000800080L,
        0x0000004081020004L, 0x0000010002008080L, 0x0000020004008080L, 0x0000040008008080L,
        0x0000080010008080L, 0x0000100020008080L, 0x0000200040008080L, 0x0000204000808000L,
        0x0000800040800100L, 0x0000020001010004L, 0x0000020080800400L, 0x0000040080800800L,
        0x0000080080801000L, 0x0000100080802000L, 0x0000200040401000L, 0x0000204000800080L,
        0x0000800080004100L, 0x0000010080800200L, 0x0000020080040080L, 0x0000040080080080L,
        0x0000080080100080L, 0x0000100080200080L, 0x0000200040005000L, 0x0000208080004000L,
        0x0000020000408104L, 0x0000010100020004L, 0x0000808002000400L, 0x0000808004000800L,
        0x0000808008001000L, 0x0000808010002000L, 0x0000404000201000L, 0x0000208000400080L,
        0x0000800040800100L, 0x0000800100020080L, 0x0000800200040080L, 0x0000800400080080L,
        0x0000800800100080L, 0x0000801000200080L, 0x0000400020005000L, 0x0000800020400080L,
        0x0080002040800100L, 0x0080008001000200L, 0x0080010200040080L, 0x0080020400080080L,
        0x0080040800100080L, 0x0080081000200080L, 0x0040001000200040L, 0x0080001020400080L
    };

    private static int[] BISHOP_SHIFTS = {
        58, 59, 59, 59, 59, 59, 59, 58,
        59, 59, 59, 59, 59, 59, 59, 59,
        59, 59, 57, 57, 57, 57, 59, 59,
        59, 59, 57, 55, 55, 57, 59, 59,
        59, 59, 57, 55, 55, 57, 59, 59,
        59, 59, 57, 57, 57, 57, 59, 59,
        59, 59, 59, 59, 59, 59, 59, 59,
        58, 59, 59, 59, 59, 59, 59, 58
    };

    private static long[] BISHOP_MAGICS = {
        0x0002020202020200L, 0x0000040404040400L, 0x0000000404080200L, 0x0000000010020200L,
        0x0000000000208800L, 0x0000000020841000L, 0x0000002082082000L, 0x0000104104104000L,
        0x0002020202020000L, 0x0004040404040000L, 0x0000040408020000L, 0x0000001002020000L,
        0x0000000020880000, 0x0000002084100000L, 0x0000208208200000L, 0x0000410410400000L,
        0x0001010101000200L, 0x0002020202000400L, 0x0001010101000200L, 0x0000080100400400L,
        0x0000002011000800L, 0x0000082088001000L, 0x0000410410002000L, 0x0000820820004000L,
        0x0000808080010400L, 0x0001010100020800L, 0x0000808100020100L, 0x0000404040040100L,
        0x0000020080080080L, 0x0000104400080800L, 0x0000820800101000L, 0x0001041000202000L,
        0x0000404000820800L, 0x0000808001041000L, 0x0000404002011000L, 0x0000840000802000L,
        0x0000404004010200L, 0x0000208004010400L, 0x0001040008080800L, 0x0002080010101000L,
        0x0000200041041000L, 0x0000400082082000L, 0x0000200100884000L, 0x0000800400A00000L,
        0x0000800802004000L, 0x0001000202020200L, 0x0002000404040400L, 0x0004000808080800L,
        0x0000002082082000L, 0x0000004104104000L, 0x0000008210400000L, 0x0000011040000000L,
        0x0000040400800000L, 0x0000040102020000L, 0x0000020202020200L, 0x0000040404040400L,
        0x0000104104104000L, 0x0000410410400000L, 0x0000821040000000L, 0x0001104000000000L,
        0x0004040080000000L, 0x0004010202000000L, 0x0002020202020000L, 0x0002020202020200L
    };

    public long[] bitboards;

    public ChessBoard() {
        this.bitboards = new long[] {BLACK_ROOKS, BLACK_KNIGHTS, BLACK_BISHOPS, BLACK_QUEEN, BLACK_KING, BLACK_PAWNS, 
        WHITE_ROOKS, WHITE_KNIGHTS, WHITE_BISHOPS, WHITE_QUEEN, WHITE_KING, WHITE_PAWNS};
    }

    public String toString() {
        String boardString = "";
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                boolean squareIsBlank = true;
                
                for (int k = 0; k < bitboards.length; k++) {
                    int rowShift = BOARD_SIZE * i;
                    long row = bitboards[k] & (TOP >>> rowShift); // mask out everything but the desired row
                    row &= LEFT >>> j; // mask out everything but the desired column within the row
                    
                    // If row is zero, it means that piece isn't present in that row and column.
                    if (row != 0) {
                        boardString += PIECE_LETTERS[k];
                        squareIsBlank = false;
                        break;
                    }
                    
                }

                if (squareIsBlank) {
                    boardString += BLANK_SQUARE;
                }
            }
            
            boardString += "\n";  
        }

        return boardString;
    }

    public void setBoard(long board, int pos) {
        this.bitboards[pos] = board;
    }

    public void zeroBoard() {
        for (int i = 0; i < this.bitboards.length; i++) {
            this.setBoard(0L, i);
        }
    }

    public long[][] getLegalMoves() {
        return null;
    }

    private long[][] getPseudoLegalMoves() {
        return null;
    }

    public static long[][] getRookMoves() {
        long[][] rookMoves = new long[BOARD_SIZE * BOARD_SIZE][4096];
                
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            long square = getSquare(i);
            int count = 0;
            
            for (int[] shift: getRookShifts(i)) {
                for (int j = shift[0]; j <= shift[1]; j++) {
                    rookMoves[i][count] |= move(square, j, 0);
                }

                for (int j = shift[2]; j <= shift[3]; j++) {
                    rookMoves[i][count] |= move(square, 0, j);
                }
                
                rookMoves[i][count] &= ~square;
                count++;
            }
        }

        return rookMoves;
    }

    public static long[][] getBishopMoves() {
        long[][] bishopMoves = new long[BOARD_SIZE * BOARD_SIZE][4096];
 
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            long square = getSquare(i);
            int count = 0;

            for (int[] shift : getBishopShifts(i)) {
                for (int j = shift[0]; j <= shift[1]; j++) {
                    bishopMoves[i][count] |= move(square, j, -j);
                }

                for (int j = shift[2]; j <= shift[3]; j++) {
                    bishopMoves[i][count] |= move(square, j, j);
                }

                bishopMoves[i][count] &= ~square;
                count++;
            }
        }

        return bishopMoves;
    }

    public static long[] getKnightMoves() {
        long[] knightMoves = new long[BOARD_SIZE * BOARD_SIZE];

        for (int i = 0; i < knightMoves.length; i++) {
            long square = getSquare(i);

            knightMoves[i] = move(square, -2, -1)
            | move(square, -2, 1)
            | move(square, -1, -2)
            | move(square, -1, 2)
            | move(square, 1, -2)
            | move(square, 1, 2)
            | move(square, 2, -1)
            | move(square, 2, 1);
        }

        return knightMoves;
    }

    public static long[] getKingMoves() {
        long[] kingMoves = new long[BOARD_SIZE * BOARD_SIZE];

        for (int i = 0; i < kingMoves.length; i++) {
            long square = getSquare(i);

            kingMoves[i] = move(square, -1, -1)
            | move(square, -1, 0)
            | move(square, -1, 1)
            | move(square, 0, -1)
            | move(square, 0, 1)
            | move(square, 1, -1)
            | move(square, 1, 0)
            | move(square, 1, 1);   
        }

        return kingMoves;
    }

    public static long[] getForwardPawnMoves(boolean black) {
        long[] pawnMoves = new long[BOARD_SIZE * (BOARD_SIZE - 1)];

        for (int i = BOARD_SIZE; i < pawnMoves.length; i++) {
            pawnMoves[i] = move(getSquare(i), 0, black ? -1 : 1);
        }

        return pawnMoves;
    }

    public static long[] getDiagonalPawnMoves(boolean black) {
        long[] pawnMoves = new long[BOARD_SIZE * (BOARD_SIZE - 1)];

        for (int i = BOARD_SIZE; i < pawnMoves.length; i++) {
            long square = getSquare(i);

            pawnMoves[i] = move(square, -1, black ? -1 : 1)
            | move(square, 1, black ? -1 : 1);
        }

        return pawnMoves;
    }

    public static long[] getRowColMasks() {
        long[] masks = new long[BOARD_SIZE * BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            long square = getSquare(i);
            
            for (int j = 0; j < BOARD_SIZE; j++) {
                masks[i] |= move(square, -j, 0);
                masks[i] |= move(square, 0, -j);
                masks[i] |= move(square, 0, j);
                masks[i] |= move(square, j, 0);
            }

            // Match Dr. Kannan's formatting of excluding edges (that aren't one of the rook's files) and square
            long orig_mask = masks[i];
            masks[i] &= (LEFT & orig_mask) != LEFT ? ~LEFT : 0xffffffffffffffffL;
            masks[i] &= (RIGHT & orig_mask) != RIGHT ? ~RIGHT : 0xffffffffffffffffL;
            masks[i] &= (TOP & orig_mask) != TOP ? ~TOP : 0xffffffffffffffffL;
            masks[i] &= (BOTTOM & orig_mask) != BOTTOM ? ~BOTTOM : 0xffffffffffffffffL;
            masks[i] &= ~square;
        }

        return masks;
    }

    public static long[] getDiagonalMasks() {
        long[] masks = new long[BOARD_SIZE * BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            long square = getSquare(i);

            for (int j = 0; j < BOARD_SIZE; j++) {
                masks[i] |= move(square, -j, -j);
                masks[i] |= move(square, -j, j);
                masks[i] |= move(square, j, -j);
                masks[i] |= move(square, j, j);
            }

            // In order to match Dr. Kannan's formatting, make sure they don't include the edges or the square
            masks[i] &= ~(LEFT | RIGHT | TOP | BOTTOM);
            masks[i] &= ~square;
        }

        return masks;
    }

    // Generates all possible 15-bit integers and splits them into 2 parts: a row pattern (first 8 bits) and a column pattern
    // (last 7 bits). These represent all possible patterns of pieces on a given row/col combination on a board.
    public static long[] getRowColBlockerPatterns(int squareNum) {
        int row = squareNum / BOARD_SIZE;
        int column = squareNum % BOARD_SIZE;
        int patternLength = 2 * BOARD_SIZE - 1; // 8 bits for the row, 8 for the column, -1 for the intersection
        int numPatterns = 1 << (patternLength + 1);
        int rowMask = 0x7f80; // mask out the last 7 bits, because these will be used for the column.

        long[] patterns = new long[numPatterns];

        for (int i = 0; i < numPatterns; i++) {
            // write row pattern
            long rowPattern = (long) (i & rowMask);
            rowPattern >>>= BOARD_SIZE - 1; // right shift it to the end.
            patterns[i] |= rowPattern << (BOARD_SIZE - row - 1) * BOARD_SIZE;

            // write column pattern
            long colPattern = (long) i & (~rowMask);
            int bitIndex = 0;
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (j == BOARD_SIZE - row - 1) {
                    continue; // skip intersection with row
                }
                long bit = colPattern & (1L << bitIndex);
                bit >>>= bitIndex; // right shift it to the end;
                bit <<= BOARD_SIZE - column - 1; // left shift it to the proper column
                patterns[i] |= bit << BOARD_SIZE * j; // shift to proper row and add to pattern
                bitIndex++;
            }
        }

        return patterns;
    }

    // Returns a square shifted horizontally and vertically by a given amount, or 0 if this shift would
    // take the square out of bounds.
    private static long move(long square, int horizontalShift, int verticalShift) {
        if ((horizontalShift == 0) && (verticalShift == 0)) {
            return square;
        }

        if (horizontalShift > 0) {
            square = (square & (~RIGHT)) >>> 1;
            horizontalShift--;
        } else if (horizontalShift < 0) {
            square = (square & (~LEFT)) << 1;
            horizontalShift++;
        }

        if (verticalShift > 0) {
            square = (square & (~TOP)) << BOARD_SIZE;
            verticalShift--;
        } else if (verticalShift < 0) {
            square = (square & (~BOTTOM)) >>> BOARD_SIZE;
            verticalShift++;
        }

        return (square == 0) ? 0 : move(square, horizontalShift, verticalShift); 
    }

    private static long getSquare(int squareNum) {
        return 1L << (BOARD_SIZE * BOARD_SIZE - squareNum - 1); 
    }

    public static int[][] getBishopShifts(int squareNum) {
        int row = squareNum / BOARD_SIZE;
        int column = squareNum % BOARD_SIZE;

        int maxLeftShift = -column;
        int maxRightShift = BOARD_SIZE - column - 1;
        int maxDownShift = row + 1 - BOARD_SIZE;
        int maxUpShift = row;

        int negativeDiagLeft = -Math.min(-maxLeftShift, maxUpShift);
        int negativeDiagRight = Math.min(maxRightShift, -maxDownShift);
        int positiveDiagLeft = -Math.min(-maxLeftShift, -maxDownShift);
        int positiveDiagRight = Math.min(maxRightShift, maxUpShift);

        int[][] negativeDiagShifts = getLinearShifts(negativeDiagLeft, negativeDiagRight);
        int[][] positiveDiagShifts = getLinearShifts(positiveDiagLeft, positiveDiagRight);
        int[][] shifts = new int[negativeDiagShifts.length * positiveDiagShifts.length][4];

        int count = 0;

        for (int[] negativeDiagShift: negativeDiagShifts) {
            for (int[] positiveDiagShift : positiveDiagShifts) {
                shifts[count][0] = negativeDiagShift[0];
                shifts[count][1] = negativeDiagShift[1];
                shifts[count][2] = positiveDiagShift[0];
                shifts[count][3] = positiveDiagShift[1];
                count++;
            }
        }

        return shifts;
    }

    public static int[][] getRookShifts(int squareNum) {
        int row = squareNum / BOARD_SIZE;
        int column = squareNum % BOARD_SIZE;

        int maxLeftShift = -column;
        int maxRightShift = BOARD_SIZE - column - 1;
        int maxDownShift = row + 1 - BOARD_SIZE;
        int maxUpShift = row;

        int[][] horizontalShifts = getLinearShifts(maxLeftShift, maxRightShift);
        int[][] verticalShifts = getLinearShifts(maxDownShift, maxUpShift);
        int[][] shifts = new int[horizontalShifts.length * verticalShifts.length][4];

        int count = 0;

        for (int[] horizontalShift: horizontalShifts) {
            for (int[] verticalShift : verticalShifts) {
                shifts[count][0] = horizontalShift[0];
                shifts[count][1] = horizontalShift[1];
                shifts[count][2] = verticalShift[0];
                shifts[count][3] = verticalShift[1];
                count++;
            }
        }

        return shifts;
    }

    public static int[][] getLinearShifts(int maxLeftShift, int maxRightShift) {        
        int minLeftShift = (maxLeftShift == 0 ? 0 : -1);
        int minRightShift = (maxRightShift == 0 ? 0 : 1);

        int[][] shifts = new int[(1 + minLeftShift - maxLeftShift) * (1 + maxRightShift - minRightShift)][2];
        int count = 0;

        for (int i = maxLeftShift; i <= minLeftShift; i++) {
            for (int j = minRightShift; j <= maxRightShift; j++) {
                shifts[count][0] = i;
                shifts[count][1] = j;
                count++;
            }
        }

        return shifts;
    }
}
