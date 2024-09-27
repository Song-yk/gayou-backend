# gayou-backend

이 저장소는 여행 경로 추천 플랫폼인 **이대로 가유** 프로젝트의 백엔드 구현을 포함하고 있습니다. 백엔드는 **Spring Boot**와 **Gradle**을 사용하여 구축되었으며, **Vite** 환경에서 개발된 프론트엔드에 API를 제공합니다. 이 프로젝트는 안전하고 효율적인 여행 추천을 제공하는 것을 목표로 합니다.

## 목차

- [사용 기술](#사용-기술)
- [환경 변수](#환경-변수)
- [설치 방법](#설치-방법)
- [API 엔드포인트](#api-엔드포인트)
- [인증](#인증)
- [추가 의존성](#추가-의존성)

## 사용 기술

- **Spring Boot** (Gradle)
- **MySQL** (데이터베이스)
- **JWT** (인증)
- **Spring Security** (보안)
- **Vite** (프론트엔드 환경)

## 환경 변수

다음 환경 변수들이 필요하며, `.env` 파일에 추가해야 합니다:

- `MYSQL_HOST`: MySQL 데이터베이스 호스트
- `MYSQL_DB`: MySQL 데이터베이스 이름
- `MYSQL_ID`: MySQL 데이터베이스 사용자 ID
- `MYSQL_PW`: MySQL 데이터베이스 사용자 비밀번호
- `EMAIL_NAME`: 이메일 발신 주소
- `EMAIL_PASSWORD`: 이메일 계정 비밀번호
- `KAKAO_REST_API_KEY`: 카카오 API 키
- `CORS_ALLOWED_ORIGINS`: CORS 설정에서 허용된 출처 목록

## 설치 방법

1. 저장소를 클론합니다:

   ```bash
   git clone https://github.com/yourusername/yourproject.git
   ```

2. 프로젝트 디렉토리로 이동합니다:

   ```bash
   cd yourproject
   ```

3. 루트 디렉토리에 `.env` 파일을 생성하고 필요한 환경 변수를 추가합니다:

   ```bash
   MYSQL_HOST=your_mysql_host
   MYSQL_DB=your_mysql_database
   MYSQL_ID=your_mysql_user_id
   MYSQL_PW=your_mysql_password
   EMAIL_NAME=your_email
   EMAIL_PASSWORD=your_email_password
   KAKAO_REST_API_KEY=your_kakao_api_key
   CORS_ALLOWED_ORIGINS=http://localhost:3000
   ```

4. Gradle을 사용하여 프로젝트를 빌드하고 실행합니다:

   ```bash
   ./gradlew bootRun
   ```

5. 백엔드는 프론트엔드 Vite 서버에 API 엔드포인트를 제공하므로, 프론트엔드 서버도 실행되어 있어야 합니다.

## API 엔드포인트

백엔드는 다음 주요 API 엔드포인트를 제공합니다:

- **GET /api/routes**: 추천 여행 경로를 가져옵니다.
- **POST /api/auth/login**: JWT를 사용하여 사용자 인증을 수행합니다.
- **GET /api/auth/user**: JWT 토큰에 기반하여 사용자 정보를 가져옵니다.

### CORS

백엔드는 `.env` 파일에 정의된 `CORS_ALLOWED_ORIGINS` 설정에 따라 교차 출처 요청을 허용하도록 구성되어 있습니다.

## 인증

인증은 **JWT**와 **Spring Security**를 사용하여 처리됩니다. 사용자는 로그인을 통해 JWT 토큰을 받아야 하며, 보호된 엔드포인트에 접근하려면 이 토큰이 필요합니다.

- **로그인**: `/api/auth/login` 엔드포인트를 사용하여 인증하고 토큰을 받습니다.
- **토큰 사용**: 보호된 라우트에 API 요청을 할 때, `Authorization` 헤더에 `Bearer <token>` 형식으로 토큰을 포함시킵니다.

## 추가 의존성

### 데이터베이스 연동

- **MyBatis**: MyBatis를 사용하여 데이터베이스 연동을 처리합니다.
- **Spring Data JPA**: JPA를 사용한 데이터베이스 연동을 지원합니다.

### 보안 및 인증

- **Spring Security**: 애플리케이션 보안을 처리하며, JWT를 이용한 인증과 권한 부여를 구현합니다.
- **JWT**: JWT(Json Web Token)를 이용하여 사용자의 인증 및 권한을 관리합니다.

### 기타 라이브러리

- **Lombok**: Getter, Setter, 생성자 등 반복되는 코드를 간결하게 하기 위해 사용합니다.
- **dotenv**: `.env` 파일에서 환경 변수를 읽어오기 위해 사용합니다.
- **카카오 OAuth 2.0 클라이언트**: 카카오 로그인 처리를 위한 OAuth 2.0 클라이언트를 사용합니다.
- **Spring Boot Mail**: 이메일 전송 기능을 제공합니다.
