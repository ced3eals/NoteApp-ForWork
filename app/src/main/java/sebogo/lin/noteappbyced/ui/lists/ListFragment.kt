package sebogo.lin.noteappbyced.ui.lists


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import sebogo.lin.noteappbyced.MainActivity
import sebogo.lin.noteappbyced.NoteApp
import sebogo.lin.noteappbyced.R
import sebogo.lin.noteappbyced.database.roomdb.NoteRoomBd
import sebogo.lin.noteappbyced.databinding.FragmentRegisterBinding
import sebogo.lin.noteappbyced.databinding.FragmentListBinding
import sebogo.lin.noteappbyced.model.Note
import sebogo.lin.noteappbyced.ui.entries.RegisterFragment
import sebogo.lin.noteappbyced.utilities.addFragmentToActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_list.*
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.design.snackbar





class ListFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            ListFragment().apply {
                arguments = bundle
            }
    }

    lateinit var viewModel: ListViewModel
    lateinit var binding: FragmentListBinding
    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders
            .of(this, ListViewModel.Factory(ListRepo(NoteRoomBd.getDatabase(NoteApp.contextApp!!)?.listNoteDao()!!)))
            .get(ListViewModel::class.java)

        lifecycle.addObserver(viewModel)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        activity?.title = getString(R.string.list_note)
        ((activity) as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)
        binding.noteList = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        floatingActionButton.setOnClickListener {
            ((activity) as MainActivity).addFragmentToActivity(RegisterFragment.newInstance(Bundle()), RegisterFragment::class.java.simpleName, R.id.containerFragment)
        }
    }


    override fun onStart() {
        super.onStart()
        viewModel.resulClickEdit
            .autoDisposable(scopeProvider)
            .subscribe { notes ->
                val bundle = Bundle().apply {
                    putParcelable(RegisterFragment.EDIT_NOTE, notes)
                }
                ((activity) as MainActivity).addFragmentToActivity(RegisterFragment.newInstance(bundle), RegisterFragment::class.java.simpleName, R.id.containerFragment)
            }
        viewModel.resultClickDelete
            .autoDisposable(scopeProvider)
            .subscribe {
                deleteNote(it)
            }
        viewModel.goodTransaction
            .autoDisposable(scopeProvider)
            .subscribe ({
                snackbar(rootLayoutList, "Deleted !")
                    .show()
            },{
                longSnackbar(rootLayoutList, "Error delete").show()
            })
    }

    private fun deleteNote(note: Note) {
        viewModel.deleteNote(note)
    }

}
