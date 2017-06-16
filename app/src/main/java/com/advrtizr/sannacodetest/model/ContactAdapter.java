package com.advrtizr.sannacodetest.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.advrtizr.sannacodetest.R;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context context;
    private List<Contact> contactList;

    public ContactAdapter(Context context, ContactBook contactBook) {
        this.context = context;
        contactList = contactBook.getContactList();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_info, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        // setting data to list item
        Contact contact = contactList.get(position);
        holder.contactName.setText(contact.getName());
        holder.contactLastName.setText(contact.getLastName());
        holder.contactPhone.setText(contact.getPhoneNumber());
        holder.contactEmail.setText(contact.getEmail());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contactName;
        TextView contactLastName;
        TextView contactPhone;
        TextView contactEmail;

        ContactViewHolder(View itemView) {
            super(itemView);
            contactName = (TextView) itemView.findViewById(R.id.tv_contact_name);
            contactLastName = (TextView) itemView.findViewById(R.id.tv_contact_last_name);
            contactPhone= (TextView) itemView.findViewById(R.id.tv_contact_phone);
            contactEmail = (TextView) itemView.findViewById(R.id.tv_contact_email);
        }
    }
}
