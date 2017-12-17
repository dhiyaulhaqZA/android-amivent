package id.ac.amikom.avent.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dhiyaulhaqza on 12/2/17.
 */

public final class UserPreferenceUtil {

    private static SharedPreferences sUserPreferences;
    private static final String PREF_NAME = "id.ac.amikom.avent.utility.PREF_USER";
    private static final String KEY_USER_NO_ID = "no_id";
    private static final String KEY_USER_ORGANIZATION = "organization";
    private static final String KEY_USER_PHONE_NUMBER = "phone_number";

    private UserPreferenceUtil() {
    }

    public static UserPreferenceUtil newInstance(Context context) {
        if (sUserPreferences == null) {
            sUserPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        return new UserPreferenceUtil();
    }

    public void writeNoId(String noId) {
        SharedPreferences.Editor editor = sUserPreferences.edit();
        editor.putString(KEY_USER_NO_ID, noId).apply();
    }

    public String readNoId() {
        return sUserPreferences.getString(KEY_USER_NO_ID, "");
    }

    public void writeOrganization(String organization) {
        SharedPreferences.Editor editor = sUserPreferences.edit();
        editor.putString(KEY_USER_ORGANIZATION, organization).apply();
    }

    public String readOrganization() {
        return sUserPreferences.getString(KEY_USER_ORGANIZATION, "");
    }

    public void writePhoneNumber(String phoneNumber) {
        SharedPreferences.Editor editor = sUserPreferences.edit();
        editor.putString(KEY_USER_PHONE_NUMBER, phoneNumber).apply();
    }

    public String readPhoneNumber() {
        return sUserPreferences.getString(KEY_USER_PHONE_NUMBER, "");
    }
}
