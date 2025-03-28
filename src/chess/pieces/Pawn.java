package chess.pieces;

import boardGame.Board;
import boardGame.Position;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

	public Pawn(Board board, Color color) {
 		super(board, color);
 	}
 
 	@Override
 	public boolean[][] possibleMoves() {
 		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
 		
 		Position p = new Position(0, 0);
 
 		if (getColor() == Color.white) {
 			p.setValues(position.getRow() - 1, position.getColumn());
 			if (getBoard().positionExist(p) && !getBoard().thereIsAPiece(p)) {
 				mat[p.getRow()][p.getColumn()] = true;
 			}
 			p.setValues(position.getRow() - 2, position.getColumn());
 			Position p2 = new Position(position.getRow() - 1, position.getColumn());
 			if (getBoard().positionExist(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExist(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
 				mat[p.getRow()][p.getColumn()] = true;
 			}
 			p.setValues(position.getRow() - 1, position.getColumn() - 1);
 			if (getBoard().positionExist(p) && isThereAOponentPiece(p)) {
 				mat[p.getRow()][p.getColumn()] = true;
 			}			
 			p.setValues(position.getRow() - 1, position.getColumn() + 1);
 			if (getBoard().positionExist(p) && isThereAOponentPiece(p)) {
 				mat[p.getRow()][p.getColumn()] = true;
 			}			
 		}
 		else {
 			p.setValues(position.getRow() + 1, position.getColumn());
 			if (getBoard().positionExist(p) && !getBoard().thereIsAPiece(p)) {
 				mat[p.getRow()][p.getColumn()] = true;
 			}
 			p.setValues(position.getRow() + 2, position.getColumn());
 			Position p2 = new Position(position.getRow() + 1, position.getColumn());
 			if (getBoard().positionExist(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExist(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
 				mat[p.getRow()][p.getColumn()] = true;
 			}
 			p.setValues(position.getRow() + 1, position.getColumn() - 1);
 			if (getBoard().positionExist(p) && isThereAOponentPiece(p)) {
 				mat[p.getRow()][p.getColumn()] = true;
 			}			
 			p.setValues(position.getRow() + 1, position.getColumn() + 1);
 			if (getBoard().positionExist(p) && isThereAOponentPiece(p)) {
 				mat[p.getRow()][p.getColumn()] = true;
 			}	
 		}
 		return mat;
 	}
 	
 	@Override
 	public String toString() {
 		return "P";
 	}
 	
}
