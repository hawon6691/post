# 📌 Project Name

간단한 프로젝트 설명을 여기에 작성하세요.  
(예: Spring Boot 기반 웹 게시판 시스템)

---

## 🚀 Features
- 🧩 모듈형 구조 (Controller, DTO, DAO, Service 계층)
- 🌐 데이터베이스 연동 (MySQL, MariaDB 지원)
- 🎨 기본 템플릿 제공 (회원가입, 로그인, 글 작성, 상세보기 등)
- 🔒 인증/인가 (세션 관리 포함)
- 🧪 테스트 및 배포 워크플로우 내장

---

## 📂 Project Structure

project-root/ 
┣ 📂 src 
┃ ┣ 📂 main
┃ ┃ ┣ 📂 java # Java 소스 코드 
┃ ┃ ┗ 📂 resources # 설정 파일, 템플릿 
┃ ┗ 📂 test # 테스트 코드 
┣ build.gradle 
┗ README.md


---

## ⚙️ Requirements
- Java 21+
- Gradle
- MySQL or MariaDB

---

## 🛠 Installation & Run

```bash
# 프로젝트 클론
git clone https://github.com/username/project-name.git
cd project-name

# 빌드
gradle build

# 실행
gradle run
```

## 🧪 Testing

```bash
gradle test
```

## 📖 Usage
1. 애플리케이션 실행

2. 브라우저에서 http://localhost:8080 접속

3. 회원가입 → 로그인 → 글 작성/조회
