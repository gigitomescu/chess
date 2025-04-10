package com.gdt.chess.model;
import com.gdt.chess.model.enums.Color;
import lombok.Getter; 

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter    
public class Game {
    private String id;
    private Board board;
    private Color currentTurn;
    private List<Move> moveHistory;
    private GameStatus status;
    
    public enum GameStatus {
        ACTIVE, CHECK, CHECKMATE, STALEMATE, DRAW
    }

    public Game() {
        this.id = UUID.randomUUID().toString();
        this.board = new Board();
        this.currentTurn = Color.WHITE; // White always starts
        this.moveHistory = new ArrayList<>();
        this.status = GameStatus.ACTIVE;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public Board getBoard() {
        return board;
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }

    public List<Move> getMoveHistory() {
        return moveHistory;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    // Game logic methods
    public void makeMove(Move move) {
        board.movePiece(move);
        moveHistory.add(move);
        switchTurn();
        updateGameStatus();
    }

    private void switchTurn() {
        currentTurn = (currentTurn == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private void updateGameStatus() {
        // This would be implemented with checks for check, checkmate, etc.
        // Simplified for now
    }
}
