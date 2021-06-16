package com.example.mensajeriasms

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter
    (
    private val context: Context,
    private val list: List<ContactModel>
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private lateinit var onClickListener: View.OnClickListener
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contactName: TextView = itemView.findViewById(R.id.contactName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent,false)
        view.setOnClickListener(onClickListener)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.contactName.text = list[position].name
    }

    fun setOnClickListener(onClickListener: View.OnClickListener) {
        this.onClickListener = onClickListener
    }
    override fun getItemCount(): Int {
        return list.size
    }

}