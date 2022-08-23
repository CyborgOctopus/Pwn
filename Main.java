import java.math.BigInteger;

public class Main {
   
    public static void main(String[] args) {

        ChessBoard chessBoard = new ChessBoard();
        System.out.print(chessBoard);

        System.out.println("KING MOVES: ");

        for (long kingMove : ChessBoard.getKingMoves()) {
            System.out.println(String.format("%064d", new BigInteger(Long.toBinaryString(kingMove))));
        }

        System.out.println("WHTIE PAWN MOVES: ");

        for (long pawnMove : ChessBoard.getWhitePawnMoves()) {
            System.out.println(String.format("%064d", new BigInteger(Long.toBinaryString(pawnMove))));
        }

        System.out.println("BLACK PAWN MOVES: ");

        for (long pawnMove : ChessBoard.getBlackPawnMoves()) {
            System.out.println(String.format("%064d", new BigInteger(Long.toBinaryString(pawnMove))));        
        }

    }

}
