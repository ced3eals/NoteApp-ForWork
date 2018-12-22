package sebogo.lin.noteappbyced

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import sebogo.lin.noteappbyced.ui.lists.ListFrag
import sebogo.lin.noteappbyced.utilities.replaceFragmentInActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragmentInActivity(ListFrag.newInstance( Bundle()), R.id.containerFragment)
    }
}
