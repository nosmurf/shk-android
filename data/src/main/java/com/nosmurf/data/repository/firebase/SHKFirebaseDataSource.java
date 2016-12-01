package com.nosmurf.data.repository.firebase;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import javax.inject.Inject;

import rx.Observable;

public class SHKFirebaseDataSource implements FirebaseDataSource {

    public static final String TAG = "FirebaseDatabaseSource";

    StorageReference storageReference;

    @Inject
    public SHKFirebaseDataSource() {
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public Observable<Boolean> uploadPhoto(String imagePath) {
        File image = new File(imagePath);
        Uri uri = Uri.fromFile(image);
        // TODO: 01/12/2016 Change "photos" to USER ID
        final StorageReference userPhotosRef = storageReference.child("photos").child(uri.getLastPathSegment());
        return Observable.create(subscriber -> {
            userPhotosRef.putFile(uri)
                    .addOnSuccessListener(task -> {
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                    })
                    .addOnFailureListener(subscriber::onError);
        });
    }
}