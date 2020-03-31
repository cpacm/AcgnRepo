package com.cpacm.comic.model.http

import java.net.InetAddress
import java.net.Socket
import java.security.GeneralSecurityException
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.SecureRandom
import java.util.*
import javax.net.ssl.*

/**
 * <p>
 *
 * @author cpacm 2019-12-26
 */
class PicaTLSSocketFactory : SSLSocketFactory() {

    private var factory: SSLSocketFactory

    init {
        val instance = SSLContext.getInstance("TLS")
        instance.init(null as Array<KeyManager?>?, arrayOf<TrustManager?>(systemDefaultTrustManager()), null as SecureRandom?)
        factory = instance.socketFactory
    }

    override fun getDefaultCipherSuites(): Array<String> {
        return factory.defaultCipherSuites
    }

    override fun createSocket(s: Socket?, host: String?, port: Int, autoClose: Boolean): Socket {
        return factory.createSocket(s, host, port, autoClose)
    }

    override fun createSocket(host: String?, port: Int): Socket {
        return factory.createSocket(host, port)
    }

    override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket {
        return factory.createSocket(host, port, localHost, localPort)
    }

    override fun createSocket(host: InetAddress?, port: Int): Socket {
        return factory.createSocket(host, port)
    }

    override fun createSocket(address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int): Socket {
        return factory.createSocket(address, port, localAddress, localPort)
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return factory.supportedCipherSuites
    }

    fun systemDefaultTrustManager(): X509TrustManager? {
        try {
            val instance: TrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            instance.init(null as KeyStore?)
            val trustManagers: Array<TrustManager> = instance.trustManagers
            if (trustManagers.size == 1 && trustManagers[0] is X509TrustManager) {
                return trustManagers[0] as X509TrustManager
            }
            throw IllegalStateException("Unexpected default trust managers:" + trustManagers.contentToString())
        } catch (unused: GeneralSecurityException) {
            throw AssertionError()
        }
    }
}