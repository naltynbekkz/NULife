package com.naltynbekkz.nulife.di.main.clubs;

import androidx.lifecycle.ViewModel;

import com.naltynbekkz.nulife.di.ViewModelAssistedFactory;
import com.naltynbekkz.nulife.di.ViewModelKey;
import com.naltynbekkz.nulife.ui.clubs.viewmodel.ClubViewModel;
import com.naltynbekkz.nulife.ui.clubs.viewmodel.ClubViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.clubs.viewmodel.ClubsViewModel;
import com.naltynbekkz.nulife.ui.clubs.viewmodel.ClubsViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.clubs.viewmodel.EventViewModel;
import com.naltynbekkz.nulife.ui.clubs.viewmodel.EventViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.clubs.viewmodel.EventsViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ClubsViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ClubsViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindClubsViewModel(ClubsViewModel_AssistedFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(EventsViewModel.class)
    abstract ViewModel bindEventsViewModel(EventsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EventViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindEventViewModel(EventViewModel_AssistedFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(ClubViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindClubViewModel(ClubViewModel_AssistedFactory viewModelFactory);


}
