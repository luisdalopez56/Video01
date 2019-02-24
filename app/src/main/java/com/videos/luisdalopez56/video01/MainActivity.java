package com.videos.luisdalopez56.video01;

import android.content.Context;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private Button btn_reproducir, btn_pausar;
    private Spinner spinner;
    private SurfaceView surfaceView;
    private TextView mediaUri;
    private Boolean pausa;
    private Uri pelicula;
    private MediaPlayer reproductor;
    private SurfaceHolder surfaceHolder;
    private Uri mediaSrc;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_pausar = findViewById(R.id.btn_pausar);
        btn_reproducir = findViewById(R.id.btn_reproducir);

        spinner = findViewById(R.id.spinner);

        surfaceView = findViewById(R.id.surfaceView);

        mediaUri = findViewById(R.id.txt_ficheroVideo);
        mediaSrc = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = managedQuery(mediaSrc,null,null,null,MediaStore.Audio.Media.TITLE);

        String[] nombre = {MediaStore.MediaColumns.TITLE};
        int[] ids = {android.R.id.text1};
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1, cursor, nombre, ids);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = adapter.getCursor();
                cursor.moveToPosition(position);
                String indice = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                pelicula = Uri.withAppendedPath(mediaSrc,indice);
                mediaUri.setText(pelicula.toString());
                Toast.makeText(getApplicationContext(),pelicula.toString(),Toast.LENGTH_SHORT).show();
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setFixedSize(176, 144);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        reproductor = new MediaPlayer();

        btn_reproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausa = false;

                if(reproductor.isPlaying()) {
                    reproductor.reset();
                }

                reproductor.setAudioStreamType(AudioManager.STREAM_MUSIC);
                reproductor.setDisplay(surfaceHolder);

                    try {
                        reproductor.setDataSource(getBaseContext(), pelicula);
                        reproductor.prepare();

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace(); }

                reproductor.start();
                }

        });

        btn_pausar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pausa == true){
                    pausa = false;
                    reproductor.start();
                }else {
                    pausa = true;
                    reproductor.pause();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reproductor.release();
        getWindow().setFormat(PixelFormat.UNKNOWN);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
