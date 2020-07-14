package com.raunakgarments

import android.app.Activity
import android.util.Log.d
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class Login {
    var mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var mAuthListener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener {}
    private var RC_SIGN_IN = 123
    private lateinit var caller: Activity

    constructor(caller: Activity) {
        this.caller = caller
        mFirebaseAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
            if (mFirebaseAuth.currentUser == null) {
                signIn()
                Toast.makeText(this.caller.baseContext, "Welcome Back", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun signIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        // Create and launch sign-in intent
        this.caller.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    fun signOut() {
        AuthUI.getInstance()
            .signOut(caller)
            .addOnCompleteListener {
                d("Log out", "User logged out")
                detachListener()
            }
        attachListener()
    }
    fun attachListener() {
        mFirebaseAuth.addAuthStateListener(mAuthListener)
    }

    fun detachListener() {
        mFirebaseAuth.removeAuthStateListener(mAuthListener)
    }

}