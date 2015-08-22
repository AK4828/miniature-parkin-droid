package com.hoqii.sales.selfservice.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.SignageAppication;
import com.hoqii.sales.selfservice.SignageVariables;
import com.hoqii.sales.selfservice.adapter.ContactAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.ContactDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Contact;
import com.hoqii.sales.selfservice.event.GenericEvent;
import com.hoqii.sales.selfservice.job.ContactDeleteJob;
import com.hoqii.sales.selfservice.job.ContactJob;
import com.hoqii.sales.selfservice.job.ContactUpdateJob;
import com.hoqii.sales.selfservice.util.AuthenticationUtils;
import com.path.android.jobqueue.JobManager;

import org.meruvian.midas.core.defaults.DefaultActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 15/08/15.
 */
public class ContactListActivity extends DefaultActivity {
    @InjectView(R.id.list_contacts)
    ListView listContacts;
    @InjectView(R.id.button_add)
    ImageButton addContact;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private ContactAdapter contactAdapter;
    private ContactDatabaseAdapter contactDbAdapter;
    private JobManager jobManager;
    private SharedPreferences preferences;
    private ProgressDialog dialog;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int layout() {
        return R.layout.activity_list_contacts;
    }

    @Override
    public void onViewCreated(Bundle bundle) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        preferences = this.getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        contactDbAdapter = new ContactDatabaseAdapter(this);

        contactAdapter = new ContactAdapter(this, R.layout.adapter_contact, new ArrayList<Contact>(), this);
        listContacts.setAdapter(contactAdapter);

        findAllContact();

        jobManager = SignageAppication.getInstance().getJobManager();

    }

    @OnClick(R.id.button_add)
    public void addContact(){
        dialogContact(null, false);
    }

    public void dialogContact(final String contactId, final boolean update) {
        View view = View.inflate(this, R.layout.view_contact, null);

        final EditText contactName = (EditText) view.findViewById(R.id.edit_contact_name);
        final EditText receiveName = (EditText) view.findViewById(R.id.edit_receive_name);
        final EditText receiveTelp = (EditText) view.findViewById(R.id.edit_receive_telp);
        final EditText province = (EditText) view.findViewById(R.id.edit_province);
        final EditText city = (EditText) view.findViewById(R.id.edit_city);
        final EditText subDistrict = (EditText) view.findViewById(R.id.edit_sub_district);
        final EditText street = (EditText) view.findViewById(R.id.edit_street);
        final EditText posCode = (EditText) view.findViewById(R.id.edit_pos_code);

        if (contactId != null) {
            Contact contact = contactDbAdapter.findContactById(contactId);
            contactName.setText(contact.getContactName());
            receiveName.setText(contact.getRecipient());
            receiveTelp.setText(contact.getPhone());
            province.setText(contact.getProvince());
            city.setText(contact.getCity());
            subDistrict.setText(contact.getSubDistrict());
            street.setText(contact.getAddress());
            posCode.setText(contact.getZip());

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle(R.string.add_contact);
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startDialog();

                Contact contact = new Contact();

                contact.getLogInformation().setCreateBy(AuthenticationUtils.getCurrentAuthentication().getUser().getId());
                contact.getLogInformation().setLastUpdateBy(AuthenticationUtils.getCurrentAuthentication().getUser().getId());

                contact.getUser().setId(AuthenticationUtils.getCurrentAuthentication().getUser().getId());
                contact.setContactName(contactName.getText().toString());
                contact.setRecipient(receiveName.getText().toString());
                contact.setPhone(receiveTelp.getText().toString());
                contact.setProvince(province.getText().toString());
                contact.setCity(city.getText().toString());
                contact.setSubDistrict(subDistrict.getText().toString());
                contact.setAddress(street.getText().toString());
                contact.setZip(posCode.getText().toString());

                if (contactId != null) {
                    contact.setId(contactId);
                }
                String cId = contactDbAdapter.updateContact(contact);

                if (update) {
                    String contactRefId = contactDbAdapter.getContactRefIdById(cId);
                    syncUpdateContact(contactRefId, cId);
                    contactAdapter.clear();
                    contactAdapter.addAll(contactDbAdapter.findAllContact());
                } else  {
                    syncContact(cId);
                    contact.setId(cId);
                    contactAdapter.add(contact);
                }

                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public void onEventMainThread(GenericEvent.RequestInProgress requestInProgress) {
        Log.d(getClass().getSimpleName(), "RequestInProgress: " + requestInProgress.getProcessId());
    }

    public void onEventMainThread(GenericEvent.RequestSuccess requestSuccess) {
        try {
            switch (requestSuccess.getProcessId()) {
                case ContactJob.PROCESS_ID: {
                    dialog.dismiss();
                    Log.d(getClass().getSimpleName(), "Response ContactRefId : " + requestSuccess.getRefId());
                    break;
                }
                case ContactUpdateJob.PROCESS_ID: {
                    dialog.dismiss();
                    Log.d(getClass().getSimpleName(), "Response ContactRefId : " + requestSuccess.getRefId());
                    break;
                }
                case ContactDeleteJob.PROCESS_ID: {
                    dialog.dismiss();
                    Log.d(getClass().getSimpleName(), "Success Delete.");
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    public void onEventMainThread(GenericEvent.RequestFailed failed) {
        dialog.dismiss();
        Toast.makeText(ContactListActivity.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();

        Log.e(getClass().getSimpleName(),
                failed.getResponse().getHttpResponse().getStatusLine().getStatusCode() + " :"
                        + failed.getResponse().getHttpResponse().getStatusLine().getReasonPhrase());
    }

    public void syncContact(String cId) {
        jobManager.addJobInBackground(new ContactJob(cId, preferences.getString("server_url", "")));
    }

    public void syncUpdateContact(String contactRefId, String cId) {
        jobManager.addJobInBackground(new ContactUpdateJob(contactRefId, cId, preferences.getString("server_url", "")));
    }

    public void syncDeleteContact(String contactRefId, String cId) {
        jobManager.addJobInBackground(new ContactDeleteJob(contactRefId, cId, preferences.getString("server_url", "")));
    }

    public void startDialog() {
        dialog = new ProgressDialog(ContactListActivity.this);
        dialog.setMessage("Menyimpan data ...");
        dialog.show();
        dialog.setCancelable(false);
    }

    private void findAllContact() {
        contactAdapter.addAll(contactDbAdapter.findAllContact());
    }

}
