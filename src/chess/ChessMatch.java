package chess;

import java.util.ArrayList;
import java.util.List;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	private int turn;
	private Color currentPLayer;
	
	private List<ChessPiece> piecesOnBoard = new ArrayList<>();
	private List<ChessPiece> capturedPieces = new ArrayList<>();
	
	public ChessMatch() {
		board = new Board(8,8);
		turn =1;
		currentPLayer = Color.white;
		initialSetup();
	}
	
	
	
	public int getTurn() {
		return turn;
	}



	public Color getCurrentPLayer() {
		return currentPLayer;
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
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source,target);
		Piece capturedPiece = makeMove(source,target);
		nextTurn();
		return (ChessPiece) capturedPiece;
	}
	
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);
		
		if(capturedPiece != null) {
			piecesOnBoard.remove(capturedPiece);
			capturedPieces.add((ChessPiece) capturedPiece);
		}
		board.placePiece(target,p);
		return capturedPiece;
	}

	private void validateSourcePosition(Position source) {
		if(!board.thereIsAPiece(source)) {
			throw new ChessException(" source position is not valid! ");
		}
		if(currentPLayer !=(((ChessPiece) board.piece(source)).getColor())) {
			throw new ChessException("the chosen piece is not yours");
		}
		if(!board.piece(source).isThereAnyPossibleMove()) {
			throw new ChessException(" there is no possible moves for the chosen piece! ");
		}
	}
	
	private void validateTargetPosition(Position source,Position target) {
		if(!board.piece(source).possibleMove(target)) {
			throw new ChessException("target position is not valid for this piece");
		}
	}
	
	private void nextTurn() {
		turn++;
		currentPLayer = (currentPLayer==Color.white) ? Color.black : Color.white;
	}

	private void placeNewPiece(char column,int row, Piece piece) {
		board.placePiece(new ChessPosition(row,column).toPosition(), piece);
		piecesOnBoard.add((ChessPiece) piece);
	}
	
	private void initialSetup() {
		placeNewPiece('c', 1, new Rook(board, Color.white));
        placeNewPiece('c', 2, new Rook(board, Color.white));
        placeNewPiece('d', 2, new Rook(board, Color.white));
        placeNewPiece('e', 2, new Rook(board, Color.white));
        placeNewPiece('e', 1, new Rook(board, Color.white));
        placeNewPiece('d', 1, new King(board, Color.white));

        placeNewPiece('c', 7, new Rook(board, Color.black));
        placeNewPiece('c', 8, new Rook(board, Color.black));
        placeNewPiece('d', 7, new Rook(board, Color.black));
        placeNewPiece('e', 7, new Rook(board, Color.black));
        placeNewPiece('e', 8, new Rook(board, Color.black));
        placeNewPiece('d', 8, new King(board, Color.black));
	}
}
