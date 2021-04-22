package com.example.warriorsocial.ui.organizations;
import com.example.warriorsocial.BottomActivity;
import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.home.CalendarEvent;
import com.example.warriorsocial.ui.home.EventDetailFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrganizationProfile extends Fragment {

    private static final String TAG = "OrganizationProfile";

    private static final int PICK_IMAGE_REQUEST = 1;
    public static final String EXTRA_ORGANIZATION_KEY = "organization_key";
    private static final int RESULT_OK = -1;

    //Organization reference + OrganizationListener + organization key
    private StorageReference mOrganizationStorageReference;
    private DatabaseReference mOrganizationPostsReference;
    private DatabaseReference mOrganizationReference;
    private ValueEventListener mOrganizationListener;
    private String mOrganizationKey;

    // Edit Button
    private ImageView editButtonImageView;

    private Uri mImageUri;

    private ImageView mImageView;

    private FirebaseRecyclerAdapter<StudentOrganizationPost, OrganizationPostViewHolder> mAdapter;
    private LinearLayoutManager mManager;
    private RecyclerView recyclerView;

    private FloatingActionButton newPostFAB;
    public static final boolean BOOLEAN_DEFAULT = false;



    // For sending notifications
    public static final String NOTIFICATION_S = "fromSettingsFragment";
    View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        root = inflater.inflate(R.layout.activity_orgs, container, false);

        mImageView = root.findViewById(R.id.tv_image);
        recyclerView = root.findViewById(R.id.recycler_view_posts);
        newPostFAB = root.findViewById(R.id.newPostFAB);
        editButtonImageView = root.findViewById(R.id.edit_organization_profile_button);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        Boolean account = sharedPreferences.getBoolean("AccountType",false);

        if(account){
            newPostFAB.setVisibility(View.VISIBLE);
        }
        else{
            newPostFAB.setVisibility(View.INVISIBLE);
        }

        //Back button from fragment functionality
        //https://stackoverflow.com/questions/40395067/android-back-button-not-working-in-fragment/52331709
        setHasOptionsMenu(true);

        return root;
    }

    // onViewCreated (fires after onCreateView): 1st: Get Key from passed in args, 2nd: Get database reference using the key
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup Layout Manager
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mManager);

        // Get StudentOrganization key from arguments
        mOrganizationKey = requireArguments().getString(EXTRA_ORGANIZATION_KEY);
        if (mOrganizationKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_ORGANIZATION_KEY");
        }

        // Initialize Organization Database
        mOrganizationReference = FirebaseDatabase.getInstance().getReference()
                .child("StudentOrganizations/" + mOrganizationKey);

        // Initialize Posts Database (in a way)
        mOrganizationPostsReference = FirebaseDatabase.getInstance().getReference();
        // Initialize Storage
        mOrganizationStorageReference = FirebaseStorage.getInstance().getReference("StudentOrganizationPosts");
    }

    @Override
    public void onStart() {
        super.onStart();

        System.out.println("Inside onStart in OrganizationProfile");

        // Connect editOrg functionality
        editButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigation to NewPostFragment (Could pass in some args here)
                NavController navController = NavHostFragment.findNavController(OrganizationProfile.this);

                // Attach the org profile information to
                Bundle args = new Bundle();
                args.putString(OrganizationProfile.EXTRA_ORGANIZATION_KEY, mOrganizationKey);

                navController.navigate(R.id.action_organizationProfile_to_editOrganizationProfile, args);
            }
        });

        // Connect newPostFAB functionality
        newPostFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigation to NewPostFragment (Could pass in some args here)
                NavController navController = NavHostFragment.findNavController(OrganizationProfile.this);

                // Attach the org profile information to
                Bundle args = new Bundle();
                args.putString(OrganizationProfile.EXTRA_ORGANIZATION_KEY, mOrganizationKey);

                navController.navigate(R.id.action_organizationProfile_to_newPostFragment, args);
            }
        });

        // Connect pseudo-buttons functionality
        RelativeLayout postsFakeButton = getActivity().findViewById(R.id.posts_fake_button);
        RelativeLayout infoFakeButton = getActivity().findViewById(R.id.info_fake_button);

        final RelativeLayout relativeLayoutPosts = getActivity().findViewById(R.id.relativelayout_posts);
        final RelativeLayout relativeLayoutInfo = getActivity().findViewById(R.id.relativelayout_info);

        final TextView postsLabel = getActivity().findViewById(R.id.posts_label);
        final TextView infoLabel = getActivity().findViewById(R.id.info_label);

        postsFakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayoutPosts.setVisibility(View.VISIBLE);
                relativeLayoutInfo.setVisibility(View.GONE);
                postsLabel.setTypeface(null, Typeface.BOLD);
                infoLabel.setTypeface(null, Typeface.NORMAL);
            }
        });

        infoFakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayoutPosts.setVisibility(View.GONE);
                relativeLayoutInfo.setVisibility(View.VISIBLE);
                postsLabel.setTypeface(null, Typeface.NORMAL);
                infoLabel.setTypeface(null, Typeface.BOLD);
            }
        });

        // Connect Image to upload new image
        ImageView organizationImage = getActivity().findViewById(R.id.tv_image);
        organizationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Check if organization is correct in shared preferences before allowing image to be changed
                openFileChooser();
            }
        });

        // Add value event listener to the StudentOrganization defining onDataChange and onCancelled for the listener
        ValueEventListener OrganizationListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get StudentOrganization object and use the values to update the UI
                if (dataSnapshot.exists()) {
                    // Get TextViews and ImageViews from R
                    final ImageView organizationImage = getActivity().findViewById(R.id.tv_image);

                    TextView organizationName = getActivity().findViewById(R.id.tv_name);
                    TextView organizationEmail = getActivity().findViewById(R.id.tv_email);
                    TextView organizationPhone = getActivity().findViewById(R.id.tv_phone);
                    TextView organizationPresidentName = getActivity().findViewById(R.id.tv_president_name);
                    TextView organizationVicePresidentName = getActivity().findViewById(R.id.tv_vice_president_name);
                    TextView organizationDescription = getActivity().findViewById(R.id.tv_address);

                    // Get StudentOrganization object from dataSnapshot (handled by firebase using getters and setters)
                    final StudentOrganization studentOrganization = dataSnapshot.getValue(StudentOrganization.class);

                    System.out.println("Organization Name: " + studentOrganization.getOrganizationName());
                    Uri testingUri = Uri.parse(studentOrganization.getOrganizationImageUrl());
                    System.out.println("Got testingUri" + testingUri.toString());
                    final Bitmap[] bm = {null};
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            bm[0] = getImageBitmap(studentOrganization.getOrganizationImageUrl());
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    organizationImage.setImageBitmap(bm[0]);

                                }
                            });
                        }
                    });
                    thread.start();

                    System.out.println("Past the setting of the ImageURI");

                    organizationName.setText(studentOrganization.getOrganizationName());
                    organizationEmail.setText(studentOrganization.getOrganizationEmail());
                    organizationPhone.setText(studentOrganization.getOrganizationPhoneNumber());
                    organizationPresidentName.setText(studentOrganization.getOrganizationPresident());
                    organizationVicePresidentName.setText(studentOrganization.getOrganizationVicePresident());
                    organizationDescription.setText(studentOrganization.getOrganizationDescription());

                    // Set the recyclerView according to that particular SO's posts
                    System.out.println("Database reference: " + "StudentOrganizationPosts/" + studentOrganization.getOrganizationEmail());
                    Query query = mOrganizationPostsReference.child("StudentOrganizationPosts/" + studentOrganization.getOrganizationEmail().toLowerCase());
                    // Setting up FirebaseRecyclerOptions be based off class:  "StudentOrganizationsPost"
                    FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<StudentOrganizationPost>()
                            .setQuery(query, StudentOrganizationPost.class)
                            .build();
                    //Setting up the FirebaseRecyclerAdapter to work with StudentOrganizationPost as a model and OrganizationPostViewHolder to construct them
                    System.out.println("Setup mAdapter");
                    mAdapter = new FirebaseRecyclerAdapter<StudentOrganizationPost, OrganizationPostViewHolder>(options) {
                        @Override
                        public OrganizationPostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                            System.out.println("inside onCreateViewHolder in OrganizationProfile");
                            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                            return new OrganizationPostViewHolder(inflater.inflate(R.layout.student_organization_post_card, viewGroup, false));
                        }

                        @Override
                        protected void onBindViewHolder(OrganizationPostViewHolder viewHolder, int position, final StudentOrganizationPost model) {
                            System.out.println("inside onBindViewHolder in OrganizationProfile");
                            // Get specific post's reference using position
                            final DatabaseReference specificPostRef = getRef(position);
                            final String postKey = specificPostRef.getKey();

                            // Determine if the current user has liked this post and set UI accordingly
                            // TODO: Get Images for Liked and not yet liked
                            if (model.likes.containsKey(getUid())) {
                                viewHolder.post_likes_image.setImageResource(R.drawable.ic_baseline_thumb_up_24);
                            } else {
                                viewHolder.post_likes_image.setImageResource(R.drawable.ic_baseline_thumb_up_outline_24);
                            }

                            //TODO: Create clickable post for comments?
                            Handler h = new Handler(); // Needed to do bitmap get in a background thread
                            View.OnClickListener likeClickListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String postUserEmailProcessed = model.postEmail.replaceAll("\\.", "_");
                                    postUserEmailProcessed = postUserEmailProcessed.toLowerCase();
                                    DatabaseReference postRef = mOrganizationPostsReference.child("StudentOrganizationPosts/"
                                            + postUserEmailProcessed + "/" + postKey);

                                    // Update database
                                    onLikeClicked(postRef);
                                }
                            };
                            viewHolder.bindToPost(model, likeClickListener, h);
                        }
                    };
                    System.out.println("set recyclerView to mAdapter");
                    mAdapter.startListening();
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting StudentOrganizationPost failure, log an error
                Log.w(TAG, "loadStudentOrganizationPost:onCancelled", databaseError.toException());
                Toast.makeText(getContext(), "Failed to load StudentOrganizationPost.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mOrganizationReference.addValueEventListener(OrganizationListener);

        // Keep copy of StudentOrganizationPostListener so we can stop it later.
        mOrganizationListener = OrganizationListener;
    }

    @Override
    public void onStop() {
        super.onStop();
        // Remove StudentOrganizationPost value event listener
        if (mOrganizationListener != null) {
            mOrganizationReference.removeEventListener(mOrganizationListener);
        }
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    // Function returns the filetype of a given file
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    // uploadFile() will create unique key for the newly uploaded file, save it to that particular organization, and upload it to FirebaseStorage
    private void uploadFile() {
        System.out.println("Inside uploadFile in OrganizationProfile");
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
                                    childUpdates.put("organizationImageUrl", imageReference);
                                    mOrganizationReference.updateChildren(childUpdates);
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
                            pd.setMessage("Percentage: " + (int) progressPercent + "%");
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Null File!", Toast.LENGTH_SHORT).show();
        }
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
            mImageView.setImageURI(mImageUri);
            uploadFile();
        }
    }

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

    private void onLikeClicked(DatabaseReference postRef) {
        System.out.println("onLikeClicked");
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                StudentOrganizationPost SOPost = mutableData.getValue(StudentOrganizationPost.class);
                if (SOPost == null) {
                    return Transaction.success(mutableData);
                }

                if (SOPost.likes.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    SOPost.likeCount = SOPost.likeCount - 1;
                    SOPost.likes.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    SOPost.likeCount = SOPost.likeCount + 1;
                    SOPost.likes.put(getUid(), true);

                    // Makes sure that a SO user exists and that the post id is the same as the current user
                    if ((SOPost.uid != null) && SOPost.uid.equals(getUid())) {
                        // For sending notifications to user
                        // Need to figure out how to send notifications when another user likes
                        // when does the users screen update from the database?
                        createNotificationChannels();
                        BottomActivity.getInstance().sendNotification(root);
                    }
                }

                // Set value and report transaction success
                mutableData.setValue(SOPost);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed,
                                   DataSnapshot currentData) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    // For creating notifications
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_S,
                    "Notification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setDescription("User Notification");

            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

}
