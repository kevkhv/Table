package com.kevkhv.table.dialoghelper

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.kevkhv.table.MainActivity
import com.kevkhv.table.R
import com.kevkhv.table.accountHelper.AccountHelper
import com.kevkhv.table.databinding.SignDialogBinding

class DialogHelper(act: MainActivity) {
    private val act = act
    private val accHelper = AccountHelper(act)

    fun createSignDialog(index: Int) {
        val builder = AlertDialog.Builder(act)
        val binding = SignDialogBinding.inflate(act.layoutInflater)
        val view = binding.root
        builder.setView(view)                                 // установить разметку в диалоговое окно
        setDialogState(index, binding)
        val dialog =
            builder.create()                         // создать диалоговое окно c помощью билдера

        binding.btSignUpIn.setOnClickListener {
            setOnClickSignUpIn(index, binding, dialog)
        }
        binding.btForgetP.setOnClickListener {
            setOnClickResetPassword(binding, dialog)
        }
        dialog.show()                                         // отобразить диалоговое окно
    }

    // лисенер забыли пароль
    private fun setOnClickResetPassword(binding: SignDialogBinding, dialog: AlertDialog?) {
        with(binding) {
            if (edSignEmail.text.isNotEmpty()) {
                act.mAuth.sendPasswordResetEmail(edSignEmail.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) Toast.makeText(
                            act,
                            R.string.email_reset_password_was_sent,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                dialog?.dismiss()
            } else {
                tvDialogMessage.visibility = View.VISIBLE
            }
        }
    }

    // выбор состояния диалога в зависиомти от действия(вход или регистрация)
    private fun setDialogState(index: Int, binding: SignDialogBinding) {
        with(binding) {
            if (index == DialogConst.SIGN_UP_STATE) {
                tvSignTitle.text = act.resources.getString(R.string.ac_sign_up)
                btSignUpIn.text = act.resources.getString(R.string.sign_up_action)
            } else {
                tvSignTitle.text = act.resources.getString(R.string.ac_sign_in)
                btSignUpIn.text = act.resources.getString(R.string.sign_in_action)
                btForgetP.visibility = View.VISIBLE
            }
        }
    }

    // лисенер на кнопку войти в зависимости от диалога(вход или регистрация)
    private fun setOnClickSignUpIn(index: Int, binding: SignDialogBinding, dialog: AlertDialog?) {
        dialog?.dismiss()                                       // скрыть/сломать диалоговое окно
        with(binding) {
            if (index == DialogConst.SIGN_UP_STATE) {
                accHelper.signUpWithEmail(
                    edSignEmail.text.toString(),
                    edSignPassword.text.toString()
                )
            } else {
                accHelper.signInWithEmail(
                    edSignEmail.text.toString(),
                    edSignPassword.text.toString()
                )
            }
        }
    }
}
