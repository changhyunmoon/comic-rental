# 📚 Comic Rental System

CLI(Command Line Interface) 기반으로 동작하는 만화책 대여 관리 시스템입니다.  
만화 등록, 회원 관리, 대여 및 반납 기능을 효율적으로 관리할 수 있도록 설계되었습니다.

---

## 1. 🛠 기술 스택
| 기술 | 설명 |
| :--- | :--- |
| **Java 17** | 애플리케이션 메인 로직 개발 언어 |
| **MySQL** | 관계형 데이터베이스 관리 시스템 |
| **Dotenv** | 환경 변수 관리 (.env 파일 사용) |
---
## 2. 📝 프로젝트 소개
**Comic Rental System**은 콘솔 환경에서 동작하는 만화책 대여 관리 프로그램입니다.  
사용자는 다음과 같은 작업을 수행할 수 있습니다.

* **만화책 관리**: 등록 / 조회 / 수정 / 삭제 (CRUD)
* **회원 관리**: 회원 등록 및 조회
* **대여 관리**: 만화책 대여 및 반납 처리
* **목록 조회**: 대여 기록 확인 및 연체 관리
---
## 3. 🚀 실행 방법 (Getting Started)

프로젝트를 로컬 환경에서 실행하기 위해 아래 단계에 따라 설정을 진행해 주세요.

### 3.1 프로젝트 클론 (Clone)

터미널(Terminal) 또는 커맨드 창을 열고 아래 명령어를 입력하여 프로젝트를 복제합니다.

```bash
# 레포지토리 클론
git clone https://github.com/changhyunmoon/comic-rental.git
```

프로젝트 디렉토리로 이동
```bash
cd comic-rental
```

### 3.2 환경 변수 설정 (`.env`)
프로젝트의 보안과 유연한 설정을 위해 루트 디렉토리에 `.env` 파일을 생성하고, 본인의 DB 환경에 맞춰 아래 내용을 작성합니다.
```

```env
# Database 연결 정보
DB_URL=jdbc:mysql://localhost:3306/comic_rental?serverTimezone=Asia/Seoul
DB_USER=your_username    # MySQL 계정명 (예: root)
DB_PASS=your_password    # MySQL 비밀번호
```
### 3.3 데이터베이스 설정
MySQL에서 아래 SQL을 실행합니다.
```
USE comic_rental;

CREATE TABLE comic (
    comic_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    is_rented BOOLEAN NOT NULL DEFAULT FALSE,
    created_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE member (
    member_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    phoneNumber VARCHAR(20) NOT NULL,
    createDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL
);

CREATE TABLE rental (
    rental_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    comic_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    rent_date DATETIME NOT NULL,
    return_date DATETIME NULL,
    created_date DATETIME NOT NULL,
    updated_date DATETIME NOT NULL,
    FOREIGN KEY (comic_id) REFERENCES comic(comic_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
);
```
### 3.4 애플리케이션 실행
IDE(IntelliJ 등)에서 아래 클래스를 실행합니다.
```
ComicApplication.java
```
---
## 4. ⚙️ 구현 기능

### 4.1 만화 관리
- 만화 등록 및 전체 목록 조회
- ID를 이용한 만화 상세 조회
- 만화 정보 수정 및 삭제 (CRUD)

### 4.2 회원 관리
- 신규 회원 등록
- 등록된 전체 회원 목록 조회

### 4.3 대여 관리
- 만화 대여 및 반납 프로세스
- 전체 대여 목록 및 미반납 목록 필터링 조회
- **연체 관리:** 대여 기간이 7일을 초과한 목록 별도 조회

---

## 5. ⌨️ 사용 가능한 명령어

콘솔 창에서 아래 명령어를 입력하여 시스템을 조작할 수 있습니다.

| 명령어 | 설명 |
| :--- | :--- |
| `comic-add` | 만화책 등록 |
| `comic-list` | 만화책 목록 조회 |
| `comic-detail [id]` | 만화책 상세 조회 |
| `comic-update [id]` | 만화책 정보 수정 |
| `comic-delete [id]` | 만화책 삭제 |
| `member-add` | 회원 등록 |
| `member-list` | 회원 목록 조회 |
| `rent [cId] [mId]` | 만화 대여 (cId: 만화 ID, mId: 회원 ID) |
| `return [rentalId]` | 만화 반납 (rentalId: 대여 번호) |
| `rental-list` | 전체 대여 목록 조회 |
| `rental-list open` | 미반납 목록 조회 |
| `rental-overdue` | 연체 목록 조회 (7일 초과) |
| `help` | 사용 가능한 명령어 목록 안내 |
| `exit` | 프로그램 종료 |

---

## 6. 🧠  구현하며 고민했던 점

### 6.1 대여 상태 관리
만화책의 중복 대여를 방지하기 위해 `comic` 테이블에 `is_rented` 필드를 설계했습니다.
- **대여 시:** `true`로 업데이트하여 추가 대여 차단
- **반납 시:** `false`로 업데이트하여 대여 가능 상태로 전환

### 6.2 데이터 무결성
`rental` 테이블의 `comic_id`와 `member_id`를 **Foreign Key(외래 키)**로 설정하였습니다. 이를 통해 데이터베이스 수준에서 존재하지 않는 만화나 회원의 대여 기록이 생성되는 것을 방지했습니다.

### 6.3 계층 구조 설계 (Layered Architecture)
비즈니스 로직과 데이터 접근 로직을 분리하여 유지보수성을 높였습니다.

```text
       [ Architecture ]

      ┌──────────────┐
      │  Controller  │ ─── 사용자 요청 수신 및 응답
      └──────┬───────┘
             ▼
      ┌──────────────┐
      │    Service   │ ─── 주요 비즈니스 로직 수행
      └──────┬───────┘
             ▼
      ┌──────────────┐
      │  Repository  │ ─── 데이터베이스 접근 계층
      └──────┬───────┘
             ▼
      ┌──────────────┐
      │   Database   │ ─── MySQL (Data Storage)
      └──────────────┘
```
이를 통해 비즈니스 로직과 데이터 접근 로직을 분리했습니다.

---
## 7. 아쉬웠던 점
-   CLI 기반이라 사용자 인터페이스가 제한적
-   예외 처리 로직을 더 보완할 필요
-   테스트 코드 추가 필요

