# 🚌 Advanced Bus Route Planner  
### PDSA-Focused Web Application (Spring Boot)

## 📌 Project Overview
The **Advanced Bus Route Planner** is a **web-based application** developed using **Pure Data Structures and Algorithms (PDSA)** principles and implemented with **Spring Boot and HTML/CSS/JavaScript**.

The system helps users find **optimal bus routes in Sri Lanka** based on:
- Distance
- Travel time
- Fare cost
- Number of transfers
- Accessibility and real-world constraints

🚫 **No Machine Learning is used**.  
All routing decisions are made using **graph algorithms**, ensuring strong **academic and algorithmic rigor**.

---

## 🎯 Project Objectives
- Implement real-world **graph-based routing**
- Apply **Dijkstra, BFS, and A\*** algorithms
- Support **multi-criteria route optimization**
- Analyze **time and space complexity**
- Integrate backend algorithms with a web frontend
- Build a user-friendly and responsive web interface

---

## 🚀 Key Features
- 🔹 Shortest route (distance-based)
- 🔹 Fastest route (time-based)
- 🔹 Cheapest route (fare-based)
- 🔹 Minimum bus transfers
- 🔹 Multi-objective weighted routing
- 🔹 Landmark-based navigation
- 🔹 Accessibility-friendly routing
- 🔹 Simulated real-time route recalculation
- 🔹 Parallel computation of alternative routes
- 🔹 Offline graph storage and compression

---

## 🧠 Algorithms Used (PDSA)
- **Dijkstra’s Algorithm** – shortest, fastest, cheapest routes
- **Breadth First Search (BFS)** – minimum transfer routing
- **A\* Algorithm** – heuristic-based optimal routing
- **Bidirectional / Parallel Search** – performance optimization
- **Weighted Multi-Criteria Optimization**

---

## 🗂️ Data Structures Used
- Graph (Adjacency List)
- Priority Queue (Min Heap)
- HashMap
- Multi-layer Graphs
- Serialized Graph Storage

---

## 🏗️ System Architecture
Web UI (HTML/CSS/JavaScript)
↓
Spring Boot Controllers
↓
Routing Service Layer
↓
Graph Algorithms Engine (PDSA)
↓
Optimized Route Results


---

## 👥 Team Members & Responsibilities

### 👤 Member 1 – Core Graph & Shortest Path Engineer
- Graph design (nodes, edges, weights)
- Dijkstra implementation
- Edge case handling
- Complexity analysis

### 👤 Member 2 – Advanced Routing & Optimization Engineer
- Multi-objective routing
- Landmark-based navigation
- Minimum transfer routing (BFS)
- Accessibility-first routing

### 👤 Member 3 – Performance & Storage Engineer
- Real-time route recalculation (simulation)
- Parallel route computation
- Route difficulty scoring
- Offline graph compression

### 👤 Member 4 – Web Application & Integration Engineer
- Spring Boot backend development
- REST controllers & services
- HTML/CSS/JavaScript frontend
- Route visualization and UI integration

---

## 🛠️ Technologies Used
- **Backend:** Java, Spring Boot
- **Frontend:** HTML, CSS, JavaScript
- **IDE:** IntelliJ IDEA / Eclipse
- **Algorithms:** Dijkstra, BFS, A*
- **Version Control:** Git & GitHub
- **Database (Optional):** MySQL / File-based storage

---

## 🌐 How to Run the Project

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/your-username/advanced-bus-route-planner.git
2️⃣ Open the Project
Open using IntelliJ IDEA or Eclipse

Sync Maven/Gradle dependencies

3️⃣ Run the Application
mvn spring-boot:run
4️⃣ Access the Web App
http://localhost:8080
📊 Academic Focus (PDSA)
Graph representation comparison

Time & space complexity analysis

Algorithm performance evaluation

Real-world constraint handling

Trade-off analysis between routing strategies

🎓 Learning Outcomes
Practical application of graph data structures

Mastery of shortest path algorithms

Understanding optimization techniques

Backend–Frontend integration

Real-world system design using PDSA

📄 License
This project is developed for academic purposes only.
