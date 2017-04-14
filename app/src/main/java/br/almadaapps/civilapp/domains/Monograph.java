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

import br.almadaapps.civilapp.interfaces.GetterMonographs;

/**
 * Created by vinicius-almada on 03/04/17.
 */

public class Monograph {
    public static final String DR_MONOGRAPHS = "monographs";
    private static List<Monograph> list = new ArrayList<>();
    private String title;
    private String author;
    private String link;

    public Monograph() {
    }

    @Exclude
    public static void getMonographs(final GetterMonographs getterMonographs) {
        DatabaseReference monoRef = FirebaseDatabase.getInstance().getReference().child(DR_MONOGRAPHS);
        monoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Monograph>> t = new GenericTypeIndicator<List<Monograph>>() {
                };
                list = dataSnapshot.getValue(t);
                getterMonographs.getMonographs(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getLink() {
        return link;
    }
}
