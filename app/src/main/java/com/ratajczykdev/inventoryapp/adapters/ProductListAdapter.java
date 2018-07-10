package com.ratajczykdev.inventoryapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ratajczykdev.inventoryapp.R;
import com.ratajczykdev.inventoryapp.data.ImageHelper;
import com.ratajczykdev.inventoryapp.database.Product;

import java.util.List;
import java.util.Locale;

public class ProductListAdapter extends ArrayAdapter<Product> {
    //  TODO: refactor

    private Context context;
    private List<Product> productsList;
    private TextView textName;
    private ImageView imagePhoto;
    private TextView textPrice;
    private TextView textQuantity;


    private ProductListAdapter(@NonNull Context context, List<Product> productsList) {
        super(context, 0, productsList);
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
        }
        Product currentProduct = productsList.get(position);

        textName = listItem.findViewById(R.id.text_name);
        textName.setText(currentProduct.getName());

        imagePhoto = listItem.findViewById(R.id.image_photo);
        setPhoto(currentProduct, imagePhoto);

        textPrice = listItem.findViewById(R.id.text_price);
        textPrice.setText(String.format(Locale.US, "%.2f", currentProduct.getPrice()));

        textQuantity = listItem.findViewById(R.id.text_quantity);
        textQuantity.setText(String.valueOf(currentProduct.getQuantity()));

        return listItem;
    }

    private void setPhoto(Product currentProduct, ImageView imagePhoto) {
        if (currentProduct.getPhotoUri() != null) {
            String stringPhotoUri = currentProduct.getPhotoUri();
            Uri photoUri = Uri.parse(stringPhotoUri);
            Bitmap photoBitmap = ImageHelper.getBitmapFromUri(photoUri, context);
            imagePhoto.setImageBitmap(photoBitmap);
        }
    }
}
