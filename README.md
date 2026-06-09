# Embedding-Based Document Router

A Spring AI powered Multi-PDF RAG system that implements **Embedding-Based Document Routing** using a **Two-Stage Retrieval Architecture**.

Instead of using an LLM to decide which document should be searched, the application uses **vector embeddings** to semantically route the question to the most relevant document before performing chunk retrieval.

---

## Features

- Multi-PDF Support
- Persistent Main Vector Store
- Persistent Router Vector Store
- Embedding-Based Document Routing
- Metadata-Based Filtering
- Automatic Document Description Generation
- Automatic Router Embedding Generation
- Duplicate PDF Detection
- List Uploaded PDFs
- Two-Stage Retrieval Architecture

---

## Architecture

```

                    Upload PDF
                         │
        ┌────────────────┴────────────────┐
        │                                 │
        ▼                                 ▼
   Chunk Documents              Generate Description
        │                                 │
        ▼                                 ▼
 Main VectorStore               Router VectorStore
(vector-store.json)        (router-vector-store.json)

```

### Query Flow

```

User Question
        │
        ▼
Router VectorStore
(Document-Level Retrieval)
        │
        ▼
Selected fileName
        │
        ▼
Main VectorStore
(Chunk-Level Retrieval)
        │
        ▼
Top K Chunks
        │
        ▼
LLM
        │
        ▼
Answer

```

---

## Tech Stack

- Java 21
- Spring Boot
- Spring AI
- Ollama
- SimpleVectorStore
- Jackson
- Maven

---

## Key Idea

Traditional routing:

```

Question
     ↓
LLM Router
     ↓
Vector Search
     ↓
LLM

```

This project:

```

Question
     ↓
Router Vector Store
     ↓
Document Selection
     ↓
Main Vector Store
     ↓
LLM

```

This eliminates one LLM call, making the system faster, cheaper, and more scalable.

---

## Future Improvements

- Hybrid Search
- Reranking
- Multi-Query Retrieval
- Context Compression
- Parent-Child Retrieval
- Agentic RAG
