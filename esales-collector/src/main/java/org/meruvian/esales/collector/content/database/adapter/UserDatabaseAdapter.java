package org.meruvian.esales.collector.content.database.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.meruvian.esales.collector.SignageVariables;
import org.meruvian.esales.collector.content.MidasContentProvider;
import org.meruvian.esales.collector.content.database.model.DefaultPersistenceModel;
import org.meruvian.esales.collector.content.database.model.UserDatabaseModel;
import org.meruvian.esales.collector.core.LogInformation;
import org.meruvian.esales.collector.core.commons.Address;
import org.meruvian.esales.collector.core.commons.Bank;
import org.meruvian.esales.collector.core.commons.Name;
import org.meruvian.esales.collector.core.commons.User;
import org.meruvian.esales.collector.core.commons.UserUpline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by meruvian on 12/09/15.
 */
public class UserDatabaseAdapter extends DefaultDatabaseAdapter {
    private Uri dbUriUser = Uri.parse(MidasContentProvider.CONTENT_PATH
            + MidasContentProvider.TABLES[11]);

    private Context context;

    public UserDatabaseAdapter(Context context) {
        this.context = context;
    }

    public void saveUsers(List<User> users) {
        for (User user : users) {
            ContentValues contentValues = new ContentValues();

            if (findUserByRefId(user.getId()) == null) {
                UUID uuid = UUID.randomUUID();
                String id = String.valueOf(uuid);

                Log.d(getClass().getSimpleName(), "Save User: " + id);
                Log.d(getClass().getSimpleName(), "User refId: " + user.getId());

                if (user.getAddress() != null) {
                    contentValues.put(UserDatabaseModel.ADDRESS_STREET1, user.getAddress().getStreet1());
                    contentValues.put(UserDatabaseModel.ADDRESS_STREET2, user.getAddress().getStreet2());
                    contentValues.put(UserDatabaseModel.ADDRESS_CITY, user.getAddress().getCity());
                    contentValues.put(UserDatabaseModel.ADDRESS_STATE, user.getAddress().getState());
                    contentValues.put(UserDatabaseModel.ADDRESS_ZIP, user.getAddress().getZip());
                }

                if (user.getBank() != null) {
                    contentValues.put(UserDatabaseModel.BANK_NAME, user.getBank().getBankName());
                    contentValues.put(UserDatabaseModel.ACCOUNT_NUMBER, user.getBank().getAccountName());
                    contentValues.put(UserDatabaseModel.ACCOUNT_NAME, user.getBank().getAccountName());
                }

                if (user.getUpline() != null) {
                    contentValues.put(UserDatabaseModel.UPLINE, user.getUpline().getId());
                    Log.d(getClass().getSimpleName(), "Upline: " + user.getUpline().getId());
                } else {
                    Log.d(getClass().getSimpleName(), "Upline: NULL");
                }

                contentValues.put(UserDatabaseModel.ID, id);
                contentValues.put(UserDatabaseModel.CREATE_BY, user.getLogInformation().getCreateBy());

                if (user.getLogInformation().getCreateDate() != null) {
                    contentValues.put(UserDatabaseModel.CREATE_DATE, user.getLogInformation().getCreateDate().getTime());
                } else {
                    contentValues.put(UserDatabaseModel.CREATE_DATE, new Date().getTime());
                }

                if (user.getLogInformation().getLastUpdateDate() != null) {
                    contentValues.put(UserDatabaseModel.UPDATE_DATE, user.getLogInformation().getLastUpdateDate().getTime());
                } else {
                    contentValues.put(UserDatabaseModel.UPDATE_DATE, new Date().getTime());
                }

                contentValues.put(UserDatabaseModel.UPDATE_BY, user.getLogInformation().getLastUpdateBy());
                contentValues.put(UserDatabaseModel.USERNAME, user.getUsername());
                contentValues.put(UserDatabaseModel.PASSWORD, user.getPassword());
                contentValues.put(UserDatabaseModel.EMAIL, user.getEmail());
                contentValues.put(UserDatabaseModel.PHONE, user.getPhone());
                contentValues.put(UserDatabaseModel.NAME_PREFIX, user.getName().getPrefix());
                contentValues.put(UserDatabaseModel.NAME_FIRST, user.getName().getFirst());
                contentValues.put(UserDatabaseModel.NAME_MIDDLE, user.getName().getMiddle());
                contentValues.put(UserDatabaseModel.NAME_LAST, user.getName().getLast());

                contentValues.put(UserDatabaseModel.PHONE, user.getPhone());

                contentValues.put(UserDatabaseModel.REFERENCE, user.getReference());
                contentValues.put(UserDatabaseModel.AGENT_CODE, user.getAgentCode());

                contentValues.put(UserDatabaseModel.REF_ID, user.getId());

                contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
                contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);


                Log.d(getClass().getSimpleName(), "11111");
                context.getContentResolver().insert(dbUriUser, contentValues);
                Log.d(getClass().getSimpleName(), "22222222");
            } else {
                Log.d(getClass().getSimpleName(), "Update User: " + user.getRefId());

                if (user.getAddress() != null) {
                    contentValues.put(UserDatabaseModel.ADDRESS_STREET1, user.getAddress().getStreet1());
                    contentValues.put(UserDatabaseModel.ADDRESS_STREET2, user.getAddress().getStreet2());
                    contentValues.put(UserDatabaseModel.ADDRESS_CITY, user.getAddress().getCity());
                    contentValues.put(UserDatabaseModel.ADDRESS_STATE, user.getAddress().getState());
                    contentValues.put(UserDatabaseModel.ADDRESS_ZIP, user.getAddress().getZip());
                }

                if (user.getBank() != null) {
                    contentValues.put(UserDatabaseModel.BANK_NAME, user.getBank().getBankName());
                    contentValues.put(UserDatabaseModel.ACCOUNT_NUMBER, user.getBank().getAccountName());
                    contentValues.put(UserDatabaseModel.ACCOUNT_NAME, user.getBank().getAccountName());
                }

                if (user.getUpline() != null) {
                    contentValues.put(UserDatabaseModel.UPLINE, user.getUpline().getId());
                    Log.d(getClass().getSimpleName(), "Upline: " + user.getUpline().getId());
                } else {
                    Log.d(getClass().getSimpleName(), "Upline: NULL");
                }

                if (user.getLogInformation().getLastUpdateDate() != null) {
                    contentValues.put(UserDatabaseModel.UPDATE_DATE, user.getLogInformation().getLastUpdateDate().getTime());
                } else {
                    contentValues.put(UserDatabaseModel.UPDATE_DATE, new Date().getTime());
                }

                contentValues.put(UserDatabaseModel.UPDATE_BY, user.getLogInformation().getLastUpdateBy());
                contentValues.put(UserDatabaseModel.UPDATE_DATE, user.getLogInformation().getLastUpdateDate().getTime());
                contentValues.put(UserDatabaseModel.USERNAME, user.getUsername());
                contentValues.put(UserDatabaseModel.PASSWORD, user.getPassword());
                contentValues.put(UserDatabaseModel.EMAIL, user.getEmail());
                contentValues.put(UserDatabaseModel.PHONE, user.getPhone());
                contentValues.put(UserDatabaseModel.NAME_PREFIX, user.getName().getPrefix());
                contentValues.put(UserDatabaseModel.NAME_FIRST, user.getName().getFirst());
                contentValues.put(UserDatabaseModel.NAME_MIDDLE, user.getName().getMiddle());
                contentValues.put(UserDatabaseModel.NAME_LAST, user.getName().getLast());

                contentValues.put(UserDatabaseModel.PHONE, user.getPhone());

                contentValues.put(UserDatabaseModel.REFERENCE, user.getReference());
                contentValues.put(UserDatabaseModel.AGENT_CODE, user.getAgentCode());

//                contentValues.put(UserDatabaseModel.REF_ID, user.getRefId());

                contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
                contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

                context.getContentResolver().update(dbUriUser, contentValues,
                        UserDatabaseModel.REF_ID + " = ?", new String[] { user.getId() });
            }
        }
    }

    public String saveUser(User user) {
        ContentValues contentValues = new ContentValues();

        if (findUserByRefId(user.getId()) == null) {
            UUID uuid = UUID.randomUUID();
            String id = String.valueOf(uuid);

            if (user.getAddress() != null) {
                contentValues.put(UserDatabaseModel.ADDRESS_STREET1, user.getAddress().getStreet1());
                contentValues.put(UserDatabaseModel.ADDRESS_STREET2, user.getAddress().getStreet2());
                contentValues.put(UserDatabaseModel.ADDRESS_CITY, user.getAddress().getCity());
                contentValues.put(UserDatabaseModel.ADDRESS_STATE, user.getAddress().getState());
                contentValues.put(UserDatabaseModel.ADDRESS_ZIP, user.getAddress().getZip());
            }

            if (user.getBank() != null) {
                contentValues.put(UserDatabaseModel.BANK_NAME, user.getBank().getBankName());
                contentValues.put(UserDatabaseModel.ACCOUNT_NUMBER, user.getBank().getAccountName());
                contentValues.put(UserDatabaseModel.ACCOUNT_NAME, user.getBank().getAccountName());
            }

            if (user.getUpline() != null) {
                contentValues.put(UserDatabaseModel.UPLINE, user.getUpline().getId());
                Log.d(getClass().getSimpleName(), "Upline: " + user.getUpline().getId());
            } else {
                Log.d(getClass().getSimpleName(), "Upline: NULL");
            }

            contentValues.put(UserDatabaseModel.ID, id);
            contentValues.put(UserDatabaseModel.CREATE_BY, user.getLogInformation().getCreateBy());

            if (user.getLogInformation().getCreateDate() != null) {
                contentValues.put(UserDatabaseModel.CREATE_DATE, user.getLogInformation().getCreateDate().getTime());
            } else {
                contentValues.put(UserDatabaseModel.CREATE_DATE, new Date().getTime());
            }

            if (user.getLogInformation().getLastUpdateDate() != null) {
                contentValues.put(UserDatabaseModel.UPDATE_DATE, user.getLogInformation().getLastUpdateDate().getTime());
            } else {
                contentValues.put(UserDatabaseModel.UPDATE_DATE, new Date().getTime());
            }

            contentValues.put(UserDatabaseModel.UPDATE_BY, user.getLogInformation().getLastUpdateBy());
            contentValues.put(UserDatabaseModel.USERNAME, user.getUsername());
            contentValues.put(UserDatabaseModel.PASSWORD, user.getPassword());
            contentValues.put(UserDatabaseModel.EMAIL, user.getEmail());
            contentValues.put(UserDatabaseModel.PHONE, user.getPhone());
            contentValues.put(UserDatabaseModel.NAME_PREFIX, user.getName().getPrefix());
            contentValues.put(UserDatabaseModel.NAME_FIRST, user.getName().getFirst());
            contentValues.put(UserDatabaseModel.NAME_MIDDLE, user.getName().getMiddle());
            contentValues.put(UserDatabaseModel.NAME_LAST, user.getName().getLast());

            contentValues.put(UserDatabaseModel.PHONE, user.getPhone());

            contentValues.put(UserDatabaseModel.REFERENCE, user.getReference());
            contentValues.put(UserDatabaseModel.AGENT_CODE, user.getAgentCode());

            contentValues.put(UserDatabaseModel.REF_ID, user.getId());

            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
            contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

            context.getContentResolver().insert(dbUriUser, contentValues);
            return user.getId();
        } else {
            Log.d(getClass().getSimpleName(), "Update User: " + user.getRefId());

            if (user.getAddress() != null) {
                contentValues.put(UserDatabaseModel.ADDRESS_STREET1, user.getAddress().getStreet1());
                contentValues.put(UserDatabaseModel.ADDRESS_STREET2, user.getAddress().getStreet2());
                contentValues.put(UserDatabaseModel.ADDRESS_CITY, user.getAddress().getCity());
                contentValues.put(UserDatabaseModel.ADDRESS_STATE, user.getAddress().getState());
                contentValues.put(UserDatabaseModel.ADDRESS_ZIP, user.getAddress().getZip());
            }

            if (user.getBank() != null) {
                contentValues.put(UserDatabaseModel.BANK_NAME, user.getBank().getBankName());
                contentValues.put(UserDatabaseModel.ACCOUNT_NUMBER, user.getBank().getAccountName());
                contentValues.put(UserDatabaseModel.ACCOUNT_NAME, user.getBank().getAccountName());
            }

            if (user.getUpline() != null) {
                contentValues.put(UserDatabaseModel.UPLINE, user.getUpline().getId());
                Log.d(getClass().getSimpleName(), "Upline: " + user.getUpline().getId());
            } else {
                Log.d(getClass().getSimpleName(), "Upline: NULL");
            }

            if (user.getLogInformation().getLastUpdateDate() != null) {
                contentValues.put(UserDatabaseModel.UPDATE_DATE, user.getLogInformation().getLastUpdateDate().getTime());
            } else {
                contentValues.put(UserDatabaseModel.UPDATE_DATE, new Date().getTime());
            }

            contentValues.put(UserDatabaseModel.UPDATE_BY, user.getLogInformation().getLastUpdateBy());
            contentValues.put(UserDatabaseModel.UPDATE_DATE, user.getLogInformation().getLastUpdateDate().getTime());
            contentValues.put(UserDatabaseModel.USERNAME, user.getUsername());
            contentValues.put(UserDatabaseModel.PASSWORD, user.getPassword());
            contentValues.put(UserDatabaseModel.EMAIL, user.getEmail());
            contentValues.put(UserDatabaseModel.PHONE, user.getPhone());
            contentValues.put(UserDatabaseModel.NAME_PREFIX, user.getName().getPrefix());
            contentValues.put(UserDatabaseModel.NAME_FIRST, user.getName().getFirst());
            contentValues.put(UserDatabaseModel.NAME_MIDDLE, user.getName().getMiddle());
            contentValues.put(UserDatabaseModel.NAME_LAST, user.getName().getLast());

            contentValues.put(UserDatabaseModel.PHONE, user.getPhone());

            contentValues.put(UserDatabaseModel.REFERENCE, user.getReference());
            contentValues.put(UserDatabaseModel.AGENT_CODE, user.getAgentCode());

//                contentValues.put(UserDatabaseModel.REF_ID, user.getRefId());

            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
            contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

            context.getContentResolver().update(dbUriUser, contentValues,
                    UserDatabaseModel.REF_ID + " = ?", new String[]{user.getId()});
            return user.getId();
        }
    }

    public String updateUser(User user) {
        ContentValues contentValues = new ContentValues();

        if (user.getId() != null) {
            Log.d(getClass().getSimpleName(), "Update User: " + user.getId());

            contentValues.put(UserDatabaseModel.UPDATE_BY, user.getLogInformation().getLastUpdateBy());
            contentValues.put(UserDatabaseModel.UPDATE_DATE, user.getLogInformation().getLastUpdateDate().getTime());
            contentValues.put(UserDatabaseModel.USERNAME, user.getUsername());
            contentValues.put(UserDatabaseModel.PASSWORD, user.getPassword());
            contentValues.put(UserDatabaseModel.EMAIL, user.getEmail());
            contentValues.put(UserDatabaseModel.PHONE, user.getPhone());
            contentValues.put(UserDatabaseModel.NAME_PREFIX, user.getName().getPrefix());
            contentValues.put(UserDatabaseModel.NAME_FIRST, user.getName().getFirst());
            contentValues.put(UserDatabaseModel.NAME_MIDDLE, user.getName().getMiddle());
            contentValues.put(UserDatabaseModel.NAME_LAST, user.getName().getLast());
            contentValues.put(UserDatabaseModel.ADDRESS_STREET1, user.getAddress().getStreet1());
            contentValues.put(UserDatabaseModel.ADDRESS_STREET2, user.getAddress().getStreet2());
            contentValues.put(UserDatabaseModel.ADDRESS_CITY, user.getAddress().getCity());
            contentValues.put(UserDatabaseModel.ADDRESS_STATE, user.getAddress().getState());
            contentValues.put(UserDatabaseModel.ADDRESS_ZIP, user.getAddress().getZip());
            contentValues.put(UserDatabaseModel.BANK_NAME, user.getBank().getBankName());
            contentValues.put(UserDatabaseModel.ACCOUNT_NUMBER, user.getBank().getAccountName());
            contentValues.put(UserDatabaseModel.ACCOUNT_NAME, user.getBank().getAccountName());
            contentValues.put(UserDatabaseModel.PHONE, user.getPhone());
            contentValues.put(UserDatabaseModel.UPLINE, user.getUpline().getId());
            contentValues.put(UserDatabaseModel.REFERENCE, user.getReference());
            contentValues.put(UserDatabaseModel.AGENT_CODE, user.getAgentCode());

            contentValues.put(UserDatabaseModel.REF_ID, user.getRefId());

            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
            contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

            context.getContentResolver().update(dbUriUser, contentValues,
                    UserDatabaseModel.ID + " = ?", new String[] { user.getId() });
            return user.getId();
        } else {
            UUID uuid = UUID.randomUUID();
            String id = String.valueOf(uuid);

            Log.d(getClass().getSimpleName(), "Save User: " + id);
            contentValues.put(UserDatabaseModel.ID, id);
            contentValues.put(UserDatabaseModel.CREATE_BY, user.getLogInformation().getCreateBy());
            contentValues.put(UserDatabaseModel.CREATE_DATE, user.getLogInformation().getCreateDate().getTime());
            contentValues.put(UserDatabaseModel.UPDATE_BY, user.getLogInformation().getLastUpdateBy());
            contentValues.put(UserDatabaseModel.UPDATE_DATE, user.getLogInformation().getLastUpdateDate().getTime());
            contentValues.put(UserDatabaseModel.USERNAME, user.getUsername());
            contentValues.put(UserDatabaseModel.PASSWORD, user.getPassword());
            contentValues.put(UserDatabaseModel.EMAIL, user.getEmail());
            contentValues.put(UserDatabaseModel.PHONE, user.getPhone());
            contentValues.put(UserDatabaseModel.NAME_PREFIX, user.getName().getPrefix());
            contentValues.put(UserDatabaseModel.NAME_FIRST, user.getName().getFirst());
            contentValues.put(UserDatabaseModel.NAME_MIDDLE, user.getName().getMiddle());
            contentValues.put(UserDatabaseModel.NAME_LAST, user.getName().getLast());
            contentValues.put(UserDatabaseModel.ADDRESS_STREET1, user.getAddress().getStreet1());
            contentValues.put(UserDatabaseModel.ADDRESS_STREET2, user.getAddress().getStreet2());
            contentValues.put(UserDatabaseModel.ADDRESS_CITY, user.getAddress().getCity());
            contentValues.put(UserDatabaseModel.ADDRESS_STATE, user.getAddress().getState());
            contentValues.put(UserDatabaseModel.ADDRESS_ZIP, user.getAddress().getZip());
            contentValues.put(UserDatabaseModel.BANK_NAME, user.getBank().getBankName());
            contentValues.put(UserDatabaseModel.ACCOUNT_NUMBER, user.getBank().getAccountName());
            contentValues.put(UserDatabaseModel.ACCOUNT_NAME, user.getBank().getAccountName());
            contentValues.put(UserDatabaseModel.PHONE, user.getPhone());
            contentValues.put(UserDatabaseModel.UPLINE, user.getUpline().getId());
            contentValues.put(UserDatabaseModel.REFERENCE, user.getReference());
            contentValues.put(UserDatabaseModel.AGENT_CODE, user.getAgentCode());

            contentValues.put(UserDatabaseModel.REF_ID, user.getRefId());

            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
            contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

            context.getContentResolver().insert(dbUriUser, contentValues);
            return id;
        }
    }

    public User findUserByRefId(String refId) {
        String criteria = UserDatabaseModel.REF_ID + " = ?";
        String[] parameter = { refId };
        Cursor cursor = context.getContentResolver().query(dbUriUser,
                null, criteria, parameter, null);

        User user  = null;

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                LogInformation log = getLogInformationDefault(cursor);
                Name name = new Name();
                name.setPrefix(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.NAME_PREFIX)));
                name.setFirst(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.NAME_FIRST)));
                name.setMiddle(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.NAME_MIDDLE)));
                name.setLast(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.NAME_LAST)));

                Address address = new Address();
                address.setStreet1(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_STREET1)));
                address.setStreet2(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_STREET2)));
                address.setCity(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_CITY)));
                address.setState(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_STATE)));
                address.setZip(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_ZIP)));

                UserUpline userUpline = new UserUpline();
                userUpline.setId(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.UPLINE)));

                Bank bank = new Bank();
                bank.setBankName(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.BANK_NAME)));
                bank.setAccountName(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ACCOUNT_NAME)));
                bank.setAccountNumber(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ACCOUNT_NUMBER)));


                user = new User();
                user.setId(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.ID)));
                user.setLogInformation(log);
                user.setUsername(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.PASSWORD)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.EMAIL)));
                user.setName(name);
                user.setAddress(address);
                user.setAgentCode(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.AGENT_CODE)));
                user.setUpline(userUpline);
                user.setReference(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.REFERENCE)));
                user.setPhone(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.PHONE)));
                user.setBank(bank);

            }
        }

        cursor.close();

        return user;
    }


    public User findUserById(String id) {
        String criteria = UserDatabaseModel.ID + " = ?";
        String[] parameter = { id };
        Cursor cursor = context.getContentResolver().query(dbUriUser,
                null, criteria, parameter, null);

        User user = new User();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                LogInformation log = getLogInformationDefault(cursor);
                Name name = new Name();
                name.setPrefix(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.NAME_PREFIX)));
                name.setFirst(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.NAME_FIRST)));
                name.setMiddle(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.NAME_MIDDLE)));
                name.setLast(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.NAME_LAST)));

                Address address = new Address();
                address.setStreet1(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_STREET1)));
                address.setStreet2(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_STREET2)));
                address.setCity(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_CITY)));
                address.setState(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_STATE)));
                address.setZip(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_ZIP)));

                UserUpline userUpline = new UserUpline();
                userUpline.setId(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.UPLINE)));

                Bank bank = new Bank();
                bank.setBankName(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.BANK_NAME)));
                bank.setAccountName(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ACCOUNT_NAME)));
                bank.setAccountNumber(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ACCOUNT_NUMBER)));

                user = new User();
                user.setId(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.ID)));
                user.setLogInformation(log);
                user.setUsername(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.PASSWORD)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.EMAIL)));
                user.setName(name);
                user.setAddress(address);
                user.setAgentCode(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.AGENT_CODE)));
                user.setUpline(userUpline);
                user.setReference(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.REFERENCE)));
                user.setPhone(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.PHONE)));
                user.setBank(bank);

            }
        }

        cursor.close();

        return user;
    }

    public List<User> findAllUser() {
        String criteria = UserDatabaseModel.STATUS_FLAG + " = ? ";
        String[] parameter = { SignageVariables.ACTIVE };
        Cursor cursor = context.getContentResolver().query(dbUriUser,
                null, criteria, parameter, null);

        List<User> users = new ArrayList<User>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                LogInformation log = getLogInformationDefault(cursor);
                Name name = new Name();
                name.setPrefix(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.NAME_PREFIX)));
                name.setFirst(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.NAME_FIRST)));
                name.setMiddle(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.NAME_MIDDLE)));
                name.setLast(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.NAME_LAST)));

                Address address = new Address();
                address.setStreet1(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_STREET1)));
                address.setStreet2(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_STREET2)));
                address.setCity(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_CITY)));
                address.setState(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_STATE)));
                address.setZip(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_ZIP)));

                UserUpline userUpline = new UserUpline();
                userUpline.setId(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.UPLINE)));

                Bank bank = new Bank();
                bank.setBankName(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.BANK_NAME)));
                bank.setAccountName(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ACCOUNT_NAME)));
                bank.setAccountNumber(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ACCOUNT_NUMBER)));

                User user = new User();
                user.setId(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.ID)));
                user.setLogInformation(log);
                user.setUsername(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.PASSWORD)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.EMAIL)));
                user.setName(name);
                user.setAddress(address);
                user.setAgentCode(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.AGENT_CODE)));
                user.setUpline(userUpline);
                user.setReference(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.REFERENCE)));
                user.setPhone(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.PHONE)));
                user.setBank(bank);

                users.add(user);
            }
        }
        cursor.close();
        return users;
    }

    public List<User> findUserByUserId(String uplineId) {
        String criteria = UserDatabaseModel.UPLINE + " = ?";
        String[] parameter = { uplineId };
        Cursor cursor = context.getContentResolver().query(dbUriUser,
                null, criteria, parameter, null);

        List<User> users = new ArrayList<User>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                LogInformation log = getLogInformationDefault(cursor);
                Name name = new Name();
                name.setPrefix(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.NAME_PREFIX)));
                name.setFirst(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.NAME_FIRST)));
                name.setMiddle(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.NAME_MIDDLE)));
                name.setLast(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.NAME_LAST)));

                Address address = new Address();
                address.setStreet1(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_STREET1)));
                address.setStreet2(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_STREET2)));
                address.setCity(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_CITY)));
                address.setState(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_STATE)));
                address.setZip(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ADDRESS_ZIP)));

                UserUpline userUpline = new UserUpline();
                userUpline.setId(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.UPLINE)));

                Bank bank = new Bank();
                bank.setBankName(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.BANK_NAME)));
                bank.setAccountName(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ACCOUNT_NAME)));
                bank.setAccountNumber(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.ACCOUNT_NUMBER)));

                User user = new User();
                user.setId(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.ID)));
                user.setLogInformation(log);
                user.setUsername(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.PASSWORD)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(UserDatabaseModel.EMAIL)));
                user.setName(name);
                user.setAddress(address);
                user.setAgentCode(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.AGENT_CODE)));
                user.setUpline(userUpline);
                user.setReference(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.REFERENCE)));
                user.setPhone(cursor.getString(
                        cursor.getColumnIndex(UserDatabaseModel.PHONE)));
                user.setBank(bank);

                users.add(user);
            }
        }
        cursor.close();

        return users;
    }

    public void deleteUser(String menuId) {
        String criteria = UserDatabaseModel.ID + " = ?";
        String[] parameter = { menuId };

        context.getContentResolver()
                .delete(dbUriUser, criteria, parameter);

    }

    public String getUserRefIdById(String userId) {
        String criteria = UserDatabaseModel.ID + " = ?";
        String[] parameter = { userId };
        Cursor cursor = context.getContentResolver().query(dbUriUser,
                null, criteria, parameter, null);

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getString(cursor
                        .getColumnIndex(UserDatabaseModel.REF_ID));
            }
        }

        cursor.close();

        return id;
    }

    public String getUserId() {
        Cursor cursor = context.getContentResolver().query(dbUriUser,
                null, null, null,
                UserDatabaseModel.CREATE_DATE + " DESC LIMIT 1");

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getString(cursor
                        .getColumnIndex(UserDatabaseModel.ID));
            }
        }

        cursor.close();

        return id;
    }

    public List<String> getAllRefId() {
        Cursor cursor = context.getContentResolver().query(dbUriUser, null, null, null, null);

        List<String> categories = new ArrayList<String>();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    categories.add(cursor.getString(cursor
                            .getColumnIndex(UserDatabaseModel.REF_ID)));
                }
            }
        }

        cursor.close();

        return categories;
    }

    public void updateSyncStatusById(String id, String refId) {
        Log.d(getClass().getSimpleName(), "updateSyncStatusById.Success1");
        ContentValues values = new ContentValues();

        values.put(UserDatabaseModel.SYNC_STATUS, 1);
        values.put(UserDatabaseModel.REF_ID, refId);

        context.getContentResolver().update(dbUriUser, values, UserDatabaseModel.ID + " = ? ", new String[]{id});
    }

    public void deleteByRefIds(List<String> refIds) {
        for(String id : refIds) {
            ContentValues values = new ContentValues();
            values.put(UserDatabaseModel.STATUS_FLAG, 0);

            context.getContentResolver().update(dbUriUser, values, UserDatabaseModel.REF_ID + " = ? ", new String[] { id });
        }
    }

    public void deleteUserById(String userId) {
        String criteria = UserDatabaseModel.ID + " = ?";
        String[] parameter = { userId };

        ContentValues values = new ContentValues();
        values.put(UserDatabaseModel.STATUS_FLAG, 0);

        context.getContentResolver().update(dbUriUser, values, criteria, parameter);
    }
}
