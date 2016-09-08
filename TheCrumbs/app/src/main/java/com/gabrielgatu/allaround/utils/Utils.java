package com.gabrielgatu.allaround.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gabrielgatu on 12/05/15.
 */
public class Utils {

    public static final String SERVER_ROOT_URL = "http://192.168.1.4:3000";

    // Socket events
    public static final String SEND_MESSAGE = "chat.message";
    public static final String RECEIVED_MESSAGE = "chat.message";

    // Server Routes
    public static final String SERVER_SIGNIN_URL = SERVER_ROOT_URL + "/api/user/auth";
    public static final String SERVER_SIGNUP_URL = SERVER_ROOT_URL + "/api/user/new";
    public static final String SERVER_USER_FIND_URL = SERVER_ROOT_URL + "/api/user/find";
    public static final String SERVER_USER_UPDATELOCATION_URL = SERVER_ROOT_URL + "/api/user/updatelocation";
    public static final String SERVER_USER_PROFILE_URL = SERVER_ROOT_URL + "/api/user/profile";
    public static final String SERVER_USER_UPDATE_IMAGE_URL = SERVER_ROOT_URL + "/api/user/updateimage";
    public static final String SERVER_USER_UPDATE_USERNAME_URL = SERVER_ROOT_URL + "/api/user/updateusername";
    public static final String SERVER_USER_UPDATE_EMAIL_URL = SERVER_ROOT_URL + "/api/user/updateemail";

    public static final String SERVER_MESSAGE_FIND_URL = SERVER_ROOT_URL + "/api/message/find";

    public static final String SERVER_DISCUSSION_FIND_URL = SERVER_ROOT_URL + "/api/discussion/find";
    public static final String SERVER_DISCUSSION_CREATE_URL = SERVER_ROOT_URL + "/api/discussion/create";

    public static final String SERVER_AREA_USERS_URL = SERVER_ROOT_URL + "/api/area/users";

    public static final String SERVER_IMAGE_FIND_URL = SERVER_ROOT_URL + "/api/image/find";
    public static final String SERVER_IMAGE_CREATE_URL = SERVER_ROOT_URL + "/api/image/create";

    public static final String SERVER_FRIENDSHIP_CREATE_URL = SERVER_ROOT_URL + "/api/friendship/create";

    // System Preferences Tags
    public static final String TAG_USER = "com.gabrielgatu.allaround.USER";
    public static final String TAG_USER_ID = "com.gabrielgatu.allaround.USER_ID";
}