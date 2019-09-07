package protocol.message_type_http.body;

import java.io.ByteArrayInputStream;

import javax.json.Json;
import javax.json.JsonObject;

import protocol.message_base.Body;

public class JsonBody extends Body<JsonObject> {

	public void setValue(JsonObject value) {
		this.value = value;
	}

	@Override
	public int getLength() {
		return value.toString().getBytes().length;
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public void parse(String source) {
		this.value = Json.createReader(new ByteArrayInputStream(source.getBytes())).readObject();
	}
}