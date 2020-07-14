package endpass.android.endpass_sdk.presentation.di



import endpass.android.endpass_sdk.data.impl.LoginRepositoryImpl
import endpass.android.endpass_sdk.data.impl.MainRepositoryImpl
import endpass.android.endpass_sdk.domain.interactor.*
import endpass.android.endpass_sdk.domain.repository.LoginRepository
import endpass.android.endpass_sdk.domain.repository.MainRepository
import endpass.android.endpass_sdk.presentation.ui.auth.AuthViewModel
import endpass.android.endpass_sdk.presentation.ui.home.DocFlowViewModel
import endpass.android.endpass_sdk.presentation.ui.auth.OAuthViewModel

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


val archModule = module {
    module("repository") {

        factory {
            LoginRepositoryImpl(get()) as LoginRepository
        }
        factory {
            MainRepositoryImpl(get()) as MainRepository
        }
        factory {
            LoginUseCase(get())
        }
        factory {
            CheckPasswordUseCase(get())
        }
        factory {
            RequestCodeUseCase(get())
        }
        factory {
            VerifyEmailUseCase(get())
        }
        factory {
            ResetPasswordUseCase(get())
        }
        factory {
            ConfirmPasswordUseCase(get())
        }
        factory {
            GetDocumentsUseCase(get())
        }
        factory {
            ChangePasswordUseCase(get())
        }
        factory {
            RecoverUseCase(get())
        }
        factory {
            RecoverConfirmUseCase(get())
        }
        factory {
            DeleteDocumentUseCase(get())
        }
        factory {
            CheckDocumentUseCase(get())
        }
        factory {
            AddDocumentUseCase(get())
        }
        factory {
            UploadDocumentUseCase(get())
        }
        factory {
            UploadDocumentStatusUseCase(get())
        }
        factory {
            ConfirmDocumentStatusUseCase(get())
        }
        factory {
            GetDocumentUseCase(get())
        }
        factory {
            OauthUseCase(get())
        }
        factory {
            GetOauthLoginUseCase(get())
        }
        factory {
            OauthSettingsUseCase(get())
        }
        factory {
            PostOauthLoginUseCase(get())
        }
        factory {
            GetOauthConsentUseCase(get())
        }
        factory {
            GetOauthScopesUseCase(get())
        }
        factory {
            PostOauthConsentUseCase(get())
        }
        factory {
            GetRedirectUrlUseCase(get())
        }
        factory {
            PostOauthTokenUseCase(get())
        }
        factory {
            RequireDocumentUseCase(get())
        }

        module("viewModel") {

            viewModel {
                AuthViewModel(
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get()
                )
            }
            viewModel {
                DocFlowViewModel(get(), get(), get(), get(), get(), get(),get(), get())
            }

            viewModel {
                OAuthViewModel(get(), get(),get(), get(), get(), get(), get(), get(), get())
            }
        }
    }

}


