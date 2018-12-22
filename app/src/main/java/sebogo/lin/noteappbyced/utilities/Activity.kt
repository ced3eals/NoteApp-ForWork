package sebogo.lin.noteappbyced.utilities

import android.app.Activity
import android.content.Context
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import sebogo.lin.noteappbyced.R

fun Activity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val inputMethodManager = getSystemService(Context
                .INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}

fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, @IdRes frameId: Int) {
    val fragManager = supportFragmentManager.beginTransaction()
    fragManager.setCustomAnimations(R.anim.fade_in,
        R.anim.fade_out, R.anim.fade_in,
        R.anim.fade_out)
    fragManager.replace(frameId, fragment)
    fragManager.commit()
}


fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, tag: String, @IdRes frameId: Int) {
    val fragManager = supportFragmentManager.beginTransaction()
    fragManager.setCustomAnimations(
        R.anim.fade_in,
        R.anim.fade_out, R.anim.fade_in,
        R.anim.fade_out)
    fragManager.add(frameId, fragment, tag)
    fragManager.addToBackStack(tag)
    fragManager.commit()
}


fun Activity.setTitleToolbar(title: String){
    actionBar.title = title
}