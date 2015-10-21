package org.meruvian.esales.collector.content.database.adapter;

import android.database.Cursor;

import org.meruvian.esales.collector.content.database.model.DefaultPersistenceModel;
import org.meruvian.midas.core.entity.LogInformation;

import java.util.Date;
import java.util.UUID;

/**
 * Created by meruvian on 25/03/15.
 */
public class DefaultDatabaseAdapter {

    public static String generateId() {
        UUID uuid = UUID.randomUUID();

        return String.valueOf(uuid);
    }

    public static LogInformation getLogInformation(Cursor cursor) {
        LogInformation logInformation = new LogInformation();
        logInformation.setCreateDate(new Date(cursor.getLong(cursor.getColumnIndex(DefaultPersistenceModel.CREATE_DATE))));
        logInformation.setCreateBy(cursor.getString(cursor.getColumnIndex(DefaultPersistenceModel.CREATE_BY)));
        logInformation.setUpdateDate(new Date(cursor.getLong(cursor.getColumnIndex(DefaultPersistenceModel.UPDATE_DATE))));
        logInformation.setUpdateBy(cursor.getString(cursor.getColumnIndex(DefaultPersistenceModel.UPDATE_BY)));
        logInformation.setActiveFlag(cursor.getInt(cursor.getColumnIndex(DefaultPersistenceModel.STATUS_FLAG)));
//        logInformation.setRefId(cursor.getString(cursor.getColumnIndex(DefaultPersistenceModel.REF_ID)));
//        logInformation.setSyncStatus(cursor.getInt(cursor.getColumnIndex(DefaultPersistenceModel.SYNC_STATUS)));

        return logInformation;
    }

    public static org.meruvian.esales.collector.core.LogInformation getLogInformationDefault(Cursor cursor) {
        org.meruvian.esales.collector.core.LogInformation logInformation = new org.meruvian.esales.collector.core.LogInformation();
        logInformation.setCreateDate(new Date(cursor.getLong(cursor.getColumnIndex(DefaultPersistenceModel.CREATE_DATE))));
        logInformation.setCreateBy(cursor.getString(cursor.getColumnIndex(DefaultPersistenceModel.CREATE_BY)));
        logInformation.setLastUpdateDate(new Date(cursor.getLong(cursor.getColumnIndex(DefaultPersistenceModel.UPDATE_DATE))));
        logInformation.setLastUpdateBy(cursor.getString(cursor.getColumnIndex(DefaultPersistenceModel.UPDATE_BY)));
        logInformation.setActiveFlag(cursor.getInt(cursor.getColumnIndex(DefaultPersistenceModel.STATUS_FLAG)));
        logInformation.setSite(cursor.getString(cursor.getColumnIndex(DefaultPersistenceModel.SITE_ID)));

//        logInformation.setRefId(cursor.getString(cursor.getColumnIndex(DefaultPersistenceModel.REF_ID)));
//        logInformation.setSyncStatus(cursor.getInt(cursor.getColumnIndex(DefaultPersistenceModel.SYNC_STATUS)));

        return logInformation;
    }

    
}
