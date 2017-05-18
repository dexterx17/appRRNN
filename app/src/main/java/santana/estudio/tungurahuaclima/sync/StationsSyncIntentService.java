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
        StationsSyncTask.syncStations(this);
    }
}
