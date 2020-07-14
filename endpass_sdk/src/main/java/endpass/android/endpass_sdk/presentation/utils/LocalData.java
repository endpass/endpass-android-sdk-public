package endpass.android.endpass_sdk.presentation.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Date;

public class LocalData {

    private SharedPreferences pref;
    private Editor editor;
    private static final String ACCESS_TOKEN = "access_token";
    private static final String EMAIL = "email_user";
    private static final String PREF_NAME = "endpass";


    private static final String OAUTH_TOKEN = "oauth_token";
    private static final String OAUTH_TOKEN_CREATED_DATE = "oauth_token_created";


    @SuppressLint("CommitPrefEdits")
    public LocalData(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }


    public void setAccessToken(String token) {
        editor.putString(ACCESS_TOKEN, token);
        editor.apply();
    }

    public void setEmail(String email) {
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public String getAccessToken() {
        return pref.getString(ACCESS_TOKEN, "");
    }


    public void setOauthToken(String token) {
        setOauthTokenCreatedTime(DateUtils.INSTANCE.getCurrentTime());
        editor.putString(OAUTH_TOKEN, token);
        editor.apply();
    }


    public String getOauthToken() {
        return pref.getString(OAUTH_TOKEN, "");
    }


    public void setOauthTokenCreatedTime(Date date) {
        editor.putLong(OAUTH_TOKEN_CREATED_DATE, date.getTime());
        editor.apply();
    }

    public Long getOauthTokenCreatedTime() {
        return pref.getLong(OAUTH_TOKEN_CREATED_DATE, 0L);
    }

    public String getEmail() {
        return pref.getString(EMAIL, "");
    }

}
