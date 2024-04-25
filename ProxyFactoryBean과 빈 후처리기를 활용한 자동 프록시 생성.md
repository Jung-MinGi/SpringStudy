<h2>ProxyFactoryBean과 빈 후처리기를 활용한 자동 프록시 생성</h2>
스프링의 ProxyFactoryBean은 프록시를 생성해서 빈 오브젝트로 등록하게 해주는 팩토리 빈이다.

![image](https://github.com/Jung-MinGi/SpringStudy/assets/118701129/6a78829b-e070-4466-8738-520532e09843)


 ProxyFactoryBean은 프록시를 생성해주긴 하는데 빈껍데기만 만들어주는 거임 그래서 외부에서DI를 활용해 유연한게 만들 수 있음.

ProxyFactoryBean은 순수하게 프록시를 생성하는 작업만을 담당하고어드바이스와 포인트컷을 각각 주입시켜 사용하게 된다

🧐 advice --특정 타깃오브젝트에 종속되지않는 순수하게 부가기능을 담은 오브젝트를 칭함

MethodInterceptor를 구현해서 만드는데 타깃오브젝트의 정보가 없어 싱글톤으로 생성이 가능하다.

🧐 포인트컷 --메서드및클래스 선정 알고리즘을 담은 오브젝트를 칭함



🧐DefaultAdvisorAutoProxyCreator

빈 후처리기가 빈으로 등록되어있으면 빈 오브젝트가 생성될때마다 빈후처리기에 보내서 후처리 작업을 요청한다. 이때 생성되는 빈을 프록시로 만들 수 있다

빈 오브젝트가 빈후처리기에게 오면 DefaultAdvisorAutoProxyCreator는 빈으로 등록된 모든 어드바이저내의 포인트컷을 이용해 전달받은 빈이 프록시 적용 대상인지 확인한다.

🧐사용법

![image](https://github.com/Jung-MinGi/SpringStudy/assets/118701129/6d9f6b43-91ac-4218-9e08-1b0e9b454d1a)


빈 후처리기를 빈으로 등록

![image](https://github.com/Jung-MinGi/SpringStudy/assets/118701129/4c6c44ba-cef6-4535-b5e5-46145ad5a7db)


사용할  포인트컷빈과 어드바이스 빈을 등록해준후

![image](https://github.com/Jung-MinGi/SpringStudy/assets/118701129/6ba66538-35cd-438e-a36b-22e62bf91118)


어드바이저에 각각 DI해주면 알아서 포인트컷에 작성된 조건에 맞으면 프록시가 생성된다.

🧐tx네임스페이스를 이용한 트랜잭션 어드바이스 간편 설정

![image](https://github.com/Jung-MinGi/SpringStudy/assets/118701129/19cca804-c729-47d5-bcbe-36f4510a894c)


tx:advice 태그에 의해 스프링이 제공하는 TransactionInterceptor이 빈으로 등록

tx:attributes태그에 각 메서드이름패턴을 적용해서 각각의 메서드에 트랜잭션속성을 정의한다

🧐@Transactional

스프링은  @Transactional이 붙은 모든 오브젝트를 타깃으로 인식한다

트랜잭션 어드바이스인 TransactionInterceptor은 설정정보에 적힌 메서드이름패턴으로 트랜잭션 설정정보를 가져오지만 @Transactional를 사용하게 되면  @Transactional의 엘리먼트에서 트랜잭션 속성을 가져온다. 또  동시에 포인트컷도  @Transactional이 부여된 대상을 확인해서 포인트컷을 한다.  

 @Transactional이거 하나로 포인트컷&트랜잭션속성을 지정가능

 @Transactional를 사용하는데 필요한 설정

![image](https://github.com/Jung-MinGi/SpringStudy/assets/118701129/55842b01-71b3-4b33-b109-68dcebc14d7e)


이거 하나로 트랜잭션 애노테이션을 이용하는데 필요한 어드바이저,어드바이스,포인트컷메서드별 트랜잭션 속성정보가 모두 자동등록됨 (이거하나 이해할려고 서비스추상화부터 시작함)
<hr>

<h2>AOP존재이유</h2>

1.처음에서는 트랜잭션 경계설정코드를 서비스 클래스에서 분리하기 위해 서비스 추상화기법을 적용함

결국 런타임시 관계를 보면

client––––→<<트랜잭션담당코드>>––––→<<실제타깃오브젝트>>

🧐문제점 1.타깃인터페이스를 메서드를 구현해야하는 번거로움

                  2. 부가기능 코드의 중복 발생

2.jdk다이내믹 프록시가 위 문제점을 해결해줌

다이내믹 프록시는 리플렉션기능을 이용해 프록시를 만듦(위에서는 직접 코드로 개발자가만듦)

![image](https://github.com/Jung-MinGi/SpringStudy/assets/118701129/6260adc5-bd60-44ec-883c-b8675c1ad817)


invocationHandler구현후 

Proxy.newProxyInstance()만들어서 클래스로더,구현인터페이스,invocationhandler구현클래스를 넣어서 생성해주면 됨.﻿

3.DI를 위한 다이내믹프록시를 빈으로 등록해주는 팩토리 빈

Proxy.newProxyInstance()는 일반적인 스프링 빈으로 등록할 수가 없다.

팩토리 빈을 활용해서 스프링의 빈으로 등록할 수 있게 만듦

🧐문제점 1. 설정정보가 너무늘어난다. 설정정보에 중복된 부분이 많이 발생

                 2.부가기능을 담당하는 transactionHandler이 프록시가 생성될때마다 계속 생성됨

4.스프링의 ProxyFactoryBean

3번 방법과 다른점은 ProxyFactoryBean은 프록시를 생성해주긴 하는데 빈껍데기만 만들어주는 거임 그래서 외부에서DI를 활용해 유연한게 만들 수 있음.

![image](https://github.com/Jung-MinGi/SpringStudy/assets/118701129/9da59ae6-f868-4473-ab10-dc0e6430459a)


🧐문제점 1.프록시를 적용하고 싶은 클래스는 모두 위와같은 설정정보를 만들어줘야함 설정정보가 굉장히 길어짐

5.자동프록시 생성 기법 사용

빈후처리기를 활용한 자동프록시 생성 
