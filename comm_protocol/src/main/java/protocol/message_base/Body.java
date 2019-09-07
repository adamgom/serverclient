package protocol.message_base;

public abstract class Body<T> {

	protected T value;
	
	public T getValue() {
		return value;
	}
	
	public abstract int getLength();
	
	public abstract String toString();
	
	public abstract void parse(String source);
}

