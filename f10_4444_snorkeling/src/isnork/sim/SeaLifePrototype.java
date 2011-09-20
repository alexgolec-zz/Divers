package isnork.sim;

import java.awt.Color;

public class SeaLifePrototype implements Cloneable{
	protected int speed;
	protected String name;
	protected double happiness;
	protected boolean dangerous = false;
	protected int minCount;
	protected int maxCount;
	protected String filename;
	
	public Object clone()
	{
		SeaLifePrototype r = new SeaLifePrototype();
		r.filename=filename;
		r.speed=speed;
		r.name=name;
		r.happiness=happiness;
		r.dangerous=dangerous;
		r.minCount=minCount;
		r.maxCount=maxCount;
		return r;
	}
	public String getFilename()
	{
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getMinCount() {
		return minCount;
	}
	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getHappinessD()
	{
		return happiness;
	}
	public int getHappiness() {
		return (int)happiness;
	}
	public void setHappiness(int happiness) {
		this.happiness = happiness;
	}
	public boolean isDangerous() {
		return dangerous;
	}
	public void setDangerous(boolean dangerous) {
		this.dangerous = dangerous;
	}
	public SeaLifePrototype()
	{
		
	}
}
