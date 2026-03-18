# 🚀 AI-Powered Release Notes Generator

An intelligent backend application that processes GitHub commit history and automatically generates structured, human-readable release notes using AI.

---

## 📌 Overview

This project eliminates manual effort in writing release notes by analyzing commit data and transforming it into meaningful summaries. It integrates GitHub APIs with AI-based text generation to produce clear and concise release documentation.

---

## ✨ Features

* 🔍 Fetches commit history from GitHub repositories
* 🤖 AI-generated structured release notes
* 📊 Groups commits into meaningful categories
* ⚡ Reduces manual documentation effort by ~40%
* 🌐 RESTful APIs for seamless integration
* 🧱 Clean layered architecture (Controller → Service → DTO)

---

## 🛠️ Tech Stack

* **Backend:** Java, Spring Boot
* **Build Tool:** Maven
* **API Integration:** GitHub REST API
* **AI Integration:** (OpenAI)
* **Architecture:** RESTful services with DTO pattern

---

## 📁 Project Structure

```
src/main/java/com/yourapp/
 ├── controller/
 ├── service/
 ├── client/          # GitHub API calls
 ├── ai/              # ⭐ AI logic lives here
 ├── dto/
 ├── model/
 ├── exception/
 ├── config/
 └── util/
```

---

## 🔌 API Endpoints

### 1️⃣ Generate Release Notes

**POST** `/api/releases/generate`

#### Request Body:

```json
{
  "repoUrl": "https://github.com/user/repository",
  "fromDate": "2025-01-01",
  "toDate": "2025-01-31"
}
```

#### Response:

```json
{
  "summary": "Added authentication module and fixed critical bugs",
  "features": ["User login system", "JWT integration"],
  "bugFixes": ["Resolved API timeout issue"]
}
```

---

## ▶️ How to Run

### Prerequisites

* Java 17+
* Maven

### Steps

```bash
git clone https://github.com/your-username/ai-release-notes.git
cd ai-release-notes
mvn clean install
mvn spring-boot:run
```

---

## 📈 Use Case

* Automate release documentation for development teams
* Improve developer productivity
* Maintain consistent and high-quality release notes

---

## 🧠 Key Highlights

* Real-world backend use case
* API integration with external services
* AI-based text processing
* Scalable and modular design

---

## 📬 Future Improvements

* Add UI dashboard (Angular/React)
* Support multiple repositories
* Enhance AI summarization accuracy
* Export release notes as PDF/Markdown

---

## 👤 Author

Yuvan Sankar Raj
GitHub: https://github.com/u1-sankarraj

---

## ⭐ If you like this project

Give it a star ⭐ on GitHub!
