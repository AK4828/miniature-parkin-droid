package com.meruvian.pxc.selfservice.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageAppication;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.event.LoginEvent;
import com.meruvian.pxc.selfservice.job.LoginManualFXPCJob;
import com.meruvian.pxc.selfservice.job.LoginManualJob;
import com.meruvian.pxc.selfservice.task.RequestTokenFxpc;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;
import com.meruvian.pxc.selfservice.util.SocialAuthenticationUtils;
import com.path.android.jobqueue.JobManager;

import org.meruvian.midas.core.defaults.DefaultActivity;
import org.meruvian.midas.core.service.TaskService;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by akm on 26/02/16.
 */
public class FXPCLoginActvity extends AppCompatActivity implements TaskService {

    @Bind(R.id.button_login) Button submit;
    @Bind(R.id.edit_username) TextView username;
    @Bind(R.id.edit_password) TextView password;
    @Bind(R.id.login_progress) View loginProgress;

    private ProgressDialog progressDialog;

    private JobManager jobManager;
    private SharedPreferences preferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_url, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_setup_parameter){
            openDialogSetupParameter();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fxpc);
        preferences = getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        EventBus.getDefault().register(this);
        SharedPreferences.Editor editor = preferences.edit();

        if (!(preferences.getBoolean("has_url_point", false))) {
            editor.putString("server_url_point", SignageVariables.SERVER_URL);

            editor.commit();
        }
        editor.putBoolean("has_url_point", true);
        editor.putString("login status", "fxpc user");
        editor.commit();

        jobManager = SignageAppication.getInstance().getJobManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AuthenticationUtils.getCurrentAuthentication() != null) {
            goToMainActivity();
        }
        if (SocialAuthenticationUtils.getCurrentAuthentication() != null) {
            goToMainActivity();
        }
    }

    @OnClick(R.id.button_login)
    public void submitLogin(Button button) {
        if (username.getText()==null && password.getText()==null) {
            Toast.makeText(this, "Username or Password is empty", Toast.LENGTH_SHORT).show();
        } else {
            LoginManualFXPCJob loginJob = new LoginManualFXPCJob(username.getText().toString(), password.getText().toString());
            jobManager.addJobInBackground(loginJob);
        }
    }

    @OnClick(R.id.button_login_PXC)
    public void onPXCClicked() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


    public void onEventMainThread(LoginEvent.DoLogin doLogin) {
        submit.setVisibility(View.GONE);
        loginProgress.setVisibility(View.VISIBLE);
        username.setEnabled(false);
        password.setEnabled(false);
    }

    private void goToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void onEventMainThread(LoginEvent.LoginSuccess loginSuccess) {

        goToMainActivity();
    }
    public void onEventMainThread(LoginEvent.LoginFailed loginFailed) {
        loginProgress.setVisibility(View.GONE);
        submit.setVisibility(View.VISIBLE);
        username.setEnabled(true);
        password.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    private void openDialogSetupParameter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Setup Parameter");

        LayoutInflater inflater = this.getLayoutInflater();
        View convertView = inflater.inflate(R.layout.setup_parameter, null);

        builder.setView(convertView);

        final EditText server = (EditText) convertView.findViewById(R.id.edit_server);

        server.setText(preferences.getString("server_url_point", ""));

        builder.setCancelable(false);
        builder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("server_url_point", server.getText().toString());
                editor.commit();

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    @Override
    public void onExecute(int code) {
        progressDialog = new ProgressDialog(this);
        if (code == SignageVariables.FXPC_REQUEST_ACCESS) {
            progressDialog.setMessage("Connecting...");
        } else if (code == SignageVariables.FXPC_REQUEST_TOKEN_TASK) {
            progressDialog.setMessage("Signing in...");
        }
        progressDialog.show();
    }

    @Override
    public void onSuccess(int code, Object result) {
        progressDialog.dismiss();
        if (result != null) {
            if(code == SignageVariables.FXPC_REQUEST_TOKEN_TASK) {
                String results = (String) result;
                if (result != null && !"".equalsIgnoreCase(results)) {

                    goToMainActivity();

                }
            } else if(code == SignageVariables.FXPC_REQUEST_ACCESS) {
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", result.toString());
                intent.setData(Uri.parse(result.toString()));
                startActivityForResult(intent, SignageVariables.FXPC_REQUEST_ACCESS);

            }
        } else {
            Toast.makeText(this, getString(R.string.failed_recieve), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancel(int code, String message) {

    }

    @Override
    public void onError(int code, String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        progressDialog.dismiss();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SignageVariables.FXPC_REQUEST_ACCESS) {
                new RequestTokenFxpc(this, this).execute(parseCode(data));
            }
        }
    }

    private String parseCode(Intent data) {
        Uri uri = data.getData();
        if (uri != null && uri.toString().startsWith(SignageVariables.FXPC_CALLBACK)) {
            String code = uri.getQueryParameter("code");
            if (code != null && !"".equalsIgnoreCase(code)) {
                return code;
            }
        }

        return "null";
    }
}
