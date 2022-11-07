package com.ur.urcap.examples.toolchanger.installation;


public enum Tool {
	GRIPPER("gripper-tcp-id", "gripper-enabled", "Gripper", "Gripper", 0, 0, 200, 0, 0, 0),
	SCREWDRIVER("screwdriver-tcp-id", "screwdriver-enabled", "Screwdriver", "Screwdriver", 0, -50, 150, 0, 0, 0),
	WELDING_TOOL("welding-tool-tcp-id", "welding-tool-enabled", "WeldingTool", "Welding tool", 10, 10, 100, 0, -Math.PI/2, 0);

	private String id;
	private String modelKey;
	private String name;
	private String title;

	private double x;
	private double y;
	private double z;
	private double rx;
	private double ry;
	private double rz;


	Tool(String id, String modelKey, String name, String title, double x, double y, double z, double rx, double ry, double rz) {
		this.id = id;
		this.modelKey = modelKey;
		this.name = name;
		this.title = title;

		this.x = x;
		this.y = y;
		this.z = z;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
	}

	public String getId() {
		return id;
	}

	public String getModelKey() {
		return modelKey;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public double getRx() {
		return rx;
	}

	public double getRy() {
		return ry;
	}

	public double getRz() {
		return rz;
	}
}
