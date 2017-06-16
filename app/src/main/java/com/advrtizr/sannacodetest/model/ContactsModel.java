package com.advrtizr.sannacodetest.model;

public interface ContactsModel {
    void addContactToDB(Contact contact, String userId);
    ContactBook readContactsFromDB(String userId);
}
