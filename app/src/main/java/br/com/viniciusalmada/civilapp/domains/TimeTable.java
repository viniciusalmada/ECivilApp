package br.com.viniciusalmada.civilapp.domains;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by vinicius-almada on 23/03/17.
 */

public class TimeTable implements Parcelable {
    public static final String DR_TIMETABLE = "timetables";
    public static final Creator<TimeTable> CREATOR = new Creator<TimeTable>() {
        @Override
        public TimeTable createFromParcel(Parcel source) {
            return new TimeTable(source);
        }

        @Override
        public TimeTable[] newArray(int size) {
            return new TimeTable[size];
        }
    };

    private String name;
    private String prof;
    private String code;
    private List<Integer> timeinit;
    private int year;
    private int period;
    private List<Integer> day;

    public TimeTable() {
    }

    protected TimeTable(Parcel in) {
        name = in.readString();
        prof = in.readString();
        code = in.readString();
        in.readList(timeinit, List.class.getClassLoader());
        year = in.readInt();
        period = in.readInt();
        in.readList(day, List.class.getClassLoader());
    }

    public static String getStringTimetable(int timeInit, int duration) {
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

        return siH + ":" + siM + "\n" + sfH + ":" + sfM;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Integer> getTimeinit() {
        return timeinit;
    }

    public void setTimeinit(List<Integer> timeinit) {
        this.timeinit = timeinit;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public List<Integer> getDay() {
        return day;
    }

    public void setDay(List<Integer> day) {
        this.day = day;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(prof);
        dest.writeString(code);
        dest.writeList(timeinit);
        dest.writeInt(year);
        dest.writeInt(period);
        dest.writeList(day);
    }

    public static class TimeLine {
        private String subject;
        private String professor;
        private String time;
        private String code;
        private int timeinit;

        public TimeLine(String subject, String professor, String time, String code, int timeinit) {
            this.subject = subject;
            this.professor = professor;
            this.time = time;
            this.code = code;
            this.timeinit = timeinit;
        }

        public String getSubject() {
            return subject;
        }

        public String getProfessor() {
            return professor;
        }

        public String getTime() {
            return time;
        }

        public String getCode() {
            return code;
        }

        public Integer getTimeinit() {
            return timeinit;
        }
    }
}