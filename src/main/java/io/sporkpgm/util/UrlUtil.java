package io.sporkpgm.util;

import io.sporkpgm.util.SporkConfig.Settings;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

public class UrlUtil {

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject getJSON(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public static JSONObject getAPI(String suffix) throws IOException, JSONException {
		String api = Settings.api();
		if(!api.startsWith("http://") && !api.startsWith("https://")) {
			api = "http://";
		}

		if(api.endsWith("/") && suffix.startsWith("/")) {
			suffix = suffix.substring(1);
		} else if(!api.endsWith("/") && !suffix.startsWith("/")) {
			api = api + "/";
		}
		String url = api + suffix;
		return getJSON(url);
	}

}