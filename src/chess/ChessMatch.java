package chess;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	private int turn;
	private Color currentPLayer;
	
	private List<ChessPiece> piecesOnBoard = new ArrayList<>();
	private List<ChessPiece> capturedPieces = new ArrayList<>();
	
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;
	
	public ChessMatch() {
		board = new Board(8,8);
		turn =1;
		currentPLayer = Color.white;
		initialSetup();
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public ChessPiece getPromoted() {
 		return promoted;
 	}
	
	public int getTurn() {
		return turn;
	}

	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
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
		
		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		
		// #specialmove promotion
		promoted = null;
		if (movedPiece instanceof Pawn) {
			if ((movedPiece.getColor() == Color.white && target.getRow() == 0)
					|| (movedPiece.getColor() == Color.black && target.getRow() == 7)) {
				promoted = (ChessPiece) board.piece(target);
				promoted = replacePromotedPiece("Q");
			}
		}
		
		check = (testCheck(oponent(currentPLayer))) ? true: false;
		
		if(testCheckMate(oponent(currentPLayer))) {
			checkMate = true;
		}
		else {
			nextTurn();
		}

		// #specialmove en passant
		if (movedPiece instanceof Pawn
				&& (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
			enPassantVulnerable = movedPiece;
		} else {
			enPassantVulnerable = null;
		}

		return (ChessPiece) capturedPiece;
	}
	
	public ChessPiece replacePromotedPiece(String type) {
 		if (promoted == null) {
 			throw new IllegalStateException("There is no piece to be promoted");
 		}
 		if (!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")) {
 			return promoted;
 		}
 		
 		Position pos = promoted.getChessPosition().toPosition();
 		Piece p = board.removePiece(pos);
 		piecesOnBoard.remove(p);
 		
 		ChessPiece newPiece = newPiece(type, promoted.getColor());
 		board.placePiece(pos, newPiece);
 		piecesOnBoard.add(newPiece);
 		
 		return newPiece;
 	}
 	
 	private ChessPiece newPiece(String type, Color color) {
 		if (type.equals("B")) return new Bishop(board, color);
 		if (type.equals("N")) return new Knight(board, color);
 		if (type.equals("Q")) return new Queen(board, color);
 		return new Rook(board, color);
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
		
		// #specialmove castling kingside rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
			board.placePiece(targetT, rook);
			rook.increaseMoveCount();
		}

		// #specialmove castling queenside rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
			board.placePiece(targetT, rook);
			rook.increaseMoveCount();
		}
		 		
		// #specialmove en passant
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position pawnPosition;
				if (p.getColor() == Color.white) {
					pawnPosition = new Position(target.getRow() + 1, target.getColumn());
				} else {
					pawnPosition = new Position(target.getRow() - 1, target.getColumn());
				}
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add((ChessPiece) capturedPiece);
				piecesOnBoard.remove(capturedPiece);
			}
		}
		
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
		
		// #specialmove castling kingside rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(sourceT, rook);
			rook.decreaseMoveCount();
		}

		// #specialmove castling queenside rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(sourceT, rook);
			rook.decreaseMoveCount();
		}

		// #specialmove en passant
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece) board.removePiece(target);
				Position pawnPosition;
				if (p.getColor() == Color.white) {
					pawnPosition = new Position(3, target.getColumn());
				} else {
					pawnPosition = new Position(4, target.getColumn());
				}
				board.placePiece(pawnPosition, pawn);
			}
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
		 placeNewPiece('d', 1, new Queen(board, Color.white));
		 placeNewPiece('b', 1, new Knight(board, Color.white));
		 placeNewPiece('c', 1, new Bishop(board, Color.white));
         placeNewPiece('e', 1, new King(board, Color.white,this));
         placeNewPiece('g', 1, new Knight(board, Color.white));
         placeNewPiece('f', 1, new Bishop(board, Color.white));
         placeNewPiece('h', 1, new Rook(board, Color.white));
         placeNewPiece('a', 2, new Pawn(board, Color.white,this));
         placeNewPiece('b', 2, new Pawn(board, Color.white,this));
         placeNewPiece('c', 2, new Pawn(board, Color.white,this));
         placeNewPiece('d', 2, new Pawn(board, Color.white,this));
         placeNewPiece('e', 2, new Pawn(board, Color.white,this));
         placeNewPiece('f', 2, new Pawn(board, Color.white,this));
         placeNewPiece('g', 2, new Pawn(board, Color.white,this));
         placeNewPiece('h', 2, new Pawn(board, Color.white,this));
        
         placeNewPiece('a', 8, new Rook(board, Color.black));
         placeNewPiece('d', 8, new Queen(board, Color.black));
         placeNewPiece('b', 8, new Knight(board, Color.black));
         placeNewPiece('c', 8, new Bishop(board, Color.black));
         placeNewPiece('e', 8, new King(board, Color.black,this));
         placeNewPiece('f', 8, new Bishop(board, Color.black));
         placeNewPiece('g', 8, new Knight(board, Color.black));
         placeNewPiece('h', 8, new Rook(board, Color.black));
         placeNewPiece('a', 7, new Pawn(board, Color.black,this));
         placeNewPiece('b', 7, new Pawn(board, Color.black,this));
         placeNewPiece('c', 7, new Pawn(board, Color.black,this));
         placeNewPiece('d', 7, new Pawn(board, Color.black,this));
         placeNewPiece('e', 7, new Pawn(board, Color.black,this));
         placeNewPiece('f', 7, new Pawn(board, Color.black,this));
         placeNewPiece('g', 7, new Pawn(board, Color.black,this));
         placeNewPiece('h', 7, new Pawn(board, Color.black,this));
	}
}
