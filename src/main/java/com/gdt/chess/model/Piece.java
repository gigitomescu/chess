package com.gdt.chess.model;

import com.gdt.chess.model.enums.Color;
import com.gdt.chess.model.enums.PieceType;
import lombok.Getter; 

@Getter
public class Piece {
    private PieceType type;
    private Color color;
    private Position position;
    private boolean hasMoved;

    public Piece(PieceType type, Color color, Position position) {
        this.type = type;
        this.color = color;
        this.position = position;
        this.hasMoved = false;
    }

    // Getters and setters
    public PieceType getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    @Override
    public String toString() {
        return color + " " + type;
    }
}
