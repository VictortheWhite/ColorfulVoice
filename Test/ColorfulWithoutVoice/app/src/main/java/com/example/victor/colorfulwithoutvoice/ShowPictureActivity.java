package com.example.victor.colorfulwithoutvoice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.content.Intent;
import android.graphics.Typeface;



public class ShowPictureActivity extends Activity {

    public static final int Width=500;
    public static final int Height=800;
    public static final int Stride=Height;  //Must be greater than Width
    private Intent intent;
    private Bundle bundle;
    private double[] Frequency;
    private static double[] Volume;
    private static byte[] Green;
    private static byte[] Blue;

    private static double MaxVolume;
    private static double MinVolume;
    private static int count;


    private static int[] createColors() {
        //double []data=new double[];
        //int []data={-1000,-100000,-1000000,-13142151,-121413413,-6218648,-472914,-446284,-42837};
        //int []data=new int[10000];
        //for(int i=0;i<data.length;i++)
        //    data[i]=-10000*i;
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

        for(int i=0;i<colors.length;i++){            colors[i]=-65536;

        }*/
        for (int i = 1279; Volume[i] == 0 && Green[i] == 0 && Blue[i] ==0;i--)
            count=i;
        double FillInRatio=(double)count/(double)colors.length;

        System.out.println(count);
        System.out.println(count);
        System.out.println(count);
        System.out.println(count);
        System.out.println(count);
        
        int X=(int)(Stride*FillInRatio);
        int Y=(int)(Height*FillInRatio);
        int [][]pexiels=new int[Y][X];

        int XDistance=Stride/X;
        int YDistance=Height/Y;

        System.out.println("XDit="+XDistance);
        System.out.println("YDit="+YDistance);

        for(int i=0;i<Y;i++){
            for (int j=0;j<X;j++){
                pexiels[i][j]=PixelsGenerator(Volume[i+j*X],Green[i+j*X],Blue[i+j*X]);
            }
        }
        /*
        for(int i=0;i<Y;i++){
            for (int j=0;j<X;j++)
                System.out.print(pexiels[j][i]+" ");
            System.out.println();
        }*/
        //System.out.println("X="+X);
        //System.out.println("Y="+Y);

        for(int i=0;i<colors.length;i++){
            int x=i%Stride;
            int y=i/Stride;
            /*
            System.out.println("x="+x);
            System.out.println("y="+y);
            */
            int a=y/YDistance;
            int b=x/XDistance;
            if(b<X&&a<Y){
                colors[i]=pexiels[a][b];
            }else if(b<X){
                colors[i]=pexiels[Y-1][b];
            }else if(a<Y){
                colors[i]=pexiels[a][X-1];
            }else {
                colors[i]=pexiels[Y-1][X-1];
            }
            /*
            else{
                colors[i]=pexiels[Height/YDistance-100][Stride/XDistance-100];
            }*/
        }
        return colors;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SampleView(this));

        intent=this.getIntent();
        bundle=intent.getExtras();
        Volume=bundle.getDoubleArray("Volume");
        Green=bundle.getByteArray("Green");
        Blue=bundle.getByteArray("Blue");
        count=bundle.getInt("count");

        System.out.println(Green.length+" "+Blue.length+" "+Volume.length);
        System.out.println(Green.length+" "+Blue.length+" "+Volume.length);
        System.out.println(Green.length+" "+Blue.length+" "+Volume.length);
        System.out.println(Green.length+" "+Blue.length+" "+Volume.length);
        System.out.println(Green.length+" "+Blue.length+" "+Volume.length);
        System.out.println(Green.length+" "+Blue.length+" "+Volume.length);
        System.out.println(Green.length+" "+Blue.length+" "+Volume.length);
        System.out.println(Green.length+" "+Blue.length+" "+Volume.length);
        System.out.println(Green.length+" "+Blue.length+" "+Volume.length);
        System.out.println(Green.length+" "+Blue.length+" "+Volume.length);
        System.out.println(Green.length+" "+Blue.length+" "+Volume.length);
        System.out.println(Green.length+" "+Blue.length+" "+Volume.length);
        System.out.println(Green.length+" "+Blue.length+" "+Volume.length);
        System.out.println(Green.length+" "+Blue.length+" "+Volume.length);
        System.out.println(Green.length+" "+Blue.length+" "+Volume.length);


        for(int i=0;i<count;i++){
            System.out.println(i+": "+Volume[i]+" "+Green[i]+" "+Blue[i]);
        }

        //Frequency=bundle.getDoubleArray("Frequency");
        MaxVolume=Volume[count];
        MinVolume=Volume[0];
    /*
        for(int i=0;i<Volume.length;i++) {
            System.out.print(Volume[i]+"  ");
            System.out.println(Frequency[i]);
        }
    */
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
                    Bitmap.Config.ARGB_8888);

            canvas.drawBitmap(bitmap, 105,100, paint);


            Paint p = new Paint();
            String familyName ="宋体";
            Typeface font = Typeface.create(familyName,Typeface.BOLD);
            p.setColor(Color.RED);
            p.setTypeface(font);
            p.setTextSize(22);
            canvas.drawText("This is the fucking picture",200,1000,p);
        }

    }

    private static int PixelsGenerator(double vol,byte G,byte B){
        System.out.print("We are here \n");
        long Pixel=0;
        Pixel+=255;
        Pixel=Pixel<<8;
        Pixel+=(int)((vol-MinVolume)/(MaxVolume-MinVolume)*255);
        Pixel=Pixel<<8;
        Pixel+=G;
        Pixel=Pixel<<8;
        Pixel+=B;
        Pixel=Pixel<<8;

        System.out.println((int) Pixel);
        return (int)Pixel;
    }
}
