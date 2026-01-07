# Board - Spring MVC 게시판 시스템

> 전통적인 Spring MVC 패턴을 활용한 서버사이드 렌더링 게시판 웹 애플리케이션

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-blue.svg)](https://www.mysql.com/)

---

## 📋 목차

- [프로젝트 개요](#프로젝트-개요)
- [기술 스택](#기술-스택)
- [주요 기능](#주요-기능)
- [아키텍처](#아키텍처)
- [데이터베이스 설계](#데이터베이스-설계)
- [화면 구성](#화면-구성)
- [설치 및 실행](#설치-및-실행)
- [개발 포인트](#개발-포인트)

---

## 프로젝트 개요

Board는 **전통적인 Spring MVC 패턴**과 **서버사이드 렌더링(SSR)**을 활용한 게시판 시스템입니다. REST API가 아닌 서버에서 HTML을 생성하여 반환하는 방식으로, Spring Framework의 기본적인 웹 개발 방식을 보여줍니다.

### 개발 목표

- **Spring MVC 패턴**: Controller-Service-DAO 계층 분리
- **Thymeleaf 템플릿**: 서버사이드 렌더링
- **Spring JDBC**: NamedParameterJdbcTemplate 활용
- **Session 기반 인증**: 전통적인 세션 방식 로그인
- **페이지네이션**: 대용량 데이터 처리

---

## 기술 스택

### Backend
- **Java 21** - 최신 LTS 버전
- **Spring Boot 3.5.5** - 웹 애플리케이션 프레임워크
- **Spring JDBC** - NamedParameterJdbcTemplate 기반 데이터 접근
- **Spring Validation** - 입력 데이터 검증

### Frontend
- **Thymeleaf** - 서버사이드 템플릿 엔진
- **HTML/CSS** - 기본 웹 표준
- **Bootstrap** (선택) - UI 프레임워크

### Database
- **MySQL 8.x** - 관계형 데이터베이스
- **MariaDB** - MySQL 호환 데이터베이스

### Tools
- **Lombok** - 보일러플레이트 코드 제거
- **Gradle** - 빌드 자동화

---

## 주요 기능

### 🔐 회원 관리
- ✅ 회원가입 (중복 검증)
- ✅ 로그인/로그아웃
- ✅ Session 기반 인증
- ✅ 비밀번호 암호화

### 📝 게시글 관리
- ✅ 게시글 작성 (로그인 필수)
- ✅ 게시글 목록 조회 (페이지네이션)
- ✅ 게시글 상세 조회 (조회수 증가)
- ✅ 게시글 수정/삭제 (작성자만 가능)
- ✅ 제목/내용 검색

### 💬 댓글 시스템
- ✅ 댓글 작성 (로그인 필수)
- ✅ 댓글 목록 조회
- ✅ 댓글 삭제 (작성자/관리자만 가능)

### 👥 권한 관리
- ✅ 역할 기반 접근 제어 (USER, ADMIN)
- ✅ 관리자 전용 기능
- ✅ 작성자 본인 확인

---

## 아키텍처

### Spring MVC 패턴

```
┌─────────────────────────────────────┐
│         View Layer (Thymeleaf)      │  ← HTML Templates
│  (list.html, detail.html, etc.)     │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         Controller Layer            │  ← HTTP Request/Response
│  (PostController, UserController)   │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│          Service Layer              │  ← Business Logic
│  (PostService, CommentService)      │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│           DAO Layer                 │  ← Data Access
│  (PostDao, UserDao, CommentDao)     │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         MySQL Database              │  ← Data Storage
└─────────────────────────────────────┘
```

### 패키지 구조

```
com.example.board
├── controller/          # MVC 컨트롤러
│   ├── PostController   # 게시글 관련 요청 처리
│   ├── UserController   # 회원 관련 요청 처리
│   └── CommentController # 댓글 관련 요청 처리
├── service/            # 비즈니스 로직
│   ├── PostService
│   ├── UserService
│   └── CommentService
├── dao/                # 데이터 접근 계층
│   ├── PostDao         # NamedParameterJdbcTemplate 사용
│   ├── UserDao
│   └── CommentDao
├── dto/                # 데이터 전송 객체
│   ├── Post
│   ├── User
│   ├── Comment
│   └── LoginInfo       # 세션 정보
└── exception/          # 예외 처리
    ├── GlobalExceptionHandler
    ├── ResourceNotFoundException
    └── UnauthorizedException
```

---

## 데이터베이스 설계

### ERD

```
┌─────────┐         ┌──────────┐         ┌──────────┐
│  User   │         │   Post   │         │ Comment  │
├─────────┤         ├──────────┤         ├──────────┤
│ userId  │◄────┐   │ postId   │◄────┐   │commentId │
│ username│     │   │ userId   │     │   │ postId   │
│ password│     └───│ title    │     └───│ userId   │
│ email   │         │ content  │         │ content  │
│ name    │         │ viewCount│         │createdAt │
│ role    │         │ active   │         └──────────┘
│createdAt│         │createdAt │
└─────────┘         │updatedAt │
                    └──────────┘
```

### 주요 테이블

#### User (회원)
```sql
CREATE TABLE user (
    userId INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER',
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

#### Post (게시글)
```sql
CREATE TABLE post (
    postId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    viewCount INT DEFAULT 0,
    active BOOLEAN DEFAULT TRUE,
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    updatedAt DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES user(userId)
);
```

#### Comment (댓글)
```sql
CREATE TABLE comment (
    commentId INT AUTO_INCREMENT PRIMARY KEY,
    postId INT NOT NULL,
    userId INT NOT NULL,
    content TEXT NOT NULL,
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (postId) REFERENCES post(postId) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES user(userId)
);
```

---

## 화면 구성

### 주요 페이지

| 페이지 | 템플릿 파일 | URL | 설명 |
|--------|------------|-----|------|
| 메인 | `list.html` | `/` | 게시글 목록 (페이지네이션) |
| 상세보기 | `detail.html` | `/post?postId={id}` | 게시글 상세 + 댓글 |
| 작성 | `writeForm.html` | `/writeForm` | 게시글 작성 폼 |
| 수정 | `updateform.html` | `/updateForm?postId={id}` | 게시글 수정 폼 |
| 로그인 | `loginform.html` | `/loginform` | 로그인 폼 |
| 회원가입 | `userRegForm.html` | `/userRegForm` | 회원가입 폼 |
| 웰컴 | `welcome.html` | `/welcome` | 가입 완료 페이지 |

### 페이지 흐름

```
/ (list.html)
   ├─> /post?postId=1 (detail.html)
   │      ├─> POST /addComment (댓글 작성)
   │      └─> POST /deleteComment?commentId=1
   │
   ├─> /writeForm (로그인 필요)
   │      └─> POST /write
   │
   ├─> /updateForm?postId=1 (작성자만)
   │      └─> POST /update
   │
   └─> /loginform
          └─> POST /login
                 └─> HttpSession 생성
```

---

## 설치 및 실행

### 1. 저장소 클론

```bash
git clone <repository-url>
cd 04_post/post
```

### 2. 데이터베이스 설정

MySQL/MariaDB에 데이터베이스 생성:

```sql
CREATE DATABASE boarddb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 설정 파일 수정

`src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/boarddb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Thymeleaf 설정
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
```

### 4. 애플리케이션 실행

```bash
./gradlew bootRun
```

### 5. 브라우저 접속

http://localhost:8080

---

## 개발 포인트

### 1. Spring JDBC 활용

**NamedParameterJdbcTemplate을 사용한 SQL 쿼리:**

```java
@Repository
public class PostDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<Post> getPosts(int page) {
        String sql = """
            SELECT p.*, u.username, u.name
            FROM post p
            INNER JOIN user u ON p.userId = u.userId
            WHERE p.active = true
            ORDER BY p.postId DESC
            LIMIT :limit OFFSET :offset
            """;

        Map<String, Object> params = Map.of(
            "limit", 10,
            "offset", (page - 1) * 10
        );

        return jdbcTemplate.query(sql, params, postRowMapper());
    }
}
```

### 2. Session 기반 인증

**전통적인 HttpSession 활용:**

```java
@PostMapping("/login")
public String login(@RequestParam String username,
                   @RequestParam String password,
                   HttpSession session) {
    User user = userService.login(username, password);

    LoginInfo loginInfo = new LoginInfo(
        user.getUserId(),
        user.getUsername(),
        user.getRole()
    );
    session.setAttribute("loginInfo", loginInfo);

    return "redirect:/";
}
```

### 3. Thymeleaf 템플릿 엔진

**서버사이드 렌더링:**

```html
<!-- list.html -->
<div th:each="post : ${list}">
    <h3>
        <a th:href="@{/post(postId=${post.postId})}"
           th:text="${post.title}">제목</a>
    </h3>
    <p th:text="${post.name}">작성자</p>
    <p th:text="${#temporals.format(post.createdAt, 'yyyy-MM-dd HH:mm')}">
        작성일
    </p>
</div>

<!-- 로그인 상태에 따른 조건부 렌더링 -->
<div th:if="${loginInfo != null}">
    <span th:text="${loginInfo.username}">사용자</span>님 환영합니다!
    <a href="/logout">로그아웃</a>
</div>
<div th:unless="${loginInfo != null}">
    <a href="/loginform">로그인</a>
</div>
```

### 4. 페이지네이션 구현

**효율적인 대용량 데이터 처리:**

```java
@GetMapping("/")
public String list(@RequestParam(defaultValue = "1") int page,
                  Model model) {
    int totalCount = postService.getTotalCount();
    int pageSize = 10;
    int pageCount = (int) Math.ceil((double) totalCount / pageSize);

    List<Post> posts = postService.getPosts(page);

    model.addAttribute("list", posts);
    model.addAttribute("pageCount", pageCount);
    model.addAttribute("currentPage", page);

    return "list";
}
```

### 5. 권한 기반 접근 제어

**작성자 검증:**

```java
@PostMapping("/delete")
public String delete(@RequestParam int postId, HttpSession session) {
    LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");

    if (loginInfo == null) {
        throw new UnauthorizedException("로그인이 필요합니다");
    }

    Post post = postService.getPost(postId);

    // 작성자 또는 관리자만 삭제 가능
    if (!post.getUserId().equals(loginInfo.getUserId())
        && !"ADMIN".equals(loginInfo.getRole())) {
        throw new UnauthorizedException("권한이 없습니다");
    }

    postService.deletePost(postId);
    return "redirect:/";
}
```

### 6. 예외 처리

**GlobalExceptionHandler로 일관된 에러 페이지:**

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorized(UnauthorizedException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/403";
    }
}
```

---

## 프로젝트 특징

### REST API vs MVC 비교

| 특징 | REST API | Spring MVC (본 프로젝트) |
|------|----------|-------------------------|
| **렌더링** | 클라이언트 (CSR) | 서버 (SSR) |
| **응답 형식** | JSON | HTML |
| **템플릿** | React/Vue | Thymeleaf |
| **인증** | JWT | Session |
| **SEO** | 추가 작업 필요 | 기본 지원 |
| **초기 로딩** | 느림 (JS 로드) | 빠름 (완성된 HTML) |

### 전통적인 웹 개발 방식의 장점

1. **SEO 최적화**: 서버에서 완성된 HTML 제공
2. **빠른 초기 렌더링**: JavaScript 로드 불필요
3. **간단한 구조**: 프론트엔드 빌드 과정 불필요
4. **낮은 진입 장벽**: HTML/CSS만으로 UI 구성

---

## 향후 계획

- [ ] Spring Security 적용
- [ ] 파일 첨부 기능
- [ ] 게시글 검색 고도화
- [ ] 관리자 페이지
- [ ] 게시글 좋아요 기능
- [ ] 비밀 댓글 기능

---

**Last Updated:** 2025-01-08
