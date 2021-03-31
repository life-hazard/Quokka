package com.app.quokka

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle



class Utils {
}

class CompleteTaskDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog? {
        return activity?.let {
            // Builder class for dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Are you sure you want to mark task as complete?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener{ dialog, id ->

                }).setNegativeButton("No", DialogInterface.OnClickListener{ dialog, id ->

                })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

