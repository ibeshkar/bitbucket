package com.vv0z.currencywatcher.activities;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.vv0z.currencywatcher.R;
import com.vv0z.currencywatcher.adapters.RatesAdapter;
import com.vv0z.currencywatcher.models.Currency;
import com.vv0z.currencywatcher.tasks.ApiClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RatesActivity extends AppCompatActivity {

    @BindView(R.id.rv_currencies_rates)
    RecyclerView AllRates;

    private static final String TAG = "RatesActivity";
    private RatesAdapter adapter;
    public String base = "EUR";
    private Disposable disposable;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public double val = 1.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();

    }

    private void init() {

        AllRates.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RatesAdapter(new ArrayList<>());
        AllRates.setAdapter(adapter);

        // Get rates from server
        startUpdating();
    }

    /**
     * Update rates every on second
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void startUpdating() {

        disposable = Observable.interval(1000, TimeUnit.MILLISECONDS, Schedulers.io())
                .map(__ -> ApiClient.getApiInterface().getCurrencyRates("latest?base=" + base))
                .observeOn(Schedulers.newThread())
                .subscribe(observable -> observable.subscribe(result -> {

                    if (result != null) {

                        JSONObject rates = new JSONObject(new Gson().toJson(result.getRates()));

                        if (rates.length() > 0) {

                            List<Currency> currencies = new ArrayList<>();
                            currencies.add(0, new Currency(base, 1.00 * val));

                            for (int i = 0; i < rates.length(); i++) {

                                Currency currency = new Currency();
                                currency.setUnit(rates.names().get(i).toString());
                                currency.setValue((Double) rates.get(rates.names().get(i).toString()) * val);
                                currencies.add(currency);
                            }

                            runOnUiThread(() -> adapter.setList(currencies));

                        }
                    }

                }, throwable -> {

                    Log.d(TAG, "get_rates_error-> " + throwable.getMessage());

                }));

        compositeDisposable.add(disposable);
    }

    /**
     * Stop interval for change data
     */
    public void stopUpdating() {

        if (disposable != null) {

            compositeDisposable.clear();
            disposable.dispose();
        }
    }
}
