# REST-API для прохождения игры "Крестики Нолики"

**http://localhost:8080/gameplay**

**POST (/create-game)** - Создать новую игру.     
**Parameters:**  firstPlayerName, secondPlayerName  

**PUT (/make-move/gameId)** - Сделать ход в игре.   
**Parameters:** x, y
 
**GET (/current-game-state/gameId)** - Получить текущее состояние игры. 
