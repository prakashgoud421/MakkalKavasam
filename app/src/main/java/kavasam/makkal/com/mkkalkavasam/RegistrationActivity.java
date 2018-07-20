package kavasam.makkal.com.mkkalkavasam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends Activity implements View.OnClickListener {

    private TextView back;
    private CleanableEditText edtUsername;
    private CleanableEditText edtLastname;
    private CleanableEditText mobile;
    private CleanableEditText password;
    private CleanableEditText confPassword;
    private CleanableEditText email;
    private CleanableEditText address;
    private CleanableEditText city;
    private CleanableEditText state;
    private TextView signIn;
    EmailValidator emailValidator;
    private ProgressBar progressBar;
    android.app.FragmentManager fragmentManager;
    private  String userFullname,useremail,username,userpassword, userAdd,phonenumber,userState,userCity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setContentView(R.layout.activity_registration);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findViews();

        emailValidator = new EmailValidator();
       /* loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                RegistrationActivity.this.startActivity(intent);
            }
        });*/

        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.back:
                finish();
                break;

            case R.id.signIn:
                inputsValidation();
                break;

        }
    }

    private void inputsValidation() {


        progressBar.setVisibility(View.VISIBLE);
        userFullname = edtUsername.getText().toString();
        username = edtLastname.getText().toString();
        useremail = email.getText().toString();
        userpassword = password.getText().toString();
        userAdd = address.getText().toString();
        phonenumber = mobile.getText().toString();
        userCity = city.getText().toString();
        userState = state.getText().toString();


        if (!emailValidator.validateEmail(email.getText().toString())) {
            email.setError(getString(R.string.valid_email_error));
        }
        else if(edtUsername.getText().toString().isEmpty()) {

            edtUsername.setError(getString(R.string.valid_user_name));

        }
        else if(password.getText().toString().isEmpty()) {

            password.setError(getString(R.string.valid_password));
        }
        else if(confPassword.getText().toString().isEmpty()) {

            confPassword.setError(getString(R.string.valid_password));
        }
        else if(mobile.getText().toString().isEmpty()||mobile.getText().toString().length()>10) {

            mobile.setError(getString(R.string.valid_mobile_number));
        }
        else{

            signUpUser(userFullname,username,useremail,phonenumber, userpassword,userAdd,userCity,userState);

        }


}

    private void signUpUser(String fullname,String name,String email,String mobile,String password,String address,String state,String city)
    {

        progressBar.setVisibility(View.VISIBLE);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<signInResponse> call = apiService.signInUser(fullname,name,email,mobile, password, address,state, city);
        call.enqueue(new Callback<signInResponse>() {
            @Override
            public void onResponse(Call<signInResponse> call, Response<signInResponse> response) {
                signInResponse signUp = response.body();
                progressBar.setVisibility(View.VISIBLE);

                if( signUp.equals("l")){
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(RegistrationActivity.this,"SignUp Failed.Please enter valid credentials",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    finish();
                    Intent intent =new Intent(RegistrationActivity.this,LoginActivity.class) ;
                    RegistrationActivity.this.startActivity(intent);
                    Toast.makeText(RegistrationActivity.this,"Successfully Registered",Toast.LENGTH_SHORT).show();
                }
                //images = slider.getSliderImages();
                //Toast.makeText(DashboardActivity.context, "img count: " + images.size(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<signInResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(RegistrationActivity.this, "sign Up failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void findViews() {
        back = (TextView)findViewById( R.id.back );
        edtUsername = (CleanableEditText)findViewById( R.id.edt_username );
        edtLastname = (CleanableEditText)findViewById( R.id.edt_lastname );
        mobile = (CleanableEditText)findViewById( R.id.mobile );
        password = (CleanableEditText)findViewById( R.id.password );
        confPassword = (CleanableEditText)findViewById( R.id.conf_password );
        email = (CleanableEditText)findViewById( R.id.email );
        address = (CleanableEditText)findViewById( R.id.address );
        city = (CleanableEditText)findViewById( R.id.city );
        state = (CleanableEditText)findViewById( R.id.state );
        signIn = (TextView)findViewById( R.id.signIn );
    }
}
