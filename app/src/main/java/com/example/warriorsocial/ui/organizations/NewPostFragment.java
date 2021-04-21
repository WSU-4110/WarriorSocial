package com.example.warriorsocial.ui.organizations;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.login.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;

public class NewPostFragment extends Fragment {
    private static final String TAG = "NewPostFragment";
    private static final String REQUIRED = "Required";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int RESULT_OK = -1;

    private DatabaseReference mDatabase;
    private DatabaseReference mOrganizationReference;
    private StorageReference mOrganizationStorageReference;

    private FloatingActionButton postFAB;
    private EditText titleEditText;
    private EditText descriptionEditText;

    private Uri mImageUri;
    private ImageView postImageView;
    private Button uploadImageButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_post, container, false);

        titleEditText = root.findViewById(R.id.editTextTitle);
        descriptionEditText = root.findViewById(R.id.editTextDescription);
        postImageView = root.findViewById(R.id.postImageView);
        uploadImageButton = root.findViewById(R.id.uploadImageButton);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        postFAB = getActivity().findViewById(R.id.postFAB);
        postFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

        // Initialize Organization Database
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        currentUserEmail = currentUserEmail.replaceAll("\\.", "_");
        mOrganizationReference = FirebaseDatabase.getInstance().getReference()
                .child("StudentOrganizationPosts/" + currentUserEmail);

        // Initialize Storage
        mOrganizationStorageReference = FirebaseStorage.getInstance().getReference("StudentOrganizationPosts");
    }

    private void submitPost() {
        final String title = titleEditText.getText().toString();
        final String description = descriptionEditText.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(title)) {
            titleEditText.setError(REQUIRED);
            return;
        }

        // Description is required
        if (TextUtils.isEmpty(description)) {
            descriptionEditText.setError(REQUIRED);
            return;
        }

        // Image is optional

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(getContext(), "Posting...", Toast.LENGTH_SHORT).show();

        // Get current user's id
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String postEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        System.out.println("UserId Check: " + userId);
        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(getContext(),
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post (postImage handled later)
                            writeNewPost(description, postEmail, "", title, userId);
                        }

                        setEditingEnabled(true);
                        // Navigate back to the org
                        NavController navController = NavHostFragment.findNavController(NewPostFragment.this);

                        // Attach args
                        Bundle args = new Bundle();
                        String mOrganizationKey = requireArguments().getString(OrganizationProfile.EXTRA_ORGANIZATION_KEY);
                        if (mOrganizationKey == null) {
                            throw new IllegalArgumentException("Must pass EXTRA_ORGANIZATION_KEY");
                        }
                        args.putString(OrganizationProfile.EXTRA_ORGANIZATION_KEY, mOrganizationKey);

                        navController.navigate(R.id.action_newPostFragment_to_organizationProfile, args);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        setEditingEnabled(true);
                    }
                });
    }

    private void setEditingEnabled(boolean enabled) {
        titleEditText.setEnabled(enabled);
        descriptionEditText.setEnabled(enabled);
        if (enabled) {
            postFAB.show();
        } else {
            postFAB.hide();
        }
    }

    private void writeNewPost(String postDescription, String postEmail, String postImage, String postTitle, String uid) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mOrganizationReference.push().getKey();
        StudentOrganizationPost newPost = new StudentOrganizationPost(postDescription, postEmail, postImage, postTitle, uid);
        Map<String, Object> postValues = newPost.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);

        uploadFile(key);
        mOrganizationReference.updateChildren(childUpdates);
    }

    // Open up user's image gallery for them to pick an image
    private void openFileChooser() {
        System.out.println("Inside openFileChooser in OrganizationProfile");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST); // Pass to onActivityResult
    }

    // onActivityResult executes after openFileChooser finishes
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("Inside onActivityResult in OrganizationProfile");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            // Check for the freshest data.
            getActivity().getContentResolver().takePersistableUriPermission(mImageUri, takeFlags);
            postImageView.setImageURI(mImageUri);
        }
    }

    // uploadFile() will create unique key for the newly uploaded file, save it to that particular organization, and upload it to FirebaseStorage
    private void uploadFile(final String key) {
        System.out.println("Inside uploadFile in NewPostFragment");
        if (mImageUri != null) {
            final StorageReference fileReference = mOrganizationStorageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            final ProgressDialog pd = new ProgressDialog(getContext());
            pd.setTitle("Uploading image...");
            pd.show();

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            StorageMetadata snapshotMetadata = taskSnapshot.getMetadata();
                            Toast.makeText(getContext(), "Upload successful!", Toast.LENGTH_SHORT);


                            // String downloadedUrl = mImageUri.toString();
                            Task<Uri> downloadedUrl = fileReference.getDownloadUrl();
                            downloadedUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Update existing imageUrl in the current profile
                                    Map<String, Object> childUpdates = new HashMap<>();
                                    String imageReference = uri.toString();
                                    System.out.println("Image Reference: " + imageReference);
                                    childUpdates.put("postImage", imageReference);
                                    mOrganizationReference.child(key).updateChildren(childUpdates);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            pd.setMessage("Percentage: " + (int)progressPercent + "%");
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Null File!", Toast.LENGTH_SHORT).show();
        }
    }

    // Get correct bitmap from a correctly formatted url (http://
    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;
    }

    // Function returns the filetype of a given file
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}

