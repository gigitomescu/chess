package com.gdt.chess.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gdt.chess.model.Game;
import com.gdt.chess.model.Move;
import com.gdt.chess.model.Position;
import com.gdt.chess.service.ChessService;
import com.gdt.chess.service.IllegalMoveException;
import com.gdt.chess.service.MoveValidationService;

@RestController
@RequestMapping("/api/chess")
public class ChessController {

    @Autowired
    private ChessService chessService;
    
    @Autowired
    private MoveValidationService moveValidationService;

    @PostMapping("/games")
    public ResponseEntity<Game> createGame() {
        Game game = chessService.createGame();
        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    }

    @GetMapping("/games/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable String gameId) {
        return chessService.getGame(gameId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/games/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable String gameId) {
        chessService.deleteGame(gameId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/games/{gameId}/moves")
    public ResponseEntity<?> makeMove(
            @PathVariable String gameId,
            @RequestBody Map<String, String> moveRequest) {
        
        String from = moveRequest.get("from");
        String to = moveRequest.get("to");
        
        try {
            Move move = chessService.makeMove(gameId, from, to);
            return ResponseEntity.ok(move);
        } catch (IllegalMoveException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/games/{gameId}/moves")
    public ResponseEntity<List<Move>> getMoveHistory(@PathVariable String gameId) {
        try {
            List<Move> moves = chessService.getMoveHistory(gameId);
            return ResponseEntity.ok(moves);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/games/{gameId}/valid-moves/{position}")
    public ResponseEntity<List<Position>> getValidMoves(
            @PathVariable String gameId,
            @PathVariable String position) {
        
        try {
            Game game = chessService.getGame(gameId)
                    .orElseThrow(() -> new IllegalArgumentException("Game not found"));
            
            Position pos = Position.fromChessNotation(position);
            List<Position> validMoves = moveValidationService.getValidMoves(game, pos);
            
            return ResponseEntity.ok(validMoves);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}