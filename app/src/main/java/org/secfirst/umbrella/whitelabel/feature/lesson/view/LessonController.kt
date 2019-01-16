package org.secfirst.umbrella.whitelabel.feature.lesson.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.lesson_view.*
import kotlinx.android.synthetic.main.lesson_view.view.*
import org.secfirst.umbrella.whitelabel.R
import org.secfirst.umbrella.whitelabel.UmbrellaApplication
import org.secfirst.umbrella.whitelabel.data.database.lesson.Lesson
import org.secfirst.umbrella.whitelabel.data.database.lesson.Subject
import org.secfirst.umbrella.whitelabel.data.database.segment.Markdown
import org.secfirst.umbrella.whitelabel.feature.about.AboutController
import org.secfirst.umbrella.whitelabel.feature.base.view.BaseController
import org.secfirst.umbrella.whitelabel.feature.difficulty.view.DifficultyController
import org.secfirst.umbrella.whitelabel.feature.lesson.DaggerLessonComponent
import org.secfirst.umbrella.whitelabel.feature.lesson.interactor.LessonBaseInteractor
import org.secfirst.umbrella.whitelabel.feature.lesson.presenter.LessonBasePresenter
import org.secfirst.umbrella.whitelabel.feature.segment.view.controller.HostSegmentController
import javax.inject.Inject


class LessonController : BaseController(), LessonView {

    @Inject
    internal lateinit var presenter: LessonBasePresenter<LessonView, LessonBaseInteractor>
    private val lessonClick: (Subject) -> Unit = this::onLessonClicked
    private val groupClick: (String) -> Unit = this::onGroupClicked
    private val groupAdapter = GroupAdapter<ViewHolder>()
    private var isRecycledView = false

    companion object {
        private const val RECYCLER_STATE = "recycle_state"
    }

    override fun onInject() {
        DaggerLessonComponent.builder()
                .application(UmbrellaApplication.instance)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.lesson_view, container, false)
        presenter.onAttach(this)
        presenter.submitLoadAllLesson()
        view.lessonMenu.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = groupAdapter
        }
        setUpToolbar(view)
        return view
    }

    override fun showAllLesson(lessons: List<Lesson>) {
        if (!isRecycledView) {
            lessons.forEach { it ->
                val lessonGroup = LessonGroup(it.moduleId, it.pathIcon, it.moduleTitle, groupClick)
                val groups = ExpandableGroup(lessonGroup)
                it.topics.forEach { subject -> groups.add(LessonItem(subject, lessonClick)) }
                groupAdapter.add(groups)
            }
            lessonMenu.apply { adapter = groupAdapter }
        }
    }

    override fun onSaveViewState(view: View, outState: Bundle) {
        super.onSaveViewState(view, outState)
        outState.putBoolean(RECYCLER_STATE, true)
    }

    override fun onRestoreViewState(view: View, savedViewState: Bundle) {
        super.onRestoreViewState(view, savedViewState)
        isRecycledView = savedViewState.getBoolean(RECYCLER_STATE)
    }

    private fun setUpToolbar(view: View) {
        view.lessonToolbar.let {
            mainActivity.setSupportActionBar(it)
            mainActivity.supportActionBar?.title = context.getString(R.string.lesson_title)
        }
    }

    private fun onLessonClicked(subject: Subject) = presenter.submitSelectLesson(subject)

    private fun onGroupClicked(moduleSha1ID: String) = presenter.submitSelectHead(moduleSha1ID)

    override fun startSegment(markdownIds: ArrayList<String>, enableFilter: Boolean) =
            router.pushController(RouterTransaction.with(HostSegmentController(markdownIds, enableFilter)))

    override fun startDifficultyController(subject: Subject) =
            router.pushController(RouterTransaction.with(DifficultyController(subject)))

    override fun startSegmentAlone(markdown: Markdown) =
            router.pushController(RouterTransaction.with(AboutController(markdown)))
}