package com.example.stockapp3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Albert Setiawan on 8/29/2017.
 */

public class RSI {

    List<BigDecimal> list_gain,
                     list_loss,
                     price_prev,
                     price_now,
                     price_delta,
                     avrg_gain,
                     avrg_loss,
                     rsi_list,
                     rs_list;

    int period = 24;

    public RSI(){

        list_gain = new ArrayList<BigDecimal>();
        list_loss = new ArrayList<BigDecimal>();
        price_prev = new ArrayList<BigDecimal>();
        price_delta = new ArrayList<BigDecimal>();
        avrg_gain = new ArrayList<BigDecimal>();
        avrg_loss = new ArrayList<BigDecimal>();
        rsi_list = new ArrayList<BigDecimal>();
        price_now = new ArrayList<BigDecimal>();
        rs_list = new ArrayList<BigDecimal>();

    }

    public void list_in(List<BigDecimal> price){

        for (int i = price.size() - period, j = 0; i < price.size() - 1; i++, j++){

            price_prev.add(price.get(i));
            price_now.add(price.get(i + 1));
            price_delta.add((price_now.get(j)
                       .subtract(price_prev.get(j)))
                       .divide(price_prev.get(j), 3));

        }
    }

    public void delta_check (){
        for (BigDecimal delta: price_delta){
            if (delta.signum() == 1){

                list_gain.add(delta);
                list_loss.add(BigDecimal.ZERO);

            } else {

                list_gain.add(BigDecimal.ZERO);
                list_loss.add(delta.abs());

            }
        }
    }

    public void avrg(){

        for (int i = 0; i < 9; i++) {

            BigDecimal sum_gain = new BigDecimal(0), sum_loss = new BigDecimal(0);

            for (int j = 0 + i; j < 15 + i; j++) {
                sum_gain = sum_gain.add(list_gain.get(j));
                sum_loss = sum_loss.add(list_loss.get(j));
            }
            if (i == 0){
                avrg_gain.add(sum_gain
                         .divide(BigDecimal.valueOf(14), 3));

                avrg_loss.add(sum_loss
                         .divide(BigDecimal.valueOf(14), 3));
            } else {
                avrg_gain.add((avrg_gain.get(i - 1)
                         .multiply(BigDecimal.valueOf(13))
                         .add(list_gain.get(i + 14)))
                         .divide(BigDecimal.valueOf(14), 3));

                avrg_loss.add((avrg_loss.get(i - 1)
                         .multiply(BigDecimal.valueOf(13))
                         .add(list_loss.get(i + 14)))
                         .divide(BigDecimal.valueOf(14), 3));
            }
        }
    }

    public void rsi_count(){
        for (int i = 0; i < 9; i++) {
            rs_list .add(avrg_gain.get(i)
                    .divide(avrg_loss.get(i),3));

            rsi_list.add(BigDecimal.valueOf(100)
                    .subtract(BigDecimal.valueOf(100)
                    .divide(BigDecimal.ONE
                    .add(rs_list.get(i)), 3)));
        }
    }

    public void rsi_all(List<BigDecimal> price){
       list_in(price);
        delta_check();
        avrg();
        rsi_count();
    }


}

