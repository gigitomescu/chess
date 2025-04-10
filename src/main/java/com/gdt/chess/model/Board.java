package com.gdt.chess.model;
import java.util.ArrayList;
import java.util.List;

import com.gdt.chess.model.enums.Color;
import com.gdt.chess.model.enums.PieceType;
import lombok.Getter;

@Getter
public class Board {
    private Piece[][] squares;
    public static final int SIZE = 8;

    public Board() {
        squares = new Piece[SIZE][SIZE];
        setupInitialPosition();
    }

    private void setupInitialPosition() {
        // Set up pawns
        for (int col = 0; col < SIZE; col++) {
            squares[1][col] = new Piece(PieceType.PAWN, Color.BLACK, new Position(1, col));
            squares[6][col] = new Piece(PieceType.PAWN, Color.WHITE, new Position(6, col));
        }

        // Set up rooks
        squares[0][0] = new Piece(PieceType.ROOK, Color.BLACK, new Position(0, 0));
        squares[0][7] = new Piece(PieceType.ROOK, Color.BLACK, new Position(0, 7));
        squares[7][0] = new Piece(PieceType.ROOK, Color.WHITE, new Position(7, 0));
        squares[7][7] = new Piece(PieceType.ROOK, Color.WHITE, new Position(7, 7));

        // Set up knights
        squares[0][1] = new Piece(PieceType.KNIGHT, Color.BLACK, new Position(0, 1));
        squares[0][6] = new Piece(PieceType.KNIGHT, Color.BLACK, new Position(0, 6));
        squares[7][1] = new Piece(PieceType.KNIGHT, Color.WHITE, new Position(7, 1));
        squares[7][6] = new Piece(PieceType.KNIGHT, Color.WHITE, new Position(7, 6));

        // Set up bishops
        squares[0][2] = new Piece(PieceType.BISHOP, Color.BLACK, new Position(0, 2));
        squares[0][5] = new Piece(PieceType.BISHOP, Color.BLACK, new Position(0, 5));
        squares[7][2] = new Piece(PieceType.BISHOP, Color.WHITE, new Position(7, 2));
        squares[7][5] = new Piece(PieceType.BISHOP, Color.WHITE, new Position(7, 5));

        // Set up queens
        squares[0][3] = new Piece(PieceType.QUEEN, Color.BLACK, new Position(0, 3));
        squares[7][3] = new Piece(PieceType.QUEEN, Color.WHITE, new Position(7, 3));

        // Set up kings
        squares[0][4] = new Piece(PieceType.KING, Color.BLACK, new Position(0, 4));
        squares[7][4] = new Piece(PieceType.KING, Color.WHITE, new Position(7, 4));
    }

    public Piece getPiece(Position position) {
        return squares[position.getRow()][position.getCol()];
    }

    public void setPiece(Position position, Piece piece) {
        squares[position.getRow()][position.getCol()] = piece;
        if (piece != null) {
            piece.setPosition(position);
        }
    }

    public boolean isPositionValid(Position position) {
        return position.getRow() >= 0 && position.getRow() < SIZE &&
               position.getCol() >= 0 && position.getCol() < SIZE;
    }

    public List<Piece> getPiecesByColor(Color color) {
        List<Piece> pieces = new ArrayList<>();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Piece piece = squares[row][col];
                if (piece != null && piece.getColor() == color) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    public void movePiece(Move move) {
        Piece piece = move.getPiece();
        setPiece(move.getFrom(), null);
        setPiece(move.getTo(), piece);
        piece.setHasMoved(true);
    }
}
