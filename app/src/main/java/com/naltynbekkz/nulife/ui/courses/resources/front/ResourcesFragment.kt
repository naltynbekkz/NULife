package com.naltynbekkz.nulife.ui.courses.resources.front

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.model.Resource
import com.naltynbekkz.nulife.model.Resource.Companion.DONE
import com.naltynbekkz.nulife.model.Resource.Companion.LOADING
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.courses.resources.adapter.YearsAdapter
import com.naltynbekkz.nulife.ui.courses.resources.viewmodel.ResourcesViewModel
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.fragment_resources.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject


class ResourcesFragment : Fragment() {
    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val viewModel: ResourcesViewModel by viewModels {
        viewModelProvider.create(this, arguments)
    }

    private lateinit var adapter: YearsAdapter
    private lateinit var downloadManager: DownloadManager
    private lateinit var observer: Observer<List<Resource>>
    private var offlineData = ArrayList<Resource>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).coursesComponent.inject(this)
    }

    private val onComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
            val c = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))

            if (c == null) {
                Toast.makeText(context, "Download not found!", Toast.LENGTH_LONG).show()
            } else {
                c.moveToFirst()
                offlineData.forEach {
                    val name: String =
                        URLEncoder.encode(it.getFileName(), StandardCharsets.UTF_8.toString())
                            .replace("+", "%20")
                    if (c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)).contains(
                            name
                        )
                    ) {
                        it.status = DONE
                        viewModel.update(it)
                    }
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        downloadManager = context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        activity!!.registerReceiver(
            onComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )

        observer = Observer {
            it.forEach { resource ->
                if (resource.status == LOADING) {
                    viewModel.delete(resource)
                }
            }
            viewModel.savedResources.removeObserver(observer)
        }
        adapter =
            YearsAdapter(
                click = fun(resource: Resource) {
                    if (resource.contentType == "pdf" || resource.contentType == "doc" || resource.contentType == "docx") {
                        ResourceBottomSheet(
                            resource = resource,
                            openFolder = fun() {
                                startActivity(Intent(DownloadManager.ACTION_VIEW_DOWNLOADS))
                            },
                            forceDownload = fun() {
                                download(resource)
                            }
                        ).show(parentFragmentManager, "tag")
                    } else if (resource.contentType == "img") {
                        view(resource)
                    }
                },
                download = fun(resource: Resource) {
                    if (resource.contentType == "pdf" || resource.contentType == "doc" || resource.contentType == "docx") {
                        download(resource)
                    } else if (resource.contentType == "img") {
                        view(resource)
                    }
                }
            )


    }

    override fun onDestroy() {
        super.onDestroy()
        activity!!.unregisterReceiver(onComplete)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_resources, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.resources.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })
        viewModel.savedResources.observe(viewLifecycleOwner, observer)
        viewModel.savedResources.observe(viewLifecycleOwner, Observer {
            offlineData = ArrayList()
            it.forEach { resource ->
                offlineData.add(resource)
            }
            adapter.setOfflineData(offlineData)
        })

        recycler_view.adapter = adapter
    }

    private fun view(resource: Resource) {
        StfalconImageViewer.Builder(context, resource.getUriImages()) { view, image ->
            Glide.with(this)
                .load(image)
                .into(view)
        }.allowZooming(true).show()
    }

    private fun download(resource: Resource) {
        resource.status = LOADING
        viewModel.insert(resource)
        downloadManager.enqueue(
            DownloadManager.Request(resource.getUriImages().firstOrNull()).apply {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                setAllowedOverRoaming(false)
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
                setDestinationInExternalFilesDir(
                    context,
                    Environment.DIRECTORY_DOWNLOADS,
                    resource.getFileName() + "." + resource.contentType
                )
            }
        )
    }

}
