package com.naltynbekkz.nulife.di.main;

import androidx.lifecycle.ViewModel;

import com.naltynbekkz.nulife.di.ViewModelKey;
import com.naltynbekkz.nulife.di.auth.AuthScope;
import com.naltynbekkz.nulife.ui.MainViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindMainViewModel(MainViewModel viewModel);

}
