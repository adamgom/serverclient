package protocol.message_base;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import protocol.session.Session;

public abstract class Message<T extends Body<?>> {
	protected List<Header> headers;
	protected T body;
	
	private Session session;

	public Message(InputStream is) {
		headers = new ArrayList<>();
		if (is != null) {
			parseMessage(is);
		}
	}
	
	public T getBody() {
		return body;
	}
	
	public void addHeader(Header header) {
		int i = findHeader(header);
		if (i > -1) {
			headers.add(i, header);
		} else {
			headers.add(header);	
		}
	}
	
	public Header getHeader(String name) {
		int i = findHeader(name);
		if (i > -1) {
			return headers.get(i);
		} 
		return null;	
	}

	protected int findHeader(String name) {
		for (int i = 0 ; i < headers.size() ; i++) {
			if (headers.get(i).getName().toLowerCase().equals(name.toLowerCase())) return i;
		}
		return -1;
	}
	
	private int findHeader(Header header) {
		return findHeader(header.getName());
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void setBody(T body) {
		this.body = body;
	}

	public abstract String getFullMessage();
	
	protected abstract void parseMessage(InputStream is);
}
