package com.example.qrgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class MainActivity extends AppCompatActivity {

    private AppCompatActivity activity;
    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    private Bitmap bitmap;
    private static final int WRITE_EXTERNAL_STORAGE_CODE=1;
    BitmapDrawable drawable;
    TextView tvQr;
    EditText etValue;
    Button btGenerate,btSave;
    ImageView ivQr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvQr=findViewById(R.id.tvQr);
        etValue=findViewById(R.id.etValue);
        btGenerate=findViewById(R.id.btGenerate);
        btSave=findViewById(R.id.btSave);
        ivQr=findViewById(R.id.ivQr);
        activity=this;

        btGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data= etValue.getText().toString();
                if (data.isEmpty()){
                    etValue.setError("Value Required");
                }else {
                    QRGEncoder qrgEncoder=new QRGEncoder(data,null, QRGContents.Type.TEXT,500);
                   // qrgEncoder.setColorBlack(Color.RED);
                   // qrgEncoder.setColorWhite(Color.BLUE);
                    Bitmap qrBits =qrgEncoder.getBitmap();
                    ivQr.setImageBitmap(qrBits);
                }

            }
        });



        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                            PackageManager.PERMISSION_DENIED){

                        String[] permission= {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,WRITE_EXTERNAL_STORAGE_CODE);

                    }else {
                        saveimage();
                    }
                }







                /*if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        boolean save = new QRGSaver().save(savePath, etValue.getText().toString().trim(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
                        String result = save ? "Image Saved" : "Image Not Saved";
                        Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
                        etValue.setText(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }*/

                /*drawable=(BitmapDrawable)ivQr.getDrawable();
                bitmap=drawable.getBitmap();

                FileOutputStream outputStream=null;

                File path =Environment.getExternalStorageDirectory();
                //File directory =new File(sdCard.getAbsolutePath()+"/QrGenerator");
                File directory =new File(path +"/DCIM   ");
                directory.mkdir();
                String fileName = String.format("%d.jpg",System.currentTimeMillis());
                File outFile = new File(directory,fileName);

                OutputStream out;

                Toast.makeText(MainActivity.this,"Image Saved Successfully",Toast.LENGTH_SHORT).show();

                try {
                    outputStream=new FileOutputStream(outFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                    outputStream.flush();
                    outputStream.close();

                    Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outFile));
                    sendBroadcast(intent);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/



            }
        });


    }

    private void saveimage() {
        bitmap=((BitmapDrawable)ivQr.getDrawable()).getBitmap();
        String time =new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        File path=Environment.getExternalStorageDirectory();
        File dir = new File(path+"/DCIM");
        dir.mkdir();
        String imagename=time+".PNG";
        File file=new File(dir,imagename);

        OutputStream outputStream;

        try {
            outputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.flush();
            outputStream.close();

            Toast.makeText(MainActivity.this,"Image Save In DCIM",Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case WRITE_EXTERNAL_STORAGE_CODE:{
                if (grantResults.length>0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }else {
                    Toast.makeText(this,"Permission enabled",Toast.LENGTH_SHORT).show();
                }
            }
            break;

        }
    }
}