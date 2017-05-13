package com.nbdeg.unityplanner.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.UserInfo;
import com.nbdeg.unityplanner.LauncherLogin;
import com.nbdeg.unityplanner.R;
import com.nbdeg.unityplanner.Settings;

import java.util.Arrays;
import java.util.Collections;

public class LinkLogin extends AppCompatActivity {

    private static final int RC_LINK_SIGN_IN = 146;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        signIn();
    }

    /**
     * Launches AuthUI sign-in activity.
     */
    private void signIn() {
        boolean googleExisting = false;
        boolean facebookExisting = false;

        AuthUI.IdpConfig googleIdp = new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                .setPermissions(Arrays.asList(LauncherLogin.SCOPES))
                .build();

        for (UserInfo info : Database.getUser().getProviderData()) {
            if (info.getProviderId().equals("google.com")) {
                googleExisting = true;
            } else if (info.getProviderId().equals("facebook.com")) {
                facebookExisting = true;
            }
        }

        if (!facebookExisting && !googleExisting) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(Arrays.asList(
                                    googleIdp,
                                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                            .setLogo(R.mipmap.ic_launcher_round)
                            .build(),
                    RC_LINK_SIGN_IN);
        } else if (!facebookExisting) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(Collections.singletonList(
                                    googleIdp))
                            .setLogo(R.mipmap.ic_launcher_round)
                            .build(),
                    RC_LINK_SIGN_IN);
        } else if (!googleExisting) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(Collections.singletonList(
                                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                            .setLogo(R.mipmap.ic_launcher_round)
                            .build(),
                    RC_LINK_SIGN_IN);
        } else {
            startActivity(new Intent(LinkLogin.this, Settings.class));
        }
    }

    /**
     * Receives data after AuthUI finishes its sign-in procedure.
     * @param requestCode RC code passed from AuthUI
     * @param resultCode AuthUI code telling is user was successfully signed in
     * @param data AuthUI error code, if given
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_LINK_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {

                Database.refreshDatabase();
                finish();
                return;

            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "An internet connection is required to login.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "An unknown error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }

            finish();
            Toast.makeText(this, "An unknown error occurred. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
