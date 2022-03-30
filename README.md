# REST-API для прохождения игры "Крестики Нолики"

**http://localhost:8080/gameplay**

**POST (/create-game)** - Создать новую игру.     
**Parameters:**  
**firstPlayerName** - имя первого игрока;     
**secondPlayerName** - имя второго игрока.

**PUT (/make-move/gameId)** - Сделать ход в игре.   
**Parameters:**     
**x** - номер ячейки по горизонтали (от 1 до 3);    
**y** - номер ячейки по вертикали (от 1 до 3).
 
**GET (/current-game-state/gameId)** - Получить текущее состояние действующей игры. 

**GET (/replay-game)** - Воспроизвести ход игры по файлу xml или json.  
**Parameters:**  
**fileName** - имя файла в формате fileName.xml или fileName.json 
