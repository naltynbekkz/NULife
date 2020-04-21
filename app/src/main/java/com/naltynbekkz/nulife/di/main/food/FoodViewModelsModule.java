package com.naltynbekkz.nulife.di.main.food;

import androidx.lifecycle.ViewModel;

import com.naltynbekkz.nulife.di.ViewModelAssistedFactory;
import com.naltynbekkz.nulife.di.ViewModelKey;
import com.naltynbekkz.nulife.ui.food.viewmodel.CafeDetailsViewModel;
import com.naltynbekkz.nulife.ui.food.viewmodel.CafeDetailsViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.food.viewmodel.CafeMenuViewModel;
import com.naltynbekkz.nulife.ui.food.viewmodel.CafeMenuViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.food.viewmodel.CafeViewModel;
import com.naltynbekkz.nulife.ui.food.viewmodel.CafeViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.food.viewmodel.CafesViewModel;
import com.naltynbekkz.nulife.ui.food.viewmodel.NewReviewViewModel;
import com.naltynbekkz.nulife.ui.food.viewmodel.NewReviewViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.food.viewmodel.ReviewViewModel;
import com.naltynbekkz.nulife.ui.food.viewmodel.ReviewViewModel_AssistedFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class FoodViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(CafesViewModel.class)
    abstract ViewModel bindCafesViewModel(CafesViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CafeDetailsViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindCafeDetailsViewModel(CafeDetailsViewModel_AssistedFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(CafeViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindCafeViewModel(CafeViewModel_AssistedFactory viewModelFactory);


    @Binds
    @IntoMap
    @ViewModelKey(CafeMenuViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindCafeMenuViewModel(CafeMenuViewModel_AssistedFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(ReviewViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindReviewViewModel(ReviewViewModel_AssistedFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(NewReviewViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindNewReviewViewModel(NewReviewViewModel_AssistedFactory viewModelFactory);

}
