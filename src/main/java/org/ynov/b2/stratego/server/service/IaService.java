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
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
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
import org.ynov.b2.stratego.server.socket.model.ResultTurn;
import org.ynov.b2.stratego.server.socket.model.StartGame;

/**
 * @author sebboursier
 *
 */
@Service
public class IaService extends StompSessionHandlerAdapter {

	private Map<Integer, Player> games = new HashMap<>();
	private Map<Integer, Subscription> subscriptions = new HashMap<>();

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
		return ResultTurn.class;
	}

	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
			Throwable exception) {
		System.out.println("EXCEPTION");
		exception.printStackTrace();
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		final ResultTurn turn = (ResultTurn) payload;
		play(turn);
	}

	@Override
	public void handleTransportError(StompSession session, Throwable exception) {
		System.out.println("ERROR");
		exception.printStackTrace();
	}

	private void play(final ResultTurn resultTurn) {
		if (games.containsKey(resultTurn.getIdGame())) {
			if (resultTurn.getResult().equals(MoveResult.DEFEAT) || resultTurn.getResult().equals(MoveResult.VICTORY)
					|| resultTurn.getResult().equals(MoveResult.SERVER_ERROR)) {
				games.remove(resultTurn.getIdGame());
				subscriptions.get(resultTurn.getIdGame()).unsubscribe();
				subscriptions.remove(resultTurn.getIdGame());
			} else {
				final Player player = games.get(resultTurn.getIdGame());
				if (resultTurn.getTurn() % 2 != player.getNum()) {
					gameMessenger.play(resultTurn.getIdGame(), bouchonService.generateReceiveTurn(player));
				}
			}
		}
	}

	public void start(final StartGame startGame) {
		final Player player = playerRepository.findByGameIdAndTeamUuid(startGame.getIdGame(), startGame.getUuidTeam());
		games.put(startGame.getIdGame(), player);
		subscriptions.put(startGame.getIdGame(), session.subscribe("/listen/game/" + startGame.getIdGame(), this));
		if (startGame.getNum() == 0) {
			final ResultTurn turn = new ResultTurn();
			turn.setTurn(-1);
			turn.setIdGame(startGame.getIdGame());
			handleFrame(null, turn);
		}
	}

}
