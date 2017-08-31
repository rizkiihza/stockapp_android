package com.example.stockapp3;
import android.util.Log;

import java.math.BigDecimal;
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
        BigDecimal alpha = new BigDecimal(2).movePointLeft(2);
        alpha = alpha.divide(new BigDecimal(N+1).movePointLeft(2),3);
        BigDecimal st = new BigDecimal(0);
        int index = values.size() - N - akhir;
        for(int i = 0; i<N; i++){
            if(i == 0){
                st = values.get(index);
            }
            else{
                st = (alpha.multiply(values.get(index)));
                BigDecimal ones = new BigDecimal(1);
                st = st.add(ones.subtract(alpha).multiply(values.get(i)));
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