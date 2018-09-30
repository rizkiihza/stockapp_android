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

	List<BigDecimal> historical_quote = new ArrayList<BigDecimal>();
	List<BigDecimal> historical_low = new ArrayList<BigDecimal>();
	List<BigDecimal> historical_high = new ArrayList<BigDecimal>();
	MACD macd = new MACD();
	RSI cekRSI = new RSI();

	Button btnShow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


	}


	private void get_price(Stock stock_name) {

	}
}
