package com.example.data

import com.example.crypto.CryptoEngine
import com.example.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class SovereignRepository(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {

    // Current User Profile
    private val _currentUser = MutableStateFlow(
        UserProfile(
            handle = "@ali_sovereign",
            displayName = "علي - مهندس النظم",
            bio = "مدافع عن الخصوصية والسيادة الرقمية • Self-Hosted Node Operator",
            avatarColor = 0xFF10B981,
            publicKeyFingerprint = "7F8B 3A91 E204 55BC 90A1 4C18",
            serverNodeUrl = "https://node.sovereign.local:8443",
            isConnected = true,
            isRtl = true,
            isDarkMode = true
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    // Alternate user for dual-user testing (المستخدم B)
    private val secondaryUser = UserProfile(
        handle = "@tariq_privacy",
        displayName = "طارق - مستشار الأمن",
        bio = "تشفير طرفي • باحث في الأنظمة الموزعة",
        avatarColor = 0xFF38BDF8,
        publicKeyFingerprint = "9B21 C410 882E 11FA 4402 7E10",
        serverNodeUrl = "https://node.sovereign.local:8443",
        isConnected = true,
        isRtl = true,
        isDarkMode = true
    )

    // Chat Threads
    private val _threads = MutableStateFlow<List<ChatThread>>(emptyList())
    val threads: StateFlow<List<ChatThread>> = _threads.asStateFlow()

    // Messages mapped by threadId
    private val _messages = MutableStateFlow<Map<String, List<MessageItem>>>(emptyMap())
    val messages: StateFlow<Map<String, List<MessageItem>>> = _messages.asStateFlow()

    // Channels
    private val _channels = MutableStateFlow<List<ChannelItem>>(emptyList())
    val channels: StateFlow<List<ChannelItem>> = _channels.asStateFlow()

    // Channel Posts mapped by channelId
    private val _channelPosts = MutableStateFlow<Map<String, List<ChannelPost>>>(emptyMap())
    val channelPosts: StateFlow<Map<String, List<ChannelPost>>> = _channelPosts.asStateFlow()

    // Social Feed Posts
    private val _feedPosts = MutableStateFlow<List<SocialPost>>(emptyList())
    val feedPosts: StateFlow<List<SocialPost>> = _feedPosts.asStateFlow()

    // Active Call Session
    private val _activeCall = MutableStateFlow<CallSession?>(null)
    val activeCall: StateFlow<CallSession?> = _activeCall.asStateFlow()

    // Network connection latency status
    private val _nodeLatency = MutableStateFlow(24)
    val nodeLatency: StateFlow<Int> = _nodeLatency.asStateFlow()

    init {
        seedInitialData()
    }

    private fun seedInitialData() {
        val thread1Id = "thread_tariq"
        val thread2Id = "thread_group_sovereign"
        val thread3Id = "thread_fatima"

        _threads.value = listOf(
            ChatThread(
                id = thread1Id,
                title = "طارق - مستشار الأمن",
                handle = "@tariq_privacy",
                isGroup = false,
                lastMessage = "تم التحقق من بصمة التشفير، الاتصال آمن تماماً.",
                lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 5,
                unreadCount = 1,
                avatarColor = 0xFF38BDF8,
                isVerified = true,
                isEncrypted = true,
                peerPublicKeyFingerprint = "9B21 C410 882E 11FA 4402 7E10"
            ),
            ChatThread(
                id = thread2Id,
                title = "مجلس السيادة التقنية (مجموعة مشفرة)",
                handle = "@sovereign_core_team",
                isGroup = true,
                lastMessage = "تم تجهيز ملفات Docker Compose لتشغيل العقدة ذاتياً.",
                lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 35,
                unreadCount = 3,
                avatarColor = 0xFF10B981,
                isVerified = true,
                isEncrypted = true,
                membersCount = 12
            ),
            ChatThread(
                id = thread3Id,
                title = "د. فاطمة الزهراء",
                handle = "@fatima_crypto",
                isGroup = false,
                lastMessage = "مرفق تقرير فحص بروتوكول المصافحة والتشفير.",
                lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 120,
                unreadCount = 0,
                avatarColor = 0xFFA855F7,
                isVerified = true,
                isEncrypted = true,
                peerPublicKeyFingerprint = "3D44 11FE 0988 A1C2 BB39 9901"
            )
        )

        _messages.value = mapOf(
            thread1Id to listOf(
                MessageItem(
                    id = "m1",
                    threadId = thread1Id,
                    senderHandle = "@tariq_privacy",
                    recipientHandle = "@ali_sovereign",
                    content = "مرحباً علي، هل العقدة السحابية الذاتية جاهزة لاستقبال الاتصالات المشفرة؟",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 15,
                    status = MessageStatus.READ,
                    isEncrypted = true,
                    isSelf = false
                ),
                MessageItem(
                    id = "m2",
                    threadId = thread1Id,
                    senderHandle = "@ali_sovereign",
                    recipientHandle = "@tariq_privacy",
                    content = "نعم بكل تأكيد، قمنا بعزل الخوادم وتفعيل بروتوكول TLS ومفاتيح AES-256.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 10,
                    status = MessageStatus.READ,
                    isEncrypted = true,
                    isSelf = true
                ),
                MessageItem(
                    id = "m3",
                    threadId = thread1Id,
                    senderHandle = "@tariq_privacy",
                    recipientHandle = "@ali_sovereign",
                    content = "تم التحقق من بصمة التشفير، الاتصال آمن تماماً.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 5,
                    status = MessageStatus.DELIVERED,
                    isEncrypted = true,
                    isSelf = false
                )
            ),
            thread2Id to listOf(
                MessageItem(
                    id = "mg1",
                    threadId = thread2Id,
                    senderHandle = "@omar_sys",
                    recipientHandle = "@sovereign_core_team",
                    content = "السلام عليكم جميعاً، نرحب بكم في غرفة العمليات السيادية.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60,
                    status = MessageStatus.READ,
                    isEncrypted = true,
                    isSelf = false
                ),
                MessageItem(
                    id = "mg2",
                    threadId = thread2Id,
                    senderHandle = "@ali_sovereign",
                    recipientHandle = "@sovereign_core_team",
                    content = "تم تجهيز ملفات Docker Compose لتشغيل العقدة ذاتياً.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 35,
                    status = MessageStatus.READ,
                    isEncrypted = true,
                    isSelf = true
                )
            )
        )

        // Channels
        val c1Id = "channel_sovereign_broadcast"
        val c2Id = "channel_crypto_tech"
        val c3Id = "channel_digital_defense"

        _channels.value = listOf(
            ChannelItem(
                id = c1Id,
                title = "قناة إعلانات السيادة الرقمية",
                handle = "@sovereign_official",
                description = "القناة الرسمية لنشر التحديثات والأبحاث المتعلقة بالأنظمة المستقلة ومفاتيح التشفير.",
                subscriberCount = 14820,
                avatarColor = 0xFF10B981,
                isPublic = true,
                isAdmin = true,
                isVerified = true
            ),
            ChannelItem(
                id = c2Id,
                title = "تقنيات التشفير المتقدم (E2EE Lab)",
                handle = "@crypto_advances",
                description = "أوراق بحثية ومناقشات حول بروتوكولات Signal وMatrix والخصوصية الصفرية.",
                subscriberCount = 8940,
                avatarColor = 0xFF38BDF8,
                isPublic = true,
                isAdmin = false,
                isVerified = true
            ),
            ChannelItem(
                id = c3Id,
                title = "درع الأمن السيبراني والمراقبة",
                handle = "@cyber_shield",
                description = "رصد التسريبات وتحليل التهديدات وتأمين خوادم الاستضافة الذاتية.",
                subscriberCount = 21300,
                avatarColor = 0xFFF59E0B,
                isPublic = true,
                isAdmin = false,
                isVerified = true
            )
        )

        _channelPosts.value = mapOf(
            c1Id to listOf(
                ChannelPost(
                    id = "cp1",
                    channelId = c1Id,
                    channelTitle = "قناة إعلانات السيادة الرقمية",
                    channelHandle = "@sovereign_official",
                    text = "📢 بيان رسمي: تم إطلاق بنية الاستضافة الذاتية الكاملة لشبكة Sovereign. بياناتك لم تعد ملكاً لأي احتكار رقمي.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 180,
                    viewsCount = 3400,
                    forwardsCount = 412,
                    isPinned = true
                ),
                ChannelPost(
                    id = "cp2",
                    channelId = c1Id,
                    channelTitle = "قناة إعلانات السيادة الرقمية",
                    channelHandle = "@sovereign_official",
                    text = "🔒 نصيحة اليوم: قم دائماً بمطابقة بصمة الأمان (Safety Number) قبل إرسال ملفات حساسة عبر الاتصال المشفر طرفياً.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 45,
                    viewsCount = 1820,
                    forwardsCount = 195
                )
            ),
            c2Id to listOf(
                ChannelPost(
                    id = "cp3",
                    channelId = c2Id,
                    channelTitle = "تقنيات التشفير المتقدم (E2EE Lab)",
                    channelHandle = "@crypto_advances",
                    text = "تحليل بروتوكول تبادل المفاتيح Double Ratchet: لماذا يعد الأفضل لحماية الرسائل حتى في حال اختراق مفتاح جلسة سابقة.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 300,
                    viewsCount = 950,
                    forwardsCount = 88
                )
            )
        )

        // Social Posts (Timeline)
        _feedPosts.value = listOf(
            SocialPost(
                id = "p1",
                authorHandle = "@ali_sovereign",
                authorName = "علي - مهندس النظم",
                authorAvatarColor = 0xFF10B981,
                content = "السيادة الرقمية ليست ترفاً أو خياراً ثانوياً؛ بل هي حق أصيل لكل أمة ومجتمع لحماية هويته وبياناته من التجسس والتحكم المركزي. مرحباً بكم في عصر التواصل الحر! 🌐🛡️",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 20,
                likesCount = 42,
                repostsCount = 15,
                commentsCount = 3,
                isLiked = true,
                hashtags = listOf("السيادة_الرقمية", "الخصوصية_أولاً", "SelfHosted"),
                replies = listOf(
                    PostComment(
                        id = "c1",
                        authorHandle = "@tariq_privacy",
                        authorName = "طارق - مستشار الأمن",
                        text = "خطوة جبارة في الاتجاه الصحيح، الخصوصية الافتراضية أصبحت حقيقة.",
                        timestamp = System.currentTimeMillis() - 1000 * 60 * 12
                    )
                )
            ),
            SocialPost(
                id = "p2",
                authorHandle = "@fatima_crypto",
                authorName = "د. فاطمة الزهراء",
                authorAvatarColor = 0xFFA855F7,
                content = "أكملنا اليوم فحص التشفير الطرفي للرسائل والمكالمات. المفاتيح تولد على جهازك الخاص ولا تغادره أبداً. لا خادم وسيط يستطيع فك الشفرة. 🔐✨",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 95,
                likesCount = 89,
                repostsCount = 34,
                commentsCount = 5,
                isLiked = false,
                hashtags = listOf("E2EE", "الأمن_السيبراني", "Cryptography")
            ),
            SocialPost(
                id = "p3",
                authorHandle = "@open_source_arabia",
                authorName = "مجتمع المصدر المفتوح",
                authorAvatarColor = 0xFF38BDF8,
                content = "أحدث إصدار يدعم الاستضافة الذاتية عبر Docker وPostgreSQL وWebRTC جاهز للتحميل والتشغيل. ابنِ عقدتك الخاصة اليوم! 💻🚀",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 240,
                likesCount = 132,
                repostsCount = 57,
                commentsCount = 11,
                isLiked = false,
                hashtags = listOf("OpenSource", "Docker", "DevOps")
            )
        )
    }

    // ==========================================
    // Messaging API & Realtime WebSocket Events
    // ==========================================

    fun sendMessage(
        threadId: String,
        content: String,
        mediaType: MediaType = MediaType.NONE,
        mediaUrl: String? = null,
        audioDurationSec: Int = 0,
        selfDestructSec: Int = 0
    ) {
        val user = _currentUser.value
        val thread = _threads.value.find { it.id == threadId } ?: return

        val newMsg = MessageItem(
            id = UUID.randomUUID().toString(),
            threadId = threadId,
            senderHandle = user.handle,
            recipientHandle = thread.handle,
            content = content,
            timestamp = System.currentTimeMillis(),
            status = MessageStatus.SENT,
            isEncrypted = true,
            mediaType = mediaType,
            mediaUrl = mediaUrl,
            audioDurationSec = audioDurationSec,
            selfDestructSec = selfDestructSec,
            isSelf = true
        )

        // Add to messages map
        _messages.update { currentMap ->
            val list = (currentMap[threadId] ?: emptyList()) + newMsg
            currentMap + (threadId to list)
        }

        // Update thread last message
        _threads.update { list ->
            list.map {
                if (it.id == threadId) {
                    it.copy(
                        lastMessage = if (mediaType == MediaType.AUDIO) "🎙️ رسالة صوتية (${audioDurationSec}s)" else content,
                        lastTimestamp = newMsg.timestamp
                    )
                } else it
            }
        }

        // Simulate WebSocket delivery to peer, followed by peer acknowledgment and auto-reply
        scope.launch {
            delay(600)
            // Update to DELIVERED
            updateMessageStatus(threadId, newMsg.id, MessageStatus.DELIVERED)

            delay(1200)
            // Update to READ
            updateMessageStatus(threadId, newMsg.id, MessageStatus.READ)

            // If messaging tariq or individual, simulate intelligent sovereign peer response
            if (!thread.isGroup && thread.handle != user.handle) {
                delay(1500)
                simulatePeerReply(threadId, thread)
            }
        }
    }

    private fun updateMessageStatus(threadId: String, messageId: String, status: MessageStatus) {
        _messages.update { currentMap ->
            val list = currentMap[threadId]?.map { msg ->
                if (msg.id == messageId) msg.copy(status = status) else msg
            } ?: emptyList()
            currentMap + (threadId to list)
        }
    }

    private fun simulatePeerReply(threadId: String, thread: ChatThread) {
        val replyContent = when ((0..3).random()) {
            0 -> "تم استلام رسالتك وتأكيد التوقيع الرقمي بنجاح 🛡️."
            1 -> "العقدة المستقلة تعمل بكفاءة عالية، زمن الاستجابة ممتاز!"
            2 -> "أوافقك الرأي تماماً. نحن نمتلك بياناتنا ونحمي خصوصيتنا."
            else -> "ممتاز، سأقوم بمراجعة التحديث ومزامنة المفاتيح المشفرة."
        }

        val peerMsg = MessageItem(
            id = UUID.randomUUID().toString(),
            threadId = threadId,
            senderHandle = thread.handle,
            recipientHandle = _currentUser.value.handle,
            content = replyContent,
            timestamp = System.currentTimeMillis(),
            status = MessageStatus.DELIVERED,
            isEncrypted = true,
            isSelf = false
        )

        _messages.update { currentMap ->
            val list = (currentMap[threadId] ?: emptyList()) + peerMsg
            currentMap + (threadId to list)
        }

        _threads.update { list ->
            list.map {
                if (it.id == threadId) {
                    it.copy(
                        lastMessage = replyContent,
                        lastTimestamp = peerMsg.timestamp,
                        unreadCount = it.unreadCount + 1
                    )
                } else it
            }
        }
    }

    fun markThreadAsRead(threadId: String) {
        _threads.update { list ->
            list.map {
                if (it.id == threadId) it.copy(unreadCount = 0) else it
            }
        }
    }

    fun createChatThread(title: String, handle: String, isGroup: Boolean): ChatThread {
        val newThread = ChatThread(
            id = "thread_${UUID.randomUUID().toString().take(8)}",
            title = title,
            handle = if (handle.startsWith("@")) handle else "@$handle",
            isGroup = isGroup,
            lastMessage = "تم إنشاء المحادثة المشفرة.",
            lastTimestamp = System.currentTimeMillis(),
            unreadCount = 0,
            avatarColor = if (isGroup) 0xFF10B981 else 0xFF38BDF8,
            isVerified = true,
            isEncrypted = true,
            peerPublicKeyFingerprint = CryptoEngine.generateSafetyNumber(_currentUser.value.handle, handle)
        )

        _threads.update { listOf(newThread) + it }
        return newThread
    }

    // ==========================================
    // Channel & Broadcast APIs
    // ==========================================

    fun broadcastToChannel(channelId: String, text: String) {
        val channel = _channels.value.find { it.id == channelId } ?: return
        val newPost = ChannelPost(
            id = UUID.randomUUID().toString(),
            channelId = channelId,
            channelTitle = channel.title,
            channelHandle = channel.handle,
            text = text,
            timestamp = System.currentTimeMillis(),
            viewsCount = 1,
            forwardsCount = 0
        )

        _channelPosts.update { map ->
            val list = listOf(newPost) + (map[channelId] ?: emptyList())
            map + (channelId to list)
        }
    }

    fun createChannel(title: String, handle: String, description: String): ChannelItem {
        val newChannel = ChannelItem(
            id = "channel_${UUID.randomUUID().toString().take(8)}",
            title = title,
            handle = if (handle.startsWith("@")) handle else "@$handle",
            description = description,
            subscriberCount = 1,
            avatarColor = 0xFF10B981,
            isPublic = true,
            isAdmin = true,
            isVerified = true
        )

        _channels.update { listOf(newChannel) + it }
        return newChannel
    }

    // ==========================================
    // Social Network Feed APIs
    // ==========================================

    fun createSocialPost(content: String, hashtags: List<String>) {
        val user = _currentUser.value
        val newPost = SocialPost(
            id = UUID.randomUUID().toString(),
            authorHandle = user.handle,
            authorName = user.displayName,
            authorAvatarColor = user.avatarColor,
            content = content,
            timestamp = System.currentTimeMillis(),
            likesCount = 0,
            repostsCount = 0,
            commentsCount = 0,
            isLiked = false,
            isReposted = false,
            hashtags = hashtags
        )

        _feedPosts.update { listOf(newPost) + it }
    }

    fun toggleLikePost(postId: String) {
        _feedPosts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    val newLiked = !post.isLiked
                    post.copy(
                        isLiked = newLiked,
                        likesCount = if (newLiked) post.likesCount + 1 else (post.likesCount - 1).coerceAtLeast(0)
                    )
                } else post
            }
        }
    }

    fun toggleRepostPost(postId: String) {
        _feedPosts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    val newReposted = !post.isReposted
                    post.copy(
                        isReposted = newReposted,
                        repostsCount = if (newReposted) post.repostsCount + 1 else (post.repostsCount - 1).coerceAtLeast(0)
                    )
                } else post
            }
        }
    }

    fun addCommentToPost(postId: String, commentText: String) {
        val user = _currentUser.value
        val comment = PostComment(
            id = UUID.randomUUID().toString(),
            authorHandle = user.handle,
            authorName = user.displayName,
            text = commentText,
            timestamp = System.currentTimeMillis()
        )

        _feedPosts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    post.copy(
                        commentsCount = post.commentsCount + 1,
                        replies = post.replies + comment
                    )
                } else post
            }
        }
    }

    // ==========================================
    // Calls & WebRTC Signaling
    // ==========================================

    fun startCall(peerHandle: String, peerName: String, callType: CallType) {
        val session = CallSession(
            id = UUID.randomUUID().toString(),
            peerHandle = peerHandle,
            peerName = peerName,
            callType = callType,
            state = CallState.RINGING,
            durationSec = 0,
            securityFingerprint = CryptoEngine.generateSafetyNumber(_currentUser.value.handle, peerHandle)
        )
        _activeCall.value = session

        // Simulate peer answering after 2 seconds
        scope.launch {
            delay(2000)
            if (_activeCall.value?.id == session.id) {
                _activeCall.update { it?.copy(state = CallState.CONNECTED) }
                // Call timer
                while (_activeCall.value?.state == CallState.CONNECTED) {
                    delay(1000)
                    _activeCall.update { it?.copy(durationSec = (it.durationSec) + 1) }
                }
            }
        }
    }

    fun toggleCallMute() {
        _activeCall.update { it?.copy(isMuted = !(it.isMuted)) }
    }

    fun toggleCallCamera() {
        _activeCall.update { it?.copy(isCameraOff = !(it.isCameraOff)) }
    }

    fun toggleCallSpeaker() {
        _activeCall.update { it?.copy(isSpeakerOn = !(it.isSpeakerOn)) }
    }

    fun endCall() {
        _activeCall.update { it?.copy(state = CallState.ENDED) }
        scope.launch {
            delay(500)
            _activeCall.value = null
        }
    }

    // ==========================================
    // Sovereignty, Dual-User Switch & Node Settings
    // ==========================================

    fun switchActiveUserForTesting() {
        val current = _currentUser.value
        if (current.handle == "@ali_sovereign") {
            _currentUser.value = secondaryUser
        } else {
            _currentUser.value = UserProfile(
                handle = "@ali_sovereign",
                displayName = "علي - مهندس النظم",
                bio = "مدافع عن الخصوصية والسيادة الرقمية • Self-Hosted Node Operator",
                avatarColor = 0xFF10B981,
                publicKeyFingerprint = "7F8B 3A91 E204 55BC 90A1 4C18",
                serverNodeUrl = "https://node.sovereign.local:8443",
                isConnected = true,
                isRtl = current.isRtl,
                isDarkMode = current.isDarkMode
            )
        }
    }

    fun updateServerNodeUrl(newUrl: String) {
        _currentUser.update { it.copy(serverNodeUrl = newUrl) }
        pingNode()
    }

    fun toggleRtl() {
        _currentUser.update { it.copy(isRtl = !it.isRtl) }
    }

    fun toggleDarkMode() {
        _currentUser.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun pingNode() {
        scope.launch {
            _nodeLatency.value = (14..38).random()
        }
    }

    fun regenerateIdentityKeys() {
        val newFingerprint = CryptoEngine.generateKeyFingerprint(_currentUser.value.handle)
        _currentUser.update { it.copy(publicKeyFingerprint = newFingerprint) }
    }

    fun purgeLocalVault() {
        _messages.value = emptyMap()
        _threads.value = emptyList()
        _feedPosts.value = emptyList()
        _channels.value = emptyList()
        seedInitialData()
    }
}
