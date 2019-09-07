package server;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import protocol.message_type_http.HttpMessage;
import protocol.message_type_http.body.JsonBody;

public class ServicesManager {
	private static ServicesManager instance;
	private Map<String, Class<?>> services;

	private ServicesManager() {
		services = new HashMap<>();

		try {
			String servicePackage = System.getProperty("server.services.package");
			File rootDir = new File(ClassLoader.getSystemResource(servicePackage.replace(".", "/")).getPath());
			
			for (String item : rootDir.list((d,f) -> f.endsWith(".class"))) {
				String className = item.substring(0, item.lastIndexOf("."));
				services.put(className.toLowerCase(), Class.forName(servicePackage + "." + className));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static ServicesManager getInstance() {
		if (instance == null) instance = new ServicesManager();
		return instance;
	}

	public String runService(HttpMessage req) {
		String service = req.getResource();
		String result = "";
		Class<?> clazz = null;
		Object obj = null;

		switch (!service.isEmpty() && services.containsKey(service) ? 0 : 1) {
			case 0:
				clazz = services.get(service);
				try {
					obj = clazz.newInstance();
					for (Method item : clazz.getMethods()) {
						if (item.getName().equals("prepareList")) {
							List<String> list = new ArrayList<>();
							for (String serviceItem : services.keySet()) {
								list.add(serviceItem);
							}
							item.invoke(obj, list);
						}
						if (item.getName().equals("wellcom")) {
							List<Object> params = new ArrayList<>();
							for(Parameter param : item.getParameters()) {
								params.add( ((JsonBody) req.getBody()).getValue().getString(param.getName()));
							}
							result = item.invoke(obj, params.toArray()).toString();
						}
					}
				} catch (InstantiationException | 
						IllegalAccessException | 
						IllegalArgumentException | 
						InvocationTargetException e1) {
					e1.printStackTrace();
				}
				break;
	
			case 1:
				clazz = services.get("servicenotfound");
				try {
					obj = clazz.newInstance();
//					clazz.getMethods()[1].invoke(obj, service);
					for (Method item : clazz.getMethods()) {
						if(item.getName().equals("setService")) {
							item.invoke(obj, service);	
						}
					}
					for (Method item : clazz.getMethods()) {
						if(item.getName().equals("wellcom")) {
							result = item.invoke(obj).toString();	
						}
					}
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
					e1.printStackTrace();
				}
				break;
		}
		return result;
	}
}
