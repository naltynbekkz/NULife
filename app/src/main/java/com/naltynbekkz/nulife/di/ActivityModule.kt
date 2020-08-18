package com.naltynbekkz.nulife.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.database.BaseDatabase
import com.naltynbekkz.core.ImageCompressor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @ActivityScoped
    @Provides
    fun provideResourcesDao(database: BaseDatabase) = database.resourcesDao()

    @ActivityScoped
    @Provides
    fun provideOccurrencesDao(database: BaseDatabase) = database.occurrencesDao()

    @ActivityScoped
    @Provides
    fun provideAuthGoogleSignInClient(@ApplicationContext context: Context) =
        GoogleSignIn.getClient(
            context, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )

    @ActivityScoped
    @Provides
    fun provideFirebaseInstanceId() = FirebaseInstanceId.getInstance()


    @ActivityScoped
    @Provides
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance()


    @ActivityScoped
    @Provides
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()


    @ActivityScoped
    @Provides
    fun provideImageCompressor(@ApplicationContext context: Context): ImageCompressor {
        return ImageCompressor(context.contentResolver)
    }

}