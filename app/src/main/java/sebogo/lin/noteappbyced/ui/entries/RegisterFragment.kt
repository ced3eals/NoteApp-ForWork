package sebogo.lin.noteappbyced.ui.entries

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.*
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import java.util.*
import sebogo.lin.noteappbyced.MainActivity
import sebogo.lin.noteappbyced.NoteApp
import sebogo.lin.noteappbyced.R
import sebogo.lin.noteappbyced.database.api.ApiClient
import sebogo.lin.noteappbyced.database.api.ApiRegService
import sebogo.lin.noteappbyced.database.roomdb.NoteRoomBd
import sebogo.lin.noteappbyced.utilities.hideSoftKeyboard
import sebogo.lin.noteappbyced.utilities.setMaskFormat
import kotlinx.android.synthetic.main.fragment_register.*
import sebogo.lin.noteappbyced.databinding.FragmentRegisterBinding
import sebogo.lin.noteappbyced.utilities.showKeyboard


class RegisterFragment : Fragment() {

    companion object {
        const val EDIT_NOTE = "edit_note"
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            RegisterFragment().apply {
                arguments = bundle
            }
    }

    lateinit var viewModel: RegisterViewModel
    lateinit var binding: FragmentRegisterBinding
    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders
            .of(this,  RegisterViewModel.Factory(RegisterRepo(NoteRoomBd.getDatabase(NoteApp.contextApp!!)?.noteDao()!!,
                ApiClient.getApiService().create(ApiRegService::class.java))))
            .get(RegisterViewModel::class.java)

        lifecycle.addObserver(viewModel)

        arguments?.let {
            viewModel.setEditNote(it.getParcelable(EDIT_NOTE))
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        activity?.title = getString(R.string.add_a_note)
        ((activity) as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ((activity) as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_close)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        return when(id){
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statDate.setMaskFormat("##/##/####")
        endDate.setMaskFormat("##/##/####")

        saveTheNote.setOnClickListener {
            activity?.hideSoftKeyboard()
            viewModel.saveNote()
        }
    }

    override fun onStart() {
        super.onStart()
        initViews()
    }

    override fun onStop() {
        super.onStop()
        activity?.hideSoftKeyboard()
    }
    private fun initViews() {
        viewModel.publishError
            .autoDisposable(scopeProvider)
            .subscribe {
                when(it.second){
                    0 -> {descriptionInput.error = if(!it.first)getString(R.string.invalid_note) else "" }
                    1 -> {startInput.error = if(!it.first)getString(R.string.invalid_date) else ""}
                    2 -> {endDateInput.error = if(!it.first)getString(R.string.invalid_date) else ""}
                }
            }

        viewModel.publishSucces
            .autoDisposable(scopeProvider)
            .subscribe {
                when(it) {
                    true -> {
                        val snackbar = Snackbar
                            .make(rootLayoutRegister, getString(R.string.saved), Snackbar.LENGTH_LONG)
                        snackbar.show()
                        description.requestFocus()
                        description.showKeyboard()
                        descriptionInput.error = null
                        startInput.error = null
                        endDateInput.error = null
                    }
                    false ->{
                        val snackbar = Snackbar
                            .make(rootLayoutRegister, getString(R.string.error_saving), Snackbar.LENGTH_LONG)
                        snackbar.show()
                    }
                }
            }

        endDate.setOnClickListener {
            val date = Calendar.getInstance()
            val calendar = DatePickerDialog(activity!!, ecouteendDate ,date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH))
            calendar.show()
        }

        statDate.setOnClickListener {
            val date = Calendar.getInstance()
            val calendar = DatePickerDialog(activity!!, ecoutestartDate ,date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(
                Calendar.DAY_OF_MONTH))
            calendar.show()
        }
    }

    @SuppressLint("SetTextI18n")
    val ecouteendDate = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        endDate.setText("")
        endDate.setText(String.format("%02d", dayOfMonth)  + String.format("%02d", month + 1) + String.format("%02d", year))
    }

    @SuppressLint("SetTextI18n")
    val ecoutestartDate = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        statDate.setText("")
        statDate.setText(String.format("%02d", dayOfMonth)  + String.format("%02d", month + 1) + String.format("%02d", year))
    }
}
