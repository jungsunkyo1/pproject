![다운로드](https://user-images.githubusercontent.com/87056402/131691179-b66f2a1a-95a1-41f6-88c7-ef4cc37a10f0.png)


# 예제 - 카카오택시

본 예제는 MSA/DDD/Event Storming/EDA 를 포괄하는 분석/설계/구현/운영 전단계를 커버하도록 구성한 예제입니다.
이는 클라우드 네이티브 애플리케이션의 개발에 요구되는 체크포인트들을 통과하기 위한 예시 답안을 포함합니다.
- 체크포인트 : https://workflowy.com/s/assessment-check-po/T5YrzcMewfo4J6LW


# Table of contents

- [예제 - 음식배달](#---)
  - [서비스 시나리오](#서비스-시나리오)
  - [체크포인트](#체크포인트)
  - [분석/설계](#분석설계)
  - [구현:](#구현-)
    - [DDD 의 적용](#ddd-의-적용)
    - [폴리글랏 퍼시스턴스](#폴리글랏-퍼시스턴스)
    - [폴리글랏 프로그래밍](#폴리글랏-프로그래밍)
    - [동기식 호출 과 Fallback 처리](#동기식-호출-과-Fallback-처리)
    - [비동기식 호출 과 Eventual Consistency](#비동기식-호출-과-Eventual-Consistency)
  - [운영](#운영)
    - [CI/CD 설정](#cicd설정)
    - [동기식 호출 / 서킷 브레이킹 / 장애격리](#동기식-호출-서킷-브레이킹-장애격리)
    - [오토스케일 아웃](#오토스케일-아웃)
    - [무정지 재배포](#무정지-재배포)
  - [신규 개발 조직의 추가](#신규-개발-조직의-추가)

# 서비스 시나리오

카카오 택시 커버하기

기능적 요구사항
1. 고객이 택시를 요청 한다
1. 택시 요청 내역이 택시 기사에게 전달 된다.
1. 택시 기사가 확인하여 요청을 접수 한다
1. 택시 기사가 고객을 픽업하여 목적지까지 이동한다.
1. 운행이 종료 되면 택시기사가 결제를 진행한다.
1. 결제가 완료되면 운행 종료 처리를 한다.
2. 택시 요청을 취소하면 접수도 취소 된다.
3. 고객이 요청 상태를 중간중간 조회한다.
4. 고객 센터는 요청/운행 상태를 조회 할수 있다.

비기능적 요구사항
1. 트랜잭션
    1. 결제가 성공해야만 운행 종료 처리가 가능하다.  Sync 호출 
1. 장애격리
    1. 택시 접수 기능이 수행되지 않더라도 요청은 365일 24시간 받을 수 있어야 한다  Async (event-driven), Eventual Consistency
    1. 요청 시스템이 과중되면 사용자를 잠시동안 받지 않고 잠시후에 요청 하도록 유도한다  Circuit breaker, fallback
1. 성능
    1. 고객이 택시 요청 상태를 요청시스템(프론트엔드)에서 확인할 수 있어야 한다  CQRS
    1. 요청상태가 바뀔때마다 카톡 등으로 알림을 줄 수 있어야 한다  Event driven


# 체크포인트

- 분석 설계


  - 이벤트스토밍: 
    - 스티커 색상별 객체의 의미를 제대로 이해하여 헥사고날 아키텍처와의 연계 설계에 적절히 반영하고 있는가?
    - 각 도메인 이벤트가 의미있는 수준으로 정의되었는가?
    - 어그리게잇: Command와 Event 들을 ACID 트랜잭션 단위의 Aggregate 로 제대로 묶었는가?
    - 기능적 요구사항과 비기능적 요구사항을 누락 없이 반영하였는가?    

  - 서브 도메인, 바운디드 컨텍스트 분리
    - 팀별 KPI 와 관심사, 상이한 배포주기 등에 따른  Sub-domain 이나 Bounded Context 를 적절히 분리하였고 그 분리 기준의 합리성이 충분히 설명되는가?
      - 적어도 3개 이상 서비스 분리
    - 폴리글랏 설계: 각 마이크로 서비스들의 구현 목표와 기능 특성에 따른 각자의 기술 Stack 과 저장소 구조를 다양하게 채택하여 설계하였는가?
    - 서비스 시나리오 중 ACID 트랜잭션이 크리티컬한 Use 케이스에 대하여 무리하게 서비스가 과다하게 조밀히 분리되지 않았는가?
  - 컨텍스트 매핑 / 이벤트 드리븐 아키텍처 
    - 업무 중요성과  도메인간 서열을 구분할 수 있는가? (Core, Supporting, General Domain)
    - Request-Response 방식과 이벤트 드리븐 방식을 구분하여 설계할 수 있는가?
    - 장애격리: 서포팅 서비스를 제거 하여도 기존 서비스에 영향이 없도록 설계하였는가?
    - 신규 서비스를 추가 하였을때 기존 서비스의 데이터베이스에 영향이 없도록 설계(열려있는 아키택처)할 수 있는가?
    - 이벤트와 폴리시를 연결하기 위한 Correlation-key 연결을 제대로 설계하였는가?

  - 헥사고날 아키텍처
    - 설계 결과에 따른 헥사고날 아키텍처 다이어그램을 제대로 그렸는가?
    
- 구현
  - [DDD] 분석단계에서의 스티커별 색상과 헥사고날 아키텍처에 따라 구현체가 매핑되게 개발되었는가?
    - Entity Pattern 과 Repository Pattern 을 적용하여 JPA 를 통하여 데이터 접근 어댑터를 개발하였는가
    - [헥사고날 아키텍처] REST Inbound adaptor 이외에 gRPC 등의 Inbound Adaptor 를 추가함에 있어서 도메인 모델의 손상을 주지 않고 새로운 프로토콜에 기존 구현체를 적응시킬 수 있는가?
    - 분석단계에서의 유비쿼터스 랭귀지 (업무현장에서 쓰는 용어) 를 사용하여 소스코드가 서술되었는가?
  - Request-Response 방식의 서비스 중심 아키텍처 구현
    - 마이크로 서비스간 Request-Response 호출에 있어 대상 서비스를 어떠한 방식으로 찾아서 호출 하였는가? (Service Discovery, REST, FeignClient)
    - 서킷브레이커를 통하여  장애를 격리시킬 수 있는가?
  - 이벤트 드리븐 아키텍처의 구현
    - 카프카를 이용하여 PubSub 으로 하나 이상의 서비스가 연동되었는가?
    - Correlation-key:  각 이벤트 건 (메시지)가 어떠한 폴리시를 처리할때 어떤 건에 연결된 처리건인지를 구별하기 위한 Correlation-key 연결을 제대로 구현 하였는가?
    - Message Consumer 마이크로서비스가 장애상황에서 수신받지 못했던 기존 이벤트들을 다시 수신받아 처리하는가?
    - Scaling-out: Message Consumer 마이크로서비스의 Replica 를 추가했을때 중복없이 이벤트를 수신할 수 있는가
    - CQRS: Materialized View 를 구현하여, 타 마이크로서비스의 데이터 원본에 접근없이(Composite 서비스나 조인SQL 등 없이) 도 내 서비스의 화면 구성과 잦은 조회가 가능한가?

  - 폴리글랏 플로그래밍
    - 각 마이크로 서비스들이 하나이상의 각자의 기술 Stack 으로 구성되었는가?
    - 각 마이크로 서비스들이 각자의 저장소 구조를 자율적으로 채택하고 각자의 저장소 유형 (RDB, NoSQL, File System 등)을 선택하여 구현하였는가?
  - API 게이트웨이
    - API GW를 통하여 마이크로 서비스들의 집입점을 통일할 수 있는가?
    - 게이트웨이와 인증서버(OAuth), JWT 토큰 인증을 통하여 마이크로서비스들을 보호할 수 있는가?
- 운영
  - SLA 준수
    - 셀프힐링: Liveness Probe 를 통하여 어떠한 서비스의 health 상태가 지속적으로 저하됨에 따라 어떠한 임계치에서 pod 가 재생되는 것을 증명할 수 있는가?
    - 서킷브레이커, 레이트리밋 등을 통한 장애격리와 성능효율을 높힐 수 있는가?
    - 오토스케일러 (HPA) 를 설정하여 확장적 운영이 가능한가?
    - 모니터링, 앨럿팅: 
  - 무정지 운영 CI/CD (10)
    - Readiness Probe 의 설정과 Rolling update을 통하여 신규 버전이 완전히 서비스를 받을 수 있는 상태일때 신규버전의 서비스로 전환됨을 siege 등으로 증명 
    - Contract Test :  자동화된 경계 테스트를 통하여 구현 오류나 API 계약위반를 미리 차단 가능한가?


# 분석/설계


## AS-IS 조직 (Horizontally-Aligned)
  ![image](https://user-images.githubusercontent.com/487999/79684144-2a893200-826a-11ea-9a01-79927d3a0107.png)

## TO-BE 조직 (Vertically-Aligned)
![79684159-3543c700-826a-11ea-8d5f-a3fc0c4cad87](https://user-images.githubusercontent.com/87056402/131693093-fb711007-b32c-4206-b6dd-4437e50871d2.png)



## Event Storming 결과
* MSAEz 로 모델링한 이벤트스토밍 결과:  http://labs.msaez.io/#/storming/ssjWGghxznaGd2kobQJthGGeKsO2/9284e1ca2893464efd2c4864b6b67ce3

### 이벤트 도출
![이벤트도출](https://user-images.githubusercontent.com/87056402/129153891-8c77c989-43aa-4bdc-bfea-fc4596d2cd65.png)

### 이벤트 정리
### 액터, 커맨드 부착
### 어그리게잇으로 묶기
![cammandMapping](https://user-images.githubusercontent.com/87056402/129156827-5a7a6fe5-6a1d-40ea-86dc-7fc9ac4a7e88.png)

    - TaxiRequest, Receipt의 요청, 접수 생성 및 결제의 결제이력은 그와 연결된 command 와 event 들에 의하여 트랜잭션이 유지되어야 하는 단위로 그들 끼리 묶어줌
    - 시나리오에서 결제는 운행 종료시 자동 발생함으로 DestinationArrived 사용, PaymentRequested는 삭제

### 바운디드 컨텍스트로 묶기

![boundedContext](https://user-images.githubusercontent.com/87056402/129158348-6138b545-0ee2-413a-8d29-b0a493934ce8.png)


    - 도메인 서열 분리 
        - Core Domain:  CustomerApp(front), DriverApp : 없어서는 안될 핵심 서비스이며, 연견 Up-time SLA 수준을 99.999% 목표, 배포주기는 app 의 경우 1주일 1회 미만, store 의 경우 1개월 1회 미만
        - Supporting Domain:   marketing, customer : 경쟁력을 내기위한 서비스이며, SLA 수준은 연간 60% 이상 uptime 목표, 배포주기는 각 팀의 자율이나 표준 스프린트 주기가 1주일 이므로 1주일 1회 이상을 기준으로 함.
        - General Domain:   pay : 결제서비스로 3rd Party 외부 서비스를 사용하는 것이 경쟁력이 높음 (핑크색으로 이후 전환할 예정)

### 폴리시의 이동과 컨텍스트 매핑 (점선은 Pub/Sub, 실선은 Req/Resp)
### 완성된 1차 모형

![1차완성모델](https://user-images.githubusercontent.com/87056402/131509212-9306cbf4-a9fc-4835-b273-db2fb1808a7e.png)
   
   - View Model 추가
    

### 1차 완성본에 대한 기능적/비기능적 요구사항을 커버하는지 검증

![완성1차검증](https://user-images.githubusercontent.com/87056402/131511103-36fc71a3-8946-4e17-ba43-08481797cf2b.png)



    - 고객이 택시를 요청 한다. (ok) 
    - 택시 요청 내역이 택시 기사에게 전달 된다. (ok)
    - 택시 기사가 확인하여 요청을 접수 한다 (ok)
    - 택시 기사가 고객을 픽업하여 목적지까지 이동한다. (ok)
    - 운행이 종료 되면 택시기사가 결제를 진행한다. (ok)
    - 결제가 완료되면 운행 종료 처리를 한다. (ok)

![완성1차검증2](https://user-images.githubusercontent.com/87056402/131514232-0cbcf386-d3ed-4205-beba-62daf204a251.png)

    - 택시 요청을 취소하면 접수도 취소 된다. (ok)
    - 고객이 요청 상태를 중간중간 조회한다. (ok)
    - 고객 센터는 요청/운행 상태를 조회 할수 있다. (ok)
    

### 비기능 요구사항에 대한 검증

    - 마이크로 서비스를 넘나드는 시나리오에 대한 트랜잭션 처리
        - 택시 운행 종료시 결제처리:  결제가 완료 되어야만 운행이 종료 되도록 ACID 트랜잭션 적용. 주문와료시 결제처리에 대해서는 Request-Response 방식 처리
        - 나머지 모든 inter-microservice 트랜잭션: 요청 상태, 운행 상태 등 모든 이벤트에 대해 조회을 처리하는 등, 데이터 일관성의 시점이 크리티컬하지 않은 모든 경우가 대부분이라 판단, Eventual Consistency 를 기본으로 채택함.



### 최종 모델
![최종](https://user-images.githubusercontent.com/87056402/131932690-70019b5b-1d99-4817-810c-adc21a55a39f.png)



## 헥사고날 아키텍처 다이어그램 도출
    
![11](https://user-images.githubusercontent.com/87056402/131933339-4f95deaa-693d-4e33-9063-3e584eadc29f.png)



    - Chris Richardson, MSA Patterns 참고하여 Inbound adaptor와 Outbound adaptor를 구분함
    - 호출관계에서 PubSub 과 Req/Resp 를 구분함
    - 서브 도메인과 바운디드 컨텍스트의 분리:  각 팀의 KPI 별로 아래와 같이 관심 구현 스토리를 나눠가짐


# 구현:

분석/설계 단계에서 도출된 헥사고날 아키텍처에 따라, 각 BC별로 대변되는 마이크로 서비스들을 스프링부트로 구현하였다. 구현한 각 서비스를 로컬에서 실행하는 방법은 아래와 같다 (각자의 포트넘버는 8081 ~ 808n 이다)

```
cd customer
mvn spring-boot:run

cd driver
mvn spring-boot:run 

cd gateway
mvn spring-boot:run  

cd payment
mvn spring-boot:run  

cd CustomerCenter
mvn spring-boot:run  


```

## CQRS

- 각 서비스내에 필요한 조회 화면(Customer, Driver, CustomerCenter)은 CQRS로 구현 하였다

- 조회할 데이터에 대한 모델 생성

![model](https://user-images.githubusercontent.com/87056402/131789356-4c70a023-cc8c-4b8c-b3be-3e91e4125b4e.png)

- Kafka Event를 수신하여서 조회 데이터를 저장

![eventhandler](https://user-images.githubusercontent.com/87056402/131789403-2dc04858-43ac-47f6-b9dd-13b4ccb1648f.png)

- 저장된 Data 조회

![조회결과](https://user-images.githubusercontent.com/87056402/131789421-b1de022f-507d-466c-927d-c72fb0b82db2.png)


## API GATEWAY

- 각 서비스들의 진입점을 하나로 만들기 위해 Api GateWay 적용
- Spring cloud Gateway 사용

#application.yaml 예시
```
server:
  port: 8088

---

spring:
  profiles: default
  cloud:
    gateway:
      routes:
        - id: customer
          uri: http://localhost:8081
          predicates:
            - Path=/taxiRequests/**, /requestStatuses/**
        - id: driver
          uri: http://localhost:8082
          predicates:
            - Path=/reciepts/**, /receiptInfos/**, /api/reciept/**
        - id: payment
          uri: http://localhost:8083
          predicates:
            - Path=/payments/** 
        - id: CustomerCenter
          uri: http://localhost:8084
          predicates:
            - Path= /requestAndReceiptInfos/**
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "*"
            allowedMethods:
              - "*"
            allowedHeaders:
              - "*"
            allowCredentials: true


---

spring:
  profiles: docker
  cloud:
    gateway:
      routes:
        - id: customer
          uri: http://customer:8080
          predicates:
            - Path=/taxiRequests/** /requestStatuses/**
        - id: driver
          uri: http://driver:8080
          predicates:
            - Path=/reciepts/** /receiptInfos/**
        - id: payment
          uri: http://payment:8080
          predicates:
            - Path=/payments/** 
        - id: CustomerCenter
          uri: http://CustomerCenter:8080
          predicates:
            - Path= /requestAndReceiptInfos/**
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "*"
            allowedMethods:
              - "*"
            allowedHeaders:
              - "*"
            allowCredentials: true

server:
  port: 8080

```

## Correlation

- Customer App과 Dirver App 서비스 사이에 requestId를 Correlation Key로 사용하여 요청 및 취소에 보상 로직을 적용.

```
# CustomerApp에서 Request 생성
http POST http://localhost:8088/taxiRequests startingPoint="seoul" destination="ulsan" headcount=3 phoneNumber="010123456"


#Driver App에서도 Request에 대한 데이터 생성 확인
http http://localhost:8088/reciepts


# CustomerApp에서 Reuqest 삭제
http DELETE http://localhost:8088/taxiRequests/1

#Dirver App에서도 데이터 삭제 확인
http http://localhost:8088/reciepts

#Customer App에서 Canceled로 상태 업데이트된것 확인
http http://localhost:8088/requestStatuses
```
![11111](https://user-images.githubusercontent.com/87056402/131799377-f34bbc04-f98f-46c8-a15c-b811e3fed147.png)




## DDD 의 적용

- 각 서비스내에 도출된 핵심 Aggregate Root 객체를 Entity 로 선언하였다: (예시는 Customer 마이크로 서비스). 이때 가능한 현업에서 사용하는 언어 (유비쿼터스 랭귀지)를 그대로 사용하려고 노력했다.

```
public class TaxiRequest {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String startingPoint;
    private String destination;
    private Integer headcount;
    private String phoneNumber;

... 생략
```
- Entity Pattern 과 Repository Pattern 을 적용하여 JPA 를 통하여 다양한 데이터소스 유형 (RDB or NoSQL) 에 대한 별도의 처리가 없도록 데이터 접근 어댑터를 자동 생성하기 위하여 Spring Data REST 의 RestRepository 를 적용하였다
```
@RepositoryRestResource(collectionResourceRel="taxiRequests", path="taxiRequests")
public interface TaxiRequestRepository extends PagingAndSortingRepository<TaxiRequest, Long>{
}

```
- 적용 후 REST API 의 테스트
```
# Customer 서버스의 택시 요청
http POST http://localhost:8088/taxiRequests startingPoint="seoul" destination="ulsan" headcount=3 phoneNumber="010123456"

# Driver 서비스의 요청 조회
http http://localhost:8088/reciepts

# 요청 상태 확인
http localhost:8088/taxiRequests/1

```


## 폴리글랏 퍼시스턴스

CustomerCenter 에서는 mssql 를 사용하기로 하였다. 별다른 작업없이 기존의 Entity Pattern 과 Repository Pattern 적용과 데이터베이스 제품의 설정 (application.yml, pom.xml) 만으로 MSSQL 에 부착시켰다

```
# RequestAndReceiptInfo.java

@Entity
@Table(name="RequestAndReceiptInfo_table")
public class RequestAndReceiptInfo {

        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        private Long id;
        private Long requestId;
        private Long recieptId;
        private String startingPoint;
        private String destination;
        private Integer headcount;
        private String phoneNumber;
        private String carNumber;
        private String driverName;
        private Integer price;
        private String status;

...생략


# RequestAndReceiptInfoRepository.java

@RepositoryRestResource(collectionResourceRel="requestAndReceiptInfos", path="requestAndReceiptInfos")
public interface RequestAndReceiptInfoRepository extends CrudRepository<RequestAndReceiptInfo, Long> {

    List<RequestAndReceiptInfo> findByRequestId(Long requestId);
    List<RequestAndReceiptInfo> findByRecieptId(Long recieptId);
}


# application.yml

  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=CustomerCenter
    driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
    username: SA
    password: ##

#Pom.xml

		<dependency>
			<groupId>com.microsoft.sqlserver</groupId>
			<artifactId>mssql-jdbc</artifactId>
			<version>9.4.0.jre8</version>
		</dependency>


```
# DB 조회 결과

![스크린샷, 2021-09-02 14-35-49](https://user-images.githubusercontent.com/87056402/131788316-801ef874-b121-46ff-808c-33c6c002974b.png)





## 동기식 호출 과 Fallback 처리

분석단계에서의 조건 중 하나로 드라이버(app)->결제(pay) 간의 호출은 동기식 일관성을 유지하는 트랜잭션으로 처리하기로 하였다. 호출 프로토콜은 이미 앞서 Rest Repository 에 의해 노출되어있는 REST 서비스를 FeignClient 를 이용하여 호출하도록 한다. 

- 결제서비스를 호출하기 위하여 Stub과 (FeignClient) 를 이용하여 Service 대행 인터페이스 (Proxy) 를 구현 

```
# (driver) PaymentService.java

@FeignClient(name="payment", url="${prop.payment.url}")
public interface PaymentService {
    @RequestMapping(method= RequestMethod.POST, path="/payments")
    public void requestPayment(@RequestBody Payment payment);

}

```

- 목적지 도착 상태 업데이트 직후(@PostUpdate) 결제를 요청하도록 처리
```
# Reciept.java (Entity)

    @PostUpdate
    public void onPostUpdate() {

        if (status.equals("AcceptRequest")) {
            ...생략
        } else if (status.equals("ArriveDestination")) {
            RecieptCanceled recieptCanceled = new RecieptCanceled();
            BeanUtils.copyProperties(this, recieptCanceled);
            recieptCanceled.publishAfterCommit();

            PaymentRequested paymentRequested = new PaymentRequested();
            BeanUtils.copyProperties(this, paymentRequested);
            paymentRequested.publishAfterCommit();

            // Following code causes dependency to external APIs
            // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

            taxi.external.Payment payment = new taxi.external.Payment();
            // mappings goes here
            DriverApplication.applicationContext.getBean(taxi.external.PaymentService.class).requestPayment(payment);

            RunningFinished runningFinished = new RunningFinished();
            BeanUtils.copyProperties(this, runningFinished);
            runningFinished.publishAfterCommit();

        }
    }
```

- 동기식 호출에서는 호출 시간에 따른 타임 커플링이 발생하며, 결제 시스템이 장애가 나면 목적지 도착 처리도 되지 않는 것을 확인


```
# 결제 (pay) 서비스를 잠시 내려놓음 (ctrl+c)

#도착 처리
http http://localhost:8088/api/reciept/arrivedestination?requestId=1&price=1000   #Fail

#상태 변경되지 않음
                "carNumber": "12d345",
                "destination": "ulsan",
                "driverName": "jsk",
                "headcount": 3,
                "phoneNumber": "010123456",
                "price": null,
                "recieptId": 1,
                "requestId": 1,
                "startingPoint": "seoul",
                "status": "Accepted"

#결제서비스 재기동
cd 결제
mvn spring-boot:run

#도착 처리
http http://localhost:8088/api/reciept/arrivedestination?requestId=1&price=1000   #Success

#상태 변경됨
                "carNumber": "12d345",
                "destination": "ulsan",
                "driverName": "jsk",
                "headcount": 3,
                "phoneNumber": "010123456",
                "price": 1000,
                "recieptId": 1,
                "requestId": 1,
                "startingPoint": "seoul",
                "status": "Finished"


```

- 또한 과도한 요청시에 서비스 장애가 도미노 처럼 벌어질 수 있다. (서킷브레이커, 폴백 처리는 운영단계에서 설명한다.)




## 비동기식 호출 / 시간적 디커플링 / 장애격리 / 최종 (Eventual) 일관성 테스트


택시 요청은 비 동기식으로 처리하여 드라이버 시스템의 처리를 위하여 블로킹 되지 않아도록 처리한다.
 
- 이를 위하여 택시 요청이 생성되면 기록을 남기고 도메인 이벤트를 카프카로 송출한다(Publish)
 
```
public class TaxiRequest {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String startingPoint;
    private String destination;
    private Integer headcount;
    private String phoneNumber;

    @PostPersist
    public void onPostPersist(){
        TaxiRequsted taxiRequsted = new TaxiRequsted();
        BeanUtils.copyProperties(this, taxiRequsted);
        taxiRequsted.publishAfterCommit();

    }
    
    ...생략
```
- 드라이버 서비스에서는 이를 수신하여 자신의 정책을 처리하도록 PolicyHandler 를 구현한다:

```
@Service
public class PolicyHandler{
    @Autowired RecieptRepository recieptRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverTaxiRequsted_ReceiveRequest(@Payload TaxiRequsted taxiRequsted){

        if(!taxiRequsted.validate()) return;

        System.out.println("\n\n##### listener ReceiveRequest : " + taxiRequsted.toJson() + "\n\n");

        // Sample Logic //
        Reciept reciept = new Reciept();
        reciept.setRequestId(taxiRequsted.getId());
        recieptRepository.save(reciept);

    }

...생략

택시 요청 시스템과 드라이버 시스템은 분리되어, 이벤트 수신에 따라 처리되기 때문에, 드라이버시스템이 유지보수로 인해 잠시 내려간 상태라도 택시 요청을 받는데 문제가 없다:
```

#드라이버 서비스 (driver) 를 잠시 내려놓음 (ctrl+c)
```
#요청 처리
http POST http://localhost:8088/taxiRequests startingPoint="seoul" destination="ulsan" headcount=3 phoneNumber="010123456"   #Success

#상태 확인
               "carNumber": null,
                "destination": "ulsan",
                "driverName": null,
                "headcount": 3,
                "phoneNumber": "010123456",
                "price": null,
                "recieptId": null,
                "requestId": 1,
                "startingPoint": "seoul",
                "status": "Cancel Requested"



#상점 서비스 기동
cd 상점
mvn spring-boot:run

#드라이버 요청 리스트 확인
http http://localhost:8088/receiptInfos     # 신규 요청이 생성된것 확인


                "carNumber": null,
                "destinationPoint": "ulsan",
                "headcount": 3,
                "phoneNumber": "010123456",
                "price": null,
                "requestId": 1,
                "startingPoint": "seoul",
                "status": "Requsted"


```


# 운영

## CI/CD 설정


각 구현체들은 각자의 source repository 에 구성되었고, 사용한 CI/CD 플랫폼은 AWS를 사용하였으며, pipeline build script 는 각 프로젝트 폴더 이하 buildspec.yml 에 포함되었다.


서비스별 CodeBuild Project 생성
![codebuild](https://user-images.githubusercontent.com/87056402/131811527-ef873dd4-badf-412a-b800-3a0dfead0724.png)


WebHook을 통한 실행 확인
![webhook](https://user-images.githubusercontent.com/87056402/131810909-3c9e5856-d5d5-4e84-8859-135820b255c1.png)


ECR Push된 정보 확인
![ecr](https://user-images.githubusercontent.com/87056402/131811377-26ed63e3-305e-4982-a35d-7e2bd06f2749.png)


k8s Deploy 확인
![deployall](https://user-images.githubusercontent.com/87056402/131812326-bfe23c9b-af9e-47a1-9f8a-8a66e48ece39.png)



### ConfigMap 설정

동기 호출 URL을 ConfigMap을 사용하여 설정

kubectl apply -f configmap
```
 apiVersion: v1
 kind: ConfigMap
 metadata:
   name: ktaxi-configmap
   namespace: ktaxi
 data:
   apiurl: "http://user19-gateway:8080"
```

driver의 builspec.yml 수정

```
              spec:
                containers:
                  - name: $_PROJECT_NAME
                    image: $AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$_PROJECT_NAME:$CODEBUILD_RESOLVED_SOURCE_VERSION
                    ports:
                      - containerPort: 8080
                    env:
                    - name: apiurl
                      valueFrom:
                        configMapKeyRef:
                          name: ktaxi-configmap
                          key: apiurl 

```

application.yml 수정

```
prop:
  payment:
    url: ${apiurl}
```

URL 호출하는 api의 200 ok 확인

![configmap](https://user-images.githubusercontent.com/87056402/131819935-8e6853ed-0d39-4184-99b9-3196157aff9c.png)



## 동기식 호출 / 서킷 브레이킹 / 장애격리

서킷 브레이킹 프레임워크의 선택: istio의 Destination Rule을 적용 Traffic 관리함.

시나리오는 드라이버서비스(driver)-->결제(payment) 시의 연결을 RESTful Request/Response 로 연동하여 구현이 되어있고, 결제 요청이 과도할 경우 CB 를 통하여 장애격리.

부하테스터 siege 툴을 통한 서킷 브레이커 동작 확인:

동시사용자 10명
10초 동안 실시

```
siege -c10 -t10s -v "http://user19-gateway:8080/payments"
```

CB가 없기 때문에 100% 성공

![seige](https://user-images.githubusercontent.com/87056402/131831053-bee8b873-6af6-45a3-b5f4-7cce4ed44d06.png)


DR 적용

```
kubectl apply -f destinationRule -n ktaxi

kind: DestinationRule
metadata:
  name: dr-payment
spec:
  host: user19-payment
  trafficPolicy:
    connectionPool:
      http:
        http1MaxPendingRequests: 1
        maxRequestsPerConnection: 1
```

CB 발동하여 일부 실패

![dr2](https://user-images.githubusercontent.com/87056402/131830733-b294e1ba-3500-4746-aaeb-eddc6b7093f4.png)

```
Lifting the server siege...
Transactions:		        1028 hits
Availability:		       94.57 %
Elapsed time:		        9.91 secs
Data transferred:	        0.62 MB
Response time:		        0.10 secs
Transaction rate:	      103.73 trans/sec
Throughput:		        0.06 MB/sec
Concurrency:		        9.91
Successful transactions:        1028
Failed transactions:	          59
Longest transaction:	        0.29
Shortest transaction:	        0.01
 
```




### 오토스케일 아웃
앞서 CB 는 시스템을 안정되게 운영할 수 있게 해줬지만 사용자의 요청을 100% 받아들여주지 못했기 때문에 이에 대한 보완책으로 자동화된 확장 기능을 적용하고자 한다. 


- 결제서비스에 대한 replica 를 동적으로 늘려주도록 HPA 를 설정한다. 설정은 CPU 사용량이 15프로를 넘어서면 replica 를 10개까지 늘려준다:
```
kubectl autoscale deploy user19-payment --min=1 --max=10 --cpu-percent=15 -n ktaxi
```
- CB 에서 했던 방식대로 부하 발생
```
siege -c10 -t10s -v "http://user19-gateway:8080/payments"
```

어느정도 시간이 흐른 후 (약 30초) 스케일 아웃이 벌어지는 것을 확인할 수 있다:

![hpa](https://user-images.githubusercontent.com/87056402/131831809-0c66a4c2-81c6-4aef-b5f5-6cba88c81665.png)



## 무정지 재배포

* 먼저 무정지 재배포가 100% 되는 것인지 확인하기 위해서 Autoscaler 이나 CB 설정을 제거함

- seige 로 배포작업 직전에 워크로드를 모니터링 함.
```
siege -c5 -t200s -v "http://user19-gateway:8080/reciepts"

```

Driver Service 신규 버전으로 배포

![dd](https://user-images.githubusercontent.com/87056402/131833665-cac54743-9724-49f7-8a7e-296c6aeac667.png)

배포기간 동안 Availability 가 변화없기 때문에 무정지 재배포가 성공한 것으로 확인됨.



## Liveness Probe

테스트를 위해 buildspec.yml을 아래와 같이 수정 후 배포

```
livenessProbe:
                      # httpGet:
                      #   path: /actuator/health
                      #   port: 8080
                      exec:
                        command:
                        - cat
                        - /tmp/healthy
```

Kail로 이동해서 Health 상태 확인.
![2](https://user-images.githubusercontent.com/87056402/131837322-f5625880-522d-444f-ab26-b892b39af1d1.png)


```
kubectl exec -it pod/user19-customer-6f656b9755-spx5z -n ktaxi -- touch /tmp/healthy
```

![1](https://user-images.githubusercontent.com/87056402/131837382-468bf2d9-77af-46eb-bab4-206b589b85cb.png)



