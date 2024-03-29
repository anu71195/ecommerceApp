package com.raunakgarments

import android.app.Activity
import android.util.Log.d
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.raunakgarments.helper.FirebaseUtil
import com.raunakgarments.model.Profile

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
                if (!mFirebaseAuth.currentUser?.isEmailVerified!!) {
                    d("userRegistration", mFirebaseAuth.uid.toString())

                    var firebaseUtil = FirebaseUtil()
                    firebaseUtil.openFbReference("userProfile/")

                    firebaseUtil.mDatabaseReference.child(userId)
                        .addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {}

                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (!snapshot.exists()) {
                                    mFirebaseAuth.currentUser?.sendEmailVerification()
                                    d("userRegistration", mFirebaseAuth.uid.toString())
                                    var profile = Profile()
                                    profile.email = mFirebaseAuth.currentUser?.email.toString()
                                    profile.userName =
                                        mFirebaseAuth.currentUser?.displayName.toString()
                                    profile.id =  mFirebaseAuth.uid.toString()
                                    firebaseUtil.mDatabaseReference.child(userId).setValue(profile)
                                }
                            }
                        })
                }
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
                reloadMenu()
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
        this.navView.menu.findItem(R.id.menu_main_actionAdmin).isVisible = isVisible
    }

    fun reloadMenu() {
        this.caller.invalidateOptionsMenu()
    }


}