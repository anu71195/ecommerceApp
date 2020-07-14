package com.raunakgarments

import android.app.Activity
import android.util.Log.d
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class Authentication {
    var mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var mAuthListener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener {}
    var mFirebaseDatabase = FirebaseDatabase.getInstance()
    var mDatabaseReference = mFirebaseDatabase.getReference()
    private var RC_SIGN_IN = 123
    private lateinit var caller: Activity
    var isAdmin = false
    var userId = ""
    lateinit var navView: NavigationView

    constructor(caller: Activity, navView: NavigationView) {
        this.caller = caller
        this.navView = navView
        setAdminOptionDrawerVisibility(false)
        mFirebaseAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
            if (mFirebaseAuth.currentUser == null) {
                signIn()
                Toast.makeText(this.caller.baseContext, "Welcome Back", Toast.LENGTH_LONG).show()
            }
            if (mFirebaseAuth.currentUser != null) {
                this.userId = mFirebaseAuth.uid.toString()
                checkAdmin(this.userId)
            }
        }
    }

    fun checkAdmin(userId: String) {
        this.isAdmin = false
        var adminRef = mDatabaseReference.child("administrators").child(userId)
        var childEventListener = object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                isAdmin = true
                d("Admin", "You are an administrator")
//                ProductActivityNew().reloadMenu()
                setAdminOptionDrawerVisibility(true)
            }
        }
        adminRef.addChildEventListener(childEventListener)
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
                attachListener()
            }
        detachListener()
        setAdminOptionDrawerVisibility(false)
    }

    fun attachListener() {
        mFirebaseAuth.addAuthStateListener(mAuthListener)
    }

    fun detachListener() {
        mFirebaseAuth.removeAuthStateListener(mAuthListener)
    }

    fun setAdminOptionDrawerVisibility(isVisible: Boolean) {
        this.navView.menu.findItem(R.id.actionAdmin).isVisible = isVisible
    }

}