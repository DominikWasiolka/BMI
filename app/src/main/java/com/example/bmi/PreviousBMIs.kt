package com.example.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_previous_bmis.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*

class PreviousBMIs : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_bmis)

        supportActionBar?.title = getString(R.string.prev_results)

        val resultType = object : TypeToken<LinkedList<Result>>() {}.type
        val jsonString: String = intent.getStringExtra("results")
        val listOfResult : LinkedList<Result> = Gson().fromJson(jsonString, resultType)

        val adapter = GroupAdapter<ViewHolder>()



        for (res in listOfResult) {
            adapter.add(BmiPrevItem(res))
        }


        recycleview_prev_bmi.adapter = adapter
        recycleview_prev_bmi.layoutManager = LinearLayoutManager(this)
    }
}

class BmiPrevItem(val result:Result): Item<ViewHolder>(){


    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.bmi.setTextColor(result.color)
        viewHolder.itemView.weight_number.text = result.weight
        viewHolder.itemView.height_number.text = result.height
        viewHolder.itemView.date.text = result.date
        viewHolder.itemView.bmi.text = result.bmi
        viewHolder.itemView.system.text = result.system
    }

    override fun getLayout(): Int {
        return R.layout.list_item
    }
}