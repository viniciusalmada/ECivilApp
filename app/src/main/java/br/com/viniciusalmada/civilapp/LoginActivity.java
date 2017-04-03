package br.com.viniciusalmada.civilapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
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
import br.com.viniciusalmada.civilapp.interfaces.NetworkChangesImpl;
import br.com.viniciusalmada.civilapp.utils.NetworkStateReceiver;

import static br.com.viniciusalmada.civilapp.domains.User.KEY_USER_CODE;
import static br.com.viniciusalmada.civilapp.domains.User.KEY_USER_EMAIL;
import static br.com.viniciusalmada.civilapp.domains.User.KEY_USER_NAME;
import static br.com.viniciusalmada.civilapp.domains.User.KEY_USER_PERIOD;
import static br.com.viniciusalmada.civilapp.domains.User.KEY_USER_PROFILEPIC;
import static br.com.viniciusalmada.civilapp.domains.User.KEY_USER_UID;

/**
 * Created by vinicius-almada on 16/03/17.
 */

public class LoginActivity extends CommonActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, NetworkChangesImpl {
    public static final String TAG = "LoginActivity";
    public static final String KEY_USER_PARCELABLE = "KEY_USER_PARCELABLE";
    public static final String PREFERENCES = "PREFERENCES";
    public static final String KEY_BOOLEAN_IS_LOGGED = "KEY_BOOLEAN_IS_LOGGED";
    private static final int RC_SIGN_IN = 9001;
    private SharedPreferences mPreferences;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager callbackManager;
    private Button btFacebook;
    private Button btGoogle;
    private ProgressBar pbLogin;

    private NetworkStateReceiver networkStateReceiver;

    public static void setUserOnSharedPreferences(Context context, User user) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES, MODE_PRIVATE).edit();
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_PROFILEPIC, user.getProfilePic());
        editor.putString(KEY_USER_CODE, user.getCode());
        editor.putString(KEY_USER_UID, user.getUid());
        editor.putInt(KEY_USER_PERIOD, user.getPeriod());
        editor.putBoolean(KEY_BOOLEAN_IS_LOGGED, true);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.activity_login);
        initSharedPreferences();
        initViews();

        IntentFilter it = new IntentFilter();
        it.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        it.addCategory(Intent.CATEGORY_DEFAULT);
        networkStateReceiver = new NetworkStateReceiver(this);

        registerReceiver(networkStateReceiver, it);
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
        if (mAuth != null && mAuthListener != null)
            mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuth != null && mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkStateReceiver);
    }

    private void initSharedPreferences() {
        mPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        boolean isLogged = mPreferences.getBoolean(KEY_BOOLEAN_IS_LOGGED, false);
        if (isLogged) {
            User user;
            user = User.getUserFromPreferences(mPreferences);
            confIntent(HomeActivity.class, user, true);
        }
    }

    @Override
    protected void initViews() {
        btFacebook = (Button) findViewById(R.id.bt_login_facebook);
        btGoogle = (Button) findViewById(R.id.bt_login_google);
        pbLogin = (ProgressBar) findViewById(R.id.pb_login);

        btFacebook.setOnClickListener(this);
        btGoogle.setOnClickListener(this);
    }

    private void initConnections() {
        try {
            initGoogleSignIn();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        initFacebookSignIn();
        initAuth();
    }

    private void initFacebookSignIn() {
        // Init Facebook Login
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: " + loginResult.getAccessToken());
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
                showLoginButtons();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: " + error.getMessage());
                showLoginButtons();
            }
        });
    }

    private void initGoogleSignIn() throws IllegalStateException {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void initAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                showProgress();
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(User.DR_USERS);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String code = dataSnapshot.child(user.getUid()).child("code").getValue(String.class);
                            if (code != null) {
                                User u = dataSnapshot.child(user.getUid()).getValue(User.class);
                                setUserOnSharedPreferences(getBaseContext(), u);
                                confIntent(HomeActivity.class, u, true);
                            } else {
                                User u = confUser(user);
                                User.writeOnFirebase(u);
                                confIntent(DataInitialInputActivity.class, u, false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    showLoginButtons();
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void firebaseAuthWithFacebook(AccessToken accessToken) {
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
        ((LinearLayout) btFacebook.getParent()).setVisibility(View.VISIBLE);
        pbLogin.setVisibility(View.GONE);
        findViewById(R.id.bt_offline).setVisibility(View.GONE);
    }

    private void showProgress() {
        ((LinearLayout) btFacebook.getParent()).setVisibility(View.GONE);
        pbLogin.setVisibility(View.VISIBLE);
        findViewById(R.id.bt_offline).setVisibility(View.GONE);
        AnimatorSet set = new AnimatorSet();
        pbLogin.setTranslationX(0);
        pbLogin.setTranslationY(0);
        Skill s = Skill.ElasticEaseIn;
        set.playTogether(
                Glider.glide(s, 2200, ObjectAnimator.ofFloat(pbLogin, "translationY", 0, -160))
        );
        set.setDuration(2200);
        set.start();

    }

    private void showOffline() {
        ((LinearLayout) btFacebook.getParent()).setVisibility(View.GONE);
        pbLogin.setVisibility(View.GONE);
        findViewById(R.id.bt_offline).setVisibility(View.VISIBLE);
    }

    private void confIntent(Class<? extends AppCompatActivity> classDestiny, User u2Intent, boolean showToast) {
        Intent intent = new Intent(this, classDestiny);
        intent.putExtra(KEY_USER_PARCELABLE, u2Intent);
        startActivity(intent);
        finish();
    }

    private User confUser(FirebaseUser user) {
        User u = new User();
        u.setName(user.getDisplayName());
        u.setEmail(user.getEmail());
        u.setProfilePic(String.valueOf(user.getPhotoUrl()));
        u.setUid(user.getUid());
        return u;
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
        showLoginButtons();
    }

    @Override
    public void setConnection(boolean isConnected) {
        Log.d(TAG, "setConnection: " + isConnected);
        if (isConnected) {
            initConnections();
        } else {
            showSnackbar("Sem conexão, ative para entrar ou acesse offline", findViewById(R.id.bt_login_google));
            showOffline();
        }
    }
}
