<h1 align="center">E-Medical Management System 🏥</h1>

<p align="center">
  <strong>A modern, intelligent, and highly optimized Java Desktop Application for Hospital Management.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java"/>
  <img src="https://img.shields.io/badge/Google_Gemini-AI-4285F4?style=for-the-badge&logo=google&logoColor=white" alt="Gemini AI"/>
  <img src="https://img.shields.io/badge/FlatLaf-Dark_Theme-2c2c2c?style=for-the-badge" alt="FlatLaf"/>
</p>

<hr>

## 🚀 Overview

The **E-Medical Management System** is a comprehensive, enterprise-grade desktop application built with Java Swing. It revolutionizes hospital administration by replacing tedious manual data entry with an **intelligent, conversational AI Assistant** powered by the **Google Gemini (Gemma-3)** model.

Designed with a focus on User Experience (UX), the application utilizes the **FlatLaf** Look and Feel to deliver a sleek, modern, and highly responsive dark-mode interface that rivals web applications.

## ✨ Key Features

- **🤖 AI-Powered Data Entry**: Administrators can add patients, doctors, and schedule appointments entirely through natural language chat.
- **🔐 Secure Authentication**: The AI assistant enforces an authorization gateway (Key: `ADMIN123`) before accepting operational commands.
- **🎨 Premium UI/UX**: Built with FlatLaf, featuring rounded geometry, striped data tables, custom typography, and vibrant accent colors.
- **📊 Real-time Dashboard**: A dynamic statistical dashboard tracking active patients, registered doctors, and pending appointments.
- **💾 Persistent Storage**: Lightweight, robust local file handling (`.txt` based JSON-like structures) ensuring zero data loss between sessions without the overhead of an SQL server.

## 🛠️ Technology Stack

- **Core Language**: Java (JDK 17+)
- **UI Framework**: Java Swing + [FlatLaf](https://www.formdev.com/flatlaf/) (Flat Light and Dark Look and Feel)
- **AI Integration**: Custom HTTP Client integration with Google's Generative AI API (`gemma-3-4b-it` model)
- **Architecture**: MVC Pattern (Model-View-Controller paradigm)

## 📦 How to Run

You don't need to be a developer to test this application! I have provided a simple execution script.

1. **Clone the repository:**
   ```bash
   git clone https://github.com/dogra1428/E-Medical-Management-System-.git
   cd E-Medical-Management-System-
   ```

2. **Run the Application (Windows):**
   Simply double-click the `Run-EMedical.bat` file in the root directory!
   
   *(Alternatively, run it from the terminal)*:
   ```bash
   Run-EMedical.bat
   ```

3. **Test the AI Assistant:**
   - Navigate to the **Patients** Tab.
   - Click the green **"Add via AI 🤖"** button in the bottom right corner.
   - When prompted for the Authorization Key, type: `ADMIN123`
   - Start typing patient details naturally!

---
<p align="center">
  <i>Developed by <b>Mayank Dogra</b> — Showcasing expertise in Java Architecture, AI Integration, and Modern UI Design.</i>
</p>
