package com.lishi.adruino.randompoetry.utils;

public class StringUtils {
    public static boolean checkLegality(String input){
        int n;
        for(int i = 0; i < input.length(); i++) {
            n = (int)input.charAt(i);
            if(!(19968 <= n && n <40869)) {
                return false;
            }
        }
        return true;
    }
}
