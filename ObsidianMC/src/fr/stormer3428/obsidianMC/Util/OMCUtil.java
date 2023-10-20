package fr.stormer3428.obsidianMC.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;

public class OMCUtil {

	private static final Pattern hexColorCodePattern = Pattern.compile("#[a-fA-F0-9]{6}");
	public static final int[] values = 			{1000,  900, 500,  400, 100,   90,  50,   40,  10,    9,   5,    4,   1};
	public static final String[] romanLetters = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
	
	public static String intToRoman(int num){
		StringBuilder roman = new StringBuilder();
		for(int i = 0; i < values.length; i++){
			while(num >= values[i]){
				num = num - values[i];
				roman.append(romanLetters[i]);
			}
		}
		return roman.toString();
	}

	public static float noteBlockToPitch(int note) {
		return (float) ((Math.pow(2, note/12f))/2f);
	}
	
	public static String translateChatColor(String s) {
		if(s == null) return null;
		s = ChatColor.translateAlternateColorCodes('&', s);
		s = ChatColor.translateAlternateColorCodes('$', s);
		s = ChatColor.translateAlternateColorCodes('§', s);
		
		Matcher hexMatcher = hexColorCodePattern.matcher(s);
		while(hexMatcher.find()) {
			String color = s.substring(hexMatcher.start(), hexMatcher.end());
			s = s.replace(color, ChatColor.of(color) + "");
			hexMatcher = hexColorCodePattern.matcher(s);
		}
		return s;
	}

	public static double map(double input_start, double input_end, double output_start, double output_end, double input) {
		return map(input, input_start, input_end, output_start, output_end, 3);
	}
	
	public static double map(double sourceNumber, double fromA, double fromB, double toA, double toB, int decimalPrecision ) {
	    double deltaA = fromB - fromA;
	    double deltaB = toB - toA;
	    double scale  = deltaB / deltaA;
	    double negA   = -1 * fromA;
	    double offset = (negA * scale) + toA;
	    double finalNumber = (sourceNumber * scale) + offset;
	    int calcScale = (int) Math.pow(10, decimalPrecision);
	    return (double) Math.round(finalNumber * calcScale) / calcScale;
	}

}














