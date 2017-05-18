package santana.estudio.tungurahuaclima.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by dexter on 17/05/2017.
 */

public class StationsSyncIntentService extends IntentService
{

    public StationsSyncIntentService(){
        super("StationsSyncIntentService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent.hasExtra(SyncUtils.SYNC_TASK)) {
            String task = intent.getStringExtra(SyncUtils.SYNC_TASK);
            switch (task) {
                case SyncUtils.STATIONS_SYNC_TASK:
                    StationsSyncTask.syncStations(this);
                    break;
                case SyncUtils.PARAMS_STATION_SYNC_TASK:
                    String stationId = intent.getStringExtra(SyncUtils.STATION_KEY_ID);
                    StationsSyncTask.syncParamsStation(this,stationId);
                    break;
            }
        }
    }
}
