package main.database;

public interface Table {

	public abstract void set(String field, Object value);
	public abstract void create();
	public abstract void update();
	public abstract void delete();
	
}
