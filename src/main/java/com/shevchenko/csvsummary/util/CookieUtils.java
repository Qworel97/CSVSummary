package com.shevchenko.csvsummary.util;

import javax.servlet.http.Cookie;

/**
 * @author Dmytro_Shevchenko4
 */
public class CookieUtils {
    public static Cookie generateCookie(String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}
