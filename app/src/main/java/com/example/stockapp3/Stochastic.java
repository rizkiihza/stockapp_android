package com.example.stockapp3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Albert Setiawan on 9/13/2017.
 */

public class Stochastic {

    BigDecimal D,
               Dtemp,
               Max,
               Min;

    List<BigDecimal> K = new ArrayList<BigDecimal>();

    public void kCount (List<BigDecimal> low, List<BigDecimal> high, List<BigDecimal> close){
        for (int n = 1; n <= 3; n++) {
            Min = low.get(low.size() - n);
            Max = Max.ZERO;
            for (int i = low.size() - n; i > low.size() - (14 + n); i--) {
                if (low.get(i).compareTo(Min) == -1) {
                    Min = low.get(i);
                }
                if (high.get(i).compareTo(Max) == 1) {
                    Max = high.get(i);
                }
            }
            K.add(close.get(close.size() - n)
                       .subtract(Min)
                       .divide(Max.subtract(Min), 3)
                       .multiply(BigDecimal.valueOf(100)));
        }
    }

    public void dCount (){
        Dtemp = Dtemp.ZERO;
        for (int i = 0; i <= 2; i++){
            Dtemp = Dtemp.add(K.get(i));
        }
        D = Dtemp.divide(BigDecimal.valueOf(3), 3);
    }
}