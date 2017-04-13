package br.com.viniciusalmada.civilapp.domains;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.viniciusalmada.civilapp.interfaces.GetterCalendar;

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
    private Integer initPeriod;
    private int endPeriod;
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

    public Integer getInitPeriod() {
        return initPeriod;
    }

    public int getEndPeriod() {
        return endPeriod;
    }

    public int getFormat() {
        return format;
    }

    public boolean isLink() {
        return isLink;
    }

    public String getLink() {
        return link;
    }
}
