package com.illicitintelligence.android.foodfinder.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.illicitintelligence.android.foodfinder.R;
import com.illicitintelligence.android.foodfinder.util.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {


    private String TAG = "TAG_X";
    private FirebaseAuth mAuth;

    @BindView(R.id.email_address_edittext)
    EditText email_address_singin;
    @BindView(R.id.signup_email_address_edittext)
    EditText email_address_signup;
    @BindView(R.id.password)
    EditText password_signin;
    @BindView(R.id.signup_password)
    EditText password_signup;
    @BindView(R.id.signup_password_verify)
    EditText password_verify_signup;
    @BindView(R.id.innerlayout_constraint)
    ConstraintLayout inner_layout_constraint;

    private boolean signingIn = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            startMainActivity();
        }
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);

    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean badEmail(String email){
        return email.length()==0||!email.contains("@");
    }

    private boolean badPassword(String password){
        return password.length()==0;
    }

    private boolean comparePasswords(@NonNull String password1, String password2){
        return password1.equals(password2);
    }

    @OnClick(R.id.signup_button)
    public void signUp(View view){
        if(badEmail(email_address_signup.getText().toString().trim())){
            Toast.makeText(this.getBaseContext(),"Input a valid email",Toast.LENGTH_SHORT).show();
        }else if(badPassword(password_signup.getText().toString())){
            Toast.makeText(this.getBaseContext(),"Input a password",Toast.LENGTH_SHORT).show();
        }else if(!comparePasswords(password_signup.getText().toString(),password_verify_signup.getText().toString())){
            Toast.makeText(this.getBaseContext(),"Password does not match the verification",Toast.LENGTH_SHORT).show();
        }else{
            signUpNewUser(new User(email_address_signup.getText().toString().trim(),
                    password_signup.getText().toString()));
        }
    }

    private void signUpNewUser(User user){
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @OnClick(R.id.login_button)
    public void login(View view){
        if(badEmail(email_address_singin.getText().toString().trim())){
            Toast.makeText(this.getBaseContext(),"Input a valid email",Toast.LENGTH_SHORT).show();
            signingIn = false;
        }else if(badPassword(password_signin.getText().toString())) {
            Toast.makeText(this.getBaseContext(), "Input a password", Toast.LENGTH_SHORT).show();
            signingIn = false;
        }else if(signingIn){
            //do nothing
        }else{
            Log.d(TAG, "login: ");
            loginUser(new User(email_address_singin.getText().toString().trim(),
                    password_signin.getText().toString()));
            signingIn = true;
        }
    }

    private void loginUser(User user){
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            startMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @OnClick(R.id.signup_textview)
    public void showSignUp(View view){
        inner_layout_constraint.setVisibility(View.VISIBLE);
        signingIn = true;
    }

    public void hideLayout(){
        inner_layout_constraint.setVisibility(View.GONE);
    }

    public boolean layoutIsVisible(){
        return inner_layout_constraint.getVisibility()==View.VISIBLE;
    }

    @Override
    public void onBackPressed() {
        if(layoutIsVisible()){
            hideLayout();
        }else {
            super.onBackPressed();
        }
    }
}
