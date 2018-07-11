package com.ratajczykdev.inventoryapp.database;
//  TODO: move outside package

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ratajczykdev.inventoryapp.R;
import com.ratajczykdev.inventoryapp.data.ImageHelper;

import java.util.List;
import java.util.Locale;

public class ProductListRecyclerAdapter extends RecyclerView.Adapter<ProductListRecyclerAdapter.ProductViewHolder> {

    private final LayoutInflater layoutInflater;
    private List<Product> productList;
    private Context context;

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

    public ProductListRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
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
            holder.textPrice.setText(String.format(Locale.US, "%.2f", currentProduct.getPrice()));
            holder.textQuantity.setText(String.valueOf(currentProduct.getQuantity()));
            //  TODO: check if working
            setProductPhotoFromUri(holder, currentProduct);
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
    public void setProducts(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    /**
     * Helper method
     */
    private void setProductPhotoFromUri(ProductViewHolder holder, Product currentProduct) {
        if (currentProduct.getPhotoUri() != null) {
            String stringPhotoUri = currentProduct.getPhotoUri();
            Uri photoUri = Uri.parse(stringPhotoUri);
            Bitmap photoBitmap = ImageHelper.getBitmapFromUri(photoUri, context);
            holder.imagePhoto.setImageBitmap(photoBitmap);
        }
    }

}
