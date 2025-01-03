# Auth Server ( OAuth 2.0 적용 )
> SDK 방식이 아닌 RSET API + DB를 활용한 Auth Server 구현
<img src="https://github.com/user-attachments/assets/d4e3ab45-9fdd-47c4-a393-ba8621903c61" alt="image" width="250" />


- 목적
> 프로젝트 개발에 대해 빠른 개발 구축을 위한 샘플 코드 및 확장성 용이하게 
- 기능 
> 카카오 로그인 ✔

> 구글 로그인 ✔

> 네이버 로그인 ✔

---

**application.properties** 설정
```application.properties

# DB 설정
spring.application.name
spring.datasource.url
spring.datasource.driver-class-name
spring.datasource.username
spring.datasource.password
spring.jpa.hibernate.ddl-auto
spring.jpa.show-sql

# 카카오 로그인 관련 설정
kakao.client.id
kakao.redirect.uri
kakao.logout.redirect.uri
spring.security.oauth2.client.registration.kakao.client-id
spring.security.oauth2.client.registration.kakao.redirect-uri
spring.security.oauth2.client.registration.kakao.authorization-grant-type
spring.security.oauth2.client.registration.kakao.scope
spring.security.oauth2.client.provider.kakao.authorization-uri
spring.security.oauth2.client.provider.kakao.token-uri
spring.security.oauth2.client.provider.kakao.user-info-uri

#네이버 로그인 관련 설정
naver.client.id
naver.client.secret
spring.security.oauth2.client.registration.naver.client-id
spring.security.oauth2.client.registration.naver.client-secret
spring.security.oauth2.client.registration.naver.scope
spring.security.oauth2.client.registration.naver.authorization-grant-type
spring.security.oauth2.client.registration.naver.redirect-uri
spring.security.oauth2.client.provider.naver.authorization-uri
spring.security.oauth2.client.provider.naver.token-uri
spring.security.oauth2.client.provider.naver.user-info-uri

#구글 로그인 관련 설정
google.client.id
google.client.secret
google.redirect.uri

spring.security.oauth2.client.registration.google.client-id
spring.security.oauth2.client.registration.google.client-secret
spring.security.oauth2.client.registration.google.scope
spring.security.oauth2.client.registration.google.redirect-uri
spring.security.oauth2.client.provider.google.authorization-uri
spring.security.oauth2.client.provider.google.token-uri
spring.security.oauth2.client.provider.google.user-info-uri
spring.security.oauth2.client.provider.google.user-name-attribute


```
DB 구조
- 기본 테이블 : 회원 테이블
- 참조 테이블 : 구글 , 네이버, 카카오 테이블
> 회원 테이블에 통합적으로 관리. 소셜 로그인은 카카오 테이블, 네이버 테이블, 구글 테이블로 별도로 두어 세부 정보를 저장

> 각 소셜 테이블은 회원 테이블의 id를 외래키로 참조
