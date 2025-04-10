import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ChessBoardComponent } from './chess-board/chess-board.component';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-root',
  standalone: true,  // This flag marks the component as standalone
  imports: [RouterOutlet, ChessBoardComponent, CommonModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'chess-frontend';
}
