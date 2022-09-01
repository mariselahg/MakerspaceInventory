package com.example.makerspace_inventory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;

import org.bson.Document;

import io.realm.mongodb.mongo.MongoCollection;

public class DetailedItemActivity extends AppCompatActivity {
    MongoCollection<Item> mongoCollection = MainActivity.mongoCollection;

    Item selectedItem;
    String quantityStr;
    int quantity;
    Integer serialNr;
    private MaterialToolbar mToolbar;
    LinearLayout tagsView;
    boolean showTags = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_item);

        setUpToolbar();

        tagsView = (LinearLayout) findViewById(R.id.tagsView);

        /* Check whether the detailed view belongs to a scanned item or an item selected from a result list */
        Intent previousIntent = getIntent();
        boolean isScannedItem = previousIntent.getBooleanExtra("isScannedItem",false);
        if(isScannedItem){
            getScannedItem();
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                // Handle navigation icon press
                @Override
                public void onClick(View view) {
                    Intent previousIntent = new Intent(DetailedItemActivity.this, MainActivity.class);
                    startActivity(previousIntent);
                }
            });
        }else{
            getSelectedItem();
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                // Handle navigation icon press
                @Override
                public void onClick(View view) {
                    Intent previousIntent = new Intent(DetailedItemActivity.this, SearchScreenActivity.class);
                    startActivity(previousIntent);
                }
            });
        }
        setValues();
    }

    /** Set up top ActionBar */
    private void setUpToolbar() {
        mToolbar = findViewById(R.id.detailedItemToolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayShowHomeEnabled(false);
    }

    /**
     * Called from onCreate() method if the item was scanned.
     * Finds the item matching the serial number scanned.
     */
    private void getScannedItem(){
        Intent previousIntent = getIntent();
        Integer parsedSerialNr = Integer.valueOf(previousIntent.getIntExtra("resultSerialNr",0));
        for(Item item: MainActivity.itemList) {
            serialNr = Integer.valueOf(item.getSerialnumber());
            if(serialNr.equals(parsedSerialNr)) {
                selectedItem = item;
                updateSelectedItem(selectedItem);
                quantity = selectedItem.getQuantity();
            }
        }
        if(selectedItem == null){
            Intent exitIntent = new Intent(DetailedItemActivity.this, MainActivity.class);
            exitIntent.putExtra("failedScan",true);
            exitIntent.putExtra("invalidSerialNr",parsedSerialNr.toString());
            startActivity(exitIntent);
            finish();
        }
    }

    private void getSelectedItem()
    {
        Intent previousIntent = getIntent();
        int parsedID = previousIntent.getIntExtra("list_id",0);
        selectedItem = MainActivity.itemList.get(Integer.valueOf(parsedID));
        updateSelectedItem(selectedItem);
    }

    /** Retrieves the quantity value of the selected item to guarantee synchronized values between app and database */
    private void updateSelectedItem(Item inputItem){
        Document queryFilter  = new Document("serialnumber", inputItem.getSerialnumber());
        mongoCollection.findOne(queryFilter).getAsync(task -> {
            if (task.isSuccess()) {
                selectedItem = task.get();
                Log.v("EXAMPLE", "successfully found a document: " + selectedItem);
                setValues();
                quantity = selectedItem.getQuantity();
            } else {
                Log.e("EXAMPLE", "failed to find document with: ", task.getError());
            }
        });
    }

    private void setValues()
    {
        TextView tv = (TextView) findViewById(R.id.itemName);
        ImageView iv = (ImageView) findViewById(R.id.itemImage);
        TextView tvRoom = (TextView) findViewById(R.id.itemRoom);
        TextView tvLocker = (TextView) findViewById(R.id.itemLocker);
        TextView tvCompartment = (TextView) findViewById(R.id.itemCompartment);
        TextView tvQuantity = (TextView) findViewById(R.id.itemQuantity);
        TextView tvUnit = (TextView) findViewById(R.id.itemUnit);
        TextView tvInfo = (TextView) findViewById(R.id.itemInformation);
        TextView tvTags = (TextView) findViewById(R.id.itemTags);


        tv.setText(selectedItem.getItemname());
        Glide.with(this)
                .load(selectedItem.getImage_url())
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(iv);
        tvRoom.setText(selectedItem.getRoom());
        tvLocker.setText(selectedItem.getLocker());
        tvCompartment.setText(selectedItem.getCompartment());
        quantityStr = String.valueOf(selectedItem.getQuantity());
        tvQuantity.setText(quantityStr);
        tvUnit.setText(selectedItem.getUnit() + ":");
        tvInfo.setText(selectedItem.getInfo().trim());
        tvTags.setText(selectedItem.getTags().trim());
    }

    /** Increase the quantity value of an item and updates the database */
    public void onClickPlus(View view){
        quantity++;

        TextView tvQuantity = (TextView) findViewById(R.id.itemQuantity);
        quantityStr = String.valueOf(quantity);
        tvQuantity.setText(quantityStr);

        Document queryFilter = new Document("serialnumber", selectedItem.getSerialnumber());
        Document updateDocument = new Document("$set", new Document("quantity", quantity));
        mongoCollection.updateOne(queryFilter, updateDocument).getAsync(task -> {
            if (task.isSuccess()) {
                long count = task.get().getModifiedCount();
                if (count == 1) {
                    Log.v("EXAMPLE", "successfully updated a document.");
                } else {
                    Log.v("EXAMPLE", "did not update a document.");
                }
            } else {
                Log.e("EXAMPLE", "failed to update document with: ", task.getError());
            }
        });
    }

    /** Reduce the quantity value of an item and updates the database */
    public void onClickMinus(View view){
        quantity--;

        TextView tvQuantity = (TextView) findViewById(R.id.itemQuantity);
        quantityStr = String.valueOf(quantity);
        tvQuantity.setText(quantityStr);

        Document queryFilter = new Document("serialnumber", selectedItem.getSerialnumber());
        Document updateDocument = new Document("$set", new Document("quantity", quantity));
        mongoCollection.updateOne(queryFilter, updateDocument).getAsync(task -> {
            if (task.isSuccess()) {
                long count = task.get().getModifiedCount();
                if (count == 1) {
                    Log.v("EXAMPLE", "successfully updated a document.");
                } else {
                    Log.v("EXAMPLE", "did not update a document.");
                }
            } else {
                Log.e("EXAMPLE", "failed to update document with: ", task.getError());
            }
        });
    }

    /** Manage the dropdown view of search tags */
    public void manageTags(View view){
        if(showTags){
            showTags();
        }else{
            hideTags();
        }
    }

    private void showTags(){
        TextView tags = (TextView) findViewById(R.id.itemTags);
        tags.setVisibility(View.VISIBLE);
        ImageView dropdownIcon = (ImageView) findViewById(R.id.dropdownIcon);
        dropdownIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
        showTags = false;
    }

    private void hideTags(){
        TextView tags = (TextView) findViewById(R.id.itemTags);
        tags.setVisibility(View.GONE);
        ImageView dropdownIcon = (ImageView) findViewById(R.id.dropdownIcon);
        dropdownIcon.setImageResource(R.drawable.ic_baseline_arrow_right_24);
        showTags = true;
    }
}