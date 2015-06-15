package com.example.victor.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.widget.ImageView;
import android.util.Log;
import android.content.Context;
import android.view.View;

import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;


public class MainActivity extends Activity {

    public static final int Width=500;
    public static final int Height=500;
    public static final int Stride=Height;  //Must be greater than Width

    private static int[] createColors()
    {
        int[] colors = new int[Stride * Height];
        /*
        for (int y = 0; y < Height; y++) {
            for (int x = 0; x < Width; x++) {
                int r = x * 255 / (Width - 1);
                int g = y * 255 / (Height - 1);
                int b = 255 - Math.min(r, g);
                int a = Math.max(r, g);
                colors[y * Stride + x] = (a << 24) | (r << 16) | (g << 8) | b;
            }
        }
        */
        for(int i=0;i<colors.length;i++){            colors[i]=-65536;

        }
        return colors;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SampleView(this));

/*        setContentView(R.layout.activity_main);
        ImageViewToDraw=(ImageView)findViewById(R.id.ImageView01);


        int[] colors=new int[100000];
        for(int i=0;i<colors.length;i++)
            colors[i]=128;

        Bitmap BITMAPPP=Bitmap.createBitmap(colors, 300, 300, Config.ALPHA_8);

        Log.d("1111","shitshitshitshit");

        Bitmap newb = Bitmap.createBitmap( 300, 300, Config.ARGB_8888 );

        Canvas canvasTemp = new Canvas( newb );
        canvasTemp.drawColor(Color.TRANSPARENT);

        Paint p = new Paint();
        String familyName ="宋体";
        Typeface font = Typeface.create(familyName,Typeface.BOLD);
        p.setColor(Color.RED);
        p.setTypeface(font);
        p.setTextSize(22);
        canvasTemp.drawText("写字。。。",50,50,p);


        ImageViewToDraw.setImageBitmap(BITMAPPP);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private static class SampleView extends View {

        // CONSTRUCTOR
        public SampleView(Context context) {
            super(context);
            setFocusable(true);

        }
        @Override
        protected void onDraw(Canvas canvas) {
            Paint paint = new Paint();

            canvas.drawColor(Color.TRANSPARENT);

            int[] mColors = createColors();
            int[] colors = mColors;


            Bitmap bitmap = Bitmap.createBitmap(colors, 0, Stride, Width, Height,
                    Config.ARGB_8888);

            canvas.drawBitmap(bitmap, 50,20, paint);
        }

    }
}


