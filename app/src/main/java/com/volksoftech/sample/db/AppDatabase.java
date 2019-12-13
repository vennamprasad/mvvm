package com.volksoftech.sample.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.volksoftech.sample.AppExecutors;
import com.volksoftech.sample.db.converter.DateConverter;
import com.volksoftech.sample.db.dao.CommentDao;
import com.volksoftech.sample.db.dao.ProductDao;
import com.volksoftech.sample.db.entity.CommentEntity;
import com.volksoftech.sample.db.entity.ProductEntity;
import com.volksoftech.sample.db.entity.ProductFtsEntity;

import java.util.List;

@Database(entities = {ProductEntity.class, ProductFtsEntity.class, CommentEntity.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    @VisibleForTesting
    private static final String DATABASE_NAME = "basic-mvvm-db";
    private static AppDatabase sInstance;
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext(), executors);
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext, final AppExecutors executors) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(() -> {
                            // Add a delay to simulate a long-running operation
                            try {
                                addDelay();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            // Generate the data for pre-population
                            AppDatabase database = AppDatabase.getInstance(appContext, executors);
                            List<ProductEntity> products = DataGenerator.generateProducts();
                            List<CommentEntity> comments = DataGenerator.generateCommentsForProducts(products);
                            insertData(database, products, comments);
                            // notify that the database was created and it's ready to be used
                            database.setDatabaseCreated();
                        });
                    }
                }).build();
    }

    private static void insertData(final AppDatabase database, final List<ProductEntity> products, final List<CommentEntity> comments) {
        database.runInTransaction(() -> {
            database.productDao().insertAll(products);
            database.commentDao().insertAll(comments);
        });
    }

    private static void addDelay() throws InterruptedException {
        Thread.sleep(4000);
    }

    public abstract ProductDao productDao();

    public abstract CommentDao commentDao();

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}