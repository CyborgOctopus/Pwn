import java.math.BigInteger;

public class Main {
   
    public static void main(String[] args) {

        ChessBoard chessBoard = new ChessBoard();
        System.out.print(chessBoard);

        System.out.println("KING MOVES: ");

        for (long move : ChessBoard.getKingMoves()) {
            //System.out.println(String.format("%064d", new BigInteger(Long.toBinaryString(kingMove))));
            chessBoard.zeroBoard();
            chessBoard.setBoard(move, 4);
            System.out.println(chessBoard);
        }

        System.out.println("WHITE PAWN MOVES: ");

        for (long move : ChessBoard.getWhitePawnMoves()) {
            //System.out.println(String.format("%064d", new BigInteger(Long.toBinaryString(pawnMove))));
            //chessBoard.zeroBoard();
            //chessBoard.setBoard(move, 5);
            //System.out.println(chessBoard);
        }

        System.out.println("BLACK PAWN MOVES: ");

        for (long move : ChessBoard.getBlackPawnMoves()) {
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

}
