package com.naltynbekkz.nulife.di.main.profile;

import androidx.lifecycle.ViewModel;

import com.naltynbekkz.nulife.di.ViewModelKey;
import com.naltynbekkz.nulife.ui.profile.viewmodel.CategoriesViewModel;
import com.naltynbekkz.nulife.ui.profile.viewmodel.EditProfileViewModel;
import com.naltynbekkz.nulife.ui.profile.viewmodel.ProfileViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ProfileViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    abstract ViewModel bindProfileViewModel(ProfileViewModel viewModel);


    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel.class)
    abstract ViewModel bindCategoriesViewModel(CategoriesViewModel viewModel);


    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel.class)
    abstract ViewModel bindEditProfileViewModel(EditProfileViewModel viewModel);

}
