package main.database;

public class Participant extends User {
	
	public Participant() { super(); }

	public Participant(String username, String password) {
		super(username, password);
	}
	
	public Participant(int id, String username, String password) {
		super(id, username, password);
	}

	@Override
	protected int getUserType() {
		return 1;
	}
	
	public static SelectBuilder<Participant> SELECT() {
		return new SelectBuilder<Participant>("users", Participant.class).joinOn("participants", new String[] {"id"});
	}

	@Override
	public void set(String field, Object value) {
		super.set(field, value);
	}
	
}
