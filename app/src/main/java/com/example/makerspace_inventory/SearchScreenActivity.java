package com.example.makerspace_inventory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;


import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class SearchScreenActivity extends AppCompatActivity {

    public static ArrayList<Item> itemList = MainActivity.itemList;

    private ListView listView;
    private String currentSearchText = "";
    private SearchView searchView;
    private MaterialToolbar mToolbar;
    private ActionBar ab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);

        //Set up toolbar at the top of the screen.
        mToolbar = findViewById(R.id.searchScreenToolbar);
        setSupportActionBar(mToolbar);
        ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayShowHomeEnabled(false);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            // Handle navigation icon press
            @Override
            public void onClick(View view) {
                Intent previousIntent = new Intent(SearchScreenActivity.this, MainActivity.class);
                startActivity(previousIntent);
            }
        });

        Intent previousIntent = getIntent();
        boolean isScannedSearch = previousIntent.getBooleanExtra("isScannedItem",false);

        //Check whether the search is from a scanned QR code or a regular search.
        if(isScannedSearch){
            handleScannedSearch();
        }else{
            initSearchWidgets();
            setUpList();
        }
        setUpOnclickListener();
    }

    /** Handle results for a scanned QR code */
    private void handleScannedSearch() {
        Intent previousIntent = getIntent();
        String scannedCode = previousIntent.getStringExtra("resultCode");
        setUpList();

        ArrayList<Item> filteredItems = new ArrayList<Item>();

        //Search for the QR code String in the search tags.
        for(Item item: itemList) {
            if(item.getTags().toLowerCase().contains(scannedCode.toLowerCase())) {
                filteredItems.add(item);
            }
        }
        ItemAdapter adapter = new ItemAdapter(getApplicationContext(), 0, filteredItems);
        listView.setAdapter(adapter);

        //Show error message if no items are found.
        if(filteredItems.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(SearchScreenActivity.this);
            builder.setTitle("Error");
            builder.setMessage("No matching item found for: "+ scannedCode + ".");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    }

    /** Creates menu at the top of the screen. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.searchscreen_menu, menu);

        return true;
    }

    /** Handles the selection of menu items. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.saved:
                return true;
            case R.id.scan:
                scanCode();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Start the CaptureActivity from the home screen */
    private void scanCode(){
        Intent scanOnMain = new Intent(SearchScreenActivity.this,MainActivity.class);
        scanOnMain.putExtra("startScan",true);
        startActivity(scanOnMain);
        finish();
    }

    /** Manage the searchbar input.
     *  Update the result list after each character input.
     */
    private void initSearchWidgets() {
        searchView = (SearchView) findViewById(R.id.itemListSearchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                currentSearchText = s;
                ArrayList<Item> filteredItems = new ArrayList<Item>();

                for(Item item: itemList) {
                    if(item.getItemname().toLowerCase().contains(s.toLowerCase()) || item.getTags().toLowerCase().contains(s.toLowerCase())) {
                        filteredItems.add(item);
                    }
                }
                ItemAdapter adapter = new ItemAdapter(getApplicationContext(), 0, filteredItems);
                listView.setAdapter(adapter);

                return false;
            }
        });
    }

    private void setUpList() {
        listView = (ListView) findViewById(R.id.itemsListView);

        ItemAdapter adapter = new ItemAdapter(getApplicationContext(), 0, itemList);
        listView.setAdapter(adapter);
    }

    /** Start the DetailedViewActivity of the selected item. */
    private void setUpOnclickListener()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                boolean isScannedItem = false;
                Item selectedItem = (Item) (listView.getItemAtPosition(position));
                Intent showDetail = new Intent(getApplicationContext(), DetailedItemActivity.class);
                showDetail.putExtra("list_id",selectedItem.getListId());
                showDetail.putExtra("isScannedItem", isScannedItem);
                startActivity(showDetail);
            }
        });
    }
}