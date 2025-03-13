package com.saveetha.e_book;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.saveetha.e_book.request.Signin;
import com.saveetha.e_book.response.SignInResponse;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public interface Constant extends PaymentResultListener {
//    String BASE_URL = "https://4612-180-235-121-242.ngrok-free.app";
    String BASE_URL = "http://180.235.121.245:8630";

    String[] GENDER = { "MALE", "FEMALE" };
    String SIGN_IN_SF       = "SF_SI";
    String NAME_SI_SF       = "SF_NAME_SI";
    String ID_SI_SF         = "SF_ID_SI";
    String EMAIL_SI_SF      = "SF_EMAIL_SI";
    String PHONE_SI_SF      = "SF_PHONE_SI";
    String USER_TYPE_SI_SF  = "SF_USER_TYPE_SI";
    String GENDER_SI_SF     = "SF_GENDER_SI";
    String PROFILE_SI_SF    = "SF_PROFILE_SI";

    default void apiCall() {
        final Context context = null;
        Call<SignInResponse> responseCall = RestClient.makeAPI().signIn(new Signin());
        responseCall.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(@NonNull Call<SignInResponse> call, @NonNull Response<SignInResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getStatus() ==200) {
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, ""+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SignInResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", t.getMessage());
            }
        });
    }

    default void payment(String packageName, long amount,String username,String userEmail) {
        // initialize Razorpay account.
        Checkout checkout = new Checkout();

        // set your id as below
        checkout.setKeyID("rzp_test_U2XWpODmhRkL0l");

        // set image
        checkout.setImage(R.drawable.profile);

        // initialize json object
        JSONObject object = new JSONObject();
        try {
            // to put name
            object.put("name", username);

            // put description
            object.put("description", "Test payment");

            // to set theme color
            object.put("theme.color", "");

            // put the currency
            object.put("currency", "INR");

            // put amount
            object.put("amount", (amount*100));

            // put mobile number
            object.put("prefill.contact", "");

            // put package name
            object.put("package name",packageName);

            // put email
            object.put("prefill.email", userEmail);

            // open razorpay to checkout activity
            checkout.open(null, object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
