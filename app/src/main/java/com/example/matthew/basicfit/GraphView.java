package com.example.matthew.basicfit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;


import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Matthew on 12/12/2017.
 */

public class GraphView extends View {

    private static String authority;
    Paint paint,paint1,paint2;

    private int size;
    private Map<String, String> graph;

    public GraphView(Context context, AttributeSet attrs) {
        super(context,attrs);


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

        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30f);
        paint.setStrokeWidth(5);
        paint1.setStyle(Paint.Style.FILL);
        paint1.setColor(Color.RED);


        authority = getResources().getString(R.string.authority);
    }

    /*
    "yyyy-MM-dd HH:mm:ss"
     */

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
                canvas.drawText(""+i+"h",i*coef,20,paint);
                canvas.drawLine(i*coef,yToOrigine(i),i*coef,yToOrigine(getHeight()),paint2);

            }
        }

    }

    public void drawGraphparametersY(Canvas canvas) {
        int h = getHeight();
        for (int i = 0; i < h ; i++) {
            if (i % 300 == 0) {
                canvas.drawText(""+i,0,yToOrigine(i),paint);
                canvas.drawLine(0,yToOrigine(i),getWidth(),yToOrigine(i),paint2);
            }
        }
    }

    public float yToOrigine(float f) {
        return (float) getHeight()-f;
    }

    @Override
    public void onDraw(Canvas canvas) {

        drawGraphparametersX(canvas);

        drawGraphparametersY(canvas);

        float coefw = canvas.getWidth()/24;
        float x1,y1,x2,y2;

        x1 = 0;
        y1 = yToOrigine(0);

        int i = 0;

        Iterator<String> it = graph.keySet().iterator();

        while(it.hasNext()) {
            String s = it.next();
            int calories = Integer.parseInt(graph.get(s));
            float heure = parseToFloat(s);
            x2 = heure*coefw;
            y2 = yToOrigine(calories);
            Log.d("coordonnées y2", "" + yToOrigine(y2));
            canvas.drawLine(x1,y1,x2,y2,paint);
            canvas.drawCircle(x2,y2,15,paint1);
            canvas.drawText(calories+" Kcal",x1+20,y1+10,paint);
            x1 = x2;
            y1 = y2;
            i++;
        }
    }
}
