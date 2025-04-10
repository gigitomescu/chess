import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'chessPiece',
  standalone: true
})
export class ChessPiecePipe implements PipeTransform {
  transform(piece: any): string {
    if (!piece) return '';
    
    const pieceSymbols: {[key: string]: {[key: string]: string}} = {
      'white': {
        'pawn': '♙',
        'rook': '♖',
        'knight': '♘',
        'bishop': '♗',
        'queen': '♕',
        'king': '♔'
      },
      'black': {
        'pawn': '♟',
        'rook': '♜',
        'knight': '♞',
        'bishop': '♝',
        'queen': '♛',
        'king': '♚'
      }
    };
    
    return pieceSymbols[piece.color][piece.type] || '';
  }
}