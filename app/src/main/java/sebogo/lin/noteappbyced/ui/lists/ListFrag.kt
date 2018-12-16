package sebogo.lin.noteappbyced.ui.lists

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.ListFragment
import android.view.*
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.frag_list.*
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.design.snackbar
import sebogo.lin.noteappbyced.MainActivity
import sebogo.lin.noteappbyced.NoteApp
import sebogo.lin.noteappbyced.R
import sebogo.lin.noteappbyced.database.roomdb.NoteRoomBd
import sebogo.lin.noteappbyced.databinding.FragListBinding
import sebogo.lin.noteappbyced.model.Note
import sebogo.lin.noteappbyced.ui.entries.RegFrag
import sebogo.lin.noteappbyced.utilities.addFragmentToActivity

class ListFrag : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            ListFragment().apply {
                arguments = bundle
            }
    }
    lateinit var theviewModel: ListViewModel
    lateinit var binding: FragListBinding
    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        theviewModel = ViewModelProviders
            .of(this, ListViewModel.Factory(ListRepo(NoteRoomBd.getDatabase(NoteApp.contextApp!!)?.listNoteDao()!!)))
            .get(ListViewModel::class.java)

        lifecycle.addObserver(theviewModel)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        activity?.title = getString(R.string.list_note)
        ((activity) as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragListBinding.inflate(inflater, container, false)
        binding.noteList = theviewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        floatingActionButton.setOnClickListener {
            ((activity) as MainActivity).addFragmentToActivity(RegFrag.newInstance(Bundle()), RegFrag::class.java.simpleName, R.id.containerFragment)
        }
    }
    private fun deleteNote(note: Note) {
        theviewModel.deleteNote(note)
    }

    override fun onStart() {
        super.onStart()
        theviewModel.resulClickEdit
            .autoDisposable(scopeProvider)
            .subscribe { notes ->
                val bundle = Bundle().apply {
                    putParcelable(RegFrag.EDIT_NOTE, notes)
                }
                ((activity) as MainActivity).addFragmentToActivity(RegFrag.newInstance(bundle), RegFrag::class.java.simpleName, R.id.containerFragment)
            }
        theviewModel.resultClickDelete
            .autoDisposable(scopeProvider)
            .subscribe {
                deleteNote(it)
            }
        theviewModel.goodTransaction
            .autoDisposable(scopeProvider)
            .subscribe ({
                snackbar(rootLayoutList, "Deleted Sucess")
                    //.view.setBackgroundColor(resources.getColor(android.R.color.holo_green_light))
                    .show()
            },{
                longSnackbar(rootLayoutList, "Error delete").show()
            })
    }


}
