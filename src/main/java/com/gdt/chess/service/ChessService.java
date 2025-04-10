package com.gdt.chess.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdt.chess.model.Game;
import com.gdt.chess.model.Move;
import com.gdt.chess.model.Piece;
import com.gdt.chess.model.Position;
import com.gdt.chess.repository.GameRepository;

@Service
public class ChessService {
    
    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private MoveValidationService moveValidationService;

    public Game createGame() {
        Game game = new Game();
        return gameRepository.save(game);
    }

    public Optional<Game> getGame(String gameId) {
        return gameRepository.findById(gameId);
    }

    public void deleteGame(String gameId) {
        gameRepository.deleteById(gameId);
    }

    public Move makeMove(String gameId, String from, String to) throws IllegalMoveException {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));
        
        Position fromPos = Position.fromChessNotation(from);
        Position toPos = Position.fromChessNotation(to);
        
        Piece piece = game.getBoard().getPiece(fromPos);
        if (piece == null) {
            throw new IllegalMoveException("No piece at position " + from);
        }
        
        if (piece.getColor() != game.getCurrentTurn()) {
            throw new IllegalMoveException("It's not " + piece.getColor() + "'s turn");
        }
        
        Move move = new Move.Builder(fromPos, toPos, piece)
                .capturedPiece(game.getBoard().getPiece(toPos))
                .build();
        
        if (!moveValidationService.isValidMove(game, move)) {
            throw new IllegalMoveException("Invalid move");
        }
        
        game.makeMove(move);
        gameRepository.save(game);
        
        return move;
    }

    public List<Move> getMoveHistory(String gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));
        return game.getMoveHistory();
    }
}