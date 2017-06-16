package com.advrtizr.sannacodetest.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.advrtizr.sannacodetest.R;
import java.util.List;
import butterknife.ButterKnife;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context context;
    private List<Contact> contactList;

    public ContactAdapter(Context context, ContactBook contactBook) {
        this.context = context;
        contactList = contactBook.getContact();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_info, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.contactName.setText(contact.getName());
        holder.contactLastname.setText(contact.getLastName());
        holder.contactPhone.setText(contact.getPhoneNumber());
        holder.contactEmail.setText(contact.getEmail());
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contactName;
        TextView contactLastname;
        TextView contactPhone;
        TextView contactEmail;


        ContactViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
            contactName = (TextView) itemView.findViewById(R.id.tv_contactName);
            contactLastname = (TextView) itemView.findViewById(R.id.tv_contactLastname);
            contactPhone= (TextView) itemView.findViewById(R.id.tv_contactPhone);
            contactEmail = (TextView) itemView.findViewById(R.id.tv_contactEmail);
        }

//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.ib_refresh:
//                    presenters.get(getAdapterPosition()).loadWeather();
//                    break;
//                case R.id.ib_delete:
//                    String key = keys.get(getAdapterPosition());
//                    if (key.equals("")) {
//                        break;
//                    }
//                    locationPreferences.edit().remove(key).apply();
//                    presenters.remove(getAdapterPosition());
//                    String selection = WeatherDBHelper._ID + " LIKE ?";
//                    String[] selectionArgs = { String.valueOf(getAdapterPosition()) };
//                    database.delete(WeatherDBHelper.TABLE_NAME, selection, selectionArgs);
//                    notifyItemRemoved(getAdapterPosition());
//                    break;
//            }
//        }
    }
}
