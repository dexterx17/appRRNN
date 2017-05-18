package santana.estudio.tungurahuaclima.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import santana.estudio.tungurahuaclima.data.RrnnContract;

/**
 * Created by dexter on 17/05/2017.
 */

public class SyncUtils {

    private static final int SYNC_INTERVAL_HOURS = 6;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static boolean sInit;

    private static final String STATIONS_SYNC_TASK = "stations-sync";

    static void syncWithServerPeriodically(@NonNull final Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job syncJob = dispatcher.newJobBuilder()
                .setService(FirebaseJobService.class)
                .setTag(STATIONS_SYNC_TASK)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(syncJob);
    }
    synchronized public static void init(@NonNull final Context context) {
        if(sInit) return;

        sInit=true;

        syncWithServerPeriodically(context);
        Thread checkEmptyDB = new Thread(new Runnable() {
            @Override
            public void run() {
                Uri stationUri = RrnnContract.StationEntry.CONTENT_URI;
                String[] proj = {RrnnContract.StationEntry._ID};
                String select = "";
                Cursor cursor = context.getContentResolver().query(
                        stationUri,
                        proj,
                        null,
                        null,
                        null);
                if (cursor == null || cursor.getCount() == 0) {
                    startSyncNow(context);
                }
                cursor.close();
            }
        });
        checkEmptyDB.start();
    }
    public static void startSyncNow(@NonNull final Context context) {
        Intent intent = new Intent(context, StationsSyncIntentService.class);
        context.startService(intent);
    }
}
