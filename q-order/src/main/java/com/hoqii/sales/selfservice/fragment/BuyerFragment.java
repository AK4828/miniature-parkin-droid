package com.hoqii.sales.selfservice.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.adapter.BuyerListAdapter;
import com.hoqii.sales.selfservice.entity.Contact;

import org.meruvian.midas.core.defaults.DefaultFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by meruvian on 15/09/15.
 */
public class BuyerFragment extends DefaultFragment {
    @InjectView(R.id.list_buyers)
    ListView listBuyer;

    private BuyerListAdapter buyerListAdapter;
    private List<Contact> contacts = new ArrayList<Contact>();

    @Override
    protected int layout() {
        return R.layout.fragment_buyer_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        buyerListAdapter = new BuyerListAdapter(getActivity(), R.layout.adapter_buyer_list,
                new ArrayList<Contact>());
        buyerListAdapter.addAll(getContacts());
        listBuyer.setAdapter(buyerListAdapter);

        listBuyer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new BuyerOrderFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, fragment);
                transaction.addToBackStack(null);
                transaction.commit();


            }
        });
    }

    private List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<Contact>();
        Contact contact = new Contact();
        contact.setContactName("Rumah");
        contact.setRecipient("Bu Ani");
        contact.setPhone("0811231212");
        contacts.add(contact);

        Contact contact1 = new Contact();
        contact1.setContactName("Kantor");
        contact1.setRecipient("Pak Adi");
        contact1.setPhone("0811234267");
        contacts.add(contact1);

        Contact contact2 = new Contact();
        contact2.setContactName("Kos");
        contact2.setRecipient("Ando");
        contact2.setPhone("0811234543");
        contacts.add(contact2);

        Contact contact3 = new Contact();
        contact3.setContactName("Rumah Kakak");
        contact3.setRecipient("Kak Nana");
        contact3.setPhone("0896787989");
        contacts.add(contact3);

        Contact contact4 = new Contact();
        contact4.setContactName("Rumah Tante");
        contact4.setRecipient("Pak Wi");
        contact4.setPhone("0896787989");
        contacts.add(contact4);

        Contact contact5 = new Contact();
        contact5.setContactName("Sohib");
        contact5.setRecipient("Febri");
        contact5.setPhone("08967343349");
        contacts.add(contact5);

        return contacts;
    }

}
