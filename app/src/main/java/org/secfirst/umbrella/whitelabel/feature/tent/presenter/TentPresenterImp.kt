package org.secfirst.umbrella.whitelabel.feature.tent.presenter

import org.secfirst.umbrella.whitelabel.feature.base.presenter.BasePresenterImp
import org.secfirst.umbrella.whitelabel.feature.tent.TentView
import org.secfirst.umbrella.whitelabel.feature.tent.interactor.TentBaseInteractor
import org.secfirst.umbrella.whitelabel.misc.AppExecutors.Companion.uiContext
import org.secfirst.umbrella.whitelabel.misc.launchSilent
import org.secfirst.umbrella.whitelabel.misc.runBlockingSilent
import javax.inject.Inject

class TentPresenterImp<V : TentView, I : TentBaseInteractor>
@Inject internal constructor(
        interactor: I) : BasePresenterImp<V, I>(
        interactor = interactor), TentBasePresenter<V, I> {

    override fun submitUpdateRepository() {
        runBlockingSilent(uiContext) {
            interactor?.let {
                getView()?.isUpdateRepository(it.updateRepository())
            }
        }
    }

    override fun submitFetchRepository() {
        runBlockingSilent(uiContext) {
            interactor?.let {
                getView()?.isFetchRepository(it.fetchRepository())
            }
        }
    }

    override fun submitLoadElementsFile() {
        runBlockingSilent(uiContext) {
            interactor?.let {
                getView()?.onLoadElementSuccess(it.loadElementsFile())
            }
        }
    }

    override fun submitLoadFile() {
        runBlockingSilent(uiContext) {
            interactor?.loadFile()
        }
    }
}