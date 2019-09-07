package server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import protocol.Fields;
import protocol.message_base.Header;
import protocol.message_type_http.HttpBodyType;
import protocol.message_type_http.HttpHeader;
import protocol.message_type_http.HttpMessage;
import protocol.message_type_http.body.HtmlBody;
import protocol.message_type_http.body.JsonBody;
import protocol.session.Session;
import server.session.SessionsManager;

public class ProcessConnection implements Runnable {
	Socket socket;

	public ProcessConnection(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			HttpMessage req = new HttpMessage(socket.getInputStream());
			Session session = null;
			Header sessionId = req.getHeader(Fields.sessionId);

			if (instanceNewSession(sessionId)) {
				session = SessionsManager.getInstance().getSession();
				session.add(Fields.sessionTime, (Long)new Date().getTime()/1000);
				sessionId = new HttpHeader(Fields.sessionId, session.getSessionId());

				req.setSession(session);
			} else {
				session = SessionsManager.getInstance().getSession(sessionId.getValue());
				session.add(Fields.sessionTime, new Date().getTime()/1000);
				req.setSession(session);
			}

			String msg = ServicesManager.getInstance().runService(req);

			HttpMessage res = new HttpMessage(false);
			
//			JsonObjectBuilder json = Json.createObjectBuilder();
//			json.add("res", msg);
//			JsonBody body = new JsonBody();
			HtmlBody body = new HtmlBody();
//			body.setValue(json.build());
			body.setValue(msg);

			res.addHeader(new HttpHeader(Fields.server, Fields.serverName));
//			res.addHeader(new HttpHeader(Fields.ContentType, HttpBodyType.JSON.getContentType()));
			res.addHeader(new HttpHeader(Fields.ContentType, HttpBodyType.HTML.getContentType()));
			res.addHeader(new HttpHeader(Fields.ContentLength, String.valueOf(body.getLength())));
			res.addHeader(sessionId);
			res.setSession(session);
			res.setBody(body);

			sendResponse(socket.getOutputStream(), res);
			socket.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private boolean instanceNewSession(Header sessionId) {
		return (sessionId.getValue().equals(" ") || !SessionsManager.getInstance().isSession(sessionId.getValue()));
	}

	private void sendResponse(OutputStream os, HttpMessage res) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
			writer.write(res.getFullMessage());
			writer.flush();
		} 
	}
}
