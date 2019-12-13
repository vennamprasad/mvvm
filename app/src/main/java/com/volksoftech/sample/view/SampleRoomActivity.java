package com.volksoftech.sample.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.volksoftech.sample.R;
import com.volksoftech.sample.model.Product;

public class SampleRoomActivity extends AppCompatActivity {
    private static final String TAG = "SampleRoomActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_room);
        // Add product list fragment if this is first creation
        if (savedInstanceState == null) {
            ProductListFragment fragment = new ProductListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, TAG).commit();
        }
    }

    /**
     * Shows the product detail fragment
     */
    public void show(Product product) {

        ProductFragment productFragment = ProductFragment.forProduct(product.getId());

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("product")
                .replace(R.id.fragment_container,
                        productFragment, null).commit();
    }
}
