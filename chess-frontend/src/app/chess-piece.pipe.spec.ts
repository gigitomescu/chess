import { ChessPiecePipe } from './chess-piece.pipe';

describe('ChessPiecePipe', () => {
  it('create an instance', () => {
    const pipe = new ChessPiecePipe();
    expect(pipe).toBeTruthy();
  });
});
