package com.gabrielgatu.allaround.rest;

import android.util.Log;

import com.gabrielgatu.allaround.utils.APISystemPreferences;
import com.gabrielgatu.allaround.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by gabrielgatu on 13/05/15.
 */
public class RESTClient {

    public static void findUserById(String id, AsyncHttpResponseHandler listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("id", id);

        client.get(Utils.SERVER_USER_FIND_URL, params, listener);
    }

    public static void updateUserLocation(String userId, double latitude, double longitude, AsyncHttpResponseHandler listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("user", userId);
        params.add("latitude", latitude + "");
        params.add("longitude", longitude + "");

        client.post(Utils.SERVER_USER_UPDATELOCATION_URL, params, listener);
    }

    public static void updateUserImage(String userId, String image, AsyncHttpResponseHandler listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("user", userId);
        params.add("image", image);

        client.post(Utils.SERVER_USER_UPDATE_IMAGE_URL, params, listener);
    }

    public static void updateUserName(String userId, String username, AsyncHttpResponseHandler listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("user", userId);
        params.add("username", username);

        client.post(Utils.SERVER_USER_UPDATE_USERNAME_URL, params, listener);
    }

    public static void updateUserEmail(String userId, String email, AsyncHttpResponseHandler listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("user", userId);
        params.add("email", email);

        client.post(Utils.SERVER_USER_UPDATE_EMAIL_URL, params, listener);
    }

    public static void getUserProfile(String userId, AsyncHttpResponseHandler listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("user", userId);

        client.get(Utils.SERVER_USER_PROFILE_URL, params, listener);
    }

    public static void createFriendship(String user, String friend, AsyncHttpResponseHandler listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("user", user);
        params.add("friend", friend);

        client.post(Utils.SERVER_FRIENDSHIP_CREATE_URL, params, listener);
    }

    public static void signUp(String username, String password, String email, AsyncHttpResponseHandler listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("username", username);
        params.add("password", password);
        params.add("email", email);

        client.post(Utils.SERVER_SIGNUP_URL, params, listener);
    }

    public static void signIn(String username, String password, AsyncHttpResponseHandler listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("username", username);
        params.add("password", password);

        client.get(Utils.SERVER_SIGNIN_URL, params, listener);
    }

    public static void getDiscussions(double latitude, double longitude, AsyncHttpResponseHandler listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("latitude", latitude + "");
        params.add("longitude", longitude + "");

        client.get(Utils.SERVER_DISCUSSION_FIND_URL, params, listener);
    }

    public static void getDiscussionDetails(String id, AsyncHttpResponseHandler listener) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.SERVER_DISCUSSION_FIND_URL + "/" + id, listener);
    }

    public static void createDiscussion(String userID, String discussionTitle, String discussionText, double latitude, double longitude, AsyncHttpResponseHandler listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("user_id", userID);
        params.add("title", discussionTitle);
        params.add("text", discussionText);
        params.add("latitude", latitude + "");
        params.add("longitude", longitude + "");

        client.post(Utils.SERVER_DISCUSSION_CREATE_URL, params, listener);
    }

    public static void getMessages(double latitude, double longitude, String discussionId, AsyncHttpResponseHandler listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("latitude", latitude + "");
        params.add("longitude", longitude + "");

        if (discussionId != null) {
            params.add("discussion_id", discussionId);
        }

        client.get(Utils.SERVER_MESSAGE_FIND_URL, params, listener);
    }

    public static void getUsersInArea(double latitude, double longitude, boolean count, AsyncHttpResponseHandler listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("latitude", latitude + "");
        params.add("longitude", longitude + "");
        params.add("count", count + "");

        client.get(Utils.SERVER_AREA_USERS_URL, params, listener);
    }

    public static void getImages(double latitude, double longitude, AsyncHttpResponseHandler listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("latitude", latitude + "");
        params.add("longitude", longitude + "");

        client.get(Utils.SERVER_IMAGE_FIND_URL, params, listener);
    }

    public static void createImage(String userId, double latitude, double longitude, String image, AsyncHttpResponseHandler listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add("user", userId);
        params.add("latitude", latitude + "");
        params.add("longitude", longitude + "");
        params.add("image", image);

        client.post(Utils.SERVER_IMAGE_CREATE_URL, params, listener);
    }
}
