package com.polatechno.androidtestexercise.AppUtils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.internal.LazilyParsedNumber;
import com.polatechno.androidtestexercise.model.PartnerAccount;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyHelperMethods {

    public static void saveUserInfo(Context context, PartnerAccount partnerAccount, Gson gson) {
        SharedPreferenceHelper.setSharedPreferenceString(context, AppContants.USER_INFO, gson.toJson(partnerAccount, PartnerAccount.class));
    }



    public static PartnerAccount getLoggedInUser(Context context, Gson gson) {

        PartnerAccount partnerAccount = null;

        String userInfoCached = SharedPreferenceHelper.getSharedPreferenceString(context, AppContants.USER_INFO, "");
        if (!TextUtils.isEmpty(userInfoCached)) {
            partnerAccount = gson.fromJson(userInfoCached, PartnerAccount.class);
        }

        return partnerAccount;
    }

    public static void logOutUser(Context context) {

        SharedPreferenceHelper.setSharedPreferenceString(context, AppContants.USER_INFO, "");

    }

    public static String unixTimeToHumanReadable(long unixSeconds) {
        // convert seconds to milliseconds
        Date date = new java.util.Date(unixSeconds * 1000L);
        // the format of your date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // give a timezone reference for formatting (see comment at the bottom)
        return sdf.format(date);

    }


}
