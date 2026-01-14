package com.gdt.chess.service;

import com.gdt.chess.model.*;
import com.gdt.chess.model.enums.PieceType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MoveValidationService {

    public boolean isValidMove(Game game, Move move) {
        // This is a simplified implementation
        // A complete implementation would check piece-specific movement rules,
        // check for check/checkmate, castling, en passant, etc.
        
        Piece piece = move.getPiece();
        Position from = move.getFrom();
        Position to = move.getTo();
        Board board = game.getBoard();
        
        // Basic checks
        if (!board.isPositionValid(to)) {
            return false;
        }
        
        Piece targetPiece = board.getPiece(to);
        if (targetPiece != null && targetPiece.getColor() == piece.getColor()) {
            return false; // Can't capture own piece
        }
        
        // Delegate to piece-specific validation methods
        switch (piece.getType()) {
            case PAWN:
                return isValidPawnMove(board, from, to, piece);
            case KNIGHT:
                return isValidKnightMove(from, to);
            case BISHOP:
                return isValidBishopMove(board, from, to);
            case ROOK:
                return isValidRookMove(board, from, to);
            case QUEEN:
                return isValidQueenMove(board, from, to);
            case KING:
                return isValidKingMove(from, to);
            default:
                return false;
        }
    }
    
    // Piece-specific movement validation methods
    private boolean isValidPawnMove(Board board, Position from, Position to, Piece pawn) {
        int rowDiff = to.getRow() - from.getRow();
        int colDiff = Math.abs(to.getCol() - from.getCol());
        
        // Determine direction: WHITE moves up (negative row), BLACK moves down (positive row)
        int direction = pawn.getColor() == com.gdt.chess.model.enums.Color.WHITE ? -1 : 1;
        
        Piece targetPiece = board.getPiece(to);
        
        // Forward move (no capture)
        if (colDiff == 0 && targetPiece == null) {
            // First move: can move 2 squares (WHITE from row 6, BLACK from row 1)
            boolean isFirstMove = (pawn.getColor() == com.gdt.chess.model.enums.Color.WHITE && from.getRow() == 6) ||
                                  (pawn.getColor() == com.gdt.chess.model.enums.Color.BLACK && from.getRow() == 1);
            if (isFirstMove && rowDiff == 2 * direction) {
                // Verify path is clear for 2-square move
                Position intermediatePos = new Position(from.getRow() + direction, from.getCol());
                return board.getPiece(intermediatePos) == null;
            }
            // Regular move: 1 square forward
            return rowDiff == direction;
        }
        
        // Diagonal capture: exactly 1 square diagonally
        if (colDiff == 1 && rowDiff == direction && targetPiece != null) {
            return true;
        }
        
        return false;
    }
    
    private boolean isValidKnightMove(Position from, Position to) {
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        // Knight moves in L-shape: 2 squares in one direction, 1 in perpendicular
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }
    
    private boolean isValidBishopMove(Board board, Position from, Position to) {
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        
        // Bishop moves diagonally
        if (rowDiff != colDiff || rowDiff == 0) {
            return false;
        }
        
        // Check path is clear
        return isPathClear(board, from, to);
    }
    
    private boolean isValidRookMove(Board board, Position from, Position to) {
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        
        // Rook moves horizontally or vertically
        if ((rowDiff == 0 && colDiff > 0) || (colDiff == 0 && rowDiff > 0)) {
            // Check path is clear
            return isPathClear(board, from, to);
        }
        return false;
    }
    
    private boolean isValidQueenMove(Board board, Position from, Position to) {
        // Queen moves like rook (straight) or bishop (diagonal)
        return isValidRookMove(board, from, to) || isValidBishopMove(board, from, to);
    }
    
    private boolean isValidKingMove(Position from, Position to) {
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        // King moves 1 square in any direction
        return rowDiff <= 1 && colDiff <= 1 && (rowDiff > 0 || colDiff > 0);
    }
    
    /**
     * Helper method to check if path between two positions is clear (no pieces in the way).
     * Used for rook, bishop, and queen moves.
     */
    private boolean isPathClear(Board board, Position from, Position to) {
        int rowStep = Integer.compare(to.getRow(), from.getRow());
        int colStep = Integer.compare(to.getCol(), from.getCol());
        
        int currentRow = from.getRow() + rowStep;
        int currentCol = from.getCol() + colStep;
        
        while (currentRow != to.getRow() || currentCol != to.getCol()) {
            Position intermediatePos = new Position(currentRow, currentCol);
            if (board.getPiece(intermediatePos) != null) {
                return false; // Path is blocked
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
        return true; // Path is clear
    }

    public List<Position> getValidMoves(Game game, Position position) {
        List<Position> validMoves = new ArrayList<>();
        // Implementation would determine all valid moves for the piece at position
        return validMoves;
    }
}
