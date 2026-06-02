package com.example.courses.presentation.customView

import android.graphics.drawable.Icon
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.courses.R
import com.example.courses.data.datastore.AppDatabase
import com.example.courses.data.repository.FavouriteCoursesRepository
import com.example.courses.domain.CourseBusinessModel
import com.example.courses.presentation.models.CourseViewModel

class CourseItemViewAdapter(private var repository : FavouriteCoursesRepository) : RecyclerView.Adapter<CourseItemViewAdapter.CourseViewHolder>() {

    var courseList : List<CourseViewModel> = emptyList()
        set(newValue){
            field = newValue
            //метод для уведомления RecyclerView об изменении данных
            notifyDataSetChanged()
        }

    var favouriteCourseList : List<CourseViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /*
    var repository : FavouriteCoursesRepository = FavouriteCoursesRepository()
        set(value) {
            field = value
        }
    */

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

    @RequiresApi(Build.VERSION_CODES.O)
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

        //если нашли в списке избранных курсов
        if(favouriteCourseList.find { course->course.id == item.id }!=null){
            holder.btnFavorite.setImageResource(android.R.drawable.star_big_on)
        }else{
            holder.btnFavorite.setImageResource(android.R.drawable.star_big_off)
        }

        holder.btnFavorite.setOnClickListener {
            Log.v("LIST ITEM", "Item COURSE LIST ${item.id}")
            val data = repository.mapper.mapToBusinessModel(item)
            if(favouriteCourseList.find { course->course.id == item.id }!=null){
                //holder.btnFavorite.setImageResource(android.R.drawable.star_big_on)
                repository.removeFavouriteCourseList(data)
            }else{
                //holder.btnFavorite.setImageResource(android.R.drawable.star_big_off)
                repository.addFavouriteCourseList(data)
            }
            //repository.addFavouriteCourseList(repository.mapper.mapToBusinessModel(item))
        }
        Log.v("CourseItemViewAdapter", "CourseItemViewAdapter || ${favouriteCourseList.size}")
    }

    override fun getItemCount(): Int = courseList.size

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val title =itemView.findViewById<TextView>(R.id.text_titleCourse)
        val text = itemView.findViewById<TextView>(R.id.textCourse)
        val cost = itemView.findViewById<TextView>(R.id.textCost)
        val rate = itemView.findViewById<TextView>(R.id.textRate)
        val startData = itemView.findViewById<TextView>(R.id.textStartDate)
        val btnFavorite = itemView.findViewById<ImageButton>(R.id.btnFavourite)
    }
}