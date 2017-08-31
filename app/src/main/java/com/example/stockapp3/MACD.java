package com.example.stockapp3;
import android.util.Log;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

public class MACD {
    private BigDecimal signal;
    private BigDecimal delta;
    private List<BigDecimal> emacede;

    public  MACD(){
        emacede = new ArrayList<BigDecimal>();
        signal = new BigDecimal(0).movePointLeft(2);
        delta = new BigDecimal(100);
    }

    public void calculate_MACD(List<BigDecimal> prices){
        for (int i = 8; i >= 0; i--){
            BigDecimal ma12 = emaN(prices,12,i);
            BigDecimal ma26 = emaN(prices,26,i);
            emacede.add(ma12.subtract(ma26));
        }
        signal = emaN(emacede,9,0);
    }

    private BigDecimal emaN(List<BigDecimal> values, int N, int akhir){
        BigDecimal alpha = BigDecimal.valueOf(2.0);
        BigDecimal pembagi = BigDecimal.valueOf(N+1);
        alpha = alpha.divide(pembagi,5, BigDecimal.ROUND_HALF_UP);
        BigDecimal st = BigDecimal.valueOf(0.0);
        int index = values.size() - N - akhir;
        for(int i = 0; i<N; i++){
            if(i == 0){
                st = values.get(index);
            }
            else{
                BigDecimal today_price = values.get(index);
                BigDecimal dummy = today_price.multiply(alpha);
                BigDecimal ones = new BigDecimal(1);
                BigDecimal prev_emacd_w = st.multiply(ones.subtract(alpha));
                st = dummy.add(prev_emacd_w);
            }
            index++;
        }
        return st;
    }

    public boolean is_buy(){
        BigDecimal val = emacede.get(8).subtract(signal);
        Log.i("Nilai val", val.toString() + " " + emacede.get(8).toString() + " " + signal.toString());
        if(val.abs().compareTo(delta) == -1 && val.compareTo(BigDecimal.ZERO) == -1)
            return true;
        else
            return false;
    }

    public boolean is_sell(){
        BigDecimal val = emacede.get(8).subtract(signal);
        if(val.abs().compareTo(delta) == -1 && val.compareTo(BigDecimal.ZERO) == 1)
            return true;
        else
            return false;
    }
}