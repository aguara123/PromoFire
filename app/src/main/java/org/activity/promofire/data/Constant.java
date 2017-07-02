package org.activity.promofire.data;

/**
 * Created by DESARROLLO on 18/02/17.
 */

public class Constant {

    /**
     * -------------------- EDIT THIS WITH YOURS -------------------------------------------------
     */

    // Edit WEB_URL with your url. Make sure you have backslash('/') in the end url

    //Casa
    //public static String WEB_URL = "http://192.168.1.53:8080/WSPromociones/ws/";
    //Android Aguara
   // public static String WEB_URL = "http://192.168.43.202:8080/WSPromociones/ws/";
    public static String WEB_URL = "http://192.168.0.54:8080/WSPromociones/ws/";


    // Device will re-register the device data to server when open app N-times
    public static int OPEN_COUNTER = 50;



    /**
     * ------------------- DON'T EDIT THIS -------------------------------------------------------
     */

    // this limit value used for give pagination (request and display) to decrease payload
    public static int NEWS_PER_REQUEST = 10;
    public static int PRODUCT_PER_REQUEST = 10;
    public static int NOTIFICATION_PAGE = 20;
    public static int WISHLIST_PAGE = 20;

    // retry load image notification
    public static int LOAD_IMAGE_NOTIF_RETRY = 3;

    // Method get path to image
    public static String getURLimgProduct(String file_name) {
        return WEB_URL + "uploads/product/" + file_name;
    }

    public static String getURLimgNews(String file_name) {
        return WEB_URL + "uploads/news/" + file_name;
    }

    public static String getURLimgCategory(String file_name) {
        return WEB_URL + "uploads/category/" + file_name;
    }
}
