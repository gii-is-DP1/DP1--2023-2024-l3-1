package org.springframework.samples.petclinic.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.samples.petclinic.dto.GameCreateDto;
import org.springframework.samples.petclinic.model.Card;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.model.GamePlayer;
import org.springframework.samples.petclinic.model.Hand;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.samples.petclinic.model.enums.Icon;
import org.springframework.samples.petclinic.repositories.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final GamePlayerService gamePlayerService;
    private final CardService cardService;
    private final HandService handService;

    @Autowired
    public GameService(GameRepository gameRepository,
        HandService handService,
        GamePlayerService gamePlayerService,
        CardService cardService
    ) {
        this.gameRepository = gameRepository;
        this.handService = handService;
        this.gamePlayerService = gamePlayerService;
        this.cardService = cardService;
    }

    @Transactional(readOnly = true)
    public Optional<Game> findGame(String id) {
        return gameRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Game> findByNameGame(String name) {
        return this.gameRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Game> getAllGamesOfPlayer(Player player) {
        Optional<List<Game>> op_games = this.gameRepository.findGamesByPlayerId(player.getId());
        return op_games.isPresent() ? op_games.get() : new ArrayList<Game>();
    }

    /**
	 * Games that are in lobby or started
	 */
	@Transactional(readOnly = true)
    public Optional<Game> getCurrentGameOfPlayer(Player player) {
        List<Game> query = this.getAllGamesOfPlayer(player).stream().filter(g -> !g.isFinished()).toList();

        return query.size() > 0 ? Optional.of(query.get(0)) : Optional.empty();
    }

    @Transactional(readOnly = true)
    public List<Game> getGamesByPlayerUsername(String username) {
        Optional<List<Game>> op_games = this.gameRepository.findGamesByPlayerUsername(username);
        return op_games.isPresent() ? op_games.get() : new ArrayList<Game>();
    }

    /**
	 * Games that are in lobby or started
	 */
    @Transactional(readOnly = true)
    public Optional<Game> getCurrentGameByUsername(String username) {
        return Optional.ofNullable(this.getGamesByPlayerUsername(username).stream().filter(g -> !g.isFinished()).toList().get(0));
    }

    @Transactional
    public Game saveGame(Game game) {
        return this.gameRepository.save(game);
    }

    @Transactional
    public Game updateGame(@Valid GameCreateDto payload, Game gameToUpdate) {
        String newGameName = payload.getName();
        Integer newGameMaxPlayers = payload.getMax_players();

        if (newGameName != null && !newGameName.isBlank()) {
            gameToUpdate.setName(newGameName);
        }
        if (newGameMaxPlayers != null) {
            gameToUpdate.setMax_players(newGameMaxPlayers);
        }

        this.gameRepository.save(gameToUpdate);
        return gameToUpdate;
    }

    @Transactional(readOnly = true)
    public Optional<List<Game>> findAll() {
        List<Game> games = gameRepository.findAll();
        return Optional.ofNullable(games);
    }

    @Transactional
    public Game createGame(GameCreateDto payload, Player creator) {
        Game game = new Game();
        game.setName(payload.getName());
        GamePlayer gp = this.gamePlayerService.createGamePlayerCreator(creator);
        // Necesario puesto que Spring requiere de que la entidad exista en la base de datos
        // antes de asociarlas
        this.saveGame(game);
        gp.setGame(game);
        game.setCreator(gp);
        this.gamePlayerService.save(gp);
        this.saveGame(game);

        return game;
    }

    @Transactional
    public void removePlayerFromGame(Game game, Player player) {
        if (game.isFinished()) {
            return;
        }

        if (game.getCreator().getPlayerId() == player.getId()) {
            this.gameRepository.delete(game);
        } else {
            game.getGame_players().removeIf(gp -> gp.getPlayerId() == player.getId());
            this.saveGame(game);
        }
    }

    @Transactional
    public void addPlayerToGame(Game game, Player player) {
        GamePlayer gp = this.gamePlayerService.createGamePlayer(player, game);
        game.getGame_players().add(gp);
        this.saveGame(game);
    }
    
    @Transactional
    public Game startGame(Game game) {
        List<GamePlayer> gps = game.getGame_players();
        List<Card> cards = this.cardService.createCards();
        Integer last_card_index = cards.size() - 1;
        Card initial_card = cards.get(last_card_index);
        cards.remove(initial_card);
        game.setInitial_card(initial_card);

        Integer firstIndex = 0;
        Integer totalPlayers = gps.size();
        Integer allCardsToDeal = cards.size();
        Integer cardsPerPlayer = allCardsToDeal / totalPlayers;

        for (GamePlayer gamePlayer : gps) {
            Hand hand = this.handService.createHand(cards.subList(firstIndex, cardsPerPlayer));
            gamePlayer.setHand(hand);
            this.gamePlayerService.save(gamePlayer);

            firstIndex += cardsPerPlayer;
            cardsPerPlayer += cardsPerPlayer;
        }

        // Eliminar las cartas que sobran
        try {
            this.cardService.deleteAll(cards.subList(firstIndex, cardsPerPlayer));
        } catch (Exception _e) {}
        
        game.setStart(LocalDateTime.now());
        return this.saveGame(game);
    }

    @Transactional
    public void playFigure(GamePlayer gp, Icon icon) throws NotFoundException {
        try {
            Optional<Card> op_player_card = gp.getHand().getCurrentCard();

            if (op_player_card.isPresent()) {
                Card player_card = op_player_card.get();
                if (player_card.hasIcon(icon)) {
                    return;
                } else {
                    throw new NotFoundException();
                }
            }
        } catch (NotFoundException _e) {
            Hand h = gp.getHand();
            this.handService.sumStrike(h);

            throw _e;
        }
    }
}
