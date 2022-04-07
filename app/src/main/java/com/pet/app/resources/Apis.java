package com.pet.app.resources;

public class Apis {

    /**
     * For local host testing kindly run php artisan serve --host yourip
     * Then copy that ip address and port number from your cmd
     * replace petcat.itcians.com with your ip and port number below in
     * baseUrl and ImageUrl
     * After that make sure your android phone and your laravel server is running on
     * same internet/Wifi i.e LAN
     *
     * NOTE: Kindly don't add localhost or 127.0.0.1 as ip that won't work on android
     * */

    public final static String baseUrl = "http://petcat.itcians.com/api/";
    public final static String ImageUrl = "http://petcat.itcians.com/images/";

    public final static String signUp = baseUrl + "signup";
    public final static String login = baseUrl + "login";
    public final static String AddPet = baseUrl + "addPet";
    public final static String UpdatePet = baseUrl + "updatePet";
    public static final String getNotifications = baseUrl + "getNotifications";
    public static final String readNotification = baseUrl + "readNotification";
    public final static String PlaceOrder = baseUrl + "placeOrder";
    public final static String AddHygiene = baseUrl + "addHygiene";
    public static final String GetHygieneReport = baseUrl + "getHygieneReport";
    public final static String GetPets = baseUrl + "getPets";
    public final static String MyPets = baseUrl + "getPet";
    public static final String kAuth = "Authorization";
    public static final String fcmUpdate = "updateFCM";
    public static final String searchDoctor = baseUrl + "searchDoctor";
    public static final String updateUser = baseUrl + "updateUser";

    public static final String getOrders = baseUrl + "getOrders";
    public static final String orderCancel = baseUrl + "cancelOrder";
    public static final String resetPass = "password/reset";

}
