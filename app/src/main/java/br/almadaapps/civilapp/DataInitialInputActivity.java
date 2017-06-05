package br.almadaapps.civilapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import br.almadaapps.civilapp.domains.User;
import br.almadaapps.civilapp.utils.GeneralMethods;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vinicius-almada on 21/03/17.
 */

public class DataInitialInputActivity extends CommonActivity {
    public static final String TAG = "DataInitial ";
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_initial);
        Log.d(TAG, "onCreate: ");
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_input_data, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_next) {

            EditText etCode = (EditText) findViewById(R.id.et_code);
            String code = etCode.getText().toString();
            if (!code.equals("")) {
                user.setCode(etCode.getText().toString());
                User.writeOnFirebase(user);
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra(LoginActivity.KEY_USER_PARCELABLE, user);
                startActivity(intent);
                finish();
            } else {
                showToast("Insira o código ou sinalize que não tem.", true);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        GeneralMethods.signOutFinish(this, LoginActivity.class);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Bem-vindo ao ECivil App", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void initViews() {
        Log.d(TAG, "initViews: ");
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_input);
        setSupportActionBar(toolbar);
        user = getIntent().getParcelableExtra(LoginActivity.KEY_USER_PARCELABLE);
        CircleImageView civProfile = (CircleImageView) findViewById(R.id.civ_pic_profile);
        TextView tvName = (TextView) findViewById(R.id.tv_name);
        TextView tvEmail = (TextView) findViewById(R.id.tv_email);
        TextView tvNoCode = (TextView) findViewById(R.id.tv_no_code);

        Picasso.with(this).load(user.getProfilePic()).into(civProfile);
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());
        tvNoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setCode("[Sem código]");
                User.writeOnFirebase(user);
                Intent intent = new Intent(DataInitialInputActivity.this, HomeActivity.class);
                intent.putExtra(LoginActivity.KEY_USER_PARCELABLE, user);
                startActivity(intent);
                finish();
            }
        });
    }
}
