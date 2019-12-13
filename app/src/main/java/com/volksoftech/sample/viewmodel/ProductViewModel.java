package com.volksoftech.sample.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.volksoftech.sample.DataRepository;
import com.volksoftech.sample.MyApplication;
import com.volksoftech.sample.db.entity.CommentEntity;
import com.volksoftech.sample.db.entity.ProductEntity;

import java.util.List;


public class ProductViewModel extends AndroidViewModel {

    private final LiveData<ProductEntity> mObservableProduct;
    private final LiveData<List<CommentEntity>> mObservableComments;
    public ObservableField<ProductEntity> product = new ObservableField<>();

    private ProductViewModel(@NonNull Application application, DataRepository repository,
                             final int productId) {
        super(application);

        mObservableComments = repository.loadComments(productId);
        mObservableProduct = repository.loadProduct(productId);
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
    public LiveData<List<CommentEntity>> getComments() {
        return mObservableComments;
    }

    public LiveData<ProductEntity> getObservableProduct() {
        return mObservableProduct;
    }

    public void setProduct(ProductEntity product) {
        this.product.set(product);
    }

    /**
     * A creator is used to inject the product ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the product ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mProductId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int productId) {
            mApplication = application;
            mProductId = productId;
            mRepository = ((MyApplication) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ProductViewModel(mApplication, mRepository, mProductId);
        }
    }
}