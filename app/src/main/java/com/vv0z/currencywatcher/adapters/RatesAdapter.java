package com.vv0z.currencywatcher.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding3.view.RxView;
import com.vv0z.currencywatcher.R;
import com.vv0z.currencywatcher.activities.RatesActivity;
import com.vv0z.currencywatcher.models.Currency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;

public class RatesAdapter extends RecyclerView.Adapter<RatesAdapter.ViewHolder> {

    private Context context;
    private List<Currency> currencies;
    private RecyclerView recyclerView;


    public RatesAdapter(List<Currency> currencies) {

        this.currencies = currencies;
    }

    public void setList(List<Currency> currencies) {

        this.currencies = currencies;
        this.notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RatesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        this.context = parent.getContext();
        View item = LayoutInflater.from(context).inflate(R.layout.rate_row, parent, false);
        return new ViewHolder(item);

    }

    @SuppressWarnings("MalformedFormatString")
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull RatesAdapter.ViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {

        holder.position = position;

        Currency currency = currencies.get(position);
        holder.currencyUnit.setText(currency.getUnit());
        holder.currencyValue.setText(String.format("%.2d", currency.getValue()));

        if (position == 0) {

            holder.currencyValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    ((RatesActivity) context).stopUpdating();
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                }

                @Override
                public void afterTextChanged(Editable editable) {

                    ((RatesActivity) context).val = Double.parseDouble(editable.toString());

                    for (Currency curr : currencies) {

                        curr.setValue(curr.getValue() * ((RatesActivity) context).val);
                    }

                    recyclerView.post(() -> notifyDataSetChanged());

                    new Handler().postDelayed(() -> ((RatesActivity) context).startUpdating(), 1000);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_currency_unit)
        TextView currencyUnit;
        @BindView(R.id.et_currency_value)
        EditText currencyValue;
        @BindView(R.id.et_temp)
        EditText temp;

        int position = 0;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(view -> {

                if (position != 0) {

                    Currency item = currencies.get(position);
                    currencies.remove(item);
                    currencies.add(0, item);
                    notifyItemMoved(position, 0);
                    recyclerView.scrollToPosition(0);
                    ((RatesActivity) context).base = item.getUnit();
                    ((RatesActivity) context).val = item.getValue();
                }
            });

            ButterKnife.bind(this, itemView);

        }
    }

}
