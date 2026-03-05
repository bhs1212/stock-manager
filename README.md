# 🍔 BURGER KING 재고관리 시스템

버거킹 매장의 재고 관리, 메뉴 판매 처리, 판매 통계 대시보드를 제공하는 웹 애플리케이션입니다.

## 📌 프로젝트 소개

매장에서 자재 재고를 등록/수정/삭제하고, 메뉴 판매 시 레시피 기반으로 재고를 자동 차감하며, 일별/주별/월별 판매 통계를 확인할 수 있는 시스템입니다.

## 🛠 기술 스택

| 구분     | 기술                                      |
| -------- | ----------------------------------------- |
| Backend  | Java 17, Spring Boot 3.5, Spring Security |
| DB       | MySQL 8.0, MyBatis                        |
| Frontend | Thymeleaf, Bootstrap 5                    |
| Build    | Maven (JAR)                               |
| etc      | Lombok, BCrypt                            |

## 📁 프로젝트 구조

```
src/main/java/com/burger/stock_manager/
├── StockManagerApplication.java       # 메인 애플리케이션
├── config/
│   ├── SecurityConfig.java            # Spring Security 설정
│   ├── WebConfig.java                 # Web MVC 설정
│   └── GlobalExceptionHandler.java    # 전역 예외 처리
├── controller/
│   ├── AuthController.java            # 회원가입, 로그인 페이지
│   ├── StockController.java           # 재고 CRUD
│   └── SalesController.java           # 판매 처리, 대시보드
├── service/
│   ├── UserService.java               # 회원가입, 중복체크
│   ├── CustomUserDetailsService.java  # Spring Security 인증
│   ├── StockService.java              # 재고 비즈니스 로직
│   └── SalesService.java              # 판매 처리, 통계 조회
├── mapper/
│   ├── UserMapper.java                # 사용자 SQL
│   ├── StockMapper.java               # 재고 SQL
│   └── SalesMapper.java               # 판매/레시피 SQL
├── model/
│   ├── UserDTO.java
│   ├── StockDTO.java
│   ├── RecipeDTO.java
│   ├── SalesLogDTO.java
│   └── SalesStatDTO.java
└── exception/
    ├── InsufficientStockException.java
    └── RecipeNotFoundException.java
```

## 🗄 ERD

```
+----------------+       +----------------+       +----------------+
|     users      |       |     stock      |       |     recipe     |
+----------------+       +----------------+       +----------------+
| id (PK)        |       | id (PK)        |<──────| id (PK)        |
| username (UNI) |       | item_name      |       | menu_name      |
| password       |       | quantity       |       | stock_id (FK)  |
| name           |       | unit           |       | required_qty   |
| role           |       | expiration_date|       +----------------+
+----------------+       | reg_date       |
                         | is_deleted     |       +----------------+
                         +----------------+       |   sales_log    |
                                                  +----------------+
                                                  | id (PK)        |
                                                  | menu_name      |
                                                  | sell_count     |
                                                  | sale_date      |
                                                  +----------------+
```

### 테이블 설명

- **users**: 사용자 계정 (role: USER / ADMIN)
- **stock**: 자재 재고 (논리 삭제 방식 - is_deleted)
- **recipe**: 메뉴별 필요 자재와 수량 (stock 테이블과 연관)
- **sales_log**: 판매 이력

## ✨ 주요 기능

### 인증/권한

- Spring Security 기반 로그인/로그아웃 (CSRF 보호 적용)
- BCrypt 비밀번호 암호화
- ADMIN/USER 역할 분리 (ADMIN만 재고 등록/수정/삭제 가능)

### 재고 관리

- 자재 등록, 수정, 삭제 (논리 삭제)
- 삭제된 자재 재등록 시 자동 복원
- 유통기한 임박/만료 시각적 경고 (배지 + 행 색상)
- 재고 부족(10개 미만) 강조 표시
- 자재명 검색 및 페이지네이션

### 판매 처리

- 메뉴 선택 시 레시피 기반 재고 자동 차감
- 차감 전 재고 충분 여부 검증
- 판매 로그 자동 저장

### 판매 대시보드

- 일별/주별/월별 판매 통계
- 연월 선택 커스텀 조회
- 최근 판매 내역 목록

## 🚀 실행 방법

### 1. 사전 요구사항

- Java 17
- MySQL 8.0
- Maven

### 2. DB 설정

```sql
CREATE DATABASE burger_stock;
USE burger_stock;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50),
    role VARCHAR(20) DEFAULT 'USER'
);

CREATE TABLE stock (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    quantity INT DEFAULT 0,
    unit VARCHAR(20),
    expiration_date DATE,
    reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted TINYINT DEFAULT 0
);

CREATE TABLE sales_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    menu_name VARCHAR(100) NOT NULL,
    sell_count INT NOT NULL,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE recipe (
    id INT AUTO_INCREMENT PRIMARY KEY,
    menu_name VARCHAR(50),
    stock_id INT,
    required_quantity INT,
    FOREIGN KEY (stock_id) REFERENCES stock(id)
);
```

### 3. 환경변수 설정

`src/main/resources/application-local.properties` 파일을 생성하고 DB 접속 정보를 입력하세요:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/burger_stock?serverTimezone=Asia/Seoul
spring.datasource.username=your_username
spring.datasource.password=your_password
```

> ⚠️ 이 파일은 `.gitignore`에 등록되어 있어 Git에 올라가지 않습니다.

### 4. 실행

```bash
./mvnw spring-boot:run
```

브라우저에서 `http://localhost:8080/login` 접속

## 🧪 테스트

```bash
./mvnw test
```

### 테스트 항목

**SalesServiceTest (4건)**

- 정상 판매 시 재고 차감 및 판매 로그 저장 검증
- 레시피 미등록 메뉴 판매 시 RecipeNotFoundException 발생
- 재고 부족 시 InsufficientStockException 발생 및 차감 미실행
- 복수 재료 메뉴의 차감량 계산 정확성 검증

**StockServiceTest (7건)**

- 유통기한 남은 일수 계산 검증
- 유통기한 만료 재고 음수 계산 검증
- 자재명 빈값 등록 시 예외 발생 검증
- 수량 음수 등록 시 예외 발생 검증
- 삭제된 자재 재등록 시 복원 검증
- 신규 자재 정상 등록 검증
- 전체 페이지 수 계산 검증
