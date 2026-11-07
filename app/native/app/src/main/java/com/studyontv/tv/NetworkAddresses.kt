package com.studyontv.tv

import org.json.JSONArray
import org.json.JSONObject
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface

/**
 * 局域网管理地址的枚举。
 *
 * 后台 HTTP 服务绑定在 0.0.0.0 上，设备上的每个网卡地址都能访问同一个管理面板。
 * 电视 / 盒子同时开 WiFi 和网线、开热点、连 VPN 时会有多个地址，旧实现只取第一个
 * 站点本地 IPv4，二维码常常指向家长手机访问不到的那张网卡。
 * 这里把所有可用地址列出来，由连接页逐个生成地址和二维码。
 */
object NetworkAddresses {
    /** host 可直接拼进 URL：IPv6 已带方括号；label 是给家长看的网卡名称；rank 越小越靠前 */
    data class Address(val host: String, val label: String, val rank: Int)

    private const val MAX = 6

    /** 所有可用地址，按「WiFi / 网线 → 热点 → VPN / 蜂窝 → IPv6」排序并去重 */
    fun all(): List<Address> {
        val found = LinkedHashMap<String, Address>()
        try {
            NetworkInterface.getNetworkInterfaces().toList()
                .filter { it.isUp && !it.isLoopback }
                .forEach { nif ->
                    val name = nif.name ?: ""
                    val label = labelOf(name)
                    nif.inetAddresses.toList().forEach inner@{ addr ->
                        val host = hostOf(addr) ?: return@inner
                        val rank = rankOf(addr, name)
                        val previous = found[host]
                        if (previous == null || rank < previous.rank) found[host] = Address(host, label, rank)
                    }
                }
        } catch (e: Exception) {
            // 个别设备枚举网卡会抛 SecurityException / SocketException，忽略即可
        }
        return found.values.sortedWith(compareBy<Address> { it.rank }.thenBy { it.host }).take(MAX)
    }

    fun hosts(): List<String> = all().map { it.host }

    /** 兼容旧调用方：返回首选地址 */
    fun primary(): String = all().firstOrNull()?.host ?: ""

    /** 生成完整管理地址，IPv6 的方括号由 hostOf() 处理 */
    fun urls(port: Int): List<String> = all().map { "http://${it.host}:$port/admin/" }

    /** /api/hello、/api/device、连接页共用的结构化地址列表 */
    fun json(port: Int): JSONArray = JSONArray().also { arr ->
        all().forEach { a ->
            arr.put(JSONObject()
                .put("host", a.host)
                .put("label", a.label)
                .put("url", "http://${a.host}:$port/admin/"))
        }
    }

    private fun hostOf(addr: InetAddress): String? {
        if (addr.isLoopbackAddress || addr.isAnyLocalAddress || addr.isLinkLocalAddress || addr.isMulticastAddress) return null
        val raw = addr.hostAddress ?: return null
        val clean = raw.substringBefore('%')
        if (clean.isEmpty()) return null
        return when (addr) {
            is Inet4Address -> clean
            // IPv6 在 URL 里必须用方括号包起来，否则冒号会被当成端口分隔符
            is Inet6Address -> "[$clean]"
            else -> null
        }
    }

    private fun rankOf(addr: InetAddress, iface: String): Int {
        val vpn = iface.startsWith("tun") || iface.startsWith("ppp") ||
            iface.startsWith("rmnet") || iface.startsWith("ccmni") ||
            iface.startsWith("dummy") || iface.startsWith("v4-")
        // 热点地址（常见 192.168.43.1）排在 WiFi / 网线之后，避免旧版二维码指向手机连不上的网段
        val hotspot = iface.startsWith("swlan") || iface.startsWith("ap") ||
            iface.contains("hotspot") || iface.startsWith("rndis")
        return when (addr) {
            is Inet4Address -> when {
                vpn -> if (addr.isSiteLocalAddress) 4 else 5
                hotspot -> if (addr.isSiteLocalAddress) 2 else 3
                addr.isSiteLocalAddress -> 0
                else -> 1
            }
            is Inet6Address -> if (addr.isSiteLocalAddress) 6 else 7
            else -> Int.MAX_VALUE
        }
    }

    private fun labelOf(iface: String): String = when {
        iface.startsWith("wlan") -> "WiFi"
        iface.startsWith("eth") -> "网线"
        iface.startsWith("swlan") || iface.startsWith("ap") || iface.contains("hotspot") -> "热点"
        iface.startsWith("tun") || iface.startsWith("ppp") -> "VPN"
        iface.startsWith("rmnet") || iface.startsWith("ccmni") -> "移动网络"
        iface.startsWith("rndis") -> "USB 共享网络"
        iface.isNotEmpty() -> iface
        else -> "其他网络"
    }
}
