package example.datlt.nextcloud.framework.presentation.nextcloud


import android.net.Uri
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.util.Xml
import android.view.View
import com.owncloud.android.lib.common.OwnCloudAccount
import com.owncloud.android.lib.common.OwnCloudBasicCredentials
import com.owncloud.android.lib.common.OwnCloudClient
import com.owncloud.android.lib.common.OwnCloudClientFactory
import com.owncloud.android.lib.common.operations.OnRemoteOperationListener
import com.owncloud.android.lib.common.operations.RemoteOperation
import com.owncloud.android.lib.common.operations.RemoteOperationResult
import com.owncloud.android.lib.resources.files.CreateFolderRemoteOperation
import com.owncloud.android.lib.resources.files.ReadFolderRemoteOperation
import com.owncloud.android.lib.resources.files.model.RemoteFile
import com.owncloud.android.lib.resources.status.GetCapabilitiesRemoteOperation
import com.owncloud.android.lib.resources.status.OCCapability
import example.datlt.nextcloud.databinding.FragmentNextCloudBinding
import example.datlt.nextcloud.framework.presentation.common.BaseFragment
import example.datlt.nextcloud.util.setPreventDoubleClickScaleView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader


class NextCloudFragment :
    BaseFragment<FragmentNextCloudBinding>(FragmentNextCloudBinding::inflate) {


    lateinit var mClient: OwnCloudClient
    lateinit var mCredentials: OwnCloudBasicCredentials
    lateinit var mAccount: OwnCloudAccount
    val mHandler = Handler()

    override fun init(view: View) {

//        val serverUri: Uri = Uri.parse("https://shared02.opsone-cloud.ch/")
//        mClient = OwnCloudClientFactory.createOwnCloudClient(
//            serverUri,
//            requireActivity(),
//            true
//        )
//        mClient.credentials = OwnCloudBasicCredentials("datbcva123@gmail.com", "YrwNR-w4TG5-qaZaj-9Lz8d-zyRx5")
//
        binding.btnLogin.setPreventDoubleClickScaleView {
//            startReadRootFolder()
            createFolder()
        }

        binding.btnClient.setPreventDoubleClickScaleView {
            //dav file
            davFile()
        }

        testInit()

    }

    private fun davFile() {
        val client = OkHttpClient()

//        val folderPath = "https://your-nextcloud-server.com/remote.php/webdav/your-folder-name/"
        val folderPath = " https://shared02.opsone-cloud.ch//remote.php/dav/Documents"
        val requestBody = """
    <?xml version="1.0" encoding="utf-8" ?>
    <D:propfind xmlns:D="DAV:">
        <D:prop>
            <D:getlastmodified />
            <D:getcontentlength />
            <D:getcontenttype />
        </D:prop>
    </D:propfind>
""".trimIndent()
        val auth = "datbcva123@gmail.com" + ":" + "Zuru30619@"
        val encodedAuth = Base64.encodeToString(auth.toByteArray(), Base64.NO_WRAP)


        var request = Request.Builder()
            .url(folderPath)
            .header("Depth", "1")
            .header("Content-Type", "application/xml")
            .header("Authorization", "Basic $encodedAuth")
            .method(
                "PROPFIND",
                RequestBody.create("application/xml".toMediaTypeOrNull(), requestBody)
            )
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            val response = client.newCall(request).execute()

            val responseBody = response.body.string()

            Log.d("CHECKNEXTCLOUD", "${response.isSuccessful}")


            if (response.isSuccessful && responseBody != null) {
                val parser = Xml.newPullParser()
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
                parser.setInput(StringReader(responseBody))
                var eventType = parser.eventType
                var currentFileName = ""
                val fileNames = mutableListOf<String>()
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            if (parser.name == "D:href") {
                                currentFileName = parser.nextText()
                            }
                        }
                        XmlPullParser.END_TAG -> {
                            if (parser.name == "D:href") {
                                if (currentFileName != folderPath && currentFileName.endsWith("/")) {
                                    fileNames.add(currentFileName.substring(folderPath.length))
                                }
                            }
                        }
                    }
                    eventType = parser.next()
                }
                for (fileName in fileNames) {
                    Log.d("CHECKNEXTCLOUD", fileName)
                }
            } else {
                Log.d("CHECKNEXTCLOUD", response.code.toString())
            }


        }
    }

    override fun subscribeObserver(view: View) {


    }

    fun testInit() {
        Thread {
            mClient = OwnCloudClientFactory.createOwnCloudClient(
                Uri.parse("https://shared02.opsone-cloud.ch/"),
                requireContext(),
                true
            )
            val remoteOperationResult = GetCapabilitiesRemoteOperation().execute(mClient)
            if (remoteOperationResult.isSuccess && remoteOperationResult.data != null && remoteOperationResult.data.size > 0
            ) {

//                mCredentials = OwnCloudBasicCredentials(
//                    "datbcva123@gmail.com",
//                    "Zuru30619@" //e3arr-B8GYG-ds3yS-MiYcA-MA454
//                )
                mCredentials = OwnCloudBasicCredentials(
                    "datbcva123@gmail.com",
                    "e3arr-B8GYG-ds3yS-MiYcA-MA454" //e3arr-B8GYG-ds3yS-MiYcA-MA454
                )

                mClient.credentials = mCredentials
                mAccount =
                    OwnCloudAccount(Uri.parse("https://shared02.opsone-cloud.ch/"), mCredentials)

                Log.d(
                    "CHECKNEXTCLOUD",
                    "testInit: ${mClient.davUri} ${mClient.cookiesString} ${mClient.uploadUri}"
                )
                val capability =
                    remoteOperationResult.data[0] as OCCapability

                Log.d("CHECKNEXTCLOUD", "done ${capability.serverName} ${capability.accountName}")

                try {
//                    primaryColor =
//                        Color.parseColor(capability.serverColor)
                } catch (e: Exception) {
                    // falls back to primary color
                }
            }
        }.start()
    }

    private fun startReadRootFolder() {


        Log.d(
            "CHECKNEXTCLOUD",
            "start  ${mClient.userId} ${mClient.credentials.username} ${mClient.baseUri}"
        )

        val refreshOperation = ReadFolderRemoteOperation("/QloudData")

        refreshOperation.execute(
            mClient,
            { operation, result ->
                Log.d(
                    "CHECKNEXTCLOUD",
                    "${result?.isSuccess} ${result?.exception?.message} ${result?.code} "
                )

                if (result!!.isSuccess) {


                    val files: ArrayList<Any>? = result.getData()

                    for (item in files!!) {
                        Log.d(
                            "CHECKNEXTCLOUD",
                            "onRemoteOperationFinish: ${(item as RemoteFile).remoteId}"
                        )
                    }

                }
            }, mHandler
        )
    }

    private fun createFolder() {

        mCredentials.toOkHttpCredentials()
        val createOperation = CreateFolderRemoteOperation("/Datlt", true)
        createOperation.execute(mClient, object : OnRemoteOperationListener {
            override fun onRemoteOperationFinish(
                operation: RemoteOperation<*>?,
                result: RemoteOperationResult<*>?
            ) {
                if (operation is CreateFolderRemoteOperation) {
                    Log.d(
                        "CHECKNEXTCLOUD",
                        "onRemoteOperationFinish: ${result?.isSuccess == true} ${result?.code}"
                    )

                    if (result!!.isSuccess) {
                        // do your stuff here
                    }
                }
            }

        }, mHandler)
    }
}