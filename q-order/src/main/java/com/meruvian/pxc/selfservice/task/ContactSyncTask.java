package com.meruvian.pxc.selfservice.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.content.database.adapter.ContactDatabaseAdapter;
import com.meruvian.pxc.selfservice.entity.Contact;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.meruvian.midas.core.service.TaskService;
import org.meruvian.midas.core.util.ConnectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miftakhul on 12/7/15.
 */
public class ContactSyncTask extends AsyncTask<String, Void, JSONObject> {

    private Context context;
    private TaskService taskService;
    private SharedPreferences preferences;

    private ContactDatabaseAdapter contactDatabaseAdapter;

    public ContactSyncTask(Context context, TaskService taskService) {
        this.context = context;
        this.taskService = taskService;

        preferences = context.getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        contactDatabaseAdapter = new ContactDatabaseAdapter(context);
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        Log.d(getClass().getSimpleName(), "?acces_token= " + AuthenticationUtils.getCurrentAuthentication().getAccessToken());
        return ConnectionUtil.get(preferences.getString("server_url_point", "") + "/api/contacts?access_token="
                + AuthenticationUtils.getCurrentAuthentication().getAccessToken());
    }

    @Override
    protected void onCancelled() {
        taskService.onCancel(SignageVariables.CONTACT_GET_TASK, "Batal");
    }

    @Override
    protected void onPreExecute() {
        taskService.onExecute(SignageVariables.CONTACT_GET_TASK);
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        try {
            if (result != null) {
                List<Contact> contacts = new ArrayList<Contact>();

                Log.d("result cotact======", result.toString());
                JSONArray jsonArray = result.getJSONArray("content");
                for (int a = 0; a < jsonArray.length(); a++) {
                    JSONObject object = jsonArray.getJSONObject(a);

                    Contact contact = new Contact();
                    contact.setId(object.getString("id"));
                    contact.setFirstName(object.getString("firstName"));
                    contact.setLastName(object.getString("lastName"));
                    contact.setOfficePhone(object.getString("officePhone"));
                    contact.setMobile(object.getString("mobile"));
                    contact.setHomePhone(object.getString("homePhone"));
                    contact.setOtherPhone(object.getString("otherPhone"));
                    contact.setFax(object.getString("fax"));
                    contact.setEmail(object.getString("email"));
                    contact.setOtherEmail(object.getString("otherEmail"));
//                    contact.setAssistant(object.getString("assistent"));
//                    contact.setAssistantPhone(object.getString("assistentPhone"));
                    contact.setAddress(object.getString("address"));
                    contact.setCity(object.getString("city"));
                    contact.setZipCode(object.getString("zipCode"));
                    contact.setCountry(object.getString("country"));
                    contact.setDescription(object.getString("description"));

                    contacts.add(contact);

                }

                contactDatabaseAdapter.saveContact(contacts);
                taskService.onSuccess(SignageVariables.CONTACT_GET_TASK, true);
            } else {
                taskService.onError(SignageVariables.CONTACT_GET_TASK, "Error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            taskService.onError(SignageVariables.CONTACT_GET_TASK, "Error");
        }


    }
}
