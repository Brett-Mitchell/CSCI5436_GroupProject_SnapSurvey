package main.database;

public class Participant extends User {

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
	
	public static SelectBuilder<Participant> SELECTOR() {
		return new SelectBuilder<Participant>("users", Participant.class).joinOn("participants", "id");
	}

	@Override
	public void set(String field, Object value) {
		super.set(field, value);
	}
	
}
