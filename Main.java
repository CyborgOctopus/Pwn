import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

public class Main {
   
    private static long[] ROOK_MASKS = {	
        0x7E80808080808000L, 0x3E40404040404000L, 0x5E20202020202000L, 0x6E10101010101000L, 0x7608080808080800L, 0x7A04040404040400L, 0x7C02020202020200L, 0x7E01010101010100L, 0x007E808080808000L, 0x003E404040404000L, 0x005E202020202000L, 0x006E101010101000L, 0x0076080808080800L, 0x007A040404040400L, 0x007C020202020200L, 0x007E010101010100L, 0x00807E8080808000L, 0x00403E4040404000L, 0x00205E2020202000L, 0x00106E1010101000L, 0x0008760808080800L, 0x00047A0404040400L, 0x00027C0202020200L, 0x00017E0101010100L, 0x0080807E80808000L, 0x0040403E40404000L, 0x0020205E20202000L, 0x0010106E10101000L, 0x0008087608080800L, 0x0004047A04040400L, 0x0002027C02020200L, 0x0001017E01010100L, 0x008080807E808000L, 0x004040403E404000L, 0x002020205E202000L, 0x001010106E101000L, 0x0008080876080800L, 0x000404047A040400L, 0x000202027C020200L, 0x000101017E010100L, 0x00808080807E8000L, 0x00404040403E4000L, 0x00202020205E2000L, 0x00101010106E1000L, 0x0008080808760800L, 0x00040404047A0400L, 0x00020202027C0200L, 0x00010101017E0100L, 0x0080808080807E00L, 0x0040404040403E00L, 0x0020202020205E00L, 0x0010101010106E00L, 0x0008080808087600L, 0x0004040404047A00L, 0x0002020202027C00L, 0x0001010101017E00L, 0x008080808080807EL, 0x004040404040403EL, 0x002020202020205EL, 0x001010101010106EL, 0x0008080808080876L, 0x000404040404047AL, 0x000202020202027CL, 0x000101010101017EL
    };

    private static long[] BISHOP_MASKS = {
        0x0040201008040200L, 0x0020100804020000L, 0x0050080402000000L, 0x0028440200000000L, 0x0014224000000000L, 0x000A102040000000L, 0x0004081020400000L, 0x0002040810204000L, 0x0000402010080400L, 0x0000201008040200L, 0x0000500804020000L, 0x0000284402000000L, 0x0000142240000000L, 0x00000A1020400000L, 0x0000040810204000L, 0x0000020408102000L, 0x0040004020100800L, 0x0020002010080400L, 0x0050005008040200L, 0x0028002844020000L, 0x0014001422400000L, 0x000A000A10204000L, 0x0004000408102000L, 0x0002000204081000L, 0x0020400040201000L, 0x0010200020100800L, 0x0008500050080400L, 0x0044280028440200L, 0x0022140014224000L, 0x00100A000A102000L, 0x0008040004081000L, 0x0004020002040800L, 0x0010204000402000L, 0x0008102000201000L, 0x0004085000500800L, 0x0002442800284400L, 0x0040221400142200L, 0x0020100A000A1000L, 0x0010080400040800L, 0x0008040200020400L, 0x0008102040004000L, 0x0004081020002000L, 0x0002040850005000L, 0x0000024428002800L, 0x0000402214001400L, 0x004020100A000A00L, 0x0020100804000400L, 0x0010080402000200L, 0x0004081020400000L, 0x0002040810200000L, 0x0000020408500000L, 0x0000000244280000L, 0x0000004022140000L, 0x00004020100A0000L, 0x0040201008040000L, 0x0020100804020000L, 0x0002040810204000L, 0x0000020408102000L, 0x0000000204085000L, 0x0000000002442800L, 0x0000000040221400L, 0x0000004020100A00L, 0x0000402010080400L, 0x0040201008040200L
    };

    public static void main(String[] args) throws InterruptedException {

        ChessBoard chessBoard = new ChessBoard();
        System.out.print(chessBoard);

        String oldBoard = "";
        System.out.println("ROOK BLOCKER PATTERNS: ");
        for (int i = 0; i < 64; i++) {
            for (long pattern : ChessBoard.getRowColBlockerPatterns(i)) {
                chessBoard.zeroBoard();
                chessBoard.setBoard(pattern, 0);
                //Thread.sleep(500);
                if (!chessBoard.toString().equals(oldBoard)) {
                    System.out.println(chessBoard);
                }
                oldBoard = chessBoard.toString();
            }
        }

        System.out.println("ROOK MOSKS:");
        // for (long mask : ROOK_MASKS) {
        //     chessBoard.zeroBoard();
        //     chessBoard.setBoard(mask, 0);
        //     Thread.sleep(500);
        //     System.out.println(chessBoard);
        // }  

        System.out.println("BOSHOP MOSKS:");
        // for (long mask : BISHOP_MASKS) {
        //     chessBoard.zeroBoard();
        //     chessBoard.setBoard(mask, 2);
        //     Thread.sleep(500);
        //     System.out.println(chessBoard);
        // }

        // System.out.println("BLOCKED BISHOP MOVES: ");
        // for (long move : ChessBoard.getBishopMoves()) {
        //     chessBoard.zeroBoard();
        //     chessBoard.setBoard(move, 2);
        //     Thread.sleep(500);
        //     System.out.println(chessBoard);        
        // }

        // System.out.println("BLOCKED ROOK MOVES: ");
        // for (long move : ChessBoard.getRookMoves()) {
        //     chessBoard.zeroBoard();
        //     chessBoard.setBoard(move, 0);
        //     Thread.sleep(250);
        //     System.out.println(chessBoard);        
        // }
        

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

        System.out.println("ROW AND COLUMN MASKS: ");

        // for (long move : ChessBoard.getRowColMasks()) {
        //     System.out.println(String.format("%064d", new BigInteger(Long.toBinaryString(move))));
        //     chessBoard.zeroBoard();
        //     chessBoard.setBoard(move, 0);
        //     Thread.sleep(500);
        //     System.out.println(chessBoard);        
        // }

        System.out.println("DIAGONAL MASKS: ");
        
        // for (long move : ChessBoard.getDiagonalMasks()) {
        //     System.out.println(String.format("%064d", new BigInteger(Long.toBinaryString(move))));
        //     chessBoard.zeroBoard();
        //     chessBoard.setBoard(move, 2);
        //     Thread.sleep(500);
        //     System.out.println(chessBoard);        
        // }

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
