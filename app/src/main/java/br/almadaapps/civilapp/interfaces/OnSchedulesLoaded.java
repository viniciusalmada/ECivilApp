package br.almadaapps.civilapp.interfaces;

import java.util.ArrayList;

import br.almadaapps.civilapp.domains.Schedule;

/**
 * Created by vinicius on 18/06/17.
 */

public interface OnSchedulesLoaded {
    void onSchedulesLoaded(ArrayList<Schedule.TimeLine> timeLines);
}
