package dev.ratajczyk.inventoryapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import java.util.Date;

/**
 * Data model class for the database
 * <p>
 * Represents single product and products table name
 *
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk(AT)gmail.com>
 */
@Entity(tableName = "products_table")
public class Product {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String photoUri;
    private float price;
    private int quantity;
    @ColumnInfo(name = "creation_date")
    private Date creationDate;

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
