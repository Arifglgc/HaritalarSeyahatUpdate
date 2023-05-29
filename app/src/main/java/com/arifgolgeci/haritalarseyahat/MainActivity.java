package com.arifgolgeci.haritalarseyahat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.arifgolgeci.haritalarseyahat.adaptor.PlaceAdaptor;
import com.arifgolgeci.haritalarseyahat.databinding.ActivityMain3Binding;
import com.arifgolgeci.haritalarseyahat.model.Place;
import com.arifgolgeci.haritalarseyahat.roomDb.PlaceDao;
import com.arifgolgeci.haritalarseyahat.roomDb.PlaceDatabase;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private ActivityMain3Binding binding;

    private CompositeDisposable compositeDisposable= new CompositeDisposable();
    PlaceDatabase db;
    PlaceDao placeDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Yerler");

        binding=ActivityMain3Binding.inflate(getLayoutInflater());
        View view= binding.getRoot();


        Intent intent1=getIntent();
        int listId = intent1.getIntExtra("listId", -1);
        Log.d("MainActivity", "Liste nin ID si.  " + listId);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(view);

        db= Room.databaseBuilder(getApplicationContext(),PlaceDatabase.class,"Places")
                //.allowMainThreadQueries() bu kod da calısır ama bizim amacımız burayı main threadde çalıştırmaya zorlamak degil
                .fallbackToDestructiveMigration()
                .build();
        placeDao =db.placeDao();

        compositeDisposable.add(placeDao.getAllByListId(listId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(MainActivity.this::handleResponse)
        );


 }
 private void handleResponse(List<Place> placeList){


     binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));


     if (placeList.isEmpty()) {
         // Yer listesi boş ise TextView'ı görüntüle
         binding.emptyTextView.setVisibility(View.VISIBLE);
         binding.recyclerView.setVisibility(View.GONE);

     } else {
         // Yer listesi dolu ise RecyclerView'ı görüntüle
         binding.emptyTextView.setVisibility(View.GONE);
         binding.recyclerView.setVisibility(View.VISIBLE);

         PlaceAdaptor placeAdaptor = new PlaceAdaptor(placeList);
         binding.recyclerView.setAdapter(placeAdaptor);
     }
 }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.travel_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.add_place){
            Intent intent1 =getIntent();
            int value= intent1.getIntExtra("listId",0);

            Intent intent =new Intent(MainActivity.this,MapsActivity.class);
            intent.putExtra("listId",value);

            intent.putExtra("info","new");

            startActivity(intent);
        }
        else if (item.getItemId() == android.R.id.home) {
            // "add_place" menü öğesi seçildiğinde yapılacak işlemler
            Intent intent1 = getIntent();
            int value = intent1.getIntExtra("listId", 0);

            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            intent.putExtra("listId", value);

            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}