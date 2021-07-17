package in.softment.playrangers.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.softment.playrangers.Model.TransactionModel;
import in.softment.playrangers.R;
import in.softment.playrangers.Services.Service;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {


    private ArrayList<TransactionModel> transactionModels;
    private Context context;
    public TransactionAdapter(Context context, ArrayList<TransactionModel> transactionModels) {
        this.transactionModels = transactionModels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.setIsRecyclable(false);
            TransactionModel transactionModel = transactionModels.get(position);

            if (transactionModel.getType().equalsIgnoreCase("credit")) {
                holder.amountView.setText("+"+transactionModel.getAmount());
                holder.amountView.setTextColor(context.getResources().getColor(R.color.successful));
            }
            else {
                holder.amountView.setText("-"+transactionModel.getAmount());
                holder.amountView.setTextColor(context.getResources().getColor(R.color.failed));
            }

            if (transactionModel.isSuccessful()) {
                holder.isSuccessView.setText("Successful");
                holder.isSuccessView.setTextColor(context.getResources().getColor(R.color.successful));
            }
            else {
                holder.isSuccessView.setText("Failed");
                holder.isSuccessView.setTextColor(context.getResources().getColor(R.color.failed));
            }

            if (transactionModel.getTitle() != null) {
                holder.titleView.setText(transactionModel.getTitle());
            }
            else {
                holder.titleView.setText("Info Not Available");
            }

            if (transactionModel.getOrderId() != null) {
                 holder.orderIdView.setText(transactionModel.getOrderId());
            }
            else {
                holder.titleView.setText("Info Not Available");
            }

            holder.dateView.setText(Service.convertDate(transactionModel.getDate()));

    }

    @Override
    public int getItemCount() {
        return transactionModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView titleView, orderIdView, dateView, amountView, isSuccessView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgTran);
            titleView = itemView.findViewById(R.id.title);
            orderIdView = itemView.findViewById(R.id.orderid);
            dateView = itemView.findViewById(R.id.date);
            amountView = itemView.findViewById(R.id.amount);
            isSuccessView = itemView.findViewById(R.id.isSuccessful);
        }
    }
}
