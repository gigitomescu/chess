document.addEventListener('DOMContentLoaded', function() {
    let gameId = null;
    const board = document.getElementById('chessboard');
    let selectedPiece = null;
    
    // Initialize game
    fetch('/api/chess/games', { method: 'POST' })
        .then(response => response.json())
        .then(game => {
            gameId = game.id;
            renderBoard(game.board);
            updateStatus(game);
        });
        
    function renderBoard(boardData) {
        // Render chess board based on board data
    }
    
    function updateStatus(game) {
        // Update game status display
    }
    
    function selectPiece(position) {
        // Handle piece selection
    }
    
    function makeMove(from, to) {
        // Make API call to move piece
        fetch(`/api/chess/games/${gameId}/moves`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ from: from, to: to })
        })
        .then(response => response.json())
        .then(data => {
            // Update board based on move result
        });
    }
});
