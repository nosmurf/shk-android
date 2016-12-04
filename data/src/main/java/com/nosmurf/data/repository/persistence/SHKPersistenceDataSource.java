package com.nosmurf.data.repository.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Created by Daniel on 03/12/2016.
 */

public class SHKPersistenceDataSource implements PersistenceDataSource {

    private final SharedPreferences sharedPreferences;

    private static String HOME_ID_KEY = "HomeIdKey";

    @Inject
    public SHKPersistenceDataSource(Context context) {
        this.sharedPreferences = context.getSharedPreferences("sharedpreferences", Context.MODE_PRIVATE);

    }

    @Override
    public void setHomeId(String id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HOME_ID_KEY, id);
        editor.commit();
    }

    @Override
    public String getHomeId() {
        return sharedPreferences.getString(HOME_ID_KEY, "");
    }


}
