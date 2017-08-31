package com.example.stockapp3;

import com.example.stockapp3.MACD;
import com.example.stockapp3.RSI;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;


import yahoofinance.*;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView display;
    Stock stock;
    EditText symbol_ET;
    String symbol_S;
    String	dispName,
    		dispPrice,
    		dispTradeTimeStr,
    		dispTradeDateStr,
    		dispTradeTime,
    		dispOpen,
    		dispDayHigh,
    		dispDayLow,
    		dispChange,
    		dispPreviousClose,
    		dispDividendAnnualYield,
    		dispExDate,
    		dispPayDate,
    		displayText;
    
    TimeZone timeZone;
    String	name,
    		tradeTimeStr,
    		tradeDateStr,
			macdIndicator;
    Calendar 	tradeTime,
    			exDate,
    			payDate;
    BigDecimal 	price,
    			open,
    			dayHigh,
    			dayLow,
    			previousClose,
    			change,
    			changeInPercent,
    			dividendAnnualYield,
    			dividendAnnualYieldPercent;

	List<BigDecimal> historical_price = new ArrayList<BigDecimal>();
	MACD macd = new MACD();
	Button btnShow, btnClear;

    static DecimalFormat formatter = new DecimalFormat("#,###.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		display = (TextView) findViewById(R.id.displayData); 
		symbol_ET = (EditText) findViewById(R.id.insertSymbol);
		btnShow = (Button) findViewById(R.id.btnShow);
		btnClear = (Button) findViewById(R.id.btnClear);

		symbol_ET.addTextChangedListener(new TextWatcher() {
			 
            @Override
            public void afterTextChanged(Editable arg0) {
 
            }
 
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                    int arg2, int arg3) {
            }
 
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
                // collect the text from the edit control, and trim off spaces.
            	symbol_S = symbol_ET.getText().toString().trim();
            }
 
        });
	}
	
	public void onClick_Show(View view) throws IOException{
		btnClear.setVisibility(View.VISIBLE);
		btnShow.setVisibility(View.INVISIBLE);

		stock = YahooFinance.get(symbol_S);
		timeZone = stock.getQuote(true).getTimeZone();
		name = stock.getName();
		tradeTimeStr = stock.getQuote(true).getLastTradeTimeStr();
		tradeDateStr = stock.getQuote(true).getLastTradeDateStr();
		tradeTime = stock.getQuote(true).getLastTradeTime();
		price = stock.getQuote(true).getPrice();
		open = stock.getQuote(true).getOpen();
		dayHigh = stock.getQuote(true).getOpen();
		dayLow = stock.getQuote(true).getOpen();
		previousClose = stock.getQuote(true).getPreviousClose();
		change = stock.getQuote(true).getChange();
		changeInPercent = stock.getQuote(true).getChangeInPercent();
		historical_price = get_historical_data(stock);
		macd.calculate_MACD(historical_price);
		if(macd.is_buy()){
			macdIndicator = "Buy";
		}
		else if(macd.is_sell()){
			macdIndicator = "Sell";
		}
		else{
			macdIndicator = "No";
		}

		dispName 						= "Name	: " + name;
		dispPrice 						= "Last Price	: Rp" + formatter.format(price);
		dispTradeTimeStr 				= "Last Trade Time	: " + tradeTimeStr;
		dispTradeDateStr 				= "Last Trade Date	: " + tradeDateStr;
		if(tradeTime==null) {
			dispTradeTime				= "Last Trade Time	: null";
		} else {
			dispTradeTime 				= "Last Trade Time	: " + tradeTime.getTime();
		}
		dispPreviousClose				= "Previous Close	: Rp" + formatter.format(previousClose);
		dispChange 						= "Change	: Rp" + formatter.format(change) + " (" + changeInPercent + "%)";
		dispOpen 						= "Open	: Rp" + formatter.format(open);
		dispDayLow 						= "Low	: Rp" + formatter.format(dayLow);
		dispDayHigh 					= "High	: Rp" + formatter.format(dayHigh);
		displayText = dispName + "\n"
					+ dispPrice + "\n"
					+ dispTradeTime + "\n"
					+ dispPreviousClose + "\n"
					+ dispChange + "\n"
					+ dispOpen + "\n"
					+ dispDayLow + "\n"
					+ dispDayHigh + "\n"
					+ macdIndicator + "\n";



		display.setText(displayText);
	}
	public void clearAction(View view){
		btnClear.setVisibility(View.INVISIBLE);
		btnShow.setVisibility(View.VISIBLE);
		List<BigDecimal> historical_price = new ArrayList<BigDecimal>();
		MACD macd = new MACD();
	}
	/*
	private int priceFraction (int priceInput) {
		int price = 0;
		
		if(priceInput < 500) {
			price = 1;
		} else if(priceInput >= 500 && priceInput < 5000) {
			price = 5;
		} else if(priceInput >= 5000) {
			price = 25;
		}
		
		return price;
	}
	*/

	private List<BigDecimal> get_historical_data (Stock stock_name) throws IOException{
		Calendar from = Calendar.getInstance();
		from.add(Calendar.WEEK_OF_YEAR,-10);
		List<HistoricalQuote> hist = stock_name.getHistory(from, Interval.DAILY);
		for(HistoricalQuote dummy:hist){
			historical_price.add(dummy.getClose());
		}
        RSI albert = new RSI();
        albert.rsi_all(historical_price);
		return historical_price;
	}
}
