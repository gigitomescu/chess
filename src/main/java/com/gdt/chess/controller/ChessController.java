package com.gdt.chess.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/chess")
@Tag(name = "Chess Controller", description = "Endpoints for managing chess games and moves")
public class ChessController {

    @Autowired
    private ChessService chessService;
    
    @Autowired
    private MoveValidationService moveValidationService;

    @Value("${api.base.url}")
    private String apiBaseUrl;
    
    @Operation(summary = "Create a new chess game", description = "Initializes a new chess game and returns its details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Game created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)))
    })
    @PostMapping("/games")
    public ResponseEntity<Game> createGame() {
        Game game = chessService.createGame();
        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    }
    
    @Operation(summary = "API base info", description = "Returns a simple status message with API base URL.")
    @GetMapping("")
    public ResponseEntity<String> base() {
        return ResponseEntity.ok("Chess API is running. API Base URL: " + apiBaseUrl);        
    }
    
    @Operation(summary = "Get game by ID", description = "Fetches a chess game by its unique identifier.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Game found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class))),
        @ApiResponse(responseCode = "404", description = "Game not found", content = @Content)
    })
    @GetMapping("/games/{gameId}")
    public ResponseEntity<Game> getGame(
            @Parameter(description = "Unique identifier of the game", example = "12345")
            @PathVariable String gameId) {
        return chessService.getGame(gameId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete game by ID", description = "Deletes a chess game by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Game deleted successfully", content = @Content)
    })
    @DeleteMapping("/games/{gameId}")
    public ResponseEntity<Void> deleteGame(
            @Parameter(description = "Unique identifier of the game to delete", example = "12345")
            @PathVariable String gameId) {
        chessService.deleteGame(gameId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Make a move", description = "Submits a move request for a given game.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Move made successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Move.class))),
        @ApiResponse(responseCode = "400", description = "Invalid move request or illegal move", content = @Content),
        @ApiResponse(responseCode = "404", description = "Game not found", content = @Content)
    })
    @PostMapping("/games/{gameId}/moves")
    public ResponseEntity<?> makeMove(
            @Parameter(description = "Unique identifier of the game", example = "12345")
            @PathVariable String gameId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Move request payload with 'from' and 'to' positions",
                required = true,
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"from\": \"e2\", \"to\": \"e4\" }"))
            )
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

    @Operation(summary = "Get move history", description = "Retrieves the list of moves made in a given game.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Moves retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Move.class))),
        @ApiResponse(responseCode = "404", description = "Game not found", content = @Content)
    })
    @GetMapping("/games/{gameId}/moves")
    public ResponseEntity<List<Move>> getMoveHistory(
            @Parameter(description = "Unique identifier of the game", example = "12345")
            @PathVariable String gameId) {
        try {
            List<Move> moves = chessService.getMoveHistory(gameId);
            return ResponseEntity.ok(moves);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get valid moves for a position", description = "Returns all valid moves for a given position in a game.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Valid moves retrieved",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Position.class))),
        @ApiResponse(responseCode = "400", description = "Invalid position or game not found", content = @Content)
    })
    @GetMapping("/games/{gameId}/valid-moves/{position}")
    public ResponseEntity<List<Position>> getValidMoves(
            @Parameter(description = "Unique identifier of the game", example = "12345")
            @PathVariable String gameId,
            @Parameter(description = "Chess notation of the position, e.g., 'e2'", example = "e2")
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
