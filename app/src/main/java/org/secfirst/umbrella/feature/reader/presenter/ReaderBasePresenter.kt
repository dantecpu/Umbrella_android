package org.secfirst.umbrella.feature.reader.presenter

import org.secfirst.umbrella.data.database.reader.FeedLocation
import org.secfirst.umbrella.data.database.reader.FeedSource
import org.secfirst.umbrella.data.database.reader.RSS
import org.secfirst.umbrella.feature.base.presenter.BasePresenter
import org.secfirst.umbrella.feature.reader.interactor.ReaderBaseInteractor
import org.secfirst.umbrella.feature.reader.view.ReaderView

interface ReaderBasePresenter<V : ReaderView, I : ReaderBaseInteractor> : BasePresenter<V, I> {

    fun submitFetchRss()

    fun submitInsertRss(rss: RSS)

    fun submitDeleteRss(rss: RSS)

    fun submitDeleteFeedLocation()

    fun submitInsertFeedSource(feedSources: List<FeedSource>)

    fun submitFeedRequest(feedLocation: FeedLocation,
                          feedSources: List<FeedSource>,
                          isFirstRequest: Boolean = false)

    fun submitFeedLocation(feedLocation: FeedLocation)

    fun submitInsertFeedLocation(feedLocation: FeedLocation)

    fun submitPutRefreshInterval(position: Int)

    fun prepareView()

    fun isSkipPassword()

    fun setSkipPassword(value : Boolean)

    fun submitChangeDatabaseAccess(userToken: String)

}