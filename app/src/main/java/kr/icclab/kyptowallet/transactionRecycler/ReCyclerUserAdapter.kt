package kr.icclab.kyptowallet.transactionRecycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kenai.jffi.Main
import kotlinx.android.synthetic.main.item_recycler.view.*
import kr.icclab.kyptowallet.MainActivity
import kr.icclab.kyptowallet.R
import kr.icclab.kyptowallet.item_view_fragment

class ReCyclerUserAdapter (private val items: ArrayList<UiTransaction>) : RecyclerView.Adapter<ReCyclerUserAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ReCyclerUserAdapter.ViewHolder, position: Int) {

        val item = items[position]
        val listener = View.OnClickListener { it ->
//            Toast.makeText(it.context, "Clicked -> ID : ${item.from}, Name : ${item.hash}", Toast.LENGTH_SHORT).show()
            val appCompatActivity = it.context as AppCompatActivity
            appCompatActivity.supportFragmentManager.
            beginTransaction()
                .replace(R.id.view, item_view_fragment.newInstance(hash = item.hash.toString()))
                .addToBackStack(null)
                .commit()
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false)
        return ReCyclerUserAdapter.ViewHolder(inflatedView)
    }

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun bind(listener: View.OnClickListener, item: UiTransaction) {
            var addmul = "- "
            var drawImage = R.drawable.status_out_button
            if(item.inout == false){
                drawImage = R.drawable.status_in_button
                addmul = "+ "
            }
            view.item_etherTextView.text = addmul+item.from
            view.item_blockTokenTextView.text = item.hash
            view.item_blockTokenTextView.isSelected =true

            view.item_ImageView.setImageDrawable(ContextCompat.getDrawable(view.context, drawImage))
            view.setOnClickListener(listener)
        }
    }
}