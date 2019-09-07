package protocol.session;

public class SessionParameter {
	private String name;
	private Object value;
	
	public SessionParameter(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof String) {
			return name.equals(obj.toString());
		} else {
			return super.equals(obj);
		}
	}
}