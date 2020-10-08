package com.raunakgarments.helper

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseUtil {
    lateinit var mFirebaseDatabase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference
    var mStorage: FirebaseStorage = FirebaseStorage.getInstance()
    var mStorageRef: StorageReference = mStorage.reference

    constructor() {}

    public fun openFbReference(ref: String) {
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase.reference.child(ref)
        connectStorage()
    }
    private fun connectStorage() {
        mStorage = FirebaseStorage.getInstance()
        mStorageRef = mStorage.reference.child("deals_pictures")
    }
}