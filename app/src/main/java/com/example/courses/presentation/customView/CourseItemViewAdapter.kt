package com.example.courses.presentation.customView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
    }

    override fun onBindViewHolder(
        holder: CourseViewHolder,
        pozition: Int
    ) {
        val item = courseList[pozition]
        holder.title.setText(item.title)
    }

    override fun getItemCount(): Int = courseList.size

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title =itemView.findViewById<TextView>(R.id.text_titleCourse)
    }
}