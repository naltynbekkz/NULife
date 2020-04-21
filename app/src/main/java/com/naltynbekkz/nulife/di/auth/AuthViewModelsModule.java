package com.naltynbekkz.nulife.di.auth;

import androidx.lifecycle.ViewModel;

import com.naltynbekkz.nulife.di.ViewModelKey;
import com.naltynbekkz.nulife.ui.auth.AuthViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AuthViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel.class)
    public abstract ViewModel bindAuthViewModel(AuthViewModel viewModel);

}
