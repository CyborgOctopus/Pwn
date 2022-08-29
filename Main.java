import java.math.BigInteger;

public class Main {
   
    public static void main(String[] args) {

        ChessBoard chessBoard = new ChessBoard();
        System.out.print(chessBoard);

        System.out.println("BLOCKED ROOK MOVES: ");
        for (long move : ChessBoard.getBlockedRookMoves()) {
            chessBoard.zeroBoard();
            chessBoard.setBoard(move, 0);
            System.out.println(chessBoard);        
        }
        

        System.out.println("LINEAR BLOCKER INDICES: ");

        for (int i = 0; i < 8; i++) {
            //print2DArray(ChessBoard.getLinearBlockerIndices(i));
        }

        System.out.println("BLOCKER INDICES: ");

        for (int i = 0; i < 64; i++) {
            //print2DArray(ChessBoard.getBlockerIndices(i));
        }

        System.out.println("KING MOVES: ");

        for (long move : ChessBoard.getKingMoves()) {
            //System.out.println(String.format("%064d", new BigInteger(Long.toBinaryString(kingMove))));
            //chessBoard.zeroBoard();
            //chessBoard.setBoard(move, 4);
            //System.out.println(chessBoard);
        }

        System.out.println("BLACK FORWARD PAWN MOVES: ");

        for (long move : ChessBoard.getForwardPawnMoves(true)) {
            //System.out.println(String.format("%064d", new BigInteger(Long.toBinaryString(pawnMove))));
            //chessBoard.zeroBoard();
            //chessBoard.setBoard(move, 5);
            //System.out.println(chessBoard);        
        }

        System.out.println("BLACK DIAGONAL PAWN MOVES: ");

        for (long move : ChessBoard.getDiagonalPawnMoves(true)) {
            //System.out.println(String.format("%064d", new BigInteger(Long.toBinaryString(pawnMove))));
            //chessBoard.zeroBoard();
            //chessBoard.setBoard(move, 5);
            //System.out.println(chessBoard);        
        }

        System.out.println("WHITE FORWARD PAWN MOVES: ");

        for (long move : ChessBoard.getForwardPawnMoves(false)) {
            //System.out.println(String.format("%064d", new BigInteger(Long.toBinaryString(pawnMove))));
            //chessBoard.zeroBoard();
            //chessBoard.setBoard(move, 11);
            //System.out.println(chessBoard);        
        }

        System.out.println("WHITE DIAGONAL PAWN MOVES: ");

        for (long move : ChessBoard.getDiagonalPawnMoves(false)) {
            //System.out.println(String.format("%064d", new BigInteger(Long.toBinaryString(pawnMove))));
            //chessBoard.zeroBoard();
            //chessBoard.setBoard(move, 11);
            //System.out.println(chessBoard);        
        }

        System.out.println("KNIGHT MOVES: ");

        for (long move : ChessBoard.getKnightMoves()) {
            //System.out.println(String.format("%064d", new BigInteger(Long.toBinaryString(knightMove))));
            //chessBoard.zeroBoard();
            //chessBoard.setBoard(move, 1);
            //System.out.println(chessBoard);        
        }

        System.out.println("ROOK MOVES: ");

        for (long move : ChessBoard.getRookMoves()) {
            //System.out.println(String.format("%064d", new BigInteger(Long.toBinaryString(rookMove))));
            //chessBoard.zeroBoard();
            //chessBoard.setBoard(move, 0);
            //System.out.println(chessBoard);        
        }

        System.out.println("BISHOP MOVES: ");
        
        for (long move : ChessBoard.getBishopMoves()) {
            //System.out.println(String.format("%064d", new BigInteger(Long.toBinaryString(bishopMove))));
            //chessBoard.zeroBoard();
            //chessBoard.setBoard(move, 2);
            //System.out.println(chessBoard);        
        }

    }

    private static void print2DArray(int[][] arr) {
        System.out.print("(");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                System.out.print(", ");
            }
        
            System.out.print("(");

            for (int j = 0; j < arr[i].length; j++) {
                if (j > 0) {
                    System.out.print(", ");
                }

                System.out.print(arr[i][j]);
            }

            System.out.print(")");
        }
        System.out.println(")");
    }

}
