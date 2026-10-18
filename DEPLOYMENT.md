# Sovereign Platform — Self-Hosting & Production Deployment Guide

## 1. Overview
The Sovereign Super App is built to be **Sovereign by Design**. It provides:
1. **Encrypted Messaging (E2EE)**: Peer-to-peer security with Signal-style cryptographic safety numbers and device identity key verification.
2. **Decentralized Channels & Communities**: Public broadcast channels and high-capacity discussion rooms.
3. **Sovereign Micro-Feed**: Open, chronological timeline for public discourse with hashtags, comments, likes, and reposts.
4. **Peer-to-Peer Calling (WebRTC)**: Direct encrypted audio and video calling with cryptographic safety number verification.
5. **Full Self-Hosting Control**: Compatible with private VPS, bare-metal servers, Docker Compose, PostgreSQL, Redis, and MinIO.

---

## 2. Testing Dual-User Workflows (المستخدم A والمستخدم B)
To verify multi-user flows directly on device:
1. In the app top bar or **السيادة (Vault)** tab, tap **التبديل إلى المستخدم B (@tariq_privacy)**.
2. Active identity switches immediately to User B.
3. Send a message to `@ali_sovereign` or post to the public feed.
4. Switch back to User A (`@ali_sovereign`) to observe real-time delivery, read receipts, and incoming notifications.

---

## 3. Server Deployment via Docker Compose
To host your own sovereign gateway node on a Linux VPS (Ubuntu 22.04+):

```bash
# 1. Clone your sovereign repository
git clone <your-sovereign-repo-url> /opt/sovereign
cd /opt/sovereign/server

# 2. Copy and configure secrets
cp .env.example .env
nano .env

# 3. Launch Docker containers
docker compose up -d

# 4. Verify service health
docker compose ps
docker compose logs -f sovereign-gateway
```

### coturn STUN/TURN for WebRTC Audio & Video Calls
For peer-to-peer WebRTC calls across restrictive NAT firewalls:
```bash
sudo apt install -y coturn
sudo systemctl enable coturn
sudo systemctl start coturn
```
Configure `/etc/turnserver.conf` with your domain and TLS certificate.

---

## 4. Backup & Disaster Recovery
- **Database Backup**:
  ```bash
  docker exec -t sovereign-postgres pg_dump -U sovereign sovereign_db > sovereign_backup_$(date +%F).sql
  ```
- **Database Restore**:
  ```bash
  cat sovereign_backup.sql | docker exec -i sovereign-postgres psql -U sovereign -d sovereign_db
  ```
- **Local App Vault Backup**:
  Users can tap "تصدير نسخة احتياطية مشفرة (AES-256)" in the **السيادة** screen to export their private key ring and message archives in encrypted format.

---

## 5. Security & Threat Model Report
- **Zero Knowledge Message Storage**: Message payloads can only be decrypted with private session keys resident on end-user devices.
- **Man-In-The-Middle (MITM) Protection**: Visual and alphanumeric Safety Numbers enable out-of-band identity verification.
- **Ephemeral Message Shredding**: Self-destruct timers wipe local cache data after expiry.
- **Sovereign Node Isolation**: All endpoints run inside dedicated Docker networks without external port exposure except TLS port 8443.
