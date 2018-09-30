package com.example.stockapp3;
import android.util.Log;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

public class MACD {
    private Double signal;
    private Double delta;
    private List<Double> emacede;

    public  MACD(){
        emacede = new ArrayList<Double>();
        signal = 0.00 ;
        delta = 100.0;
    }

    public void calculate_MACD(List<Double> prices){
        for (int i = 8; i >= 0; i--){
            Double ma12 = emaN(prices,12,i);
            Double ma26 = emaN(prices,26,i);
            emacede.add(ma12 - ma26);
        }
        signal = emaN(emacede,9,0);
    }

    private Double emaN(List<Double> values, int N, int akhir){
        Double alpha = 2.0;
        Double n = new Double(N);
        Double pembagi = n + 1;
        alpha = alpha / pembagi;
        Double st = 0.0;
        int index = values.size() - N - akhir;
        for(int i = 0; i<N; i++){
            if(i == 0){
                st = values.get(index);
            }
            else{
                Double today_price = values.get(index);
                Double dummy = today_price * alpha;
                Double prev_emacd_w = st * (1 - alpha);
                st = dummy + prev_emacd_w;
            }
            index++;
        }
        return st;
    }

    public boolean is_buy(){
        Double val = emacede.get(8) + signal;
        Log.i("Nilai val", val.toString() + " " + emacede.get(8).toString() + " " + signal.toString());
        if(Math.abs(val) < delta && val < 0)
            return true;
        else
            return false;
    }

    public boolean is_sell(){
        Double val = emacede.get(8) - signal;
        if(Math.abs(val) < delta && val > 0)
            return true;
        else
            return false;
    }
}