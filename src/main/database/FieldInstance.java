package main.database;

public class FieldInstance {

	private Field relatedField;
	private boolean changed = false;
	private Object value;
	
	FieldInstance(Field relatedField) {
		this.relatedField = relatedField;
	}
	
	public String serializeChanges() {
		return "";
	}
	
	public boolean shouldUpdate() { return this.changed; }
	
	public Object asObject() { return this.value; }
	
	public String asString() {
		return this.value.toString();
	}
	
	public Boolean asBool() throws ClassCastException {
		return (Boolean)this.value;
	}
	
	public Integer asInt() throws ClassCastException {
		return (Integer)this.value;
	}
	
	public Double asDouble() throws ClassCastException {
		return (Double)this.value;
	}
	
	public void set(Object value) {
		this.value = value;
	}

}
