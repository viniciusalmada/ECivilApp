package br.com.viniciusalmada.civilapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import br.com.viniciusalmada.civilapp.domains.User;

/**
 * Created by vinicius-almada on 16/03/17.
 */

public class LoginActivity extends CommonActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = "LoginActivity";
    public static final String KEY_USER_PARCELABLE = "KEY_USER_PARCELABLE";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager callbackManager;
    private Button btFacebook;
    private Button btGoogle;
    private ProgressBar pbLogin;

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, null /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Init Facebook Login
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: " + loginResult.getAccessToken());
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: " + error.getMessage());
                showLoginButtons();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                showProgress();
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
//                    showDialog("Initializing...", true);
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(User.DR_USERS);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String code = dataSnapshot.child(user.getUid()).child("code").getValue(String.class);
                            Log.d(TAG, "onDataChange: " + code);
                            if (code != null) {
                                Log.d(TAG, "onDataChange: code not null");
                                User u = dataSnapshot.child(user.getUid()).getValue(User.class);
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.putExtra(KEY_USER_PARCELABLE, u);
                                startActivity(intent);
                                showToast("Login successfully", true);
                                finish();
                            } else {
                                Log.d(TAG, "onDataChange: code null");
                                User u = new User();
                                u.setName(user.getDisplayName());
                                u.setEmail(user.getEmail());
                                u.setProfilePic(String.valueOf(user.getPhotoUrl()));
                                u.setUid(user.getUid());
                                User.writeOnFirebase(u);

                                Intent intent = new Intent(LoginActivity.this, DataInitialInputActivity.class);
                                intent.putExtra(KEY_USER_PARCELABLE, u);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    showLoginButtons();
                }
            }
        };
        setContentView(R.layout.activity_login);
        initViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                showLoginButtons();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialog();
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        if (accessToken != null) {
            AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

            mAuth.signInWithCredential(credential)
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthUserCollisionException) {
                                showToast("An acoount with this email already exists", true);
                            }
                        }
                    })
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                showToast("Authentication failed", true);
                            }
                        }
                    });

        }
        hideDialog();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            showToast("An acoount with this email already exists", true);
                        }
                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            showToast("Authentication failed", true);
                        }
                    }
                });

        hideDialog();
    }

    private void signInGoogle() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    Log.d(TAG, "onResult: " + status.getStatusMessage());
                }
            });
        }
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void showLoginButtons() {
        pbLogin.setVisibility(View.GONE);
        ((LinearLayout) btFacebook.getParent()).setVisibility(View.VISIBLE);
    }

    private void showProgress() {
        pbLogin.setVisibility(View.VISIBLE);
        ((LinearLayout) btFacebook.getParent()).setVisibility(View.GONE);
    }

    @Override
    protected void initViews() {
        btFacebook = (Button) findViewById(R.id.bt_login_facebook);
        btGoogle = (Button) findViewById(R.id.bt_login_google);
        pbLogin = (ProgressBar) findViewById(R.id.pb_login);

        btFacebook.setOnClickListener(this);
        btGoogle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        showProgress();
        switch (v.getId()) {
            case R.id.bt_login_facebook:
                Log.d(TAG, "onClick: " + "Facebook button clicked");
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
                break;
            case R.id.bt_login_google:
                Log.d(TAG, "onClick: " + "Google button clicked");
                signInGoogle();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
