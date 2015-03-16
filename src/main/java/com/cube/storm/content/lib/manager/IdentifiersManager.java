package com.cube.storm.content.lib.manager;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cube.storm.ContentSettings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * Identifiers manager class used for loading the identifiers structure found in the {@code data/} folder in the bundle.
 * <p/>
 * The identifiers file is used to allow the use of inter-app linking between different CMS systems. Each "app" object
 * is identified by the key of the object, where the key is the app ID defined in the CMS.
 * <p/>
 * Identifiers structure
 * <pre>
 {
	 "ARC_STORM-1-1": {
		 "android": {
		 	"packageName": "com.cube.arc.fa"
		 },
		 "ios": {
		 "countryCode": "us",
			 "iTunesId": "529160691",
			 "launcher": "ARCFA://"
		 },
		 "name": {
			 "en": "First aid",
			 "es": ""
	 	}
 	}
 }
 * </pre>
 *
 * @author Callum Taylor
 * @project LightningContent
 */
public class IdentifiersManager
{
	@Getter private HashMap<String, String> apps;

	private static IdentifiersManager instance;

	public static IdentifiersManager getInstance()
	{
		if (instance == null)
		{
			instance = new IdentifiersManager();
		}

		return instance;
	}

	/**
	 * Loads the file from the {@param path}
	 *
	 * @param path The path to the {@code identifiers.json} file
	 */
	public void loadApps(@NonNull Uri path)
	{
		try
		{
			byte[] identifiersData = ContentSettings.getInstance().getFileFactory().loadFromUri(path);

			if (identifiersData != null)
			{
				String identifierStr = new String(identifiersData, "UTF-8");
				JsonObject appsObject = new JsonParser().parse(identifierStr).getAsJsonObject();
				apps = new HashMap<String, String>();

				for (Map.Entry<String, JsonElement> entry : appsObject.entrySet())
				{
					try
					{
						String appName = entry.getKey();
						String packageName = entry.getValue().getAsJsonObject().get("android").getAsJsonObject().get("packageName").getAsString();

						apps.put(appName, packageName);
					}
					catch (Exception e)
					{
					 	e.printStackTrace();
					}
				}
			}
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Looks up the package name of an app from its storm ID
	 *
	 * @param id The storm ID
	 *
	 * @return The package name, or null if it was not found
	 */
	@Nullable
	public String getAppPackageName(@NonNull String id)
	{
		return apps == null ? null : apps.get(id);
	}
}
