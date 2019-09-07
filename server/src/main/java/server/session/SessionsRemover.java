package server.session;

import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import protocol.Fields;
import protocol.session.Session;

public class SessionsRemover implements Runnable {
	int sessionTimeout;
	ConcurrentHashMap<String, Session> map;	

	public SessionsRemover(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
		map = SessionsManager.getInstance().getSessions();
	}

	@Override
	public void run() {
		System.out.println("Session timeout: " + sessionTimeout);

		
		while(true) {
			Long currentTume = Calendar.getInstance().getTime().getTime()/1000;
			
			for (String key : map.keySet()) {
				Long sessionTime = currentTume - map.get(key).get(Fields.sessionTime, Fields.sessionTimeClass);
				System.out.println("Session ID: " + key + ", Session seconds from last request: " + sessionTime);
				if (sessionTime > sessionTimeout) {
					map.remove(key);
					System.out.println("Session ID: " + key + " removed");
				}
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
	}
}