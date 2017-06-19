package br.almadaapps.civilapp.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import br.almadaapps.civilapp.adapters.SchedulesWidgetFactoryAdapter;

/**
 * Created by vinicius on 16/06/17.
 */

public class SchedulesWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new SchedulesWidgetFactoryAdapter(this, intent);
    }
}
