package santana.estudio.tungurahuaclima.sync;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import santana.estudio.tungurahuaclima.data.RrnnContract;

/**
 * Created by dexter on 17/05/2017.
 */

public class FirebaseJobService extends JobService {
    private AsyncTask<Void, Void, Void> mFetchStations;

    @Override
    public boolean onStartJob(final JobParameters job) {
        mFetchStations=new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                StationsSyncTask.syncStations(getApplicationContext());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(job,false);
            }
        };
        mFetchStations.execute();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mFetchStations != null) {
            mFetchStations.cancel(true);
        }
        return true;
    }
}
