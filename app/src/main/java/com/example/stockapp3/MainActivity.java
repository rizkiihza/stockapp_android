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
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;


import yahoofinance.*;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

	MACD macd = new MACD();
	RSI cekRSI = new RSI();

	Button btnShow;

	List<Double> s_close = new ArrayList<Double>();
	List<Double> s_high = new ArrayList<Double>();
	List<Double> s_low = new ArrayList<Double>();
    List<String> s_date = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        btnShow = (Button) findViewById(R.id.btnShow);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_result("BBNI");
            }
        });
	}


	private void get_result(String stock_name) {
	    s_close.clear();
	    s_high.clear();
	    s_low.clear();
	    s_date.clear();

        new StockAppResult().execute(stock_name);
	}

    class StockAppResult extends AsyncTask<String, Void, JSONObject> {

        private Exception exception;

        protected JSONObject doInBackground(String... stock_name) {
            String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + stock_name[0] + ".JK&apikey=E5FDVTPST51Z1DXC";
            JSONObject response = new JSONObject();

            try{
                Log.d("Get Price", stock_name[0]);
                response = Utils.getJSONObjectFromURL(url);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return response;
        }

        protected void onPostExecute(JSONObject jsonResult) {
            // TODO: check this.exception
            // TODO: do something with the feed
            try {
                JSONObject timeSeries = jsonResult.getJSONObject("Time Series (Daily)");

                for(Iterator<String> iter = timeSeries.keys(); iter.hasNext();) {
                    String key = iter.next();

                    JSONObject price = timeSeries.getJSONObject(key);

                    // handle per kategori
                    Object close = price.get("4. close");
                    Object high = price.get("2. high");
                    Object low = price.get("3. low");

                    s_close.add(0, Double.parseDouble(close.toString()));
                    s_high.add(0, Double.parseDouble(high.toString()));
                    s_low.add(0, Double.parseDouble(low.toString()));
                    s_date.add(0, key);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
