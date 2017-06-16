package com.advrtizr.sannacodetest.model;

import java.util.List;

public class ContactBook {

    private List<Contact> contact;
    private String bookUserId;

    public List<Contact> getContact() {
        return contact;
    }

    public void setContact(List<Contact> contact) {
        this.contact = contact;
    }

    public String getBookUserId() {
        return bookUserId;
    }

    public void setBookUserId(String bookUserId) {
        this.bookUserId = bookUserId;
    }
}
