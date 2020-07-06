package my.labs.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ItemView extends AppCompatActivity implements View.OnClickListener {
    Button btnCancel;
    Button btnAccept;
    EditText etName, etAuthor, etDescription;
    DB db;
    long id;
    String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);


        btnCancel = findViewById(R.id.iv_btn_cancel);
        btnAccept = findViewById(R.id.iv_btn_accept);
        btnCancel.setOnClickListener(this);
        btnAccept.setOnClickListener(this);

        etName = findViewById(R.id.iv_et_name);
        etAuthor = findViewById(R.id.iv_et_author);
        etDescription = findViewById(R.id.iv_et_description);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        db = new DB(this);
        db.open();

        Intent intent = getIntent();
        action = intent.getAction();

        switch (action){
            case "ADD":
                changeMode(true);
                break;
            case "VIEW":
                id = intent.getLongExtra(DBHelper.COLUMN_ID, 0);
                Cursor c = db.getDataByID(id);
                if(c.moveToFirst()){
                    etName.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_NAME)));
                    etAuthor.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_AUTHOR)));
                    etDescription.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_DESCRIPTION)));
                }else
                    finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.iv_menu_editText:
                changeMode(true);
                break;
            case R.id.iv_menu_remove:
                try{
                    db.removeByID(id);

                    finish();
                }
                catch (Exception ex){
                    Toast.makeText(this, R.string.deleting_failed, Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_btn_accept:
                switch (action) {
                    case "ADD":
                        try {
                            db.addData(etName.getText().toString(), etAuthor.getText().toString(), etDescription.getText().toString());
                        } catch (Exception ex) {
                            Toast.makeText(this, R.string.adding_failed, Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case ("VIEW"):
                        try {
                            db.update(id, etName.getText().toString(), etAuthor.getText().toString(), etDescription.getText().toString());
                        } catch (Exception ex) {
                            Toast.makeText(this, R.string.saving_failed, Toast.LENGTH_SHORT).show();
                        }
                        break;

                }
                setResult(getResources().getInteger(R.integer.RQ_DATA_CHANGES));
                break;
            case R.id.iv_btn_cancel:
                switch (action) {
                    case "ADD":
                        finish();
                        break;
                    case ("VIEW"):
                        Cursor c = db.getDataByID(id);
                        if(c.moveToFirst()){
                            etName.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_NAME)));
                            etAuthor.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_AUTHOR)));
                            etDescription.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_DESCRIPTION)));
                        }
                        break;
                }
                break;
        }
        changeMode(false);
    }

    void changeMode(boolean isPerm){
        if(isPerm){
            btnAccept.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
            etName.setEnabled(true);
            etAuthor.setEnabled(true);
            etDescription.setEnabled(true);
        }else{
            btnAccept.setVisibility(View.INVISIBLE);
            btnCancel.setVisibility(View.INVISIBLE);
            etName.setEnabled(false);
            etAuthor.setEnabled(false);
            etDescription.setEnabled(false);
        }
    }
}