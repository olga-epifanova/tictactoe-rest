CREATE TABLE game
(
    id     BIGINT PRIMARY KEY AUTO_INCREMENT,
    status VARCHAR(20)
);
CREATE TABLE player
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    number     INT,
    name       VARCHAR(50),
    is_current BOOLEAN,
    game_id    BIGINT,
    FOREIGN KEY (game_id) REFERENCES game (id)
);
CREATE TABLE step
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    number    INT,
    x         INT,
    y         INT,
    player_id BIGINT,
    game_id   BIGINT,
    FOREIGN KEY (player_id) REFERENCES player (id),
    FOREIGN KEY (game_id) REFERENCES game (id)
);