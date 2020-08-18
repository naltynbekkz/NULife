package com.naltynbekkz.courses.ui.resources.front

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
import com.naltynbekkz.courses.databinding.FragmentResourcesBinding
import com.naltynbekkz.courses.model.Resource
import com.naltynbekkz.courses.model.Resource.Companion.DONE
import com.naltynbekkz.courses.model.Resource.Companion.LOADING
import com.naltynbekkz.courses.ui.resources.adapter.YearsAdapter
import com.naltynbekkz.courses.ui.resources.viewmodel.ResourcesViewModel
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class ResourcesFragment : Fragment() {
    private val viewModel: ResourcesViewModel by viewModels()

    private lateinit var adapter: YearsAdapter
    private lateinit var downloadManager: DownloadManager
    private lateinit var observer: Observer<List<Resource>>
    private var offlineData = ArrayList<Resource>()

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

        downloadManager =
            requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        requireActivity().registerReceiver(
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
        requireActivity().unregisterReceiver(onComplete)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentResourcesBinding.inflate(inflater, container, false)

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

        binding.recyclerView.adapter = adapter

        return binding.root
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
