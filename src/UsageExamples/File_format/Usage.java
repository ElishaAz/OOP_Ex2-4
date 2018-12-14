package UsageExamples.File_format;

import File_format.Csv2kml;
import File_format.CSV_MenuSettings;
import File_format.MultiCSV;
import File_format.ReadWrite;

import java.io.File;

/**
 * @author Elisha
 */
public class Usage
{
	public static final CSV_MenuSettings WigleWiFiSettings =
			new CSV_MenuSettings(1, "CurrentLatitude", "CurrentLongitude", "AltitudeMeters",
					"SSID", "FirstSeen", "yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args)
	{
		Csv2kml.convert("src/UsageExamples/File_format/WigleWifi_20171201110209.csv",
				"src/UsageExamples/File_format/WigleWifi_20171201110209.kml", WigleWiFiSettings, 60000);
		ReadWrite.writeKML(new File("src/UsageExamples/File_format/dataFile.kml"),
				MultiCSV.readFolder("src/UsageExamples/File_format/data", WigleWiFiSettings));
	}
}
