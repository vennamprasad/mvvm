package com.volksoftech.sample.view;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.volksoftech.sample.R;
import com.volksoftech.sample.databinding.ListFragmentBinding;
import com.volksoftech.sample.db.entity.ProductEntity;
import com.volksoftech.sample.model.Product;
import com.volksoftech.sample.viewmodel.ProductListViewModel;

import java.util.List;
import java.util.Objects;

public class ProductListFragment extends Fragment {

    public static final String TAG = "ProductListFragment";
    private final ProductClickCallback mProductClickCallback = new ProductClickCallback() {
        @Override
        public void onClick(Product product) {

            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((SampleRoomActivity) Objects.requireNonNull(getActivity())).show(product);
            }
        }
    };
    private ProductAdapter mProductAdapter;
    private ListFragmentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false);

        mProductAdapter = new ProductAdapter(mProductClickCallback);
        mBinding.productsList.setAdapter(mProductAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ProductListViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ProductListViewModel.class);

        mBinding.productsSearchBtn.setOnClickListener(v -> {
            Editable query = mBinding.productsSearchBox.getText();
            if (query == null || query.toString().isEmpty()) {
                subscribeUi(viewModel.getProducts());
            } else {
                subscribeUi(viewModel.searchProducts("*" + query + "*"));
            }
        });

        subscribeUi(viewModel.getProducts());
    }

    private void subscribeUi(LiveData<List<ProductEntity>> liveData) {
        // Update the list when the data changes
        liveData.observe(this, new Observer<List<ProductEntity>>() {
            @Override
            public void onChanged(@Nullable List<ProductEntity> myProducts) {
                if (myProducts != null) {
                    mBinding.setIsLoading(false);
                    mProductAdapter.setProductList(myProducts);
                } else {
                    mBinding.setIsLoading(true);
                }
                // espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
                mBinding.executePendingBindings();
            }
        });
    }
}
