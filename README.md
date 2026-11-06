# 🐦 이 몸이 서버라면

> `if (내가 서버라면) { 어떤 요청이 와도 살아남겠지; }`

"만약 ~라면?" 질문을 직접 가정하고, 설계하고, 코드로 증명해 보는 프로젝트 입니다.
하나의 커머스 도메인(주문, 쿠폰, 결제) 위에 소규모부터 대규모까지 시나리오를 하나씩 쌓아 갑니다.

## 두 개의 챕터

### 📘 Chapter 1. 만약에…
"이런 상황이 오면 어떻게 설계할까?"를 다룹니다.

| # | 만약에… | 다루는 기술 | 규모 | 상태 |
|---|---|---|---|---|
| 01 | 선착순 쿠폰에 10만 명이 몰린다면 | | 대규모 | 예정 |
| 02 | 두 요청이 동시에 재고를 깎는다면 | | 중규모 | 예정 |
| 03 | 같은 요청이 두 번 들어온다면 | | 소규모 | 예정 |

### 🕵️ Chapter 2. Catch Me If You Can
버그와 장애가 "잡을 수 있으면 잡아 봐" 하고 숨어 있는 상황입니다. 일부러 문제를 재현하고, 원인을 추적하고, 잡아냅니다.

| # | 도망친 녀석 | 다루는 기술 | 상태 |
|---|---|---|---|
| 01 | 사라진 Kafka 메시지 | | 예정 |
| 02 | 응답 없는 외부 결제 API | | 예정 |

## 시나리오를 남기는 방식

시나리오마다 **문제가 터지는 코드(before)와 해결한 코드(after)를 함께 남깁니다.**
같은 인터페이스에 구현체를 여러 개 두고, 테스트에서 나란히 비교합니다.

```
interface CouponIssuer
├── NaiveCouponIssuer        // v1 문제 재현
├── ...                      // v2, v3 개선 시도
└── ...                      // 최종 해결
```

시나리오 문서는 [`docs/what-if`](docs/what-if)와 [`docs/catch-me-if-you-can`](docs/catch-me-if-you-can)에 템플릿을 따라 작성합니다.

## 기술 스택

- Java 21, Spring Boot 3.5, Gradle (Kotlin DSL)
- MySQL 8.0, Redis 7.4, Kafka 3.9 (KRaft)
- JUnit 5, Testcontainers

## 프로젝트 구조

```
if-i-were-a-server/
├── apps/
│   ├── commerce-api/        # REST API 서버
│   └── commerce-streamer/   # Kafka 컨슈머
├── modules/
│   ├── jpa/                 # MySQL, JPA 설정
│   ├── redis/               # Redis 설정
│   └── kafka/               # Kafka 설정
├── docker/
│   └── infra-compose.yml    # 로컬 인프라
└── docs/
    ├── what-if/             # Chapter 1 문서
    └── catch-me-if-you-can/ # Chapter 2 문서
```

## 로컬 실행

```bash
# 1. 환경변수 파일 만들기 (비밀번호는 직접 채우기)
cp .env.example .env

# 2. 인프라 실행 (MySQL, Redis, Kafka)
docker compose --env-file .env -f docker/infra-compose.yml up -d

# 3. 앱 실행
set -a && source .env && set +a
./gradlew :apps:commerce-api:bootRun
```

> 접속 정보는 코드에 두지 않고 모두 환경변수로 받습니다. `.env`는 git에 올라가지 않습니다.
