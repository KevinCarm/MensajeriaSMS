package com.example.mensajeriasms

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SmsAdapter
    (
    private val context: Context,
    private val list: List<SmsModel>
) : RecyclerView.Adapter<SmsAdapter.ViewHolder>() {

    private lateinit var onClickListener: View.OnClickListener

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val number: TextView = itemView.findViewById(R.id.phoneNumber)
        val message: TextView = itemView.findViewById(R.id.bodyMessage)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_sms, parent, false)
        view.setOnClickListener(this.onClickListener)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.number.text = list[position]._address
        if(list[position]._sms.length > 40) {
            holder.message.text = list[position]._sms.substring(0,35) + "...."
        } else {
            holder.message.text = list[position]._sms
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
    fun setOnClickListener(onClickListener: View.OnClickListener) {
        this.onClickListener = onClickListener
    }
}