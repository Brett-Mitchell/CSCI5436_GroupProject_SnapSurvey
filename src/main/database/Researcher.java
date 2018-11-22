package main.database;

public class Researcher extends User {
	
	public Researcher() { super(); }
	
	public Researcher(String username, String password) {
		super(username, password);
	}
	
	public Researcher(int id, String username, String password) {
		super(id, username, password);
	}

	@Override
	protected int getUserType() {
		return 0;
	}
	
	public static SelectBuilder<Researcher> SELECT() {
		return new SelectBuilder<Researcher>("users", Researcher.class).joinOn("researchers", new String[]{"id"});
	}
	
	@Override
	public void set(String field, Object value) {
		super.set(field, value);
	}
	
}
