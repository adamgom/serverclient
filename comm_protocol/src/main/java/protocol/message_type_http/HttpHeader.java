package protocol.message_type_http;

import protocol.message_base.Header;

public class HttpHeader implements Header {
	private String name;
	private String value;
	
	public HttpHeader(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getFullHeader() {
		return name + ":" + value;
	}

}