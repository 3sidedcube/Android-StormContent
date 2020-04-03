package com.cube.storm.content.lib.manager;

import com.cube.storm.content.lib.helper.BundleHelper;
import io.reactivex.rxjava3.core.Completable;

/**
 * This is the manager class responsible for checking for and downloading updates from the server
 * <p/>
 * Access this class via {@link com.cube.storm.ContentSettings#getUpdateManager()}. Do not instantiate this class directly
 */
public interface UpdateManager
{
	void cancelPendingRequests();

	/**
	 * Downloads the latest full bundle from the server.
	 * <p>
	 * Ignores the timestamp in the local manifest file, so the downloaded bundle may be incompatible with the binary
	 * if for example a landmark publish has occurred
	 * <p>
	 * Despite the name this method will also download updates, not just check for them.
	 */
	Completable checkForBundle();

	/**
	 * Checks for updates on the server and downloads any new files in the form of a delta bundle
	 * <p>
	 * Reads the timestamp from which to check for updates from the local manifest file
	 * <p>
	 * Despite the name this method will also download updates, not just check for them.
	 */
	default Completable checkForUpdates()
	{
		Long bundleTimestamp = BundleHelper.readContentTimestamp();
		if (bundleTimestamp == null)
		{
			return Completable.error(new IllegalStateException("Cannot check for updates without timestamp"));
		}
		else
		{
			return checkForUpdates(bundleTimestamp);
		}
	}

	/**
	 * Checks for updates on the server and downloads any new files in the form of a delta bundle
	 * <p>
	 * Despite the name this method will also download updates, not just check for them.
	 *
	 * @param lastUpdate The time of the last update. Usually found in the {@code manifest.json} file
	 */
	Completable checkForUpdates(final long lastUpdate);
}
