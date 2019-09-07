package protocol.message_type_http.body;

import protocol.message_base.Body;

public class HtmlBody extends Body<String>{

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public int getLength() {
		return value.getBytes().length;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public void parse(String source) {
		this.value = source;
	}
}
