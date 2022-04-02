package com.zCodeMichael.ModAPI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class WaypointController {
	private final AtomicLong counter;
	private List<Waypoint> waypoints;
	
	private static final String SAVE_FILE = "./saves/waypoints.json";
	
	public WaypointController(){
		
		long init_count = 0;
		System.out.println(new File("./").getAbsolutePath());
		File file = new File(SAVE_FILE);
		file.getParentFile().mkdirs();
		try {
			if(file.createNewFile()) {
				// No save file found created a new save
				waypoints = new ArrayList<Waypoint>();
			}else {
				// Found a save file
				if(file.length() == 0) {
					waypoints = new ArrayList<Waypoint>();
				}else {
					waypoints = new ArrayList<Waypoint>(Arrays.asList(new ObjectMapper().readValue(Paths.get(SAVE_FILE).toFile(), Waypoint[].class))); // Need to account for empty file
					Waypoint last = waypoints.get(waypoints.size()-1);
					init_count = last.getWpt_ID();
					System.out.println("--------------");
					System.out.println("Loaded in waypoints");
					System.out.println("--------------");
					waypoints.forEach((obj) -> {
						System.out.println((Waypoint)obj);
					});
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		counter = new AtomicLong(init_count);
	}
	
	@PostMapping("/wpt")
	public ResponseEntity<?> postWayPoint(@RequestBody Waypoint wpt) throws JsonGenerationException, JsonMappingException, IOException{
		System.out.println("--------------");
		System.out.println("Before Post");
		System.out.println("--------------");
		waypoints.forEach((obj) -> {
			System.out.println((Waypoint)obj);
		});
		waypoints.add(new Waypoint(counter.incrementAndGet(), wpt));
		new ObjectMapper().writeValue(Paths.get(SAVE_FILE).toFile(), waypoints);
		System.out.println("--------------");
		System.out.println("After Post");
		System.out.println("--------------");
		waypoints.forEach((obj) -> {
			System.out.println((Waypoint)obj);
		});
		return new ResponseEntity<Waypoint>(wpt, HttpStatus.OK);
	}
	
	@GetMapping("/wpt/all")
	public List<Waypoint> getWaypoints(){
		return waypoints;
	}
}
