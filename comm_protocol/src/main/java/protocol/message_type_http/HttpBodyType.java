package protocol.message_type_http;

import protocol.message_base.Body;
import protocol.message_base.BodyType;
import protocol.message_type_http.body.HtmlBody;
import protocol.message_type_http.body.JsonBody;

public enum HttpBodyType implements BodyType {
	HTML("text/html", HtmlBody.class),
	JSON("application/json", JsonBody.class);

	private String contentType;
	private Class<? extends Body<?>> clazz;
	
	private HttpBodyType(String contentType, Class<? extends Body<?>> clazz) {
		this.contentType = contentType;
		this.clazz = clazz;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	@Override
	public Body<?> getBody() {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BodyType getBodyTypeByContentType(String contentType) {
		for (HttpBodyType item : HttpBodyType.values()) {
			if (item.getContentType().toLowerCase().equals(contentType.toLowerCase())) {
				return item;
			} 
		}
		return HttpBodyType.HTML;
	}
}
