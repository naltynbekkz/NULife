package com.naltynbekkz.nulife.di.main.timetable;

import androidx.lifecycle.ViewModel;

import com.naltynbekkz.nulife.di.ViewModelAssistedFactory;
import com.naltynbekkz.nulife.di.ViewModelKey;
import com.naltynbekkz.nulife.ui.timetable.viewmodel.MonthViewModel;
import com.naltynbekkz.nulife.ui.timetable.viewmodel.MonthViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.timetable.viewmodel.NewOccurrenceViewModel;
import com.naltynbekkz.nulife.ui.timetable.viewmodel.NewOccurrenceViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.timetable.viewmodel.WeekdayViewModel;
import com.naltynbekkz.nulife.ui.timetable.viewmodel.WeekdayViewModel_AssistedFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class TimetableViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(NewOccurrenceViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindNewOccurrenceViewModel(NewOccurrenceViewModel_AssistedFactory viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MonthViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindMonthViewModel(MonthViewModel_AssistedFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(WeekdayViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindWeekdayViewModel(WeekdayViewModel_AssistedFactory viewModelFactory);



}
