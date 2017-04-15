package br.almadaapps.civilapp.domains;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import br.almadaapps.civilapp.interfaces.GetterPalestra;

/**
 * Created by Vinicius on 14.4.17.
 */

public class Palestra {
    public static final String DR_PALESTRA = "SIMEC/palestras";
    private static List<Palestra> list;
    private Integer day;
    private Integer init;
    private String palestrante;
    private String title;

    public Palestra() {
    }

    @Exclude
    public static void getPalestras(final GetterPalestra getterPalestra) {
        DatabaseReference palestraRef = FirebaseDatabase.getInstance().getReference().child(DR_PALESTRA);
        palestraRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Palestra>> t = new GenericTypeIndicator<List<Palestra>>() {
                };
                list = dataSnapshot.getValue(t);
                getterPalestra.getPalestra(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Integer getDay() {
        return day;
    }

    public Integer getInit() {
        return init;
    }

    public String getPalestrante() {
        return palestrante;
    }

    public String getTitle() {
        return title;
    }

    @Exclude
    public String getTime() {
        int duration = 50;
        int timeInit = init;

        int iH = timeInit / 100;
        int iM = timeInit - (timeInit / 100 * 100);

        int fH = 0;
        int fM = iM + duration;
        if (fM > 60) {
            fM -= 60;
            fH = iH + 1;
        } else if (fM < 60) {
            fH = iH;
        } else {
            fM = 0;
            fH = iH + 1;
        }
        String siH;
        String siM;
        String sfH;
        String sfM;

        if (iH < 10) {
            siH = "0" + iH;
        } else {
            siH = String.valueOf(iH);
        }
        if (iM < 10) {
            siM = "0" + iM;
        } else {
            siM = String.valueOf(iM);
        }
        if (fH < 10) {
            sfH = "0" + fH;
        } else {
            sfH = String.valueOf(fH);
        }
        if (fM < 10) {
            sfM = "0" + fM;
        } else {
            sfM = String.valueOf(fM);
        }

        return siH + ":" + siM + "hs às " + sfH + ":" + sfM + "hs";
    }
}
