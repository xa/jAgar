package com.kcx.jagar;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ServerConnector
{
	public static String[] getServerData() throws Exception
	{
		String urlStr = "https://m.agar.io";
		URL url = new URL(urlStr);
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

		connection.setRequestMethod("POST");

		String urlParameters = "EU-London"+Game.mode+"\n154669603";
		
		connection.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null)
		{
			response.append(inputLine+"\n");
		}
		in.close();
		
		return response.toString().split("\n");		
	}
}
