package protocol.message_type_http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import protocol.Fields;
import protocol.message_base.Body;
import protocol.message_base.BodyType;
import protocol.message_base.Header;
import protocol.message_base.Message;
import protocol.message_type_http.HttpBodyType;
import protocol.message_type_http.HttpHeader;

public class HttpMessage extends Message<Body<?>> {
	private String method;
	private String resource;
	private boolean isRequest;

	public HttpMessage(InputStream is) {
		super(is);
	}

	public HttpMessage(boolean isRequest) {
		super(null);
		this.isRequest = isRequest;
	}

	@Override
	protected void parseMessage(InputStream is) {
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(is);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String firstLine = bufferedReader.readLine();

            processFirstLine(firstLine);  // process first line of request - method and service

            String header; // headers organized within HashMap
            while(!(header = bufferedReader.readLine()).equals("")) {
                String name = header.substring(0, header.indexOf(':'));
                String value = header.substring(header.indexOf(':') + 1);
                headers.add(new HttpHeader(name, value));
            }
            
            // request content
            StringBuilder source = new StringBuilder();
            while(bufferedReader.ready()) {
                source.append(bufferedReader.readLine());
            }

            BodyType bodyType = HttpBodyType.getBodyTypeByContentType(headers.get(this.findHeader(Fields.ContentType)).getValue());
            this.body = bodyType.getBody();
            this.body.parse(source.toString());
        } catch(IOException e) {
            e.printStackTrace();
        }
	}

	@Override
	public String getFullMessage() {
		StringBuilder req = new StringBuilder();

		req.append(generateFirstLine());
		
		for(Header header : headers) {
			req.append(header.getFullHeader() + System.lineSeparator());
		}
		
		req.append(System.lineSeparator() + body + System.lineSeparator());

		return req.toString();
	}

    private void processFirstLine(String firstLine) {		// GET /resource HTTP/1.1 - process method and service
    	if (firstLine.startsWith("HTTP/1.1")) {
    		isRequest = false;
    	} else {
    		isRequest = true;
    		String[] elements = new String[2];
            if (firstLine != null) elements = firstLine.split(" ");
        	method = elements[0];
            if (elements[1].substring(1).toLowerCase() != null) resource = elements[1].substring(1).toLowerCase();
            if (resource.length() == 0) resource = "default";
		}
    }
    
    private String generateFirstLine() {					// generate method and service
    	String output;
    	method = "GET";
    	if (isRequest) {
			output = method + " /" + resource + " " + Fields.protocol;	
		} else {
			output = Fields.protocol + " OK 200";
		}
    	return output + System.lineSeparator();
    }

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
}