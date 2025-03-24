package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
import chess.pieces.King;
import chess.pieces.Pawn;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	private int turn;
	private Color currentPLayer;
	
	private List<ChessPiece> piecesOnBoard = new ArrayList<>();
	private List<ChessPiece> capturedPieces = new ArrayList<>();
	
	private boolean check;
	private boolean checkMate;
	
	public ChessMatch() {
		board = new Board(8,8);
		turn =1;
		currentPLayer = Color.white;
		initialSetup();
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public int getTurn() {
		return turn;
	}


	public boolean getCheck() {
		return check;
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
		
		if(testCheck(currentPLayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException(" you cannot put yourself in check! ");
			
		}
		check = (testCheck(oponent(currentPLayer))) ? true: false;
		
		if(testCheckMate(oponent(currentPLayer))) {
			checkMate = true;
		}
		else {
			nextTurn();
		}
		
		return (ChessPiece) capturedPiece;
	}
	
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p =(ChessPiece) board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		
		if(capturedPiece != null) {
			piecesOnBoard.remove(capturedPiece);
			capturedPieces.add((ChessPiece) capturedPiece);
		}
		board.placePiece(target,p);
		return capturedPiece;
	}

	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p =(ChessPiece) board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(source, p);
		
		if(capturedPiece != null) {
			board.placePiece(target, capturedPiece);
			capturedPieces.remove(capturedPiece);
			piecesOnBoard.add((ChessPiece) capturedPiece);
		}
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
	
	private Color oponent(Color color) {
		return (color == color.white) ? color.black : color.white;
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p : list) {
			if(p instanceof King) {
				return (ChessPiece) p;
			}
		}
		throw new IllegalStateException(" there is no " + color+ "in the game! ");
	}

	private boolean testCheck(Color color) {
		Position KingPosition = king(color).getChessPosition().toPosition();
		List<Piece> oponentPieces = piecesOnBoard.stream().filter(x -> ((ChessPiece)x).getColor() == oponent(color)).collect(Collectors.toList());
		for(Piece p: oponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if(mat[KingPosition.getRow()][KingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testCheckMate(Color color) {
		if(!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p: list) {
			boolean[][]mat = p.possibleMoves();
			for(int i=0;i<board.getRows();i++) {
				for(int j=0;j<board.getColumns();j++) {
					if(mat[i][j]) {
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i,j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if(!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	private void placeNewPiece(char column,int row, Piece piece) {
		board.placePiece(new ChessPosition(row,column).toPosition(), piece);
		piecesOnBoard.add((ChessPiece) piece);
	}
	
	private void initialSetup() {
		 placeNewPiece('a', 1, new Rook(board, Color.white));
         placeNewPiece('e', 1, new King(board, Color.white));
         placeNewPiece('h', 1, new Rook(board, Color.white));
         placeNewPiece('a', 2, new Pawn(board, Color.white));
         placeNewPiece('b', 2, new Pawn(board, Color.white));
         placeNewPiece('c', 2, new Pawn(board, Color.white));
         placeNewPiece('d', 2, new Pawn(board, Color.white));
         placeNewPiece('e', 2, new Pawn(board, Color.white));
         placeNewPiece('f', 2, new Pawn(board, Color.white));
         placeNewPiece('g', 2, new Pawn(board, Color.white));
         placeNewPiece('h', 2, new Pawn(board, Color.white));
        
         placeNewPiece('a', 8, new Rook(board, Color.black));
         placeNewPiece('e', 8, new King(board, Color.black));
         placeNewPiece('h', 8, new Rook(board, Color.black));
         placeNewPiece('a', 7, new Pawn(board, Color.black));
         placeNewPiece('b', 7, new Pawn(board, Color.black));
         placeNewPiece('c', 7, new Pawn(board, Color.black));
         placeNewPiece('d', 7, new Pawn(board, Color.black));
         placeNewPiece('e', 7, new Pawn(board, Color.black));
         placeNewPiece('f', 7, new Pawn(board, Color.black));
         placeNewPiece('g', 7, new Pawn(board, Color.black));
         placeNewPiece('h', 7, new Pawn(board, Color.black));
	}
}
