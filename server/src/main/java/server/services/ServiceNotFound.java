package server.services;

public class ServiceNotFound {
	String service;
	
	public String wellcom() {
		return "<h2>Service \"<i>" + service + "</i>\" not found</h2><i>please specify correct service</i>";
	}
	
	public void setService(String service) {
		this.service = service;
	}
}
