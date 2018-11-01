package test;

import main.database.*;

public class TestDatabase {

	public static void main(String[] args) {
		
		try {
		Researcher re = new Researcher("new_un", "new_pass");
		re.create();
		System.out.println(re.validAuth());
		
		
		} catch (Exception e) { System.out.println(e.getMessage()); }
		
	}
	
}
