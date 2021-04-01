package model;

public class CoordGPS {
	private double latitude;
	private double longitude;
	
	public CoordGPS(String geometry) {
		this(getLatitudeFromGeometry(geometry),getLongitudeFromGeometry(geometry));
	}
	public CoordGPS(double lat, double lon) {
		latitude = lat;
		longitude = lon;
	}
	
	private static String[] extractLatLongFromGeometry(String geometry) {
		String extract = geometry.substring(2, geometry.length()-1);
		String[] coord = extract.split(", ");
		return coord;
	}
	private static double getLatitudeFromGeometry(String geometry) {
		return Double.parseDouble(extractLatLongFromGeometry(geometry)[0]);
	}
	private static double getLongitudeFromGeometry(String geometry) {
		return Double.parseDouble(extractLatLongFromGeometry(geometry)[1]);
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	@Override
	public String toString() {
		return "CoordGPS [latitude=" + latitude + ", longitude=" + longitude + "]";
	}
	public static double haversine(double lat1, double lon1,
					double lat2, double lon2)
		{
		// distance between latitudes and longitudes
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		
		// convert to radians
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);
		
		// apply formulae
		double a = Math.pow(Math.sin(dLat / 2), 2) +
		Math.pow(Math.sin(dLon / 2), 2) *
		Math.cos(lat1) *
		Math.cos(lat2);
		double rad = 6371;
		double c = 2 * Math.asin(Math.sqrt(a));
		return rad * c;
		}

		// Driver Code
		public static void hk()
		{
		double lat1 = 48.8566969;
		double lon1 = 2.3514616;
		double lat2 = 40.6892;
		double lon2 = 74.0445;
		System.out.println(haversine(lat1, lon1, lat2, lon2) + " K.M.");
		}
}
