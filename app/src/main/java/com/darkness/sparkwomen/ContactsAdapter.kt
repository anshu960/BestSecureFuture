package com.darkness.sparkwomen

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.darkness.sparkwomen.ContactsAdapter.MyViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import java.util.ArrayList
import java.util.HashMap

class ContactsAdapter internal constructor(
    var context: Context,
    var send: ArrayList<String>,
    var myOnClickListener: (Any) -> Unit
) : RecyclerView.Adapter<MyViewHolder>() {
    var contacts: HashMap<String, String>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.contact.text = send[position]
        holder.delete.setOnClickListener { view: View? -> myOnClickListener.invoke(position) }
    }

    override fun getItemCount(): Int {
        return send.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = null
        var contact: TextView
        var delete: ImageView

        init {
            contact = itemView.findViewById(R.id.contactItem)
            delete = itemView.findViewById(R.id.deleteIcon)
        }
    }
}