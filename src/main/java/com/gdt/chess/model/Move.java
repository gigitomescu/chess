package com.gdt.chess.model;
import lombok.Getter; 

@Getter
public class Move {
    private final Position from;
    private final Position to;
    private final Piece piece;
    private final Piece capturedPiece;
    private final boolean isPromotion;
    private final boolean isCastling;
    private final boolean isEnPassant;

    private Move(Builder builder) {
        this.from = builder.from;
        this.to = builder.to;
        this.piece = builder.piece;
        this.capturedPiece = builder.capturedPiece;
        this.isPromotion = builder.isPromotion;
        this.isCastling = builder.isCastling;
        this.isEnPassant = builder.isEnPassant;
    }

    // Getters
    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Piece getPiece() {
        return piece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    public boolean isCastling() {
        return isCastling;
    }

    public boolean isEnPassant() {
        return isEnPassant;
    }

    @Override
    public String toString() {
        return piece.getType() + ": " + from + " -> " + to;
    }

    // Builder pattern for Move
    public static class Builder {
        private Position from;
        private Position to;
        private Piece piece;
        private Piece capturedPiece;
        private boolean isPromotion;
        private boolean isCastling;
        private boolean isEnPassant;

        public Builder(Position from, Position to, Piece piece) {
            this.from = from;
            this.to = to;
            this.piece = piece;
        }

        public Builder capturedPiece(Piece capturedPiece) {
            this.capturedPiece = capturedPiece;
            return this;
        }

        public Builder promotion() {
            this.isPromotion = true;
            return this;
        }

        public Builder castling() {
            this.isCastling = true;
            return this;
        }

        public Builder enPassant() {
            this.isEnPassant = true;
            return this;
        }

        public Move build() {
            return new Move(this);
        }
    }
}
