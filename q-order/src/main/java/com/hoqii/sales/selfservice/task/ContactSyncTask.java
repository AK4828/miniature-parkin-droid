package com.hoqii.sales.selfservice.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.SignageVariables;
import com.hoqii.sales.selfservice.content.database.adapter.ContactDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Contact;
import com.hoqii.sales.selfservice.util.AuthenticationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.meruvian.midas.core.service.TaskService;
import org.meruvian.midas.core.util.ConnectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ludviantoovandi on 28/01/15.
 */
public class ContactSyncTask extends AsyncTask<String, Void, JSONObject> {
    private Context context;
    private TaskService service;
    private SharedPreferences preferences;

    private int phase = 1;
    private String parentId = "";

    private List<String> refIdContacts = new ArrayList<String>();
    private ContactDatabaseAdapter contactAdapter;

    public ContactSyncTask(Context context, TaskService service) {
        this.context = context;
        this.service = service;

        contactAdapter = new ContactDatabaseAdapter(context);
        refIdContacts = contactAdapter.getAllRefId();
        
        Log.d(getClass().getSimpleName(), "Ref Id contacts: " + refIdContacts.size());
        preferences = context.getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
    }

    @Override
    protected void onCancelled() {
        service.onCancel(SignageVariables.CONTACT_GET_TASK, context.getString(R.string.cancel));
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(SignageVariables.CONTACT_GET_TASK);
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        Log.d(getClass().getSimpleName(), "?access_token= " + AuthenticationUtils.getCurrentAuthentication().getAccessToken());
        return ConnectionUtil.get(preferences.getString("server_url", "") + "/api/contacts?access_token=" + AuthenticationUtils.getCurrentAuthentication().getAccessToken());
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        try {
            if (result != null) {
                JSONArray jsonArray = result.getJSONArray("content");
                List<Contact> contacts = new ArrayList<Contact>();

                for (int a = 0; a < jsonArray.length(); a++) {
                    JSONObject json = jsonArray.getJSONObject(a);

                    Log.d(getClass().getSimpleName(), "JSON Contact: " + json.toString());
                    Contact contact = new Contact();

                    JSONObject jsonUser = new JSONObject();
                    if (!json.isNull("user")) {
                        jsonUser = json.getJSONObject("user");
                        contact.getUser().setId(jsonUser.getString("id"));
                    }

                    contact.setRefId(json.getString("id"));
                    contact.setDefaultContact(json.getBoolean("defaultContact"));
                    contact.setContactName(json.getString("contactName"));
                    contact.setRecipient(json.getString("recipient"));
                    contact.setPhone(json.getString("phone"));
                    contact.setCity(json.getString("city"));
                    contact.setSubDistrict(json.getString("subDistrict"));
                    contact.setProvince(json.getString("province"));
                    contact.setZip(json.getString("zip"));
                    contact.setAddress(json.getString("address"));

                    Log.d(getClass().getSimpleName(), "Ref ID Contact: " + contact.getRefId());
                    contacts.add(contact);
                    if(refIdContacts.contains(contact.getRefId())) {
                        refIdContacts.remove(contact.getRefId());
                    }
                }

                Log.d(getClass().getSimpleName(), "Contacts Size: " + contacts.size());
                Log.d(getClass().getSimpleName(), "Contacts Size: " + refIdContacts.size());

                contactAdapter.saveContacts(contacts);
                contactAdapter.deleteByRefIds(refIdContacts);

                service.onSuccess(SignageVariables.CONTACT_GET_TASK, true);
            } else {
                service.onError(SignageVariables.CONTACT_GET_TASK, context.getString(R.string.error));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            service.onError(SignageVariables.CONTACT_GET_TASK, context.getString(R.string.error));
        }
    }
}
