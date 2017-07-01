package com.nbdeg.unityplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.firebase.auth.FirebaseAuth;
import com.nbdeg.unityplanner.utils.Database;

import java.util.Arrays;

public class LauncherLogin extends AppCompatActivity {

    public static final String[] SCOPES = { ClassroomScopes.CLASSROOM_COURSES_READONLY, ClassroomScopes.CLASSROOM_ROSTERS_READONLY, ClassroomScopes.CLASSROOM_COURSEWORK_ME_READONLY };
    private static final int RC_SIGN_IN = 145;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_login);

        // Checks if any user is signed in.
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // No user signed in, sign them in.
            signIn();
        } else {
            Intent i = new Intent(LauncherLogin.this, Dashboard.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
        }
    }

    /**
     * Launches AuthUI sign-in activity.
     */
    private void signIn() {
        AuthUI.IdpConfig googleIdp = new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                .setPermissions(Arrays.asList(SCOPES))
                .build();

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(Arrays.asList(
                                googleIdp,
                                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
                        .setLogo(R.mipmap.ic_launcher_round)
                        .build(),
                RC_SIGN_IN);
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

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {

                // Start app intro
                Database.refreshDatabase(getApplicationContext());
                Intent i = new Intent(LauncherLogin.this, Dashboard.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
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
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "An unknown error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Toast.makeText(this, "An unknown error occurred. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
