import java.math.BigInteger;

// Inspired by https://alexanderameye.github.io/notes/chess-engine/
// I learned about how magic bitboards work from here: 
// https://essays.jwatzman.org/essays/chess-move-generation-with-magic-bitboards.html


public class ChessBoard {

    private static int BOARD_SIZE = 8;
    // The letter A is used for pawns because lowercase and capital Ps look too similar.
    private static String[] PIECE_LETTERS = {"R", "N", "B", "Q", "K", "A", "r", "n", "b", "q", "k", "a"};
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

    public static long[] getRookMoves() {
        long[] rookMoves = new long[BOARD_SIZE * BOARD_SIZE];
    
        for (int i = 0; i < rookMoves.length; i++) {
            int rowShift = BOARD_SIZE * (i / BOARD_SIZE);
            int columnShift = i % BOARD_SIZE;
            rookMoves[i] = ((TOP >>> rowShift) | (LEFT >>> columnShift)) & (~getSquare(i));
        }

        return rookMoves;
    }

    public static long[] getBlockedRookMoves() {
        long[] rookMoves = new long[BOARD_SIZE * BOARD_SIZE * 4096];
        
        int count = 0;
        
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            long square = getSquare(i);
            
            for (int[] shift: getRookShifts(i)) {
                for (int j = shift[0]; j <= shift[1]; j++) {
                    rookMoves[count] |= move(square, j, 0);
                }

                for (int j = shift[2]; j <= shift[3]; j++) {
                    rookMoves[count] |= move(square, 0, j);
                }
                
                count++;
            }
        }

        int count1 = 0;

        for (long move : rookMoves) {
            if (move != 0) {
                count1++;
            }
        }

        long[] trueMoves = new long[count1];

        for (int i = 0; i < count1; i++) {
            trueMoves[i] = rookMoves[i];
        }

        return trueMoves;
    }

    // public static long[] getBlockedRookMoves() {
    //     long[] rookMoves = new long[BOARD_SIZE * BOARD_SIZE * 4096];

    //     int count = 0;

    //     for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
    //         int rowShift = BOARD_SIZE * (i / BOARD_SIZE);
    //         int columnShift = i % BOARD_SIZE;
    //         long rookMove = ((TOP >>> rowShift) | (LEFT >>> columnShift)) & (~getSquare(i));

    //         for (int[] blockedIndex : getBlockerIndices(i)) {
    //             // create mask

    //             long blockerMask = 0L;
    //             long rowBlockerMask = 0L;
    //             long columnBlockerMask = 0L;

    //              // Add left blocking
    //             for (int j = 0; j < blockedIndex[0]; j++) {
    //                 rowBlockerMask |= (LEFT >>> j);
    //             }

    //             // Add right blocking
    //             for (int j = blockedIndex[1] + 1; j < BOARD_SIZE; j++) {
    //                 rowBlockerMask |= (LEFT >>> j);
    //             }

    //             rowBlockerMask &= (TOP >>> rowShift); // Limit to proper row

    //             // Add above blocking
    //             for (int j = 0; j < blockedIndex[2]; j++) {
    //                 columnBlockerMask |= (TOP >>> j);
    //             }

    //             // Add below blocking
    //             for (int j = blockedIndex[3] + 1; j < BOARD_SIZE; j++) {
    //                 columnBlockerMask |= (TOP >>> j);
    //             }

    //             columnBlockerMask &= (LEFT >>> columnShift); // Limit to proper column

    //             blockerMask = ~(rowBlockerMask | columnBlockerMask); // Combine row and column and invert

    //             rookMoves[count] = rookMove & blockerMask;
    //             count++;
    //         }
    //     }

    //     return rookMoves;
    // }

    public static long[] getBishopMoves() {
        long[] bishopMoves = new long[BOARD_SIZE * BOARD_SIZE];

        for (int i = 0; i < bishopMoves.length; i++) {
            long square = getSquare(i);

            for (int j = 1; j < BOARD_SIZE; j++) {
                bishopMoves[i] |= move(square, -j, -j)
                | move(square, -j, j)
                | move(square, j, -j)
                | move(square, j, j);
            }
        }

        return bishopMoves;
    }

    public static long[] getBlockedBishopMoves() {
        long[] bishopMoves = new long[BOARD_SIZE * BOARD_SIZE * 4096];

        int count = 0;
        
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            long square = getSquare(i);

            for (int[] shift : getBishopShifts(i)) {
                for (int j = shift[0]; j <= shift[1]; j++) {
                    bishopMoves[count] |= move(square, j, -j);
                }

                for (int j = shift[2]; j <= shift[3]; j++) {
                    bishopMoves[count] |= move(square, j, j);
                }

                count++;
            }
        }

        int count1 = 0;

        for (long move : bishopMoves) {
            if (move != 0) {
                count1++;
            }
        }

        long[] trueMoves = new long[count1];

        for (int i = 0; i < count1; i++) {
            trueMoves[i] = bishopMoves[i];
        }

        return trueMoves;
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
        int[][] shifts = new int[(1 - maxLeftShift) * (1 + maxRightShift)][2];
        int count = 0;
        
        for (int i = maxLeftShift; i <= 0; i++) {
            for (int j = 0; j <= maxRightShift; j++) {
                shifts[count][0] = i;
                shifts[count][1] = j;
                count++;
            }
        }

        return shifts;
    }

    public static int[][] getBlockerIndices(int squareNum) {
        int row = squareNum / BOARD_SIZE;
        int column = squareNum % BOARD_SIZE;

        int[][] horizontalIndices = getLinearBlockerIndices(column);
        int[][] verticalIndices = getLinearBlockerIndices(row);
        int[][] indices = new int[horizontalIndices.length * verticalIndices.length][4];

        int count = 0;

        for (int[] horizontalIndex : horizontalIndices) {
            for (int[] verticalIndex : verticalIndices) {
                indices[count][0] = horizontalIndex[0];
                indices[count][1] = horizontalIndex[1];
                indices[count][2] = verticalIndex[0];
                indices[count][3] = verticalIndex[1];
                count++;
            }
        }

        return indices;
    }

    public static int[][] getLinearBlockerIndices(int position) {
        int boundForLeft = (position == 0) ? 1 : position;
        int boundForRight = (position == BOARD_SIZE - 1) ? position : position + 1;
        int length = boundForLeft * (BOARD_SIZE - boundForRight);
        int[][] indices = new int[length][2];

        int count = 0;

        for (int i = 0; i < boundForLeft; i++) {
            for (int j = boundForRight; j < BOARD_SIZE; j++) {
                indices[count][0] = i;
                indices[count][1] = j;
                count++;
            }
        }

        return indices;
    }

}