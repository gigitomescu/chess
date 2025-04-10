package com.gdt.chess.model;

import java.util.Objects;

import java.util.Objects;
import lombok.Getter; 

@Getter
public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    // Chess notation (e.g., "e4")
    public String toChessNotation() {
        char file = (char) ('a' + col);
        int rank = 8 - row;
        return String.format("%c%d", file, rank);
    }

    public static Position fromChessNotation(String notation) {
        if (notation == null || notation.length() != 2) {
            throw new IllegalArgumentException("Invalid chess notation: " + notation);
        }
        int col = notation.charAt(0) - 'a';
        int row = 8 - Character.getNumericValue(notation.charAt(1));
        return new Position(row, col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return toChessNotation();
    }
}

