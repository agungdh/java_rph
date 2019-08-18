/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.test.Helpers;

import static java.lang.System.out;

/**
 *
 * @author user
 */
public class ADHhelper {
    public static void d(String string, boolean pembatas) {
        if (!pembatas) {
            out.println(string);
        } else {
            d(string);
        }
    }
    
    public static void d(String string) {
        out.println("===========================================");
        out.println(string);
        out.println("===========================================");
    }
    
    public static String bulan(int i) {
        switch(i) {
            case 1:
              return "Januari";
            case 2:
              return "Februari";
            case 3:
              return "Maret";
            case 4:
              return "April";
            case 5:
              return "Mei";
            case 6:
              return "Juni";
            case 7:
              return "Juli";
            case 8:
              return "Agustus";
            case 9:
              return "September";
            case 10:
              return "Oktober";
            case 11:
              return "November";
            case 12:
              return "Desember";
            default:
              return "ERROR !!!";
          }
    }
}
