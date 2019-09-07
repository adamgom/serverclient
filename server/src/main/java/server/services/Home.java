package server.services;

import java.util.ArrayList;

public class Home {
	private String preparedList;
	
	public String wellcom() {
		return "<h2>Wellcome to our site</h2>" + "<h4>Our resources</h4>"
				+ "<ul>" + preparedList + "</ul> "
				+ "<i>by Adam</i>";
	}
	
	public void prepareList(ArrayList<String> list) {
		preparedList = "";
		for (int i = 0 ; i < list.size() ; i++ ) {
			preparedList += "<li><a href=\"localhost:" + System.getProperty("server.port") + "/" + list.get(i) + "\">" + list.get(i) + "</a></li>";
		}
	}
}
