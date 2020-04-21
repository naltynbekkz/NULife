package com.naltynbekkz.nulife.di.main.courses;

import androidx.lifecycle.ViewModel;

import com.naltynbekkz.nulife.di.ViewModelAssistedFactory;
import com.naltynbekkz.nulife.di.ViewModelKey;
import com.naltynbekkz.nulife.ui.courses.answers.viewmodel.AnswersViewModel;
import com.naltynbekkz.nulife.ui.courses.answers.viewmodel.AnswersViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.courses.answers.viewmodel.CommentsViewModel;
import com.naltynbekkz.nulife.ui.courses.answers.viewmodel.CommentsViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.courses.answers.viewmodel.NewAnswerViewModel;
import com.naltynbekkz.nulife.ui.courses.answers.viewmodel.NewAnswerViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.courses.courses.viewmodel.CoursesViewModel;
import com.naltynbekkz.nulife.ui.courses.courses.viewmodel.EditCourseViewModel;
import com.naltynbekkz.nulife.ui.courses.courses.viewmodel.EditCourseViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.courses.courses.viewmodel.EnrollCourseViewModel;
import com.naltynbekkz.nulife.ui.courses.courses.viewmodel.EnrollCourseViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.courses.courses.viewmodel.EnrollViewModel;
import com.naltynbekkz.nulife.ui.courses.deadlines.viewmodel.DeadlinesViewModel;
import com.naltynbekkz.nulife.ui.courses.deadlines.viewmodel.DeadlinesViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.courses.questions.viewmodel.NewQuestionViewModel;
import com.naltynbekkz.nulife.ui.courses.questions.viewmodel.NewQuestionViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.courses.questions.viewmodel.QuestionsViewModel;
import com.naltynbekkz.nulife.ui.courses.questions.viewmodel.QuestionsViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.courses.resources.viewmodel.NewResourceViewModel;
import com.naltynbekkz.nulife.ui.courses.resources.viewmodel.NewResourceViewModel_AssistedFactory;
import com.naltynbekkz.nulife.ui.courses.resources.viewmodel.ResourcesViewModel;
import com.naltynbekkz.nulife.ui.courses.resources.viewmodel.ResourcesViewModel_AssistedFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class CoursesViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(CoursesViewModel.class)
    abstract ViewModel bindCoursesViewModel(CoursesViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EnrollViewModel.class)
    abstract ViewModel bindEnrollViewModel(EnrollViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EnrollCourseViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindEnrollCourseViewModel(EnrollCourseViewModel_AssistedFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(EditCourseViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindEditCourseViewModel(EditCourseViewModel_AssistedFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(CommentsViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindCommentsViewModel(CommentsViewModel_AssistedFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(NewAnswerViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindNewAnswerViewModel(NewAnswerViewModel_AssistedFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(AnswersViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindAnswersViewModel(AnswersViewModel_AssistedFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(DeadlinesViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindDeadlinesViewModel(DeadlinesViewModel_AssistedFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(NewQuestionViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindNewQuestionViewModel(NewQuestionViewModel_AssistedFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(QuestionsViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindQuestionsViewModel(QuestionsViewModel_AssistedFactory viewModelFactory);


    @Binds
    @IntoMap
    @ViewModelKey(NewResourceViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindNewResourceViewModel(NewResourceViewModel_AssistedFactory viewModelFactory);


    @Binds
    @IntoMap
    @ViewModelKey(ResourcesViewModel.class)
    public abstract ViewModelAssistedFactory<? extends ViewModel> bindResourcesViewModel(ResourcesViewModel_AssistedFactory viewModelFactory);

}
