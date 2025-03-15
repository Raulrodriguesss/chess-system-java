package chess.pieces;

import boardGame.Board;
import boardGame.Position;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece {

	public Rook(Board board, Color color) {
		super(board, color);

	}

	@Override
	public String toString() {
		return "R";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

		Position p = new Position(0, 0);
		// para obter os movimentos possíveis acima
		p.setValues(position.getRow() - 1, position.getColumn());
		while (getBoard().positionExist(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() - 1);
		}
		if (getBoard().positionExist(p) && isThereAOponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// para obter os movimentos possíveis abaixo
		p.setValues(position.getRow() + 1, position.getColumn());
		while (getBoard().positionExist(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() + 1);
		}
		if (getBoard().positionExist(p) && isThereAOponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// para obter os movimentos possíveis á esquerda
		p.setValues(position.getRow(), position.getColumn() - 1);
		while (getBoard().positionExist(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() - 1);
		}
		if (getBoard().positionExist(p) && isThereAOponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// para obter os movimentos possíveis á direita
		p.setValues(position.getRow(), position.getColumn() + 1);
		while (getBoard().positionExist(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() + 1);
		}
		if (getBoard().positionExist(p) && isThereAOponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		return mat;
	}

}
