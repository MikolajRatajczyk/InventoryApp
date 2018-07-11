package com.ratajczykdev.inventoryapp.database;
//  TODO: move outside package

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ratajczykdev.inventoryapp.R;

import java.util.List;
import java.util.Locale;

public class ProductListRecyclerAdapter extends RecyclerView.Adapter<ProductListRecyclerAdapter.ProductViewHolder> {

    private final LayoutInflater layoutInflater;
    private List<Product> productList;

    /**
     * ViewHolder class
     */
    class ProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView textName;
        private final ImageView imagePhoto;
        private final TextView textPrice;
        private final TextView textQuantity;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            imagePhoto = itemView.findViewById(R.id.image_photo);
            textPrice = itemView.findViewById(R.id.text_price);
            textQuantity = itemView.findViewById(R.id.text_quantity);
        }
    }

    ProductListRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.product_list_item, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (productList != null) {
            Product currentProduct = productList.get(position);
            holder.textName.setText(currentProduct.getName());
            //  TODO: make photo set method
            holder.textPrice.setText(String.format(Locale.US, "%.2f", currentProduct.getPrice()));
            holder.textQuantity.setText(String.valueOf(currentProduct.getQuantity()));
        } else {
            //  TODO: add empty list view, delete this
            holder.textName.setText("No product available");
        }
    }

    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        } else {
            return 0;
        }
    }

    /**
     * Use of notifyDataSetChanged()
     */
    void setProducts(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

}
