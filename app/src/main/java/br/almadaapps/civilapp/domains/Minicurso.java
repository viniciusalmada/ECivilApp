package br.almadaapps.civilapp.domains;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import br.almadaapps.civilapp.interfaces.GetterMinicurso;

/**
 * Created by Vinicius on 14.4.17.
 */

public class Minicurso {
    public static final String DR_MINICURSO = "SIMEC/minicursos";
    private static final String[] days = {"08/MAI - SEG", "09/MAI - TER", "10/MAI - QUA", "11/MAI - QUI", "12/MAI - SEX"};
    private static List<Minicurso> list;
    private String author;
    private Integer dayend;
    private Integer dayinit;
    private Integer endtime;
    private String local;
    private Integer timeinit;
    private String title;

    public Minicurso() {

    }

    @Exclude
    public static void getMinicursos(final GetterMinicurso getterMinicurso) {
        DatabaseReference palestraRef = FirebaseDatabase.getInstance().getReference().child(DR_MINICURSO);
        palestraRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Minicurso>> t = new GenericTypeIndicator<List<Minicurso>>() {
                };
                list = dataSnapshot.getValue(t);
                getterMinicurso.getMinicursos(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getAuthor() {
        return author;
    }

    public Integer getDayend() {
        return dayend;
    }

    public Integer getDayinit() {
        return dayinit;
    }

    public Integer getEndtime() {
        return endtime;
    }

    public String getLocal() {
        return local;
    }

    public Integer getTimeinit() {
        return timeinit;
    }

    public String getTitle() {
        return title;
    }

    @Exclude
    public String getTime(int init, int day) {

        int iH = init / 100;
        int iM = init - (init / 100 * 100);

        int fH;
        int fM = 0;
        fH = iH + 4;
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
        sfM = "0" + fM;

        return siH + ":" + siM + "hs às " + sfH + ":" + sfM + "hs\n" + days[day];
    }
}
