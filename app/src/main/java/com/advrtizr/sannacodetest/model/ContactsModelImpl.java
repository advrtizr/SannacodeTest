package com.advrtizr.sannacodetest.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.LinkedList;
import java.util.List;

public class ContactsModelImpl implements ContactsModel {

    private SQLiteDatabase contactsDB;

    public ContactsModelImpl(Context context) {
        // creating database helper object as a singleton
        ContactsDBHelper contactsDBHelper = ContactsDBHelper.getInstance(context);
        contactsDB = contactsDBHelper.getWritableDatabase();
    }

    @Override
    public void addContactToDB(Contact contact, String userId) {
        // check on null
        if (contact != null && !userId.equals("")) {
            // adding data to content values
            ContentValues contentValues = new ContentValues();
            contentValues.put(ContactsDBHelper.KEY_NAME, contact.getName());
            contentValues.put(ContactsDBHelper.KEY_LASTNAME, contact.getLastName());
            contentValues.put(ContactsDBHelper.KEY_PHONE, contact.getPhoneNumber());
            contentValues.put(ContactsDBHelper.KEY_EMAIL, contact.getEmail());
            contentValues.put(ContactsDBHelper.KEY_USER_ID, userId);
            contactsDB.insert(ContactsDBHelper.TABLE_NAME, null, contentValues);
        }
    }

    @Override
    public ContactBook readContactsFromDB(String userId, String sort) {
        // passing sorting parameter to query
        Cursor cursor = contactsDB.query(ContactsDBHelper.TABLE_NAME, null, null, null, null, null, sort, null);
        ContactBook contactBook = new ContactBook();
        List<Contact> contactList = new LinkedList<>();

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(ContactsDBHelper.KEY_NAME);
            int lastNameIndex = cursor.getColumnIndex(ContactsDBHelper.KEY_LASTNAME);
            int phoneIndex = cursor.getColumnIndex(ContactsDBHelper.KEY_PHONE);
            int emailIndex = cursor.getColumnIndex(ContactsDBHelper.KEY_EMAIL);
            int userIndex = cursor.getColumnIndex(ContactsDBHelper.KEY_USER_ID);
            do {
                // checking if entry in database is equals to current user id
                // if true - creating a contact object
                if (!cursor.getString(userIndex).equals(userId)) {
                    continue;
                }
                // assembling a contact
                Contact tempContact = new Contact();
                tempContact.setName(cursor.getString(nameIndex));
                tempContact.setLastName(cursor.getString(lastNameIndex));
                tempContact.setPhoneNumber(cursor.getString(phoneIndex));
                tempContact.setEmail(cursor.getString(emailIndex));
                // adding a contact to the list
                contactList.add(tempContact);

            } while (cursor.moveToNext());

        }
        // checking if result list is not empty
        if (!contactList.isEmpty()) {
            contactBook.setContactList(contactList);
        }
        cursor.close();
        return contactBook;
    }
}
