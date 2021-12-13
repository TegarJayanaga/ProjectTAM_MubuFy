package com.projectam.mubufy;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String[] Items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewLagu);

//        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>(){
//            @Override
//            public void onActivityResult(ActivityResult result) {
//                if(result.getResultCode() == Activity.RESULT_OK){
//                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
//                        if(Environment.isExternalStorageManager()){
//                            Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            }
//
//        });
//
//        if (checkPermission()){
//            TampilLagu();
//        }
//        else {
//            requestPermission();
//        }
        runtimePermission();
    }

//    void requestPermission(){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
//            try{
//                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                intent.addCategory("android.intent.category.DEFAULT");
//                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
//                activityResultLauncher.launch(intent);
//            }catch (Exception e){
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                activityResultLauncher.launch(intent);
//            }
//        }
//        else {
//            ActivityCompat.requestPermissions(MainActivity.this, permission, 30);
//        }
//    }
//
//    boolean checkPermission(){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
//            return Environment.isExternalStorageManager();
//        }
//        else {
//            int readcheck = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
//            return  readcheck == PackageManager.PERMISSION_GRANTED;
//        }
//    }
//
//    @Override
//    public void  onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResult){
//        super.onRequestPermissionsResult(requestCode, permission, grantResult);
//        switch (requestCode){
//            case 30:
//                if (grantResult.length>0){
//                    boolean readper = grantResult[0] == PackageManager.PERMISSION_GRANTED;
//                    if(readper){
//                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else {
//                    Toast.makeText(getApplicationContext(), "You Denied Permission", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }

//    public void runtimePermission(){
//        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
////                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
////                            if (Environment.isExternalStorageManager()) {
////                                TampilLagu();
////                            } else {
////                                //request for the permission
////                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
////                                Uri uri = Uri.fromParts("package", getPackageName(), null);
////                                intent.setData(uri);
////                                startActivity(intent);
////                            }
////                        }
//                        TampilLagu();
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                        permissionToken.continuePermissionRequest();
//
//                    }
//                }).check();
//    }

    public void runtimePermission()
    {
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
                            if (Environment.isExternalStorageManager()) {
                                TampilLagu();
                            } else {
                                //request for the permission
                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        }
                        else {
                            TampilLagu();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();

    }

    public ArrayList<File> cariLagu(File file){
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();

        for(File singlefile: files){
            if(singlefile.isDirectory() && !singlefile.isHidden()){
                arrayList.addAll(cariLagu(singlefile));
            }
            else{
                if (singlefile.getName().endsWith(".mp3")){
                    arrayList.add(singlefile);
                }
            }
        }
        return arrayList;
    }

    void TampilLagu(){
        final ArrayList<File> Lagu = cariLagu(Environment.getExternalStoragePublicDirectory("Music"));

        Items = new String[Lagu.size()];
        for(int i = 0; i < Lagu.size(); i++){
            Items[i] = Lagu.get(i).getName().toString().replace(".mp3","");
        }
        customAdapter customAdapter = new customAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String songName = (String) listView.getItemAtPosition(i);
                startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                        .putExtra("songs", Lagu)
                        .putExtra("songname", songName)
                        .putExtra("pos", i));
            }
        });
    }

    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return Items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View myView = getLayoutInflater().inflate(R.layout.list_item, null);
            TextView textsong = myView.findViewById(R.id.txtsongname);
            textsong.setSelected(true);
            textsong.setText(Items[i]);

            return myView;
        }
    }
}