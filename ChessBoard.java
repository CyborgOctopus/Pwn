import java.math.BigInteger;

// Inspired by https://alexanderameye.github.io/notes/chess-engine/

public class ChessBoard {

    private static int BOARD_SIZE = 8;
    // The letter A is used for pawns because lowercase and capital Ps look too similar.
    private static String[] PIECE_LETTERS = {"R", "N", "B", "Q", "K", "A", "r", "n", "b", "q", "k", "a"};
    private static String BLANK_SQUARE = "_";

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
                    int rowShift = BOARD_SIZE * (BOARD_SIZE - i - 1);
                    long row = bitboards[k] & (0xffL << rowShift); // mask out everything but the desired row
                    row &= (0x80L << rowShift) >>> j; // mask out everything but the desired column within the row
                    
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

    public long[][] getLegalMoves() {
        return null;
    }

    private long[][] getPseudoLegalMoves() {
        return null;
    }

    private long[] getKnightMoves() {
        return null;
    }

    public static long[] getKingMoves() {
        long[] kingMoves = new long[BOARD_SIZE * BOARD_SIZE];

        for (int i = 0; i < kingMoves.length; i++) {
            long square = 1L << (BOARD_SIZE * BOARD_SIZE - i - 1);
            //System.out.println("i: " + i);
            //System.out.println("SQUARE: " + String.format("%064d", new BigInteger(Long.toBinaryString(square))));

            boolean isTop = i < BOARD_SIZE;
            boolean isBottom = i >= BOARD_SIZE * (BOARD_SIZE - 1);
            boolean isLeft = i % BOARD_SIZE == 0;
            boolean isRight = i % BOARD_SIZE == BOARD_SIZE - 1;

            kingMoves[i] = (isTop ? 0L : square << BOARD_SIZE) // moving up
            | (isBottom ? 0L : square >>> BOARD_SIZE) // moving down
            | (isLeft ? 0L : square << 1) // moving left
            | (isRight ? 0L : square >>> 1) // moving right
            | ((isTop || isLeft) ? 0L : square << (BOARD_SIZE + 1)) // moving up and to the left
            | ((isTop || isRight) ? 0L : square << (BOARD_SIZE - 1)) // moving up and to the right
            | ((isBottom || isLeft) ? 0L : square >>> (BOARD_SIZE - 1)) // moving down and to the left
            | ((isBottom || isRight) ? 0L : square >>> (BOARD_SIZE + 1)); // moving down and to the right
        }

        return kingMoves;
    }

    public static long[] getWhitePawnMoves() {
        long[] pawnMoves = new long[BOARD_SIZE * (BOARD_SIZE - 2)];

        for (int i = 0; i < pawnMoves.length; i++) {
            long square = 1L << (BOARD_SIZE * (BOARD_SIZE - 1) - i - 1);

            System.out.println("i: " + i);
            System.out.println("SQUARE: " + String.format("%064d", new BigInteger(Long.toBinaryString(square))));

            boolean isLeft = i % BOARD_SIZE == 0;
            boolean isRight = i % BOARD_SIZE == BOARD_SIZE - 1;

            pawnMoves[i] = (square << BOARD_SIZE) // moving up
            | (isLeft ? 0L : square << (BOARD_SIZE + 1)) // moving up and to the left
            | (isRight ? 0L : square << (BOARD_SIZE - 1)); // moving up and to the right
        }

        return pawnMoves;
    }

    public static long[] getBlackPawnMoves() {
        long[] pawnMoves = new long[BOARD_SIZE * (BOARD_SIZE - 2)];

        for (int i = 0; i < pawnMoves.length; i++) {
            long square = 1L << (BOARD_SIZE * (BOARD_SIZE - 1) - i - 1);

            boolean isLeft = i % BOARD_SIZE == 0;
            boolean isRight = i % BOARD_SIZE == BOARD_SIZE - 1;

            pawnMoves[i] = (square >>> BOARD_SIZE) // moving down
            | (isLeft ? 0L : square >>> (BOARD_SIZE - 1)) // moving down and to the left
            | (isRight ? 0L : square >>> (BOARD_SIZE + 1)); // moving down and to the right
        }

        return pawnMoves;
    }

}