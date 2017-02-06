package vilq.laptopcpucompare;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public DatabaseHelper mDBHelper;
    public DatabaseHelper mDBHelper2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new DatabaseHelper(this);
        mDBHelper2 = new DatabaseHelper(this);

        //Check exists database
        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        if(false == database.exists()) {
            mDBHelper.getReadableDatabase();
            //Copy db
            if(mDBHelper.copyDatabase(this)) {
                Toast.makeText(this, "Copy database succes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Button compare = (Button) findViewById(R.id.button);

        List<String> listaIdProc = new ArrayList<String>();
        List<String> listaProc = new ArrayList<String>();
        List<String> rodzinaProc = new ArrayList<String>();
        String polaTabeli[]={"id_procesora","nazwa_procesora"};
        String polaTabeli2[]={"nazwa_rodziny"};

        mDBHelper.openDatabase();
        Cursor cursor = mDBHelper.mDatabase.query("lista_procesorow",polaTabeli,null,null,null,null,null);

        cursor.moveToFirst();

        int lengthCursor = cursor.getCount();

        for(int i=0;i<lengthCursor;i++)
        {
            listaIdProc.add(cursor.getString(0));
            listaProc.add(cursor.getString(1));
            cursor.moveToNext();
        }

        mDBHelper.closeDatabase();

        mDBHelper2.openDatabase();
        Cursor cursor2 = mDBHelper2.mDatabase.query("rodziny_procesorow",polaTabeli2,null,null,null,null,null);
        cursor2.moveToFirst();
        int lengthCursor2 = cursor2.getCount();
        for(int i=0;i<lengthCursor2;i++)
        {
            rodzinaProc.add(cursor2.getString(0));
            cursor2.moveToNext();
        }
        mDBHelper2.closeDatabase();

        final ArrayAdapter<String> stringArrayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, rodzinaProc);
        final Spinner spinner = (Spinner)  findViewById(R.id.spinner);
        spinner.setAdapter(stringArrayAdapter);
        final Spinner spinner3 = (Spinner)  findViewById(R.id.spinner3);
        spinner3.setAdapter(stringArrayAdapter);

        final Spinner spinner2 = (Spinner)  findViewById(R.id.spinner2);
        final Spinner spinner4 = (Spinner)  findViewById(R.id.spinner4);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String text = spinner.getSelectedItem().toString();

                List<String> wybranaRodzina = new ArrayList<String>();

                mDBHelper.openDatabase();
                Cursor cursor4 = mDBHelper.mDatabase.rawQuery("select nazwa_procesora from lista_procesorow,rodziny_procesorow where lista_procesorow.id_rodziny = rodziny_procesorow.id_rodziny and rodziny_procesorow.nazwa_rodziny like '%"+ text +"%'",null);
                cursor4.moveToFirst();
                int lengthCursor4 = cursor4.getCount();

                for(int i=0;i<lengthCursor4;i++)
                {
                    wybranaRodzina.add(cursor4.getString(0));
                    cursor4.moveToNext();
                }
                mDBHelper.closeDatabase();

                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(mDBHelper.mContext,android.R.layout.simple_spinner_dropdown_item, wybranaRodzina);
                spinner2.setAdapter(stringArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String text = spinner3.getSelectedItem().toString();

                List<String> wybranaRodzina = new ArrayList<String>();

                mDBHelper.openDatabase();
                Cursor cursor5 = mDBHelper.mDatabase.rawQuery("select nazwa_procesora from lista_procesorow,rodziny_procesorow where lista_procesorow.id_rodziny = rodziny_procesorow.id_rodziny and rodziny_procesorow.nazwa_rodziny like '%"+ text +"%'",null);
                cursor5.moveToFirst();
                int lengthCursor5 = cursor5.getCount();

                for(int i=0;i<lengthCursor5;i++)
                {
                    wybranaRodzina.add(cursor5.getString(0));
                    cursor5.moveToNext();
                }
                mDBHelper.closeDatabase();

                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(mDBHelper.mContext,android.R.layout.simple_spinner_dropdown_item, wybranaRodzina);
                spinner4.setAdapter(stringArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = spinner2.getSelectedItem().toString();

                String wybranyProc = new String();

                mDBHelper.openDatabase();
                Cursor cursor7 = mDBHelper.mDatabase.rawQuery("select id_procesora from lista_procesorow where nazwa_procesora like '%"+ text +"%'",null);
                cursor7.moveToFirst();
                int lengthCursor7 = cursor7.getCount();

                for(int i=0;i<lengthCursor7;i++)
                {
                    wybranyProc = cursor7.getString(0);
                    cursor7.moveToNext();
                }
                mDBHelper.closeDatabase();

                String text2 = spinner4.getSelectedItem().toString();

                String wybranyProc1 = new String();

                mDBHelper.openDatabase();
                Cursor cursor6 = mDBHelper.mDatabase.rawQuery("select id_procesora from lista_procesorow where nazwa_procesora like '%"+ text2 +"%'",null);
                cursor6.moveToFirst();
                int lengthCursor6 = cursor6.getCount();

                for(int i=0;i<lengthCursor6;i++)
                {
                    wybranyProc1 = cursor6.getString(0);
                    cursor6.moveToNext();
                }
                mDBHelper.closeDatabase();

                TextView textView = (TextView) findViewById(R.id.textView);

                if(wybranyProc.equals(wybranyProc1)){
                    textView.setText("!!!To ten sam procesor!!!");
                    textView.setTextColor(Color.RED);
                    textView.setTextSize(20);
                }
                else if(Integer.parseInt(wybranyProc) < Integer.parseInt(wybranyProc1)){
                    textView.setText("Wybrałbym " + text);
                    textView.setTextColor(Color.BLACK);
                    textView.setTextSize(20);
                }
                else if(Integer.parseInt(wybranyProc) > Integer.parseInt(wybranyProc1)){
                    textView.setText("Wybrałbym " + text2);
                    textView.setTextColor(Color.BLACK);
                    textView.setTextSize(20);
                }

            }
        });

    }
}
