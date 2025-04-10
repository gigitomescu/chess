// chess.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs';
import { catchError, tap, switchMap, map } from 'rxjs/operators';

export interface ChessBoard {
  squares: any[][];
  currentPlayer: string;
  isCheck: boolean;
  isCheckmate: boolean;
}

export interface MoveRequest {
  fromRow: number;
  fromCol: number;
  toRow: number;
  toCol: number;
}

export interface MoveResult {
  valid: boolean;
  message: string;
  board: ChessBoard | null; // Allow board to be null;
  move: string;
}

@Injectable({
  providedIn: 'root'
})
export class ChessService {
  private apiUrl = '/api/chess';
  private currentGameId: string | null = null;
  
  constructor(private http: HttpClient) {
    // Create a new game when service is initialized
    this.createNewGame();
  }
  
  private createNewGame() {
    this.http.post<any>(`${this.apiUrl}/games`, {})
      .subscribe(game => {
        this.currentGameId = game.id;
        console.log('New game created with ID:', this.currentGameId);
      });
  }

  getBoard(): Observable<ChessBoard> {
    if (!this.currentGameId) {
      return this.http.post<any>(`${this.apiUrl}/games`, {}).pipe(
        tap(game => {
          this.currentGameId = game.id;
          console.log('New game created with ID:', this.currentGameId);
        }),
        switchMap(game => this.convertGameToChessBoard(game))
      );
    } else {
      return this.http.get<any>(`${this.apiUrl}/games/${this.currentGameId}`).pipe(
        switchMap(game => this.convertGameToChessBoard(game))
      );
    }
  }

  makeMove(moveRequest: MoveRequest): Observable<MoveResult> {
    if (!this.currentGameId) {
      return throwError(() => new Error('No active game'));
    }
    
    // Convert from row/col format to chess notation
    const from = this.convertPositionToNotation(moveRequest.fromRow, moveRequest.fromCol);
    const to = this.convertPositionToNotation(moveRequest.toRow, moveRequest.toCol);
    
    return this.http.post<any>(
      `${this.apiUrl}/games/${this.currentGameId}/moves`, 
      { from, to }
    ).pipe(
      switchMap(moveResult => 
        this.http.get<any>(`${this.apiUrl}/games/${this.currentGameId}`)
      ),
      switchMap(game => {
        const chessBoard = this.convertGameToBoardFormat(game);
        const result: MoveResult = {
          valid: true,
          message: 'Move successful',
          board: chessBoard,
          move: `${from}-${to}`
        };
        return of(result);
      }),
      catchError(error => {
        return of({
          valid: false,
          message: error.error || 'Invalid move',
          board: null,
          move: `${from}-${to}`
        });
      })
    );
  }

  resetGame(): Observable<ChessBoard> {
    // Delete current game if exists
    if (this.currentGameId) {
      return this.http.delete(`${this.apiUrl}/games/${this.currentGameId}`).pipe(
        switchMap(() => this.http.post<any>(`${this.apiUrl}/games`, {})),
        tap(game => this.currentGameId = game.id),
        switchMap(game => this.convertGameToChessBoard(game))
      );
    } else {
      return this.http.post<any>(`${this.apiUrl}/games`, {}).pipe(
        tap(game => this.currentGameId = game.id),
        switchMap(game => this.convertGameToChessBoard(game))
      );
    }
  }

  getMoveHistory(): Observable<string[]> {
    if (!this.currentGameId) {
      return of([]);
    }
    
    return this.http.get<any[]>(`${this.apiUrl}/games/${this.currentGameId}/moves`).pipe(
      map(moves => moves.map(move => `${move.from}-${move.to}`))
    );
  }
  
  // Helper methods to convert between formats
  private convertPositionToNotation(row: number, col: number): string {
    const files = 'abcdefgh';
    const ranks = '87654321'; // Chess board is numbered from top to bottom
    return files[col] + ranks[row];
  }
  
  private convertGameToChessBoard(game: any): Observable<ChessBoard> {
    // Convert game object from backend to ChessBoard format
    // This depends on how your backend represents the game
    const chessBoard = this.convertGameToBoardFormat(game);
    console.log('Converted chess board:', chessBoard); // Add log here
    return of(chessBoard);
  }
  
  private convertGameToBoardFormat(game: any): ChessBoard {
    // Convert game to your ChessBoard format
    // This is a placeholder implementation - you need to adjust this to match your backend's data structure
    
    const squares = Array(8).fill(null).map(() => Array(8).fill(null));
    
    // Example implementation - update this to match your backend structure
    if (game && game.board && game.board.squares) {
      for (let i = 0; i < 8; i++) {
        for (let j = 0; j < 8; j++) {
          const piece = game.board.squares[i][j];
          squares[i][j] = {
            piece: piece ? {
              type: piece.type.toLowerCase(),
              color: piece.color.toLowerCase()
            } : null
          };
        }
      }
    }
    
    return {
      squares: squares,
      currentPlayer: game.currentTurn || 'white',
      isCheck: game.inCheck || false,
      isCheckmate: game.checkmate || false
    };
  }
}
