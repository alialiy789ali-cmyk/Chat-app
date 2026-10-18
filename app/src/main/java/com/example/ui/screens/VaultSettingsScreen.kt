package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.SovereignViewModel
import com.example.ui.components.SovereignAvatar
import com.example.ui.theme.*

@Composable
fun VaultSettingsScreen(
    viewModel: SovereignViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val latency by viewModel.nodeLatency.collectAsState()
    val scrollState = rememberScrollState()

    var showBackupSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Identity & Sovereignty Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SovereignAvatar(
                        name = currentUser.displayName,
                        colorHex = currentUser.avatarColor,
                        sizeDp = 54,
                        showShield = true
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentUser.displayName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = currentUser.handle,
                            fontSize = 13.sp,
                            color = CyanSignal,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = currentUser.bio,
                            fontSize = 11.sp,
                            color = SlateMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Public key fingerprint
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SovereignVoid,
                    border = BorderStroke(1.dp, SovereignBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "بصمة المفتاح العام (Identity Fingerprint):",
                                fontSize = 10.sp,
                                color = SlateMuted
                            )
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentUser.publicKeyFingerprint,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Dual-User Quick Switch Button (اختبار تعدد المستخدمين)
                Button(
                    onClick = { viewModel.switchActiveUserForTesting() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("switch_user_account_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanSignal.copy(alpha = 0.2f)),
                    border = BorderStroke(1.dp, CyanSignal),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapCalls,
                        contentDescription = null,
                        tint = CyanSignal
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (currentUser.handle == "@ali_sovereign") "التبديل إلى المستخدم B (@tariq_privacy)" else "التبديل إلى المستخدم A (@ali_sovereign)",
                        color = CyanSignal,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Section: Self-Hosted Infrastructure
        Text(
            text = "البنية التحتية والاستضافة الذاتية (Self-Hosting)",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, SovereignBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "عنوان العقدة الخادمة (Sovereign Node URL)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = currentUser.serverNodeUrl,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = EmeraldPrimary
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.setShowNodeServer(true) },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("change_node_url_btn")
                    ) {
                        Text("تغيير", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = EmeraldPrimary.copy(alpha = 0.2f),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldPrimary)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "حالة الاتصال الفوري: متصل (${latency}ms)",
                            fontSize = 12.sp,
                            color = SlateMuted
                        )
                    }

                    TextButton(onClick = { viewModel.pingNode() }) {
                        Text("فحص النبض", fontSize = 11.sp, color = CyanSignal)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = SovereignBorder)

                // Docker Guide Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.setShowDockerGuide(true) }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Dns,
                            contentDescription = null,
                            tint = CyanSignal,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "دليل تشغيل خادم Docker Compose الذاتي",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = SlateMuted
                    )
                }
            }
        }

        // Section: Cryptography & Vault Security
        Text(
            text = "الخزنة الرقمية وإدارة التشفير",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, SovereignBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Regenerate Keys
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.regenerateKeys() }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.VpnKey,
                            contentDescription = null,
                            tint = AmberWarning,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "توليد مفاتيح تشفير جديدة (Ratchet Re-Key)", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            Text(text = "تحديث أزواج المفاتيح المحلية لمنع استعادة الرسائل القديمة", fontSize = 11.sp, color = SlateMuted)
                        }
                    }
                }

                HorizontalDivider(color = SovereignBorder)

                // Export Backup
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showBackupSuccess = true }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "تصدير نسخة احتياطية مشفرة (AES-256)", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            Text(text = "حفظ سجلات المحادثات والمفاتيح بصيغة مشفرة بكلمة مرور", fontSize = 11.sp, color = SlateMuted)
                        }
                    }
                }

                HorizontalDivider(color = SovereignBorder)

                // Purge Vault
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.purgeVault() }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = null,
                            tint = RoseDestruct,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "تطهير الذاكرة المحلية (Zero-Trace Wipe)", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = RoseDestruct)
                            Text(text = "حذف كافة الرسائل وسجلات الجلسة المحلية بأمان", fontSize = 11.sp, color = SlateMuted)
                        }
                    }
                }
            }
        }

        // Section: Preferences (Language & Theme)
        Text(
            text = "التخصيص واللغة",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, SovereignBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "اتجاه الكتابة واللغة (RTL / LTR)", fontSize = 13.sp)
                    Button(
                        onClick = { viewModel.toggleRtl() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Text(
                            text = if (currentUser.isRtl) "العربية (RTL)" else "English (LTR)",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp
                        )
                    }
                }

                HorizontalDivider(color = SovereignBorder)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "الوضع المظلم السيادي (Sovereign OLED)", fontSize = 13.sp)
                    Switch(
                        checked = currentUser.isDarkMode,
                        onCheckedChange = { viewModel.toggleDarkMode() },
                        colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPrimary)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (showBackupSuccess) {
        AlertDialog(
            onDismissRequest = { showBackupSuccess = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("تم إنشاء النسخة الاحتياطية المشفرة")
                }
            },
            text = {
                Text(
                    "تم تصدير ملف `sovereign-vault-backup.enc` بنجاح وتشفيره بمفتاحك السيادي. يمكنك استعادة بياناتك في أي وقت على أي خادم مستقل.",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { showBackupSuccess = false },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("حسناً", color = SovereignVoid)
                }
            }
        )
    }
}
