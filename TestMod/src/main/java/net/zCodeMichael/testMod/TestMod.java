package net.zCodeMichael.testMod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.world.biome.Biome;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.math.BlockPos;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.registry.Registry;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TestMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger("modid");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.


		/**
		 * /wpt set {waypoint name}
		 * /wpt list
		 *
		 */
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(CommandManager
					.literal("wpt")
					.then(literal("set")
					.then(
							argument("wpt_Name", string()).executes(context -> {
								//System.out.println("HELLO " + getString(context, "wpt_Name"));
								String params = "{"+
									"\"wpt_Name\": \"" + getString(context, "wpt_Name") + "\"," +
									"\"wpt_Pos_X\": " + context.getSource().getPlayer().getPos().x +"," +
									"\"wpt_Pos_Y\": " + context.getSource().getPlayer().getPos().y +"," +
									"\"wpt_Pos_Z\": " + context.getSource().getPlayer().getPos().z +"}";
								try {
									printChat(context, "Sending json: " + params);
									postHTTP(params);
								} catch (Exception e) {
									printChat(context, "Error: " + e.toString());
									e.printStackTrace();
								}
								return 1;
							})
						)
					.executes(context -> {

						return 1;
					})));
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(CommandManager
					.literal("wpt")
					.then(literal("list").executes(context -> {
						try {
							String temp = getHTTP("http://localhost:8080/wpt/all");

							Waypoint[] waypoints = new ObjectMapper().readValue(temp, Waypoint[].class);
							for(Waypoint wpt : waypoints){
								printChat(context, format(wpt));
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
						return 1;
					}))
					);
		});

	}

	private String format(Waypoint wpt) {
		return "ID " + wpt.getWpt_ID() + ": " + wpt.getWpt_Name() + " [x:" + wpt.getWpt_Pos_X() + ", y:" + wpt.getWpt_Pos_Y() + ", z:" + wpt.getWpt_Pos_Z() + "]";
	}

	private void printChat(CommandContext<ServerCommandSource> context, String message) {
		context.getSource().getEntity().sendSystemMessage(new LiteralText(message), Util.NIL_UUID);
	}

	private String getHTTP(String urlToRead) throws Exception {
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(conn.getInputStream()))) {
			for (String line; (line = reader.readLine()) != null; ) {
				result.append(line);
			}
		}
		return result.toString();
	}

	private void postHTTP(String jsonString) throws Exception {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost("http://localhost:8080/wpt");
		StringEntity params = new StringEntity(jsonString);
//		StringEntity params = new StringEntity("{"+
//				"\"wpt_Name\": \"Test6\"," +
//				"\"wpt_Pos_X\": 0," +
//				"\"wpt_Pos_Y\": 0," +
//				"\"wpt_Pos_Z\": 0}");
		request.addHeader("content-type", "application/json; utf-8\"");
		request.setEntity(params);
		HttpResponse response = httpClient.execute(request);

	}
}
