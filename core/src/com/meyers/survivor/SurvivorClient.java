package com.meyers.survivor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Queue;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.meyers.survivor.graphics.PlayerSprite;
import com.meyers.survivor.networking.ClientResponseHandler;
import com.meyers.survivor.server.SurvivorServer;
import com.meyers.survivor.server.networking.dtos.InputDTO;
import com.meyers.survivor.server.networking.dtos.PlayerDTO;
import com.meyers.survivor.server.networking.requests.JoinRequest;
import com.meyers.survivor.server.networking.requests.InputRequest;
import com.meyers.survivor.server.networking.requests.LeaveRequest;
import com.meyers.survivor.server.networking.responses.*;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meyers.survivor.helpers.Logging.logClient;
import static com.meyers.survivor.helpers.Logging.logNetwork;

public class SurvivorClient extends ApplicationAdapter {
	SpriteBatch batch;
	PlayerSprite playerSprite;
	
	Client client;
	private ClientResponseHandler handler;
	private Queue<Pair<AbstractResponse, Connection>> responsePackets;
	
	public HashMap<Integer, PlayerSprite> sprites;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		Pixmap crosshair = new Pixmap(Gdx.files.internal("images/cursor.png"));
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(crosshair, crosshair.getWidth()/2, crosshair.getHeight()/2));
		
		initClient();
	}
	
	private void initClient()
	{
		try
		{
			client = new Client();
			client.start();
			client.connect(SurvivorServer.TIMEOUT_MS, SurvivorServer.IP_ADDRESS,
					SurvivorServer.TCP_PORT, SurvivorServer.UDP_PORT);
			
			SurvivorServer.register(client);
			responsePackets = new Queue<Pair<AbstractResponse, Connection>>();
			handler = new ClientResponseHandler(this);
			sprites = new HashMap<Integer, PlayerSprite>();
			
			client.addListener(new Listener() {
				public void received (Connection connection, Object object) {
//					logNetwork("Response received: " + object.getClass().toString());
					if (object instanceof AbstractResponse)
					{
						responsePackets.addLast(new Pair<AbstractResponse, Connection>((AbstractResponse) object, connection));
					}
				}
				
			});
			
			JoinRequest req = new JoinRequest();
			client.sendUDP(req);
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		try
		{
			LeaveRequest req = new LeaveRequest();
			client.sendUDP(req);
			
			client.dispose();
			// TODO Leave Request
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		for (Map.Entry<Integer, PlayerSprite> e : sprites.entrySet()) {
			e.getValue().getTexture().dispose();
		}
		
		sprites.clear();
	}
	
	//Rendering and updates
	@Override
	public void render () {
		Gdx.gl.glClearColor(.8f, .8f, .8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		updateFromServer();
		
		batch.begin();
		updateClient(batch);
		batch.end();
		
		handleInput(Gdx.graphics.getDeltaTime());
		
	}
	
	private void updateFromServer()
	{
		if (responsePackets.isEmpty()) return;
		boolean handled = false;
		
		while(!responsePackets.isEmpty()){
			handled = false;
			
			AbstractResponse curr = responsePackets.first().getKey();
			Connection connection = responsePackets.first().getValue();
			
			if (curr instanceof PlayerJoinedResponse) {
				handled = handler.handle((PlayerJoinedResponse) curr, connection);
			} else if (curr instanceof JoinResponse) {
				handled = handler.handle((JoinResponse) curr, connection);
			} else if (curr instanceof PlayerUpdatesResponse) {
				handled = handler.handle((PlayerUpdatesResponse) curr, connection);
			} else if (curr instanceof PlayerLeftResponse) {
				handled = handler.handle((PlayerLeftResponse) curr, connection);
			}
			
			if (handled) responsePackets.removeFirst();
		}
	}
	
	
	private void updateClient(SpriteBatch batch)
	{
		for (Map.Entry<Integer, PlayerSprite> e: sprites.entrySet()) {
			PlayerSprite ps = e.getValue();
			ps.draw(batch);
		}
	}
	
	public void handleInput(float delta)
	{
		List<Integer> keycodes = new ArrayList<>();
		float x = -1, y = -1;
		
		if (Gdx.input.isKeyPressed(Input.Keys.W)) keycodes.add(Input.Keys.W);
		if (Gdx.input.isKeyPressed(Input.Keys.A)) keycodes.add(Input.Keys.A);
		if (Gdx.input.isKeyPressed(Input.Keys.S)) keycodes.add(Input.Keys.S);
		if (Gdx.input.isKeyPressed(Input.Keys.D)) keycodes.add(Input.Keys.D);
		
		boolean mouseMoved = false;
		if (Gdx.input.getDeltaX() != 0 || Gdx.input.getDeltaY() != 0) {
			mouseMoved = true;
			x = Gdx.input.getX();
			y = Gdx.input.getY();
		}
		
		if (keycodes.isEmpty() && !mouseMoved) return;
		
		InputDTO input = new InputDTO(keycodes, x, y, delta);
		InputRequest req = new InputRequest(input);
		client.sendUDP(req);
	}
	
	// ResponseFunctions
	public void addPlayer(PlayerDTO dto) {
		PlayerSprite playerSprite = new PlayerSprite(dto.getID(), new Texture(Gdx.files.internal("images/sprites/player/handgun/move/survivor-move_handgun_0.png")));
		playerSprite.setPosition(dto.getX(), dto.getY());
		sprites.put(playerSprite.getPlayerID(), playerSprite);
		this.playerSprite = playerSprite;
	}
	
	public void addAlly(PlayerDTO dto) {
		PlayerSprite allySprite = new PlayerSprite(dto.getID(), new Texture(Gdx.files.internal("images/allyShip.png")));
		allySprite.setPosition(dto.getX(), dto.getY());
		sprites.put(allySprite.getPlayerID(), allySprite);
	}
	
	public void removePlayer(PlayerDTO dto) {
		sprites.remove(dto.getID());
	}

}
