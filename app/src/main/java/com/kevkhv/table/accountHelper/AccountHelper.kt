package com.kevkhv.table.accountHelper

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kevkhv.table.MainActivity
import com.kevkhv.table.R
import com.kevkhv.table.constants.FirebaseAuthConstants

class AccountHelper(act: MainActivity) {
    private val act = act
    lateinit var sighInClient: GoogleSignInClient
    fun toast(msg:String) = Toast.makeText(act,msg,Toast.LENGTH_LONG).show()
    private val launcher: ActivityResultLauncher<Intent> =
        act.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                toast("Api exception")
            }
        }

    fun signUpWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendEmailVerification(task.result?.user!!)
                        act.uiUpdate(task.result?.user)
                    } else {
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            val exception = task.exception as FirebaseAuthUserCollisionException
//                            Log.d("MyLog", "Exception: ${exception.errorCode}")
                            if (exception.errorCode == FirebaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE) {
                                linkEmailToG(email, password)
                            }
                        } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            val exception =
                                task.exception as FirebaseAuthInvalidCredentialsException
                            if (exception.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL) {
                                toast(FirebaseAuthConstants.ERROR_INVALID_EMAIL)
                            }
                        }
                        if (task.exception is FirebaseAuthWeakPasswordException) {
                            val exception = task.exception as FirebaseAuthWeakPasswordException
                            if (exception.errorCode == FirebaseAuthConstants.ERROR_WEAK_PASSWORD) {
                                toast(FirebaseAuthConstants.ERROR_WEAK_PASSWORD)
                            }
                        }
                    }
                }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    act.uiUpdate(task.result?.user)
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        val exception = task.exception as FirebaseAuthInvalidCredentialsException
                        if (exception.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL) {
                            toast(FirebaseAuthConstants.ERROR_INVALID_EMAIL)
                        } else if (exception.errorCode == FirebaseAuthConstants.ERROR_WRONG_PASSWORD) {
//                                Log.d("MyLog","Exception: ${task.exception}")
                            toast(FirebaseAuthConstants.ERROR_WRONG_PASSWORD)
                        }
                    } else if (task.exception is FirebaseAuthInvalidUserException) {
//                            Log.d("MyLog","Exception: ${task.exception}")
                        val exception = task.exception as FirebaseAuthInvalidUserException
                        if (exception.errorCode == FirebaseAuthConstants.ERROR_USER_NOT_FOUND) {
                            toast(FirebaseAuthConstants.ERROR_USER_NOT_FOUND)
                        }
                    } else if (task.exception is FirebaseException) {
                        toast(act.resources.getString(R.string.not_found_user_error))
                    }
                }
            }
        }
    }

    fun signInWithGoogle() {
        sighInClient = getSignInClient()
        launcher.launch(sighInClient.signInIntent)
    }

    fun signOutGoogleAcc() {
        getSignInClient().signOut()
    }

    private fun getSignInClient(): GoogleSignInClient {           //Запрос клиента из оперционной системы
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(act.getString(R.string.default_web_client_id)).requestEmail().build()
        return GoogleSignIn.getClient(act, gso)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        act.mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                act.uiUpdate(task.result?.user)
                toast("Google signin done")
            } else toast("Google signin error")
        }
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                toast(act.resources.getString(R.string.send_verification_email_done))
            } else {
                toast(act.resources.getString(R.string.send_verification_email_error))
            }
        }
    }

    private fun linkEmailToG(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        if (act.mAuth.currentUser != null) {
            act.mAuth.currentUser?.linkWithCredential(credential)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast(act.resources.getString(R.string.link_done))
                }
            }
        } else {
            toast(act.resources.getString(R.string.enter_to_g))
        }
    }
}