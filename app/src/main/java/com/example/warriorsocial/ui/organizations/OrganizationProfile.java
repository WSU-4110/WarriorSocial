package com.example.warriorsocial.ui.organizations;
import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.home.CalendarEvent;
import com.example.warriorsocial.ui.home.EventDetailFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URL;
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

    private Uri mImageUri;

    private ImageView mImageView;

    private FirebaseRecyclerAdapter<StudentOrganizationPost, OrganizationPostViewHolder> mAdapter;
    private LinearLayoutManager mManager;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.activity_orgs, container, false);

        mImageView = root.findViewById(R.id.tv_image);
        recyclerView = root.findViewById(R.id.recycler_view_posts);

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
                if(dataSnapshot.exists()) {
                    // Get TextViews and ImageViews from R
                    ImageView organizationImage = getActivity().findViewById(R.id.tv_image);

                    TextView organizationName = getActivity().findViewById(R.id.tv_name);
                    TextView organizationEmail = getActivity().findViewById(R.id.tv_email);
                    TextView organizationPhone = getActivity().findViewById(R.id.tv_phone);
                    TextView organizationPresidentName = getActivity().findViewById(R.id.tv_president_name);
                    TextView organizationVicePresidentName = getActivity().findViewById(R.id.tv_vice_president_name);

                    // Get StudentOrganization object from dataSnapshot (handled by firebase using getters and setters)
                    StudentOrganization studentOrganization = dataSnapshot.getValue(StudentOrganization.class);
                    System.out.println("Organization Name: " + studentOrganization.getOrganizationName());

                    organizationImage.setImageURI(Uri.parse(studentOrganization.getOrganizationImageUrl()));

                    organizationName.setText(studentOrganization.getOrganizationName());
                    organizationEmail.setText(studentOrganization.getOrganizationEmail());
                    organizationPhone.setText(studentOrganization.getOrganizationDescription());
                    organizationPresidentName.setText(studentOrganization.getOrganizationDescription());
                    organizationVicePresidentName.setText(studentOrganization.getOrganizationEmail());

                    // Set the recyclerView according to that particular SO's posts
                    System.out.println("Database reference: " + "StudentOrganizationPosts/" + studentOrganization.getOrganizationEmail());
                    Query query = mOrganizationPostsReference.child("StudentOrganizationPosts/" + studentOrganization.getOrganizationEmail());
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
                            //TODO: Create clickable post for comments?
                            viewHolder.bindToPost(model);
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
            StorageReference fileReference = mOrganizationStorageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            final ProgressDialog pd = new ProgressDialog(getContext());
            pd.setTitle("Uploading image...");
            pd.show();

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Upload successful!", Toast.LENGTH_SHORT);

                            // Update existing imageUrl in the current profile
                            Map<String, Object> childUpdates = new HashMap<>();
                            String downloadedUrl = mImageUri.toString();
                            System.out.println("URL: " + downloadedUrl);
                            childUpdates.put("organizationImageUrl", downloadedUrl);
                            mOrganizationReference.updateChildren(childUpdates);
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

    // Open up user's image gallery for them to pick an image
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST); // Pass to onActivityResult
    }

    // onActivityResult executes after openFileChooser finishes
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            mImageView.setImageURI(mImageUri);
            uploadFile();
        }
    }
}
