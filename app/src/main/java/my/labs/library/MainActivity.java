package my.labs.library;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity{

    ListView listBook;
    DB db;
    SimpleCursorAdapter sadapter;
    MainListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listBook = findViewById(R.id.book_list);
        db = new DB(this);
        db.open();
        listener = new MainListener();

        String[] from = {DBHelper.COLUMN_NAME, DBHelper.COLUMN_AUTHOR};
        int[] to = {R.id.i_name, R.id.i_author};

        sadapter = new SimpleCursorAdapter(this, R.layout.item, db.getAllData(), from, to, 0);
        listBook.setAdapter(sadapter);

        listBook.setOnItemClickListener(listener);
        listBook.setOnItemLongClickListener(listener);
        listBook.setOnItemSelectedListener(listener);
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.m_add:
                Intent intent = new Intent(this, ItemView.class);
                intent.setAction("ADD");
                startActivityForResult(intent, getResources().getInteger(R.integer.RQ_DATA_CHANGES));
                sadapter.notifyDataSetChanged();
                break;
            case R.id.m_remove:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == getResources().getInteger(R.integer.RQ_DATA_CHANGES) && requestCode == resultCode)
        {
            Cursor newCursor = db.getAllData();
            sadapter.changeCursor(newCursor);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class MainListener implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemLongClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, ItemView.class);
            intent.setAction("VIEW");
            intent.putExtra(DBHelper.COLUMN_ID, id);
            startActivityForResult(intent, getResources().getInteger(R.integer.RQ_DATA_CHANGES));
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            return false;
        }
    }
}