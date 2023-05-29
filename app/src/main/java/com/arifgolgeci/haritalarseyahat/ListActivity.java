package com.arifgolgeci.haritalarseyahat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.arifgolgeci.haritalarseyahat.adaptor.ListAdaptor;
import com.arifgolgeci.haritalarseyahat.databinding.ActivityListBinding;
import com.arifgolgeci.haritalarseyahat.model.ListPlace;
import com.arifgolgeci.haritalarseyahat.roomDb.ListsDao;
import com.arifgolgeci.haritalarseyahat.roomDb.PlaceDatabase;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ListActivity extends AppCompatActivity {

    private ActivityListBinding binding;

    private CompositeDisposable compositeDisposable= new CompositeDisposable();
    PlaceDatabase db;
    ListsDao listsDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Listelerim");

        binding= ActivityListBinding.inflate(getLayoutInflater());
        View view= binding.getRoot();

        setContentView(view);


        db= Room.databaseBuilder(getApplicationContext(),PlaceDatabase.class,"ListPlaces")
                .fallbackToDestructiveMigration()

                //.allowMainThreadQueries() bu kod da calısır ama bizim amacımız burayı main threadde çalıştırmaya zorlamak degil
                .build();
        listsDao =db.listsDao();

        compositeDisposable.add(listsDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ListActivity.this::handleResponse)
        );

    }




    private void handleResponse(@NonNull List<ListPlace> listPlace){
        binding.recyclerViewList.setLayoutManager(new LinearLayoutManager(this));

        if (listPlace.isEmpty())
        {
            binding.emptyTextViewList.setVisibility(View.VISIBLE);
            binding.recyclerViewList.setVisibility(View.GONE);
        }
        else {

            binding.emptyTextViewList.setVisibility(View.GONE);
            binding.recyclerViewList.setVisibility(View.VISIBLE);

            ListAdaptor listAdaptor = new ListAdaptor(listPlace);
            binding.recyclerViewList.setAdapter(listAdaptor);
            listAdaptor.setOnItemLongClickListener(new ListAdaptor.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(int position) {
                    showDeleteListDialog(listPlace.get(position));
                }
            });

        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.list_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void showDeleteListDialog(ListPlace list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Listeyi Sil");
        builder.setMessage("Listeyi silmek istediğinizden emin misiniz?");

        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Listeyi veritabanından sil
                Completable completable = listsDao.delete(list)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

                Disposable disposable = completable.subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(getApplicationContext(), "Liste silindi", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // Hata durumunda yapılacak işlemler
                    }
                });

                // Disposable nesnesini CompositeDisposable'a ekle
                compositeDisposable.add(disposable);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showVeriEkleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Liste Ekle");

        // Diyalog içeriğini oluşturuldu
        final EditText isimEditText = new EditText(this);

        isimEditText.setHint("Liste ismi giriniz.");
        builder.setView(isimEditText);

        // Ekle butonunu varsayılan olarak devre dışı bırakıldı
        builder.setPositiveButton("Ekle", null);
        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();

        // TextWatcher'ı EditText'e eklendi
        isimEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Metin değişmeden önceki durum
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Metin değiştiğinde yapılacak işlemler

                // Ekle butonunun durumunu güncelle
                if (s.length() > 0) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true); // Etkinleştir
                } else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false); // Devre dışı bırak
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Ekle butonunun tıklama işlemine listener eklendi
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String yeniIsim = isimEditText.getText().toString().trim();

                        // Yeni veriyi veritabanına ekleme
                        if (!yeniIsim.isEmpty()) {
                            ListPlace yeniPlace = new ListPlace(yeniIsim);
                            Completable completable = listsDao.insert(yeniPlace)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread());

                            Disposable disposable = completable.subscribeWith(new DisposableCompletableObserver() {
                                @Override
                                public void onComplete() {
                                    Toast.makeText(getApplicationContext(), "Yeni liste eklendi", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    // Hata durumunda yapılacak işlemler
                                }
                            });

                            // Disposable nesnesini CompositeDisposable'a eklendi
                            compositeDisposable.add(disposable);

                            dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Liste adını giriniz.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



        dialog.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Activity sonlandığında CompositeDisposable'ı temizle
        compositeDisposable.clear();
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.add_list){
            showVeriEkleDialog();
            return true;
        }
     else if (item.getItemId() == android.R.id.home) {
        onBackPressed(); // Geri tuşuna basılınca onBackPressed metodu çağrılır
        return true;
    }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }

}