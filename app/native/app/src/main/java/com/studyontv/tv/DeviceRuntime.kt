package com.studyontv.tv

import android.content.Context

object DeviceRuntime {
    private var server: LanServer? = null
    lateinit var store: LocalStore
        private set
    lateinit var appContext: Context
        private set
    lateinit var learning: LearningEngine
        private set

    @Synchronized
    fun ensure(context: Context) {
        if (server != null) return
        val app = context.applicationContext
        appContext = app
        Diag.installCrashHandler()
        Diag.info("App", "start version=${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE}) sdk=${android.os.Build.VERSION.SDK_INT} model=${android.os.Build.MODEL}")
        (app as android.app.Application).registerActivityLifecycleCallbacks(object : android.app.Application.ActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: android.app.Activity) {
                Foreground.resumeOwn(activity)
                GateService.onOwnActivityResumed()
            }
            override fun onActivityPaused(activity: android.app.Activity) { Foreground.pauseOwn(activity) }
            override fun onActivityCreated(activity: android.app.Activity, state: android.os.Bundle?) = Unit
            override fun onActivityStarted(activity: android.app.Activity) = Unit
            override fun onActivityStopped(activity: android.app.Activity) = Unit
            override fun onActivitySaveInstanceState(activity: android.app.Activity, state: android.os.Bundle) = Unit
            override fun onActivityDestroyed(activity: android.app.Activity) = Unit
        })
        store = LocalStore(app)
        SeedAssets.ensureQuestions(app, store)
        learning = LearningEngine(store)
        server = LanServer(app, store, app.assets).also {
            it.start()
            Prefs.setLocalPort(app, it.boundPort)
        }
        WebShell.PrefsHolder.baseUrl = Prefs.localBase(app)
        if (GradingService.settings(store).optString("mode") == "ai") {
            val pending = store.reviews("pending")
            for (i in 0 until pending.length()) GradingService.enqueue(store, learning, pending.getJSONObject(i).getString("id"))
        }
    }

    fun connection(context: Context): org.json.JSONObject {
        ensure(context)
        val port = server?.boundPort ?: Prefs.localPort(context)
        val addresses = NetworkAddresses.json(port)
        return org.json.JSONObject()
            // ip / port 保留给旧版网页；新版连接页优先读 addresses，为每个网卡地址生成一个二维码
            .put("ip", if (addresses.length() > 0) addresses.getJSONObject(0).getString("host") else "")
            .put("port", port).put("token", store.adminToken())
            .put("addresses", addresses)
    }
}
