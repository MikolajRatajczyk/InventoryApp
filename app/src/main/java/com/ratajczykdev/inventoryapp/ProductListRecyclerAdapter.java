package com.ratajczykdev.inventoryapp;
//  TODO: move outside package

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ratajczykdev.inventoryapp.data.ImageHelper;
import com.ratajczykdev.inventoryapp.database.Product;

import java.util.List;
import java.util.Locale;

/**
 * Provides {@link RecyclerView} with data from list of {@link Product}
 *
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk@gmail.com>
 */
public class ProductListRecyclerAdapter extends RecyclerView.Adapter<ProductListRecyclerAdapter.ProductViewHolder> {

    private final LayoutInflater layoutInflater;
    private List<Product> productList;
    private Context context;

    public static final String DATA_SELECTED_PRODUCT_ID = "DATA_SELECTED_PRODUCT_ID";

    /**
     * ViewHolder class
     */
    class ProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView textName;
        private final ImageView imagePhoto;
        private final TextView textPrice;
        private final TextView textQuantity;
        private final View listItemRootView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            imagePhoto = itemView.findViewById(R.id.image_photo);
            textPrice = itemView.findViewById(R.id.text_price);
            textQuantity = itemView.findViewById(R.id.text_quantity);
            listItemRootView = itemView.findViewById(R.id.product_list_item_root_linearlayout);
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
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {
        if (productList != null) {
            final Product currentProduct = productList.get(position);
            holder.textName.setText(currentProduct.getName());
            holder.textPrice.setText(String.format(Locale.US, "%.2f", currentProduct.getPrice()));
            holder.textQuantity.setText(String.valueOf(currentProduct.getQuantity()));
            setProductPhotoFromUri(holder, currentProduct);
            holder.listItemRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra(DATA_SELECTED_PRODUCT_ID, String.valueOf(currentProduct.getId()));
                    context.startActivity(intent);
                }
            });
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
