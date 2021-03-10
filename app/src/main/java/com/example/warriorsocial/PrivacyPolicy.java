package com.example.warriorsocial;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warriorsocial.R;

// Displays the privacy policy

public class PrivacyPolicy extends AppCompatActivity {
    // TextView to display string
    public TextView tv_privacy;

    // Text of privacy policy
    public String privacyPolicy = "PRIVACY POLICY\n\n" +
            "Protecting your private information is our priority. This Privacy Policy applies to Warrior Social and governs information collection and usage. By using the Warrior Social Mobile Application, you consent to the information practices described in this statement.\n" +
            " \n" +
            "1. INFORMATION WE COLLECT DIRECTLY FROM YOU \n\n" +
            "We may collect information directly from you. By registering on this application, we will collect information you provide which may include your email address, username, password, uploaded images, or Student Organization associations. Certain information may be available to other users through your profile which includes: username, uploaded images, and Student Organization associations. Other users will also be able to access any information that you provide through the posting features on the discussion forums or in communication with Student Organizations on their event pages. \n" +
            "\n" +
            "2. HOW WE COLLECT INFORMATION\n\n" +
            "INFORMATION THAT IS GIVEN TO US DIRECTLY BY YOU:\n" +
            "Warrior Social will collect the information you provide to us in a number of ways, such as:\n" +
            "\t2.1 When you register an account with us on the Mobile Application. \n" +
            "\t2.2 When you update or make changes to your account.\n" +
            "\t2.3 When you use features of the Mobile Application such as communicating with other users and posting on discussion forums or Student Organization event posts. \n" +
            "\nINFORMATION COLLECTED PASSIVELY/AUTOMATICALLY:\t\n" +
            "Warrior Social will collect your information passively and/or automatically which may include:\n" +
            "\t2.4 Your IP Address, location, usage data, SSAID (Android ID). \n" +
            "\t2.5 Timestamp of interactions with the Mobile Application which may include time of: account creation, account updates, posts on the Discussion Forum, interactions with other users, and posts on the Student Organization event posts.\n" +
            "\n" +
            "3. HOW WE USE YOUR INFORMATION\n\n" +
            "We use the information we collect from you in the following ways:\n" +
            "\t3.1 To operate and maintain your Warrior Social account and to provide you with access to your account. Your email address and password will be used to identify you when you log in to the Mobile Application.\n" +
            "\t3.2 Your account information will be used to identify you with the posts you make on discussion forums and Student Organization event pages. \n" +
            "\t3.3 To provide you with technical support. \n" +
            "\n" +
            "4. HOW WE SHARE INFORMATION WE COLLECT\n\n" +
            "Information collected will be shared with the following:\n" +
            "\t4.1 Third party companies include the data management platform Firebase.\n" +
            "\t4.2 The types of information collected that will be shared includes all information provided to the Mobile Application by the user. This may include, for example, email address, username, password, uploaded images, Student Organization associations, discussion forum posts, or posts a user makes on a Student Organization event page. \n" +
            "\t4.3 This data is shared with Firebase out of a necessity for data hosting and management. \n" +
            "\t4.4 Warrior Social and/or Firebase may disclose your personal information, without notice, if required to do so by law or in the good faith belief that such action is necessary to: (a) conform to the edicts of the law or comply with legal process served on Warrior Social; (b) protect and defend the rights or property of Warrior Social; and/or (c) act under exigent circumstances to protect the personal safety of users of Warrior Social, or the public.\n" +
            "\n" +
            "5.RIGHT TO DELETION\n\n" +
            "Subject to certain exceptions detailed below, after receiving a verifiable request from you, we will:\n" +
            "\t5.1 Delete your personal information from our records.\n" +
            "\t5.2 Direct any service providers to delete your personal information from their records.\n" +
            "The events in which we may not be able to delete your account or personal information includes:\n" +
            "\t5.3 If it is necessary to detect security incidents, protect against malicious, deceptive, fraudulent, or illegal activity. This includes the event in which this information is necessary to prosecute those responsible for that activity. \n" +
            "\t5.4 Exercise free speech, ensure the right of another consumer to exercise their right of free speech, or exercise another right provided for by law.\n" +
            "\t5.5 Comply with an existing legal obligation. \n" +
            "\n" +
            "6. CHANGES TO THIS STATEMENT\n" +
            "Warrior Social reserves the right to change this Privacy Policy from time to time. We will notify you about changes in the way we treat personal information by sending a notice to the primary email address specified in your account, by placing a prominent notice on the Mobile Application, and/or by updating the privacy information detailed on this statement. Your continued use of the Mobile Application and/or services available after such modifications have occurred will constitute your acknowledgement of the modified Privacy Policy as well as your agreement to abide and be bound by that Policy. \n" +
            "\n" +
            "7. CONTACT INFORMATION\n" +
            "Warrior Social welcomes your questions or comments regarding this Privacy Policy. If you believe that Warrior Social has not adhered to this Policy or if you wish to delete your account or have any other issues, please contact us at warriorsocial4110@gmail.com.\n" +
            "\n" +
            "Effective as of Mar 03, 2021\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Attach to layout
        setContentView(R.layout.activity_privacy);

        // Set controls
        tv_privacy = findViewById(R.id.tv_privacy2);

        // Set text to TextView in activity_privacy
        tv_privacy.setText(privacyPolicy);
    }
}
