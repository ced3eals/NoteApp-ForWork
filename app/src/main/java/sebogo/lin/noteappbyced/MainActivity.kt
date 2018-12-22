package sebogo.lin.noteappbyced

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import sebogo.lin.noteappbyced.ui.lists.ListFragment
import sebogo.lin.noteappbyced.utilities.replaceFragmentInActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragmentInActivity(ListFragment.newInstance( Bundle()), R.id.containerFragment)
    }
}
