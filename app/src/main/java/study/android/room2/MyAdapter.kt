package study.android.room2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val list: List<String>) : RecyclerView.Adapter<MyAdapter.MyView>() {

    class MyView(itemView: View) : RecyclerView.ViewHolder(itemView){
        val valueoftext: TextView = itemView.findViewById(R.id.textView2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item,parent, false)
        return MyView(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyView, position: Int) {
        holder.valueoftext.text = list.get(position)

    }
}