package br.com.viniciusalmada.civilapp.domains;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.List;

/**
 * Created by vinicius-almada on 27/03/17.
 */

public class Syllabus implements Parcelable {
    public static final String TAG = "Syllabus";
    public static final String DR_SYLLABUS = "subjects";
    public static final Creator<Syllabus> CREATOR = new Creator<Syllabus>() {
        @Override
        public Syllabus createFromParcel(Parcel source) {
            return new Syllabus(source);
        }

        @Override
        public Syllabus[] newArray(int size) {
            return new Syllabus[size];
        }
    };

    private String name;
    private String code;
    private int period;
    private List<String> preReq;
    private int semesterLoad;
    private int weeklyLoad;
    private int credits;
    private String syllabus;
    private String objective;
    private String content;
    private String refer;

    public Syllabus() {
    }

    public Syllabus(Parcel source) {
        this.name = source.readString();
        this.code = source.readString();
        this.period = source.readInt();
        source.readList(this.preReq, List.class.getClassLoader());
        this.semesterLoad = source.readInt();
        this.weeklyLoad = source.readInt();
        this.credits = source.readInt();
        this.syllabus = source.readString();
        this.objective = source.readString();
        this.content = source.readString();
        this.refer = source.readString();
    }

    @Exclude
    public static String getCodeFromList(List<Syllabus> syllabuses, List<String> codes) {

        if (codes == null) {
            return "[Não há]";
        }
        String resultOne = "";
        String resultTwo = "";
        if (codes.size() == 1) {
            for (String c : codes) {
                for (Syllabus s : syllabuses) {
                    if (c.trim().equals(s.getCode().trim())) {
                        return s.getName();
                    }
                }
                return codes.get(0);
            }
        } else {
            for (Syllabus s : syllabuses) {
                if (codes.get(0).trim().equals(s.getCode().trim())) {
                    resultOne = s.getName();
                }
                if (codes.get(1).trim().equals(s.getCode().trim())) {
                    resultTwo = s.getName();
                }
            }
        }
        return resultOne.concat("\n\n").concat(resultTwo);
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getPeriod() {
        return period;
    }

    public List<String> getPreReq() {
        return preReq;
    }

    public int getSemesterLoad() {
        return semesterLoad;
    }

    public int getWeeklyLoad() {
        return weeklyLoad;
    }

    public int getCredits() {
        return credits;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public String getObjective() {
        return objective;
    }

    public String getContent() {
        return content;
    }

    public String getRefer() {
        return refer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(code);
        dest.writeInt(period);
        dest.writeList(preReq);
        dest.writeInt(semesterLoad);
        dest.writeInt(weeklyLoad);
        dest.writeInt(credits);
        dest.writeString(syllabus);
        dest.writeString(objective);
        dest.writeString(content);
        dest.writeString(refer);
    }
}
