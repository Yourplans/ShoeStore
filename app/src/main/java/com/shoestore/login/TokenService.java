package com.shoestore.login;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.shoestore.chat.UserDetails;

/**
 * Created by Daniel on 25/04/2017.
 */

public class TokenService  extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token  = FirebaseInstanceId.getInstance().getToken();

        registrationToken(token);
    }

    private void registrationToken(String token) {

        UserDetails.token=token;

    }
}
