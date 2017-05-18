package santana.estudio.tungurahuaclima.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import santana.estudio.tungurahuaclima.R;

/**
 * Created by dexter on 18/05/2017.
 */

public class PreferencesUtils {
    /**
     * Returns the location coordinates associated with the location. Note that there is a
     * possibility that these coordinates may not be set, which results in (0,0) being returned.
     * Interestingly, (0,0) is in the middle of the ocean off the west coast of Africa.
     *
     * @param context used to access SharedPreferences
     * @return an array containing the two coordinate values for the user's preferred location
     */
    public static String getDecimales(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String decimales = sp.getString(context.getResources().getString(R.string.pref_decimales_key),"4");

        return decimales;
    }
}
