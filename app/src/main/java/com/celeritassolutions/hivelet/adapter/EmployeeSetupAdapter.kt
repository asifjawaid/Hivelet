package com.celeritassolutions.hivelet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.celeritassolutions.hivelet.R
import com.celeritassolutions.hivelet.model.EmployeeModel


class EmployeeSetupAdapter(var list: List<EmployeeModel>, val context: Context) :
    RecyclerView.Adapter<EmployeeSetupAdapter.ViewHolder>() {

    private lateinit var txtName: AppCompatTextView
    private lateinit var txtDepartment: AppCompatTextView
    private lateinit var txtDesignation: AppCompatTextView

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            txtName = itemView.findViewById(R.id.txt_name)
            txtDesignation = itemView.findViewById(R.id.txt_dept)
            txtDepartment = itemView.findViewById(R.id.txt_designation)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.employee_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item = list[position]
        txtName.text = item.name
        txtDesignation.text = item.designation
        txtDepartment.text = item.department
    }
}

/*public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> implements Filterable {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                System.out.println("Count Number 2 " + ((List<QuizObject>) results.values).size());
                this.mAdapter.notifyDataSetChanged();
            }
        }*/