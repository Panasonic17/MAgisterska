package config;

public class CONFIG {
    // 1 -ep 2-home 3 -cluster
    public static int mode = 1;

    public static String getElasticIp() {
        if (mode == 1) return "127.0.0.1";
        return null;
    }

    public static Double getMinDistance() {
        return 1000d;
    }
}
