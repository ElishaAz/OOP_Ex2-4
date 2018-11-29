package File_format;

/**
 * @author Elisha
 */
public class MenuSettings
{
	/** the index of the menu line (usually 0). Lines before this one will be ignored. */
	public int menuIndex;
	/** Latitude's value in the menu. */
	public String latMenuValue;
	/** Longitude's value in the menu. */
	public String lonMenuValue;
	/** Altitude's value in the menu. */
	public String altMenuValue;
	/** the line's name's value in the menu. */
	public String nameMenuValue;
	/** the lin's time's value in the menu. */
	public String timeMenuValue;
	/** the format the time is in (i.e. "yyyy-MM-dd HH:mm:ss"). */
	public String timePattern;


	/**
	 * @param menuIndex     the index of the menu line (usually 0). Lines before this one will be ignored.
	 * @param latMenuValue  Latitude's value in the menu.
	 * @param lonMenuValue  Longitude's value in the menu.
	 * @param altMenuValue  Altitude's value in the menu.
	 * @param nameMenuValue the line's name's value in the menu.
	 * @param timeMenuValue the lin's time's value in the menu.
	 * @param timePattern the format the time is in (i.e. "yyyy-MM-dd HH:mm:ss").
	 *
	 * */
	public MenuSettings(int menuIndex, String latMenuValue, String lonMenuValue, String altMenuValue,
						String nameMenuValue,
						String timeMenuValue, String timePattern)
	{
		this.menuIndex = menuIndex;
		this.latMenuValue = latMenuValue;
		this.lonMenuValue = lonMenuValue;
		this.altMenuValue = altMenuValue;
		this.nameMenuValue = nameMenuValue;
		this.timeMenuValue = timeMenuValue;
		this.timePattern = timePattern;
	}

	public MenuSettings()
	{
	}

	public static class WigleWiFiSettings extends MenuSettings
	{
		public WigleWiFiSettings()
		{
			menuIndex = 1;
			this.latMenuValue = "CurrentLatitude";
			this.lonMenuValue = "CurrentLongitude";
			this.altMenuValue = "AltitudeMeters";
			this.nameMenuValue = "SSID";
			this.timeMenuValue = "FirstSeen";
			this.timePattern = "yyyy-MM-dd HH:mm:ss";
		}
	}
}
