# Android System Lab
Android 시스템 주요 개념을 직접 구현하며 익히는 학습 레포입니다.    
<br>

## 목적
완성된 프로젝트가 아닌 개념 학습용 샘플 코드입니다.    
코드를 직접 작성하고 실행 결과를 눈으로 확인하는 방식으로 학습합니다.    
Android 개발에서 필요한 개념을 각 모듈에서 단계적으로 다룹니다.    
<br>

## 모듈

| 모듈 | 사용 기술 | 내용 |
|---|---|---|
| 공통 | Kotlin, Jetpack Compose | - |
| coroutine-sample | Kotlin Coroutines | 진행 중 |
| paging-sample | Paging3, 커스텀 Paginator | Paging3 라이브러리를 직접 구현한 커스텀 Paginator와 비교하며 학습 |
| paging-caching-sample | Paging3, Room, Retrofit, Hilt | RemoteMediator 오프라인 캐싱 구현, Rick and Morty API 사용 |
| room-sample | Room, DAO | Room DB 기본 CRUD, DAO 작성, 연락처 목록 추가/삭제/정렬 |
| stateflow-sample | StateFlow, ViewModel | StateFlow를 이용한 ViewModel → UI 상태 관리 흐름 학습 |
| thread-sample | Java Thread, Synchronized, Volatile | Thread 생성 및 상태 전이, 멀티 Thread 동시 실행, Race Condition 시각화 |
<br>
