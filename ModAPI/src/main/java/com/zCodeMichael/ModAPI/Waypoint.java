package com.zCodeMichael.ModAPI;

public class Waypoint {
	private long wpt_ID;
	private String wpt_Name;
	private long wpt_Pos_X;
	private long wpt_Pos_Y;
	private long wpt_Pos_Z;
	
	public Waypoint(long wpt_ID, String wpt_Name, long wpt_Pos_X, long wpt_Pos_Y, long wpt_Pos_Z) {
		this.wpt_ID = wpt_ID;
		this.wpt_Name = wpt_Name;
		this.wpt_Pos_X = wpt_Pos_X;
		this.wpt_Pos_Y = wpt_Pos_Y;
		this.wpt_Pos_Z = wpt_Pos_Z;
	}
	
	public Waypoint(long wpt_ID, Waypoint wpt) {
		this.wpt_ID = wpt_ID;
		this.wpt_Name = wpt.getWpt_Name() != null ? wpt.getWpt_Name() : "Waypoint " + this.wpt_ID;
		this.wpt_Pos_X = wpt.getWpt_Pos_X();
		this.wpt_Pos_Y = wpt.getWpt_Pos_Y();
		this.wpt_Pos_Z = wpt.getWpt_Pos_Z();
	}
	
	public Waypoint() {
		
	}
	
	public long getWpt_ID() {
		return wpt_ID;
	}

	public String getWpt_Name() {
		return wpt_Name;
	}

	public long getWpt_Pos_X() {
		return wpt_Pos_X;
	}

	public long getWpt_Pos_Y() {
		return wpt_Pos_Y;
	}

	public long getWpt_Pos_Z() {
		return wpt_Pos_Z;
	}
	
	public String toString() {
		return "[" + wpt_Name + ", " + wpt_ID + "]" + ": (" + wpt_Pos_X + ", " + wpt_Pos_Y + ", " + wpt_Pos_Z + ")";
	}
}
