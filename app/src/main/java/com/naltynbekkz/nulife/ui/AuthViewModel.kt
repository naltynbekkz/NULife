package com.naltynbekkz.nulife.ui

import android.content.Intent
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthViewModel @ViewModelInject constructor(
    private val auth: FirebaseAuth,
    private val mGoogleSignInClient: GoogleSignInClient
) : ViewModel() {

    fun getSignInIntent(): Intent {
        return mGoogleSignInClient.signInIntent
    }

    fun signOut() {
        mGoogleSignInClient.signOut()
    }

    fun signIn(data: Intent?, success: () -> Unit, fail: () -> Unit) {
        val account = GoogleSignIn.getSignedInAccountFromIntent(data)
            .getResult(ApiException::class.java)

        if (account!!.email!!.contains("@nu.edu.kz")) {
            auth.signInWithCredential(
                GoogleAuthProvider.getCredential(
                    account.idToken,
                    null
                )
            ).addOnSuccessListener {
                mGoogleSignInClient
                    .signOut()
                    .addOnSuccessListener {
                        success()
                    }
            }.addOnFailureListener {
                fail()
            }
        } else {
            fail()
        }
    }
}