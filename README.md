# 프로젝트명 (ERP 담당 파트 - 입고)

## 개요
1년전에 완성 시켰던 팀프로젝트 ERP 입고 프로세스를 맡았으며 1년전에는 어떤식으로 코드를 작성했는지 보며 현재의 내가 다시 코드를 파악하고 작성해보자는 계기로 시작하게되었습니다.

## 1년전 깃허브 주소
https://github.com/lolu1032/ERP_Service.git
## 기술 스택
- 백엔드: Spring Boot 3.x, Spring Web, MyBatis
- 프론트엔드: Thymeleaf
- 데이터베이스: MySQL
- 빌드 도구: Gradle

## 주요 기능
- 발주 등록 및 발주 상태 변경 (진행중 → 발주마감)
- 입고 품목 검수 및 수량 등록
- 발주 목록 검색 (공급업체명, 품목명 등 키워드 기반)
- 페이징 처리된 발주 목록 조회
- 발주 상태에 따른 목록 구분 (`/bom`, `/transaction`)

## 리뉴얼 프로젝트 구조
```
src/
├── input/
│   ├── controller/
│   ├── domain/
│   ├── dto/
│   ├── repository/
│   └── service/
└── resources/
    └── mapper/
```

## 주요 API 예시

### 1. 품목 검수 처리
```http
POST /inputList
Body: {
  "provider": "공급업체A",
  "warehouse": "창고1",
  "itemName": "철근",
  "quantity": 100,
  "inputId": 5
}
POST /transaction
Body: [
  { "orderCode": "ORD001" },
  { "orderCode": "ORD002" }
]
```
## Javadoc
본 프로젝트의 모든 서비스, 도메인, 컨트롤러 클래스에 대해 Javadoc으로 문서화되어 있습니다.
이를 통해 클래스 간의 역할과 메서드의 기능을 명확히 이해할 수 있도록 구성했습니다.

```bash
./gradlew javadoc
build/docs/javadoc/index.html
```

