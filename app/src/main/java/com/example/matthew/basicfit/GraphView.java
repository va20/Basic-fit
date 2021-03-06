package com.example.matthew.basicfit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Matthew on 12/12/2017.
 */

public class GraphView extends View {

    private Paint paint,paint1,paint2,paint3;
    private int kcal = 3000;
    private static String authority;
    private int Objectif;
    private int status_calories;

    private int size;

    private Map<String, String> graph;

    public GraphView(Context context, AttributeSet attrs) {
        super(context,attrs);

        authority = getResources().getString(R.string.authority);

        graph = new TreeMap<>();

        graph.put("2010-43-12 01:50:11", "230");
        graph.put("2010-43-12 02:45:11", "430");
        graph.put("2010-43-12 03:22:11", "630");
        graph.put("2010-43-12 06:41:11", "730");
        graph.put("2010-43-12 10:15:11", "830");
        graph.put("2010-43-12 14:33:11", "940");
        graph.put("2010-43-12 22:05:11", "1190");

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint3 = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30f);
        paint.setStrokeWidth(5);
        paint1.setStyle(Paint.Style.FILL);
        paint2.setTextSize(20f);
        paint1.setColor(Color.BLACK);
        Objectif = 0;


        authority = getResources().getString(R.string.authority);
    }

    public void setMap(Map<String, String> coord) {
        graph.clear();
        graph.putAll(coord);
        invalidate();
    }

    public void setStatusCalories(int calories) {
        this.status_calories = calories;
    }

    public int getPourcentageObjectif() {
        return (status_calories / Objectif)*100;
    }

    /*
    * modifie la valeur de maximum kcal du graph.
    * @param kcal valeur du nombre de kcal que l'on veut.
     */

    public void setObjectif(int objectif) {
        this.Objectif = objectif;
    }

    public void drawObjectif(Canvas canvas) {

        float coehf = (float) canvas.getHeight() / kcal;

        paint3.setARGB(255,0,191,255);
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setPathEffect(new DashPathEffect(new float[] {20,10}, 0));

        Path path = new Path();
        path.moveTo(0,yToOrigine(Objectif*coehf));
        path.lineTo(canvas.getWidth(),yToOrigine(Objectif*coehf));
        canvas.drawPath(path,paint3);
    }


    public float parseToFloat(String s) {
        String toDouble = s.substring(11, 13);
        toDouble += s.substring(14, 16);
        Double val = new Double(toDouble);
        float valeur = val.floatValue() / 100;
        return valeur;
    }

    public void drawGraphparametersX(Canvas canvas){
        float coef = canvas.getWidth()/24;

        for(int i = 0; i < 24; i++) {
            if (i % 2 == 0) {
                canvas.drawText(""+i+"h",i*coef+50,20,paint2);
                canvas.drawLine(i*coef+50,yToOrigine(i),i*coef+50,yToOrigine(getHeight()),paint2);

            }
        }

    }


    public void drawGraphparametersY(Canvas canvas) {
        float coehf = (float) canvas.getHeight() / kcal;

        for (int i = 0; i < kcal ; i++) {
            if (i % ((int)(2000*coehf)) == 0) {
                canvas.drawText(""+i,0,yToOrigine(i*coehf),paint2);
                canvas.drawLine(50,yToOrigine(i*coehf),canvas.getWidth(),yToOrigine(i*coehf),paint2);
            }
        }
    }

    public float yToOrigine(float f) {
        return (float) getHeight()-f;
    }

    @Override
    public void onDraw(Canvas canvas) {

        drawObjectif(canvas);

        drawGraphparametersX(canvas);

        drawGraphparametersY(canvas);

        float coefw = (float) canvas.getWidth() / 24;
        float coefh = (float) canvas.getHeight() / kcal;
        float x1,y1,x2,y2;


        x1 = coefw+50;
        y1 = yToOrigine(coefh);

        int i = 0;

        Iterator<String> it = graph.keySet().iterator();

        while(it.hasNext()) {
            String s = it.next();

            int calories = Integer.parseInt(graph.get(s));
            float heure = parseToFloat(s);

            x2 = (heure)*coefw+50;
            y2 = yToOrigine(calories*coefh);

            if (getPourcentageObjectif() >= 0 && getPourcentageObjectif() < 30) {
                paint.setColor(Color.RED);
            } else if (getPourcentageObjectif() >= 30 && getPourcentageObjectif() < 60) {
                paint.setColor(Color.argb(255,255,153,0));
            }
            else {
                paint.setColor(Color.GREEN);
            }

            canvas.drawLine(x1,y1,x2,y2,paint);
            canvas.drawCircle(x2,y2,15,paint1);
            canvas.drawText(calories+" Kcal",x2+20,y2+10,paint);

            x1 = x2;
            y1 = y2;

            i++;
        }
    }
}
