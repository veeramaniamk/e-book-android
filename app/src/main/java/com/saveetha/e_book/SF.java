package com.saveetha.e_book;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.util.HashMap;
import java.util.Map;

public interface SF {

    static SharedPreferences getSignInSF(@NonNull FragmentActivity activity) {
        return activity.getSharedPreferences(Constant.SIGN_IN_SF, Context.MODE_PRIVATE);
    }
    @NonNull
    static Map<String, String> getSignInSFValue(FragmentActivity activity) {
        Map<String, String> keys = new HashMap<>();
        SharedPreferences sf = getSignInSF(activity);

        String name     = sf.getString(Constant.NAME_SI_SF,null);
        String email    = sf.getString(Constant.EMAIL_SI_SF,null);
        int userId      = sf.getInt(Constant.ID_SI_SF,0);
        String gender   = sf.getString(Constant.GENDER_SI_SF,null);
        String profile  = sf.getString(Constant.PROFILE_SI_SF,null);
        String userType = ""+sf.getInt(Constant.USER_TYPE_SI_SF,0);
        long phone      = sf.getLong(Constant.PHONE_SI_SF,0);

        keys.put(Constant.NAME_SI_SF,   name);
        keys.put(Constant.EMAIL_SI_SF,  email);
        keys.put(Constant.ID_SI_SF,     ""+userId);
        keys.put(Constant.USER_TYPE_SI_SF, userType);
        keys.put(Constant.GENDER_SI_SF, gender);
        keys.put(Constant.PROFILE_SI_SF,profile);
        keys.put(Constant.PHONE_SI_SF,""+phone);

        return keys;
    }

}
