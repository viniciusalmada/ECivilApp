package br.com.viniciusalmada.civilapp.domains;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vinicius-almada on 18/03/17.
 */
@IgnoreExtraProperties
public class User implements Parcelable {
    public static final String DR_USERS = "users";

    public static final String KEY_USER_NAME = "KEY_USER_NAME ";
    public static final String KEY_USER_EMAIL = "KEY_USER_EMAIL ";
    public static final String KEY_USER_PROFILEPIC = "KEY_USER_PROFILEPIC ";
    public static final String KEY_USER_UID = "KEY_USER_UID ";
    public static final String KEY_USER_CODE = "KEY_USER_CODE ";
    public static final String KEY_USER_PERIOD = "KEY_USER_PERIOD ";

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private String name;
    private String email;
    private String profilePic;
    private String uid;
    private String code;
    private int period;

    public User() {
    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        profilePic = in.readString();
        code = in.readString();
        uid = in.readString();
        period = in.readInt();
    }

    public static void writeOnFirebase(User u) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(DR_USERS);
        userRef.child(u.getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("User ", "onComplete: " + task.isSuccessful());
            }
        });
    }

    public static User getUserFromPreferences(SharedPreferences mPreferences) {
        User u = new User();
        u.setName(mPreferences.getString(KEY_USER_NAME, "Fulano"));
        u.setEmail(mPreferences.getString(KEY_USER_EMAIL, "fulano@email.com"));
        u.setProfilePic(mPreferences.getString(KEY_USER_PROFILEPIC, "https://cdn1.iconfinder.com/data/icons/social-shade-rounded-rects/512/anonymous-128.png"));
        u.setCode(mPreferences.getString(KEY_USER_CODE, "[Not found]"));
        u.setUid(mPreferences.getString(KEY_USER_UID, "sdfasdfasdfasdf"));
        u.setPeriod(mPreferences.getInt(KEY_USER_PERIOD, 0));
        return u;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("email", email);
        hashMap.put("profilePic", profilePic);
        hashMap.put("code", code);
        hashMap.put("uid", uid);
        hashMap.put("period", period);
        return hashMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(profilePic);
        dest.writeString(code);
        dest.writeString(uid);
        dest.writeInt(period);
    }
}
