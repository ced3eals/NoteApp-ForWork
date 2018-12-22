package sebogo.lin.noteappbyced.utilities

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView


@BindingAdapter("setAdapter")
fun RecyclerView.setAdapter(myAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
    this.adapter = myAdapter
}