package sebogo.lin.noteappbyced.utilities

import android.databinding.BindingAdapter
import android.widget.EditText

@BindingAdapter("maskFormat")
fun EditText.setMaskFormat(maskFormat: String) {
    this.addTextChangedListener(Mask.mask(maskFormat, this))
}

