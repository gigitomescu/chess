import { Component, OnInit } from '@angular/core';
import { CommonModule, NgClass } from '@angular/common';
import { ChessService } from '../chess.service';
import { ChessPiecePipe } from '../chess-piece.pipe'; 

@Component({
  selector: 'app-chess-board',
  standalone: true,
  imports: [CommonModule, NgClass, ChessPiecePipe],
  templateUrl: './chess-board.component.html',
  styleUrls: ['./chess-board.component.css']
})
export class ChessBoardComponent implements OnInit {
  board: any[][] = [];
  selectedSquare: { row: number, col: number } | null = null;
  
  constructor(private chessService: ChessService) { }

  ngOnInit(): void {
    this.loadBoard();
  }

  loadBoard(): void {
    this.chessService.getBoard().subscribe(data => {
      console.log('Board data loaded:', data);
      this.board = data.squares;
    });
  }

  onSquareClick(row: number, col: number): void {
    if (this.selectedSquare) {
      // If a square was already selected, try to make a move
      this.chessService.makeMove({
        fromRow: this.selectedSquare.row,
        fromCol: this.selectedSquare.col,
        toRow: row,
        toCol: col
      }).subscribe(result => {
        this.loadBoard();
        this.selectedSquare = null;
      });
    } else {
      // First selection - if the square has a piece, select it
      if (this.board[row][col]?.piece) {
        this.selectedSquare = { row, col };
      }
    }
  }

  getSquareClass(row: number, col: number): string {
    const isLight = (row + col) % 2 === 0;
    let classes = isLight ? 'square-light' : 'square-dark';
    
    if (this.selectedSquare && 
        this.selectedSquare.row === row && 
        this.selectedSquare.col === col) {
      classes += ' selected';
    }
    
    return classes;
  }

  getPieceClass(piece: any): string {
    if (!piece) return '';
    return `piece ${piece.color}-${piece.type}`;
  }
}
