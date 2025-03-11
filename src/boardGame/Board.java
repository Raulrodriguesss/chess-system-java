package boardGame;

public class Board {

	private int rows;
	private int columns;
	private Piece[][] pieces;
	
	public Board(int rows, int columns) {
		if(rows<=0 ||columns<=0) {
			throw new BoardException("error creating board! it1s needed at least 1 row and 1 column");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public Piece piece(int row, int column) {
		if(!positionExist(row, column)) {
			throw new BoardException("position dont exist!");
		}
		return pieces[row][column];
	}
	
	public Piece piece(Position position) {
		if(!positionExist(position)) {
			throw new BoardException("position dont exist!");
		}
		return pieces[position.getRow()][position.getColumn()];
	}
	
	public Piece removePiece(Position position) {
		if(!positionExist(position)) {
			throw new BoardException("position dont exist!");
		}
		if(piece(position) ==null) {
			return null;
		}
		Piece aux=piece(position);
		aux.position=null;
		pieces[position.getRow()][position.getColumn()] = null;
		return aux;
		
	}
	
	public void placePiece(Position position,Piece piece) {
		if(thereIsAPiece(position)) {
			throw new BoardException("position "+position+" ocupied by other piece!");
		}
		pieces[position.getRow()][position.getColumn()]=piece;
		piece.position = position;
		
	}
	
	public boolean positionExist(int row,int column) {
			return (row>=0 && row<rows)&& (column>=0 &&column<columns); 
		}
		
	public boolean positionExist(Position position) {
		return positionExist(position.getRow(), position.getColumn());
	}
	
	public boolean thereIsAPiece(Position position) {
		if(!positionExist(position)) {
			throw new BoardException("position dont exist!");
		}
			return piece(position) != null;
		}
}
