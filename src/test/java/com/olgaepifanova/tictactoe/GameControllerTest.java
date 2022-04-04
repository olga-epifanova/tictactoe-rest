package com.olgaepifanova.tictactoe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void playGameWithWinnerTest() throws Exception {
        String firstPlayerName = "Olga";
        String secondPlayerName = "Bob";
        Long gameID = createGame(firstPlayerName, secondPlayerName);
        makeMove(2, 2, gameID).andExpect(status().isOk());
        makeMove(2, 3, gameID).andExpect(status().isOk());
        makeMove(1, 1, gameID).andExpect(status().isOk());
        makeMove(2, 2, gameID)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cell is busy"));
        makeMove(3, 3, gameID).andExpect(status().isOk());
        makeMove(4, 3, gameID)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid coordinate values"));
        makeMove(1, 3, gameID).andExpect(status().isOk());
        makeMove(1, 2, gameID).andExpect(status().isOk());
        getCurrentGameState(gameID).andExpect(status().isOk());
        makeMove(3, 1, gameID)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("WIN"))
                .andExpect(jsonPath("$.winner.playerName").value(firstPlayerName));
        getCurrentGameState(gameID)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The game is over"));
        makeMove(1, 1, gameID)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The game is over"));
    }

    @Test
    void playGameWithDrawTest() throws Exception {
        String firstPlayerName = "Olga";
        String secondPlayerName = "Bob";
        Long gameID = createGame(firstPlayerName, secondPlayerName);
        makeMove(1, 1, gameID).andExpect(status().isOk());
        makeMove(2, -2, gameID)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid coordinate values"));
        makeMove(2, 2, gameID).andExpect(status().isOk());
        makeMove(1, 2, gameID).andExpect(status().isOk());
        makeMove(1, 3, gameID).andExpect(status().isOk());
        makeMove(3, 1, gameID).andExpect(status().isOk());
        getCurrentGameState(gameID).andExpect(status().isOk());
        makeMove(3, 1, gameID)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cell is busy"));
        makeMove(2, 1, gameID).andExpect(status().isOk());
        makeMove(2, 3, gameID).andExpect(status().isOk());
        makeMove(3, 2, gameID).andExpect(status().isOk());
        makeMove(3, 3, gameID)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DRAW"));
        makeMove(3, 3, gameID)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The game is over"));
        getCurrentGameState(gameID)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The game is over"));
    }

    @Test
    void gameNotFoundTest() throws Exception {
        makeMove(2, 2, 1L)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Game not found"));

        getCurrentGameState(1L)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Game not found"));
    }

    private Long createGame(String firstPlayerName, String secondPlayerName) throws Exception {
        MvcResult result = mockMvc.perform(post("/gameplay/create-game")
                .param("firstPlayerName", firstPlayerName)
                .param("secondPlayerName", secondPlayerName))
                .andExpect(status().isOk())
                .andReturn();
        return Long.valueOf(result.getResponse().getContentAsString());
    }

    private ResultActions makeMove(int x, int y, Long gameId) throws Exception {
        return mockMvc.perform(put("/gameplay/make-move/{gameId}", gameId)
                .param("x", "" + x)
                .param("y", "" + y));
    }

    private ResultActions getCurrentGameState(Long gameId) throws Exception {
        return mockMvc.perform(get("/gameplay/current-game-state/{gameId}", gameId));
    }

}
