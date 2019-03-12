package com.zankong.tool.zkapp.activity;

public class test {
    public static void main(String[] args) throws Exception {
        double[] poiRange = getPoiRange(3000, 31.2092481, 121.4834775);
        for (double v : poiRange) {
            System.out.println(v);
        }
    }
    public static double[] getPoiRange(double dis, double lng, double lat) {
        double EARTH_RADIUS = 6378137.0 ;
        double[] poi = new double[4];

        double lng1 = (Math.PI / 180) * lng;
        double lng2 = (Math.PI / 180) * lng;
        double lat1 = (Math.PI / 180) * lat;
        double lat2 = (Math.PI / 180) * lat;

        // 角度
        double theta = dis / EARTH_RADIUS;

        // 维度相同，反算经度
        double lngVal = Math.cos(theta) - Math.sin(lat1) * Math.sin(lat2);
        double lngThetaVal = lngVal / (Math.cos(lat1) * Math.cos(lat2));
        double elng1 = (-Math.acos(lngThetaVal) + lng1) / (Math.PI / 180);
        double elng2 = (Math.acos(lngThetaVal) + lng1) / (Math.PI / 180);

//        LogLIB.LogRun.info("elng1=" + UtilTools.format(elng1) + " elng2=" + UtilTools.format(elng2));

        // 经度相同，反算维度（根据辅助角公式计算 asinx + bcosx）
        // theta = a * Math.sin(lat2) + b * Math.cos(lat2) = Math.sqrt(a平方 + b平方)sin(x + arctan(b/a))
        double a = Math.sin(lat1);
        double b = Math.cos(lat1) * Math.cos(lng2 - lng1);
        double sqrt = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        double latTheteVal = Math.cos(theta) / sqrt;
        double elat1 = (Math.asin(latTheteVal) - Math.atan(b/a)) / (Math.PI / 180);
        double elat2 = (Math.asin(-latTheteVal) - Math.atan(b/a)) / (Math.PI / 180) + 180;

//        LogLIB.LogRun.info("elat1=" + UtilTools.format(elat1) + " elat2=" + UtilTools.format(elat2));

        poi[0] = elng1;
        poi[1] = elat1;
        poi[2] = elng2;
        poi[3] = elat2;

        return poi;
    }
}
