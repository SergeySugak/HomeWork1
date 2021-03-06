package com.app.home1.base.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.app.home1.R
import com.app.mobifix.base.dialogs.messagedialog.DialogFragmentPresenterImpl

@SuppressLint("WrongConstant")
class MessageDialogFragment : DialogFragment(),
    DialogInterface.OnClickListener, DialogButtonClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        if (arguments != null && TextUtils.isEmpty(
                arguments!!.getString(
                    ARGUMENT_TITLE
                )
            )
        ) {
            setStyle(STYLE_NO_TITLE, 0)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        assert(context != null)
        val builder =
            AlertDialog.Builder(context!!)
                .setMessage(
                    if (arguments != null) arguments!!.getCharSequence(
                        ARGUMENT_MESSAGE
                    ) else null
                )
        @DrawableRes var iconResId =
            arguments!!.getInt(ARGUMENT_ICON)
        when (arguments!!.getInt(ARGUMENT_ICON)) {
            DialogFragmentPresenterImpl.ICON_INFO -> iconResId = R.drawable.ic_info
            DialogFragmentPresenterImpl.ICON_QUESTION -> iconResId =
                R.drawable.ic_question
            DialogFragmentPresenterImpl.ICON_WARINING -> iconResId = R.drawable.ic_warning
            DialogFragmentPresenterImpl.ICON_ERROR -> iconResId = R.drawable.ic_error
            else -> {
            }
        }
        if (arguments!!.getInt(ARGUMENT_ICON) != DialogFragmentPresenterImpl.NO_ICON) {
            builder.setIcon(iconResId)
        }
        if (!TextUtils.isEmpty(arguments!!.getString(ARGUMENT_TITLE))) {
            builder.setTitle(arguments!!.getString(ARGUMENT_TITLE))
        }
        val buttons = arguments!!.getInt(ARGUMENT_BUTTONS)
        when (buttons) {
            DialogFragmentPresenterImpl.THREE_BUTTONS -> {
                builder.setNeutralButton(android.R.string.cancel, this)
                builder.setNeutralButton(R.string.cancel, this)
                builder.setNegativeButton(android.R.string.no, this)
                builder.setNegativeButton(R.string.no, this)
                builder.setPositiveButton(android.R.string.yes, this)
                builder.setPositiveButton(R.string.yes, this)
            }
            DialogFragmentPresenterImpl.THREE_BUTTONS_YNC -> {
                builder.setNeutralButton(R.string.cancel, this)
                builder.setNegativeButton(android.R.string.no, this)
                builder.setNegativeButton(R.string.no, this)
                builder.setPositiveButton(android.R.string.yes, this)
                builder.setPositiveButton(R.string.yes, this)
            }
            DialogFragmentPresenterImpl.TWO_BUTTONS -> {
                builder.setNegativeButton(android.R.string.no, this)
                builder.setNegativeButton(R.string.no, this)
                builder.setPositiveButton(android.R.string.yes, this)
                builder.setPositiveButton(R.string.yes, this)
            }
            DialogFragmentPresenterImpl.TWO_BUTTONS_YN -> {
                builder.setNegativeButton(R.string.no, this)
                builder.setPositiveButton(android.R.string.yes, this)
                builder.setPositiveButton(R.string.yes, this)
            }
            DialogFragmentPresenterImpl.ONE_BUTTON -> {
                builder.setPositiveButton(android.R.string.yes, this)
                builder.setPositiveButton(R.string.yes, this)
            }
            DialogFragmentPresenterImpl.ONE_BUTTON_Y -> builder.setPositiveButton(
                R.string.yes,
                this
            )
        }
        val dialog = builder.create()
        //dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        val listener = messageDialogListener
        if (listener != null && arguments != null) {
            listener.onClickDialogButton(
                dialog,
                which,
                arguments!!.getInt(ARGUMENT_REQUEST_CODE),
                arguments!!.getParcelable(ARGUMENT_PARAMS)
            )
        }
    }

    private val messageDialogListener: DialogButtonClickListener?
        private get() = DialogFragmentPresenterImpl().getMessageDialogListener(this)

    override fun onClickDialogButton(
        dialog: DialogInterface?, @DialogFragmentPresenterImpl.WhichButton whichButton: Int,
        requestCode: Int,
        params: Bundle?
    ) {
    }

    companion object {
        const val REQUEST_CODE_NONE = -1
        private val FRAGMENT_TAG =
            MessageDialogFragment::class.java.canonicalName
        private const val ARGUMENT_TITLE = "title"
        private const val ARGUMENT_MESSAGE = "message"
        private const val ARGUMENT_ICON = "icon_id"
        private const val ARGUMENT_REQUEST_CODE = "tag"
        private const val ARGUMENT_BUTTONS = "buttons"
        private const val ARGUMENT_PARAMS = "params"
        private fun newInstance(): MessageDialogFragment {
            return MessageDialogFragment()
        }

        fun newInstance(
            context: Context,
            messageResID: Int
        ): MessageDialogFragment {
            return newInstance(context.getString(messageResID))
        }

        @JvmOverloads
        fun newInstance(
            context: Context,
            titleResID: Int,
            messageResID: Int, @DialogFragmentPresenterImpl.Icon iconResID: Int = DialogFragmentPresenterImpl.ICON_INFO,
            requestCode: Int = REQUEST_CODE_NONE, @DialogFragmentPresenterImpl.Buttons buttons: Int = DialogFragmentPresenterImpl.ONE_BUTTON
        ): MessageDialogFragment {
            return newInstance(
                context.getString(titleResID), context.getString(messageResID),
                iconResID, requestCode, buttons, null
            )
        }

        fun newInstance(
            context: Context,
            titleResID: Int,
            messageResID: Int, @DialogFragmentPresenterImpl.Icon iconResID: Int,
            requestCode: Int, @DialogFragmentPresenterImpl.Buttons buttons: Int,
            params: Bundle?
        ): MessageDialogFragment {
            return newInstance(
                context.getString(titleResID), context.getString(messageResID),
                iconResID, requestCode, buttons, params
            )
        }

        fun newInstance(message: CharSequence?): MessageDialogFragment {
            val fragment = newInstance()
            val bundle = Bundle()
            bundle.putString(ARGUMENT_TITLE, null)
            bundle.putCharSequence(ARGUMENT_MESSAGE, message)
            bundle.putInt(
                ARGUMENT_ICON,
                DialogFragmentPresenterImpl.ICON_INFO
            )
            bundle.putInt(
                ARGUMENT_REQUEST_CODE,
                REQUEST_CODE_NONE
            )
            bundle.putInt(
                ARGUMENT_BUTTONS,
                DialogFragmentPresenterImpl.ONE_BUTTON
            )
            bundle.putParcelable(ARGUMENT_PARAMS, null)
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(
            context: Context,
            ex: Throwable,
            requestCode: Int,
            params: Bundle?
        ): MessageDialogFragment {
            return newInstance(
                context,
                ex,
                requestCode,
                DialogFragmentPresenterImpl.ONE_BUTTON,
                //false,
                params
            )
        }

        @JvmOverloads
        fun newInstance(
            context: Context,
            ex: Throwable,
            requestCode: Int = REQUEST_CODE_NONE, @DialogFragmentPresenterImpl.Buttons buttons: Int = DialogFragmentPresenterImpl.ONE_BUTTON,
            //stackTrace: Boolean = false,
            params: Bundle? = null
        ): MessageDialogFragment {
            var messageText = if (ex.cause != null && !TextUtils.isEmpty(ex.cause!!.message)) {
                ex.cause!!.message
            } else {
                if (!TextUtils.isEmpty(ex.message)) {
                    ex.message
                } else {
                    ex.toString()
                }
            }
//            if (stackTrace) {
//                messageText = messageText + "\n\n--- Stack trace ---\n" + getStackTraceAsString(ex)
//            }
//            Log.e("ERROR", messageText)
            return newInstance(
                context.getString(R.string.title_error), messageText,
                DialogFragmentPresenterImpl.ICON_ERROR, requestCode, buttons, params
            )
        }

        @JvmOverloads
        fun newInstance(
            title: String?,
            message: CharSequence?, @DialogFragmentPresenterImpl.Icon iconResID: Int = DialogFragmentPresenterImpl.ICON_INFO,
            requestCode: Int = REQUEST_CODE_NONE, @DialogFragmentPresenterImpl.Buttons buttons: Int = DialogFragmentPresenterImpl.ONE_BUTTON,
            params: Bundle? = null
        ): MessageDialogFragment {
            val fragment = newInstance()
            val bundle = Bundle()
            bundle.putString(ARGUMENT_TITLE, title)
            bundle.putCharSequence(ARGUMENT_MESSAGE, message)
            bundle.putInt(ARGUMENT_ICON, iconResID)
            bundle.putInt(ARGUMENT_REQUEST_CODE, requestCode)
            bundle.putInt(ARGUMENT_BUTTONS, buttons)
            bundle.putParcelable(ARGUMENT_PARAMS, params)
            fragment.arguments = bundle
            return fragment
        }

        fun display(from: Fragment, newFragment: MessageDialogFragment, tag: String?) {
            DialogFragmentPresenterImpl().display(
                from,
                newFragment,
                if (TextUtils.isEmpty(tag)) FRAGMENT_TAG else tag
            )
        }

        fun display(from: Fragment, newFragment: MessageDialogFragment) {
            DialogFragmentPresenterImpl().display(
                from,
                newFragment,
                FRAGMENT_TAG
            )
        }

        fun display(from: FragmentActivity, newFragment: MessageDialogFragment, tag: String?) {
            DialogFragmentPresenterImpl().display(
                from,
                newFragment,
                if (TextUtils.isEmpty(tag)) FRAGMENT_TAG else tag
            )
        }

        fun display(from: FragmentActivity, newFragment: MessageDialogFragment) {
            DialogFragmentPresenterImpl().display(
                from,
                newFragment,
                FRAGMENT_TAG
            )
        }

        fun hide(from: Fragment) {
            val fm = from.fragmentManager
            val prev: Fragment?
            if (fm != null) {
                prev = fm.findFragmentByTag(FRAGMENT_TAG)
                if (prev != null) {
                    val ft = fm.beginTransaction()
                    ft.remove(prev)
                    ft.commit()
                }
            }
        }

        fun hide(from: FragmentActivity) {
            val fm = from.supportFragmentManager
            val prev =
                fm.findFragmentByTag(FRAGMENT_TAG)
            if (prev != null) {
                val ft = fm.beginTransaction()
                ft.remove(prev)
                ft.commit()
            }
        }

        fun showError(activity: FragmentActivity, t: Throwable) {
            showError(activity, t, REQUEST_CODE_NONE, null, FRAGMENT_TAG)
        }

        fun showError(fragment: Fragment, t: Throwable) {
            showError(fragment, t, REQUEST_CODE_NONE, null, FRAGMENT_TAG)
        }

        @JvmOverloads
        fun showError(
            activity: FragmentActivity,
            t: Throwable,
            requestCode: Int,
            params: Bundle?,
            tag: String? = t.hashCode().toString()
        ) {
            val f =
                newInstance(activity, t, requestCode, params)
            display(activity, f, tag)
            t.printStackTrace()
        }

        @JvmOverloads
        fun showError(
            fragment: Fragment,
            t: Throwable,
            requestCode: Int,
            params: Bundle?,
            tag: String? = t.hashCode().toString()
        ) {
            if (fragment.context == null) {
                return
            }
            val f = newInstance(
                fragment.context!!,
                t,
                requestCode,
                params
            )
            display(fragment, f, tag)
            //if (requestCode == REQUEST_CODE_NONE)
            t.printStackTrace()
        }

        @JvmOverloads
        fun showMessage(
            activity: FragmentActivity,
            title: String?,
            message: CharSequence?,
            tag: String? = FRAGMENT_TAG
        ) {
            val f = newInstance(
                title,
                message,
                DialogFragmentPresenterImpl.ICON_INFO,
                REQUEST_CODE_NONE,
                DialogFragmentPresenterImpl.ONE_BUTTON,
                null
            )
            display(activity, f, tag)
        }

        @JvmOverloads
        fun showMessage(
            fragment: Fragment,
            title: String?,
            message: CharSequence?,
            tag: String? = FRAGMENT_TAG
        ) {
            val f = newInstance(
                title,
                message,
                DialogFragmentPresenterImpl.ICON_INFO,
                REQUEST_CODE_NONE,
                DialogFragmentPresenterImpl.ONE_BUTTON,
                null
            )
            display(fragment, f, tag)
        }

        @JvmOverloads
        fun showMessage(
            activity: FragmentActivity,
            title: String?,
            message: CharSequence?, @DialogFragmentPresenterImpl.Icon iconResID: Int,
            tag: String? = FRAGMENT_TAG
        ) {
            val f = newInstance(
                title,
                message,
                iconResID,
                REQUEST_CODE_NONE,
                DialogFragmentPresenterImpl.ONE_BUTTON,
                null
            )
            display(activity, f, tag)
        }

        @JvmOverloads
        fun showMessage(
            fragment: Fragment,
            title: String?,
            message: CharSequence?, @DialogFragmentPresenterImpl.Icon iconResID: Int,
            tag: String? = FRAGMENT_TAG
        ) {
            val f = newInstance(
                title,
                message,
                iconResID,
                REQUEST_CODE_NONE,
                DialogFragmentPresenterImpl.ONE_BUTTON,
                null
            )
            display(fragment, f, tag)
        }

        @JvmOverloads
        fun showMessage(
            activity: FragmentActivity,
            title: String?,
            message: CharSequence?, @DialogFragmentPresenterImpl.Icon iconResID: Int,
            requestCode: Int,
            tag: String? = FRAGMENT_TAG
        ) {
            val f = newInstance(
                title,
                message,
                iconResID,
                requestCode,
                DialogFragmentPresenterImpl.ONE_BUTTON,
                null
            )
            display(activity, f, tag)
        }

        @JvmOverloads
        fun showMessage(
            fragment: Fragment,
            title: String?,
            message: CharSequence?, @DialogFragmentPresenterImpl.Icon iconResID: Int,
            requestCode: Int,
            tag: String? = FRAGMENT_TAG
        ) {
            val f = newInstance(
                title,
                message,
                iconResID,
                requestCode,
                DialogFragmentPresenterImpl.ONE_BUTTON,
                null
            )
            display(fragment, f, tag)
        }

        @JvmOverloads
        fun showMessage(
            activity: FragmentActivity,
            title: String?,
            message: CharSequence?, @DialogFragmentPresenterImpl.Icon iconResID: Int,
            requestCode: Int, @DialogFragmentPresenterImpl.Buttons buttons: Int,
            tag: String? = FRAGMENT_TAG
        ) {
            val f = newInstance(
                title,
                message,
                iconResID,
                requestCode,
                buttons,
                null
            )
            display(activity, f, tag)
        }

        @JvmOverloads
        fun showMessage(
            fragment: Fragment,
            title: String?,
            message: CharSequence?, @DialogFragmentPresenterImpl.Icon iconResID: Int,
            requestCode: Int, @DialogFragmentPresenterImpl.Buttons buttons: Int,
            tag: String? = FRAGMENT_TAG
        ) {
            val f = newInstance(
                title,
                message,
                iconResID,
                requestCode,
                buttons,
                null
            )
            display(fragment, f, tag)
        }

        @JvmOverloads
        fun showMessage(
            fragment: Fragment,
            title: String?,
            message: CharSequence?, @DialogFragmentPresenterImpl.Icon iconResID: Int,
            requestCode: Int, @DialogFragmentPresenterImpl.Buttons buttons: Int,
            params: Bundle?,
            tag: String? = FRAGMENT_TAG
        ) {
            val f = newInstance(
                title,
                message,
                iconResID,
                requestCode,
                buttons,
                params
            )
            display(fragment, f, tag)
        }

        /////////////////////////////////////////////////////
        fun showMessage(
            activity: FragmentActivity,
            titleResID: Int,
            messageResID: Int
        ) {
            showMessage(
                activity,
                activity.getString(titleResID),
                activity.getString(messageResID),
                DialogFragmentPresenterImpl.ICON_INFO,
                REQUEST_CODE_NONE,
                DialogFragmentPresenterImpl.ONE_BUTTON
            )
        }

        fun showMessage(
            fragment: Fragment,
            titleResID: Int,
            messageResID: Int
        ) {
            showMessage(
                fragment,
                fragment.getString(titleResID),
                fragment.getString(messageResID),
                DialogFragmentPresenterImpl.ICON_INFO,
                REQUEST_CODE_NONE,
                DialogFragmentPresenterImpl.ONE_BUTTON
            )
        }

        fun showMessage(
            activity: FragmentActivity,
            titleResID: Int,
            messageResID: Int, @DialogFragmentPresenterImpl.Icon iconResID: Int
        ) {
            showMessage(
                activity,
                activity.getString(titleResID),
                activity.getString(messageResID),
                iconResID,
                REQUEST_CODE_NONE,
                DialogFragmentPresenterImpl.ONE_BUTTON
            )
        }

        fun showMessage(
            fragment: Fragment,
            titleResID: Int,
            messageResID: Int, @DialogFragmentPresenterImpl.Icon iconResID: Int
        ) {
            showMessage(
                fragment,
                fragment.getString(titleResID),
                fragment.getString(messageResID),
                iconResID,
                REQUEST_CODE_NONE,
                DialogFragmentPresenterImpl.ONE_BUTTON
            )
        }

        fun showMessage(
            activity: FragmentActivity,
            titleResID: Int,
            messageResID: Int, @DialogFragmentPresenterImpl.Icon iconResID: Int,
            requestCode: Int
        ) {
            showMessage(
                activity,
                activity.getString(titleResID), activity.getString(messageResID),
                iconResID, requestCode, DialogFragmentPresenterImpl.ONE_BUTTON
            )
        }

        fun showMessage(
            fragment: Fragment,
            titleResID: Int,
            messageResID: Int, @DialogFragmentPresenterImpl.Icon iconResID: Int,
            requestCode: Int
        ) {
            showMessage(
                fragment,
                fragment.getString(titleResID), fragment.getString(messageResID),
                iconResID, requestCode, DialogFragmentPresenterImpl.ONE_BUTTON
            )
        }

        fun showMessage(
            activity: FragmentActivity,
            titleResID: Int,
            messageResID: Int, @DialogFragmentPresenterImpl.Icon iconResID: Int,
            requestCode: Int, @DialogFragmentPresenterImpl.Buttons buttons: Int
        ) {
            showMessage(
                activity,
                activity.getString(titleResID), activity.getString(messageResID),
                iconResID, requestCode, buttons
            )
        }

        fun showMessage(
            fragment: Fragment,
            titleResID: Int,
            messageResID: Int, @DialogFragmentPresenterImpl.Icon iconResID: Int,
            requestCode: Int, @DialogFragmentPresenterImpl.Buttons buttons: Int
        ) {
            showMessage(
                fragment,
                fragment.getString(titleResID), fragment.getString(messageResID),
                iconResID, requestCode, buttons
            )
        }

        fun showMessage(
            fragment: Fragment,
            titleResID: Int,
            messageResID: Int, @DialogFragmentPresenterImpl.Icon iconResID: Int,
            requestCode: Int, @DialogFragmentPresenterImpl.Buttons buttons: Int,
            params: Bundle?
        ) {
            showMessage(
                fragment,
                fragment.getString(titleResID), fragment.getString(messageResID),
                iconResID, requestCode, buttons, params
            )
        }

        fun isShown(fragment: Fragment, tag: String?): Boolean {
            return DialogFragmentPresenterImpl().isDisplayed(fragment, tag)
        }

        @JvmStatic
        fun isShown(fragment: FragmentActivity, tag: String?): Boolean {
            return DialogFragmentPresenterImpl().isDisplayed(fragment, tag)
        }
    }
}