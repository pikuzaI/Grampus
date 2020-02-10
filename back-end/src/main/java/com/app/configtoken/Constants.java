package com.app.configtoken;

import java.util.*;

public class Constants {
    public static final String SIGN_UP_URLS = "/api/users/**";
    public static final String H2_URL = "h2-console/**";
    public static final String SECRET ="SecretKeyToGenJWTs";
    public static final String TOKEN_PREFIX= "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 300_000_000; //30 000 seconds
    public static final String FTP_SERVER = "10.11.1.155";
    public static final int FTP_PORT = 21;
    public static final String FTP_IMG_LINK = "ftp://10.11.1.155/";
    public static final String MY_EMAIL = "winlixn@gmail.com";
    public static final String MY_PASSWORD = "kapuzi121212";
    public static final String URL_RESET_PASSWORD = "http://mexanik.ddns.net:6001/api/users/reset/";

    public static final String REG_MAIL_SUBJECT = "Profile registration(GRAMPUS)";
    public static final String REG_MAIL_ARTICLE = "You're profile is register! Thank you";
    public static final String REG_MAIL_MESSAGE = "To activate you're profile visit next link: ";
    public static final String REG_URL_ACTIVATE = "http://mexanik.ddns.net:6001/api/users/activate/";

    public static final String ACHIEVE_NOTIFIC_MAIL_SUBJECT = "New Achievement(GRAMPUS)";
    public static final String ACHIEVE_NOTIFIC_MAIL_ARTICLE = "Congratulation!";
    public static final String ACHIEVE_NOTIFIC_MAIL_MESSAGE = "You got new achievement";


    public static final int DEFAULT_SIZE_MESSAGE_HISTORY = 20;

    public static List<Locale> SUPPORTED_LOCALES =  Arrays.asList(new Locale("en"), new Locale("ru"));
}
