package com.chulabs.students_alis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;


import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class GroupsListActivityAlis extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_list_alis);

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                StudentsGroup  group= (StudentsGroup) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(GroupsListActivityAlis.this, StudentsGroupActivity_Alis.class);
                intent.putExtra(StudentsGroupActivity_Alis.GROUP_NUMBER, group.getId());
                startActivity(intent);
            }
        };
        ListView listView = (ListView) findViewById(R.id.groups_list);
        listView.setOnItemClickListener(listener);

    }

    public void onGrpAddClick(View view){
        startActivity(new Intent(this, AddStudentsGroupActivity_Alis.class));
    }


    @Override
    protected void onStart() {
        super.onStart();
        getDataFromDB();
        ListView listView = (ListView) findViewById(R.id.groups_list);
        ArrayAdapter<StudentsGroup> adapter = new ArrayAdapter<StudentsGroup>(this, android.R.layout.simple_list_item_1,  getDataFromDB()
        );
        listView.setAdapter(adapter);
    }
    private ArrayList<StudentsGroup> getDataFromDB(){
        ArrayList<StudentsGroup> groups = new ArrayList<StudentsGroup>();

        SQLiteOpenHelper sqliteHelper = new StudebtsDatabaseHelperAlis(this);
        try{
            SQLiteDatabase db = sqliteHelper.getReadableDatabase();
            Cursor cursor = db.query("groups",
                    new String[] {"number", "facultyName", "educationLevel",
                            "contractExistsFlg", "privrladeExistsFlg", "id"},
                    null, null, null,
                    null, "number");
            while (cursor.moveToNext()) {
                groups.add(
                        new StudentsGroup(
                                cursor.getInt(4),
                                cursor.getString(0),
                                cursor.getString(1),
                                cursor.getInt(2),
                                (cursor.getInt(3)>0),
                                (cursor.getInt(4)>0)
                        )
                );
            }
            cursor.close();
            db.close();
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return groups;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.groups_menu, menu);
        String text ="";
        for(StudentsGroup group: StudentsGroup.getGroups()){
            text += group.getNumber() + "\n";
        }

        MenuItem menuItem = menu.findItem(R.id.action_share);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);

        return super.onCreateOptionsMenu(menu);

        // @Override
        //    public boolean onCreateOptionsMenu(Menu menu){
        //
        //        getMenuInflater().inflate(R.menu.groups_menu, menu);
        //
        //        String text = "";
        //        for(StudentsGroup group: StudentsGroup.getGroups()){
        //            text += group.getNumber() + "\n";
        //        }
        //
        //        MenuItem shareItem = menu.findItem(R.id.action_share);
        //        ShareActionProvider myShareActionProvider =
        //                (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        //        Intent myShareIntent = new Intent();
        //        myShareIntent.setAction(Intent.ACTION_SEND);
        //        myShareIntent.putExtra(Intent.EXTRA_TEXT, text);
        //        myShareIntent.setType("text/plain");
        //        startActivity(Intent.createChooser(myShareIntent, getResources().getText(R.string.btn_share)));
        //
        //
        //        return super.onCreateOptionsMenu(menu);
        //    }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_add_group:
                startActivity(
                        new Intent (this, AddStudentsGroupActivity_Alis.class)
                );
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}