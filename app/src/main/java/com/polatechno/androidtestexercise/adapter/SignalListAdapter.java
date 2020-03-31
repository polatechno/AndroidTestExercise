package com.polatechno.androidtestexercise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.polatechno.androidtestexercise.AppUtils.MyHelperMethods;
import com.polatechno.androidtestexercise.R;
import com.polatechno.androidtestexercise.model.SignalItem;
import com.polatechno.androidtestexercise.adapter.viewHolders.BaseViewHolder;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SignalListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private String appLang = "";
    private Context context;
    private List<SignalItem> mSignalList;


    public SignalListAdapter(Context mContext, List<SignalItem> signalItems) {
        this.mSignalList = signalItems;
        this.context = mContext;
    }

    public void addItems(List<SignalItem> signalItems) {
        mSignalList.clear();
        mSignalList.addAll(signalItems);
        notifyDataSetChanged();
    }

    public void clearAll() {
        mSignalList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.signal_list_item, parent, false));

    }

    @Override
    public int getItemCount() {
        if (mSignalList != null && mSignalList.size() > 0) {
            return mSignalList.size();
        } else {
            return 0;
        }
    }


    public class ViewHolder extends BaseViewHolder {


        TextView tvPair,
                tvComment,
                tvCmd,
                tvTradingSystem,
                tvPeriod,
                tvPrice,
                tvSl,
                tvTp,
                tvActualTime,
                tvId;


        private ViewHolder(View itemView) {
            super(itemView);
            tvPair = itemView.findViewById(R.id.tvPair);
            tvActualTime = itemView.findViewById(R.id.tvActualTime);
            tvCmd = itemView.findViewById(R.id.tvCmd);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvTradingSystem = itemView.findViewById(R.id.tvTradingSystem);
            tvPeriod = itemView.findViewById(R.id.tvPeriod);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSl = itemView.findViewById(R.id.tvSl);
            tvTp = itemView.findViewById(R.id.tvTp);
            tvId = itemView.findViewById(R.id.tvId);
        }

        @Override
        protected void clear() {
            tvPair.setText("");
            tvActualTime.setText("");
            tvCmd.setText("");
            tvComment.setText("");
            tvTradingSystem.setText("");
            tvPeriod.setText("");
            tvPrice.setText("");
            tvSl.setText("");
            tvTp.setText("");
            tvId.setText("");
        }


        public void onBind(int position) {
            super.onBind(position);

            final SignalItem currentSignal = mSignalList.get(position);
            tvPair.setText(currentSignal.getPair());
            tvActualTime.setText(MyHelperMethods.unixTimeToHumanReadable(currentSignal.getActualTime()) );
            tvCmd.setText(context.getString(R.string.prefix_cmd) + currentSignal.getCmd());
            tvComment.setText(currentSignal.getComment());
            tvTradingSystem.setText(context.getString(R.string.prefix_tradsys) + currentSignal.getTradingSystem());

            tvPeriod.setText( currentSignal.getPeriod().length() > 0 ?  context.getString(R.string.prefix_period) + currentSignal.getPeriod() : "" ) ;

            tvPrice.setText(context.getString(R.string.prefix_price)+ currentSignal.getPrice());
            tvSl.setText(context.getString(R.string.prefix_sl)+currentSignal.getSl());
            tvTp.setText(context.getString(R.string.prefix_tp) +currentSignal.getTp());
            tvId.setText(context.getString(R.string.prefix_id) + currentSignal.getId());

        }

    }


}