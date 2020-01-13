package com.app.home1.base.dialogs

import android.content.DialogInterface
import android.os.Bundle
import com.app.mobifix.base.dialogs.messagedialog.DialogFragmentPresenterImpl.WhichButton

/**
 * Created by sugak.sl on 10.03.2017.
 */
interface DialogButtonClickListener {
    fun onClickDialogButton(
        dialog: DialogInterface?, @WhichButton whichButton: Int,
        requestCode: Int,
        params: Bundle?
    )
}