package com.example.user1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class InsertKomplainBaru extends AppCompatActivity {

    TextView lokasi,ruangan,alat1,ur1,alat2,ur2;
    Button btnk;
    String date;
    String lokasiintent,ruanganintent;
    ProgressBar progressBar;
    private String status = "Terkirim";
    private String foto = "n";
    private String tindakan="n";
    private String tglkerja="n";
    private int PICK_IMAGE_REQUEST = 1;
    private int CAMERA = 2;

    int max = 10000000;
    int min = 1000000;

    int idk1 = new Random().nextInt((max-min)+1) + min;
    int idk2 = new Random().nextInt((max-min)+1) + min;

    int user, ruangans, lokasis,al1,al2;
    private final int verifikator = 8;
    private Uri filePath;
    private Bitmap bitmap;
    private ImageButton foto1,foto2;

    public static final String URL = "http://192.168.43.144/";
    private RequestQueue mreq;
    private StringRequest strreq;
    private String urlnotif = "http://192.168.43.144/TA/notifTU.php";
    private String urlUplImg = "http://192.168.43.144/TA/uploadImage.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_komplain_baru);
        final Bundle bundle = getIntent().getExtras();

        lokasi = findViewById(R.id.txtLokasiF);
        ruangan = findViewById(R.id.txtRuanganF);
        alat1 = findViewById(R.id.alat10);
        ur1 = findViewById(R.id.ur1);
        alat2 = findViewById(R.id.alat20);
        ur2 = findViewById(R.id.ur2);

        btnk = findViewById(R.id.btnKirim);
        progressBar = findViewById(R.id.progresKirimKomplain);

        foto1 = findViewById(R.id.ib1);
        foto2 = findViewById(R.id.ib2);

        progressBar.setVisibility(View.INVISIBLE);
        foto2.setVisibility(View.INVISIBLE);

        User us = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        user = us.getId();

        ruangans = bundle.getInt("RUANGAN_ID");
        lokasis = bundle.getInt("LOKASI_ID");
        al1 = bundle.getInt("ALAT_ID1");
        al2 = bundle.getInt("ALAT_ID2");

        lokasi.setText(bundle.getString("LOKASIFIX"));
        ruangan.setText(bundle.getString("RUANGANFIX"));

        alat1.setText(bundle.getString("ALAT1"));
        ur1.setText(bundle.getString("URAIAN1"));
        alat2.setText(bundle.getString("ALAT2"));
        ur2.setText(bundle.getString("URAIAN2"));
        lokasiintent = bundle.getString("LOKASIFIX");
        ruanganintent = bundle.getString("RUANGANFIX");
        String k2 = ur2.getText().toString();

        if (!k2.isEmpty())
        {
            foto2.setVisibility(View.VISIBLE);
        }

        foto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] item = {"Camera","Galeri"};
                AlertDialog.Builder request = new AlertDialog.Builder(InsertKomplainBaru.this)
                        .setTitle("Ambil Foto")
                        .setItems(item, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which)
                                {
                                    case 0:
                                        cameraChooser();
                                        break;
                                    case 1:
                                        showFileChooser();
                                        break;
                                }
                            }
                        });
                request.create();
                request.show();
            }
        });
        foto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] item = {"Camera","Galeri"};
                AlertDialog.Builder request = new AlertDialog.Builder(InsertKomplainBaru.this)
                        .setTitle("Ambil Foto")
                        .setItems(item, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which)
                                {
                                    case 0:
                                        cameraChooser();
                                        break;
                                    case 1:
                                        showFileChooser();
                                        break;
                                }
                            }
                        });
                request.create();
                request.show();
            }
        });

        btnk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressBar.setVisibility(View.VISIBLE);
                String komplain1 = ur1.getText().toString();
                String komplain2 = ur2.getText().toString();
                if (!komplain1.isEmpty() && komplain2.equals(""))
                {
                    insertKomplain1();
                    pushNotif();
                    uploadImage();
                }
                else if (!komplain2.isEmpty() && !komplain1.isEmpty())
                {
                    insertKomplain1();
                    insertKomplain2();
                    pushNotif();
                }
                else {

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                foto1.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == CAMERA && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Bundle extra = data.getExtras();
            bitmap = (Bitmap) extra.get("data");
            foto1.setImageBitmap(bitmap);
        }
    }

    private void insertKomplain1()
    {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(URL).build();
        InsertKomplain  api = restAdapter.create(InsertKomplain.class);
        api.insertKomplain(
                idk1,
                date = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date()),
                status,
                foto,
                tindakan,
                tglkerja,
                ruangans,
                lokasis,
                user,
                verifikator,
                idk1,
                al1,
                ur1.getText().toString(),
                new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        BufferedReader reader = null;
                        String output = "";
                        String judul = null;
                        try{

                            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                            output = reader.readLine();
                            if (output.equals("Komplain Berhasil Terkirim<br />"))
                            {
                                judul = "Komplain Berhasil Terkirim";
                            }
                            Intent a = new Intent(InsertKomplainBaru.this,MainActivity.class);
                            startActivity(a);
                            finish();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(InsertKomplainBaru.this, judul, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(InsertKomplainBaru.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

        );
    }
    private void insertKomplain2()
    {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(URL).build();
        InsertKomplain  api = restAdapter.create(InsertKomplain.class);
        api.insertKomplain(
                idk2,
                date = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date()),
                status,
                foto,
                tindakan,
                tglkerja,
                ruangans,
                lokasis,
                user,
                verifikator,
                idk2,
                al2,
                ur2.getText().toString(),
                new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        BufferedReader reader = null;
                        String output = "";
                        String judul = null;
                        try {

                            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                            output = reader.readLine();
                            if (output.equals("Komplain Berhasil Terkirim<br />"))
                            {
                                judul = "Komplain Berhasil Terkirim";
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(InsertKomplainBaru.this, output, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(InsertKomplainBaru.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

        );
    }
    private void pushNotif()
    {
        mreq = Volley.newRequestQueue(this);
        strreq = new StringRequest(Request.Method.POST, urlnotif, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(InsertKomplainBaru.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(InsertKomplainBaru.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        mreq.add(strreq);
    }
    private void uploadImage()
    {
        StringRequest request = new StringRequest(Request.Method.POST, urlUplImg, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String> param = new HashMap<>();
               String img = getStringImage(bitmap);
               param.put("idkomplain", String.valueOf(idk1));
               param.put("image",img);
               return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InsertKomplainBaru.this);
        requestQueue.add(request);
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    private void cameraChooser() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}
