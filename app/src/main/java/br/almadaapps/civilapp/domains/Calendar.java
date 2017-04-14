package br.almadaapps.civilapp.domains;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.almadaapps.civilapp.interfaces.GetterCalendar;

/**
 * Created by Vinicius on 12.4.17.
 */

public class Calendar {
    public static final String DR_CALENDAR = "calendar";
    public static final int TEXT_NORMAL = 1;
    public static final int TEXT_WARNING = 2;
    public static final int TEXT_EVENTS = 3;
    public static final int TEXT_HOLIDAY = 4;
    public static final int TEXT_GRIFFIN = 5;
    private static List<Calendar> list = new ArrayList<>();
    private String text;
    private int month;
    private Integer init;
    private int end;
    private int format;
    private boolean isLink;
    private String link;

    public Calendar() {
    }

    @Exclude
    public static void getCalendar(final GetterCalendar getterCalendar) {
        DatabaseReference calRef = FirebaseDatabase.getInstance().getReference().child(DR_CALENDAR);
        calRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Calendar>> t = new GenericTypeIndicator<List<Calendar>>() {
                };
                list = dataSnapshot.child("Y2017_1").getValue(t);
                getterCalendar.getCalendar(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getText() {
        return text;
    }

    public int getMonth() {
        return month;
    }

    public Integer getInit() {
        return init;
    }

    public int getEnd() {
        return end;
    }

    public int getFormat() {
        return format;
    }

    public boolean getIsLink() {
        return isLink;
    }

    public String getLink() {
        return link;
    }
}
