package test;

import main.database.*;

public class TestDatabase {

	public static void main(String[] args) {
		
		try {
			SelectBuilder<Researcher> sb = Researcher.SELECTOR()
					.where("users", "id", "3")
					.whereIn("users", "username", "bob", "alice");
			sb.get();
		} catch (Exception e) { System.out.println(e.getMessage()); }
		
	}
	
}
