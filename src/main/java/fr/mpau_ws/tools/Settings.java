package fr.mpau_ws.tools;

import java.util.Properties;

public class Settings {
	private static Properties properties;
	static {
		try {
			properties = new Properties();
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			properties.load(classLoader.getResourceAsStream("settings.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		String parametre = properties.getProperty(key, null);
		return parametre;
	}

	public static int getIntProperty(String key) {
		String parametreStr = properties.getProperty(key, null);
		int parametre = Integer.parseInt(parametreStr);
		return parametre;
	}

	public static long getLongProperty(String key) {
		String parametreStr = properties.getProperty(key, null);
		long parametre = Long.parseLong(parametreStr);
		return parametre;
	}

}
