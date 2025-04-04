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
    
    // These methods would contain detailed logic for each piece's movement
    private boolean isValidPawnMove(Board board, Position from, Position to, Piece pawn) {
        // Simplified implementation
        return true;
    }
    
    private boolean isValidKnightMove(Position from, Position to) {
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }
    
    private boolean isValidBishopMove(Board board, Position from, Position to) {
        // Simplified implementation
        return true;
    }
    
    private boolean isValidRookMove(Board board, Position from, Position to) {
        // Simplified implementation
        return true;
    }
    
    private boolean isValidQueenMove(Board board, Position from, Position to) {
        // Simplified implementation
        return isValidRookMove(board, from, to) || isValidBishopMove(board, from, to);
    }
    
    private boolean isValidKingMove(Position from, Position to) {
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        return rowDiff <= 1 && colDiff <= 1;
    }

    public List<Position> getValidMoves(Game game, Position position) {
        List<Position> validMoves = new ArrayList<>();
        // Implementation would determine all valid moves for the piece at position
        return validMoves;
    }
}
