package fr.stormer3428.obsidianMC;

public class OMCUtil {

	public static double degToRad(double deg) {
		return (deg*Math.PI)/180;
	}

	public static float noteBlockToPitch(int note) {
		return (float) ((Math.pow(2, note/12f))/2f);
	}
}
