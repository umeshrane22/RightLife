package com.jetsynthesys.rightlife.ui.utility;

import java.text.DecimalFormat;

public class ConversionUtils {

    private static DecimalFormat decimalFormatFtInch = new DecimalFormat("##.##");
    private static DecimalFormat decimalFormatCm = new DecimalFormat("###.##");
    private static DecimalFormat decimalFormat = new DecimalFormat("###.##");
    private static DecimalFormat decimalFormatWeight = new DecimalFormat("###.#");
    public static DecimalFormat decimalFormat0Decimal = new DecimalFormat("###");
    public static DecimalFormat decimalFormat1Decimal = new DecimalFormat("###.#");
    public static DecimalFormat decimalFormat2Decimal = new DecimalFormat("###.##");

    public static String convertCentimeterToFtInch(String centimeter) {
        try {
            double cms = Double.parseDouble(centimeter);
            double ftInch = cms / 30.48;
            return decimalFormatFtInch.format(ftInch);
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertFeetToCentimeter(String ftInch) {
        try {
            double feetInch = Double.parseDouble(ftInch);
            double centimeter = feetInch * 30.48;
            return decimalFormatCm.format(centimeter);
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertInchToCentimeter(String inch) {
        try {
            double inchConvert = Double.parseDouble(inch);
            double centimeter = inchConvert * 2.54;
            return decimalFormatCm.format(centimeter);
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertCentimeterToInch(String centimeter) {
        try {
            double cms = Double.parseDouble(centimeter);
            double inch = cms / 2.54;
            return decimalFormatCm.format(inch);
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertFeetToInch(String ftInch) {
        try {
            double feet = Double.parseDouble(ftInch);
            double inch = feet * 12;
            return decimalFormat.format(inch);
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertInchToFeet(String inch) {
        try {
            double in = Double.parseDouble(inch);
            double feet = in / 12;
            return decimalFormat.format(feet);
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertKgToLbs(String kgs) {
        try {
            double kg = Double.parseDouble(kgs);
            double lbs = kg / 2.20462;
            return decimalFormatWeight.format(lbs);
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertLbsToKgs(String lbs) {
        try {
            double lb = Double.parseDouble(lbs);
            double kgs = lb * 2.20462;
            return decimalFormatWeight.format(kgs);
        } catch (Exception e) {
            return "";
        }
    }
}
