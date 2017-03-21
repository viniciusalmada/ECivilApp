package br.com.viniciusalmada.civilapp.domains;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by vinicius-almada on 18/03/17.
 */

public class User implements Parcelable {
    public static final String DR_USERS = "users";
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
    private int[] subjects;

    public User() {
    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        profilePic = in.readString();
        uid = in.readString();
    }

    public static void writeOnFirebase(User u) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(DR_USERS);
        userRef.child(u.getUid()).setValue(u);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(profilePic);
        dest.writeString(uid);
    }
}
