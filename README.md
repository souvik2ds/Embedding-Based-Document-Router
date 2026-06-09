# 🚀 Embedding-Based Document Router

A **Spring AI** powered **Multi-PDF RAG (Retrieval-Augmented Generation)** system that implements **Embedding-Based Document Routing** using a **Two-Stage Retrieval Architecture**.

Instead of using an LLM to decide which document should be searched, this project leverages **vector embeddings** to semantically route the user's question to the most relevant document before performing chunk-level retrieval. This significantly reduces latency, eliminates an extra LLM call, and provides a more scalable architecture.

---

# ✨ Features

* 📄 Multi-PDF Knowledge Base
* 🧠 Embedding-Based Document Routing
* 🔍 Two-Stage Retrieval Architecture
* 🗂️ Metadata-Based Filtering
* 💾 Persistent Main Vector Store
* 💾 Persistent Router Vector Store
* 🤖 Automatic Document Description Generation
* ⚡ Automatic Description Embedding Generation
* 🚫 Duplicate PDF Detection
* 📋 List All Uploaded PDFs
* 🎯 Semantic Document Selection
* ⚙️ Spring AI + Ollama Integration

---

# 🏗️ Architecture

## Upload Pipeline

```
                    Upload PDF
                         │
        ┌────────────────┴────────────────┐
        │                                 │
        ▼                                 ▼
   Generate Chunks              Generate Description
        │                                 │
        ▼                                 ▼
 Main VectorStore               Router VectorStore
(vector-store.json)        (router-vector-store.json)
```

---

## Query Pipeline

```
                 User Question
                        │
                        ▼
         Router Vector Store
      (Document-Level Retrieval)
                        │
                        ▼
              Selected fileName
                        │
                        ▼
          Main Vector Store
        (Chunk-Level Retrieval)
                        │
                        ▼
               Top Relevant Chunks
                        │
                        ▼
                       LLM
                        │
                        ▼
                  Final Answer
```

---

# ⭐ Key Innovation

Traditional Multi-PDF RAG systems often use an **LLM Router**:

```
Question
     │
     ▼
LLM Router
     │
     ▼
Selected Document
     │
     ▼
Vector Search
     │
     ▼
LLM Response
```

This project replaces the router LLM with **Embedding-Based Routing**:

```
Question
     │
     ▼
Router Vector Store
     │
     ▼
Selected Document
     │
     ▼
Main Vector Store
     │
     ▼
LLM Response
```

As a result:

* One LLM call is eliminated
* Lower latency
* Lower inference cost
* Better scalability
* Cleaner architecture

---

# 📂 Project Structure

```
src
 ├── controller
 ├── service
 ├── config
 ├── model
 └── resources

vector-store.json
router-vector-store.json
```

---

# ⚙️ Tech Stack

* Java 21
* Spring Boot
* Spring AI
* Ollama
* SimpleVectorStore
* Jackson
* Maven

---

# 🔄 Retrieval Strategy

This project implements **Hierarchical (Two-Stage) Retrieval**:

### Stage 1

Document-Level Retrieval

```
Question
      │
      ▼
Router Vector Store
      │
      ▼
Most Relevant PDF
```

### Stage 2

Chunk-Level Retrieval

```
Selected PDF
      │
      ▼
Main Vector Store
      │
      ▼
Top-K Relevant Chunks
```

### Stage 3

Generation

```
Retrieved Context
      │
      ▼
LLM
      │
      ▼
Answer
```

---

# 🎯 Advantages

* Supports multiple PDFs
* Faster routing using embeddings
* Reduced LLM usage
* Production-oriented architecture
* Separation of document-level and chunk-level retrieval
* Persistent vector storage across application restarts

---

# 🚀 Future Enhancements

* Hybrid Search (Keyword + Semantic)
* Re-ranking
* Multi-Query Retrieval
* Context Compression
* Parent-Child Retrieval
* Agentic RAG
* UI Integration
* Cloud Vector Database Support

---

# 📚 Learning Outcome

This project demonstrates that production-grade RAG systems are not only about embeddings and vector search. They also require thoughtful design around:

* Document management
* Metadata design
* Semantic routing
* Persistent vector stores
* Retrieval optimization
* Scalable architecture

---

## ⭐ Developed as part of a continuous journey of learning and implementing advanced RAG architectures using Spring AI.
