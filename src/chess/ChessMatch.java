package chess;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	
	public ChessMatch() {
		board = new Board(8,8);
		initialSetup();
	}
	
	public ChessPiece[][] getPieces(){
		ChessPiece[][] mat= new ChessPiece[board.getRows()][board.getColumns()];
		for(int i=0; i<board.getRows();i++) {
			for(int j=0;j<board.getColumns();j++) {
				mat[i][j] =(ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}
	
	private void placeNewPiece(char column,int row, Piece piece) {
		board.placePiece(new ChessPosition(row,column).toPosition(), piece);
	}
	
	private void initialSetup() {
		placeNewPiece('a',5,new Rook(board, Color.white));
		//board.placePiece(new Position(4,5),new King(board, Color.white));
	}
}
