package protocol.session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Session implements Iterable<SessionParameter>{

	private final String sessionId;
	private List<SessionParameter> parameters;

	public Session(String sessionId) {
		this.sessionId = sessionId;
		parameters = new ArrayList<>();
	}

	@Override
	public Iterator<SessionParameter> iterator() {
		return parameters.iterator();
	}

	public String getSessionId() {
		return sessionId;
	}

	public void add(String name, Object value) {
		int ix = contains(name);
		SessionParameter param = new SessionParameter(name, value);

		if (ix > -1) {
			parameters.add(ix, param);
		} else {
			parameters.add(param);
		}
	}

	public int contains(String name) {
		for (int i = 0 ; i < parameters.size() ; i++ ) {
			if (parameters.get(i).equals(name)) return i;
		}
		return -1;
	}

	public <T> T get(String name, Class<T> clazz) {
		return clazz.cast(parameters.get(contains(name)).getValue());
	}

	public void remove(String name) {
		if (contains(name) > -1) parameters.remove(name);
	}
}
