package com.app.home1.base.dialogs

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Created by sugak.sl on 10.03.2017.
 */
interface DialogFragmentPresenter {
    fun display(
        from: Fragment,
        newFragment: DialogFragment,
        fragmentTag: String?
    )

    fun display(
        from: FragmentActivity,
        newFragment: DialogFragment,
        fragmentTag: String?
    )

    fun isDisplayed(
        from: Fragment,
        fragmentTag: String?
    ): Boolean

    fun isDisplayed(from: FragmentActivity, fragmentTag: String?): Boolean

    fun getMessageDialogListener(dlgFragment: DialogFragment?): DialogButtonClickListener?

    companion object {
        const val ARGUMENT_FROM_FRAGMENT = "from_fragment"
    }
}