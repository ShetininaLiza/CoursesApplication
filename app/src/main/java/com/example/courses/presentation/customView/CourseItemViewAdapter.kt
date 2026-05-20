package com.example.courses.presentation.customView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.courses.R
import com.example.courses.presentation.models.CourseViewModel

class CourseItemViewAdapter : RecyclerView.Adapter<CourseItemViewAdapter.CourseViewHolder>() {

    var courseList = emptyList<CourseViewModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CourseViewHolder {
        val context = parent.context
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.course_item, parent, false)
        val viewHolder = CourseViewHolder(itemView)
        return viewHolder
    }

    fun setList(data : List<CourseViewModel>){
        courseList = data
        //метод для уведомления RecyclerView об изменении данных
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: CourseViewHolder,
        pozition: Int
    ) {
        val item = courseList[pozition]
        holder.title.setText(item.title)
        holder.text.setText(item.text)
        holder.cost.setText(item.price.toString())
        holder.rate.setText(item.rate.toString())
        holder.startData.setText(item.startDate)
        holder.btnFavorite.setOnClickListener {
            Log.v("LIST ITEM", "Item COURSE LIST ${item.id}")
        }
    }

    override fun getItemCount(): Int = courseList.size

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val title =itemView.findViewById<TextView>(R.id.text_titleCourse)
        val text = itemView.findViewById<TextView>(R.id.textCourse)
        val cost = itemView.findViewById<TextView>(R.id.textCost)
        val rate = itemView.findViewById<TextView>(R.id.textRate)
        val startData = itemView.findViewById<TextView>(R.id.textStartDate)
        val btnFavorite = itemView.findViewById<Button>(R.id.btnFavorit)
    }
}