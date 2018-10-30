/**
 *
 */
package org.ynov.b2.stratego.server.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.ynov.b2.stratego.server.jpa.model.MoveResult;
import org.ynov.b2.stratego.server.jpa.model.Player;
import org.ynov.b2.stratego.server.jpa.repository.PlayerRepository;
import org.ynov.b2.stratego.server.socket.messenger.GameMessenger;
import org.ynov.b2.stratego.server.socket.model.StartGame;
import org.ynov.b2.stratego.server.socket.model.Turn;

/**
 * @author sebboursier
 *
 */
@Service
public class IaService extends StompSessionHandlerAdapter {

	private Map<Integer, Integer> games = new HashMap<>();

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private GameMessenger gameMessenger;

	@Autowired
	private BouchonService bouchonService;

	private StompSession session;

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		System.out.println("CONNECTED");
		this.session = session;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void connect() {
		List<Transport> transports = new ArrayList<>();
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(transports));
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());
		stompClient.connect("ws://localhost:8082/socket", this);
	}

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return Turn.class;
	}

	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
			Throwable exception) {
		System.out.println("EXCEPTION");
		exception.printStackTrace();
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		final Turn turn = (Turn) payload;
		play(turn);
	}

	@Override
	public void handleTransportError(StompSession session, Throwable exception) {
		System.out.println("ERROR");
		exception.printStackTrace();
	}

	private void play(final Turn turn) {
		if (games.containsKey(turn.getIdGame())) {
			if (turn.getResult().equals(MoveResult.DEFEAT) || turn.getResult().equals(MoveResult.VICTORY)) {
				games.remove(turn.getIdGame());
			} else {
				final Player player = playerRepository.getOne(games.get(turn.getIdGame()));
				if (turn.getTurn() % 2 != player.getNum()) {
					gameMessenger.play(player.getId(), bouchonService.generateMove());
				}
			}
		}
	}

	public void start(final StartGame startGame) {
		games.put(startGame.getIdGame(), startGame.getIdPlayer());
		session.subscribe("/listen/game/" + startGame.getIdGame(), this);
		if (startGame.getNum() == 0) {
			final Turn turn = new Turn();
			turn.setTurn(-1);
			turn.setIdGame(startGame.getIdGame());
			handleFrame(null, turn);
		}
	}

}
