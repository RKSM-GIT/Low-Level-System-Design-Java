# Project NexusMind: The Forever-Free Personal Knowledge Nexus

**Problem Statement:** Digital content is fragmented across silos (X, Reddit, YouTube, Medium). Users "save" interesting items but lose them to "digital amnesia"—forgetting where they are saved or that they exist at all. NexusMind serves as a unified, AI-powered knowledge bank that swallows any web content and makes it searchable, chat-ready, and retrievable via a private RAG (Retrieval-Augmented Generation) system.

---

## 🛠 The Tech Stack (The "Forever Free" Hybrid)
* **Backend:** Python 3.10+ (FastAPI)
* **Vector Database:** ChromaDB (Local storage on SSD)
* **Ingestion:** * *Desktop:* Custom Chrome/Brave Extension (JavaScript/Manifest V3)
    * *Mobile:* Private Telegram Bot (via `python-telegram-bot`)
* **AI Models:**
    * *Embeddings:* `nomic-embed-text` (Running locally via Ollama)
    * *Reasoning/Chat:* Gemini 1.5 Flash API (Free Tier)
* **Networking:** Tailscale (Securely connects Mobile/Extension to Local PC)

---

## 🚀 Minimum Viable Product (MVP)
1.  **Direct Ingestion:** * Browser Extension "One-Click Save" for articles/threads.
    * Telegram Bot "Forward to Save" for mobile links.
2.  **Semantic Search:** Search by *meaning*, not just keywords (e.g., "Find that tutorial about fancy CSS animations").
3.  **Nexus Chat (RAG):** Ask questions like "Summarize the key takeaways from the X threads I saved last week about AI agents."
4.  **Hardware Optimization:** Designed to run on 4GB VRAM (RTX 3060) and 8GB System RAM without crashing.

---

## 🏗 Development Path (Phased Approach)

### Phase 1: The Foundation (Local DB & API)
* Setup Python environment and ChromaDB instance.
* Create a simple API endpoint `/ingest` that accepts a URL and text.
* Integrate **Ollama** to convert incoming text into vectors.

### Phase 2: The Extension & Bot (Inputs)
* Build the Chrome Extension to scrape the active tab's HTML and send it to `/ingest`.
* Develop the Twitter-specific logic (scrolling or "unroll" parsing).
* Setup the Telegram Bot to handle link-sharing from mobile.

### Phase 3: The Intelligence (RAG)
* Integrate Gemini 1.5 Flash via Google AI Studio (Free API Key).
* Create the `/query` endpoint that retrieves context from ChromaDB and generates a response.

---

## 🌟 Future Features
* **Vision Support:** Send a screenshot to the Telegram bot; AI describes and indexes it.
* **Nexus Quizzes:** Daily 3-question "Pulse Check" via Telegram based on your saved items to ensure retention.
* **Mermaid Mind-Maps:** Generate visual relationship maps of saved topics.
* **Auto-Tagging:** AI automatically categorizes items into "Tutorials," "Opinion," "Research," etc.

---

## ⚠️ Requirements & Constraints
* **Host Machine:** Acer Nitro 5 (RTX 3060 4GB, 8GB RAM).
* **Always-On:** The PC must be running for the Mobile Bot to function (or use Wake-on-LAN).
* **Privacy:** All raw data stays in ChromaDB on the local disk. Only relevant snippets are sent to Gemini for processing.
