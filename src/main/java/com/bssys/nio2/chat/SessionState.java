package com.bssys.nio2.chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionState {
	private Map<String, String> sessionProps = new ConcurrentHashMap<String, String>();

	public String getProperty(String key) {
		return sessionProps.get(key);
	}

	public void setProperty(String key, String value) {
		sessionProps.put(key, value);
	}
}
