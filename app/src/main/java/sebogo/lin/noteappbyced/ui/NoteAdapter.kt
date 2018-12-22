package sebogo.lin.noteappbyced.ui

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import sebogo.lin.noteappbyced.BR
import sebogo.lin.noteappbyced.R
import sebogo.lin.noteappbyced.databinding.NoteListBinding
import sebogo.lin.noteappbyced.model.Note


class NoteAdapter(var listNotes: List<Note>, private val clickListener: inteAdapter): RecyclerView.Adapter<NoteAdapter.lavue>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): lavue {
        val layoutInflater =  LayoutInflater.from(parent.context)
        val binding = NoteListBinding.inflate(layoutInflater, parent, false)
        return lavue(binding)
    }

    override fun getItemCount(): Int {
        return listNotes.size
    }

    override fun onBindViewHolder(holder: lavue, position: Int) {
        val item = listNotes[position]
        holder.bind(item)
        holder.binding.itemNoteRoot.setOnClickListener {
            clickListener.onClickItem(listNotes[position], EnumOptionMenu.EDIT)
        }

        holder.binding.optionsMen.setOnClickListener {
            //creating a popup menu
            val popup = PopupMenu(it.context, holder.binding.optionsMen)
            //inflating menu from xml resource
            popup.inflate(R.menu.menu_option_list)
            popup.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.edit ->{
                        clickListener.onClickItem(listNotes[position], EnumOptionMenu.EDIT)
                    }
                    R.id.delete ->{
                        clickListener.onClickItem(listNotes[position], EnumOptionMenu.DELETE)
                    }
                }
                false
            }
            popup.show()
        }
    }

    inner class lavue(val binding: NoteListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.setVariable(BR.note, note)
            binding.executePendingBindings()
        }
    }

    interface inteAdapter{
        fun onClickItem(note: Note,enumOptionMenu: EnumOptionMenu)
    }

    enum class EnumOptionMenu{
        EDIT, DELETE
    }
}