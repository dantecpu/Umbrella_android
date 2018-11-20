package org.secfirst.umbrella.whitelabel.feature.account.presenter

import com.raizlabs.android.dbflow.config.FlowManager
import org.apache.commons.io.FileUtils
import org.secfirst.umbrella.whitelabel.UmbrellaApplication
import org.secfirst.umbrella.whitelabel.data.database.AppDatabase
import org.secfirst.umbrella.whitelabel.feature.account.interactor.AccountBaseInteractor
import org.secfirst.umbrella.whitelabel.feature.account.view.AccountView
import org.secfirst.umbrella.whitelabel.feature.base.presenter.BasePresenterImp
import org.secfirst.umbrella.whitelabel.misc.AppExecutors.Companion.uiContext
import org.secfirst.umbrella.whitelabel.misc.launchSilent
import java.io.File
import javax.inject.Inject

class AccountPresenterImp<V : AccountView, I : AccountBaseInteractor> @Inject constructor(
        interactor: I) : BasePresenterImp<V, I>(
        interactor = interactor), AccountBasePresenter<V, I> {


    override fun setUserLogIn() {
        interactor?.setLoggedIn()
    }

    override fun submitChangeDatabaseAccess(userToken: String) {
        launchSilent(uiContext) {
            interactor?.let {
                val res = it.changeDatabaseAccess(userToken)
                getView()?.isLogged(res)
            }
        }
    }

    override fun submitExportDatabase(destinationDir: String, fileName: String, isWipeData: Boolean) {

        val dstDatabase = File("$destinationDir/$fileName.db")
        val databaseFile = FlowManager.getContext().getDatabasePath("${AppDatabase.NAME}.db")
        databaseFile.copyTo(dstDatabase, true)
        if (isWipeData)
            cleanDatabase()

        getView()?.exportDatabaseSuccessfully()
    }

    private fun cleanDatabase() {
        val cacheDir = UmbrellaApplication.instance.cacheDir
        FileUtils.deleteQuietly(cacheDir)
        FlowManager.getDatabase(AppDatabase.NAME).reset()
    }

    override fun prepareShareContent(fileName: String) {
        val databaseFile = FlowManager.getContext().getDatabasePath("${AppDatabase.NAME}.db")
        val backupFile = File("${FlowManager.getContext().cacheDir}/$fileName.db")
        databaseFile.copyTo(backupFile, true)
        getView()?.onShareContent(backupFile)
    }
}