<h2>스프링의 트랜잭션 추상화</h2>
다양한 데이터 액세스 api가 존재한다 이런 기술들은 모두 트랜잭션 기능을 가지고 있어서

서비스 계층에서 특정 api의 트랜잭션을 사용하게 되면 dao에만 의존하던 서비스계층이 특정 구현체를 의존하게 되는 현상이 다시 발생하게 된다. 이런 문제를 해결하기 위해 

스프링은 여러개의 데이터액세스에서 가지고 있는 트랜잭션기능을 추상화해놓았다.

이를 이용하면 각 기술의 트랜잭션api를 사용하지 않고 일관된 방식의 트랜잭션 경계설정이 가능해진다.

![image](https://github.com/Jung-MinGi/SpringStudy/assets/118701129/d6efeb16-f888-48ff-97b9-8e37dfce96a1)


출처

https://velog.velcdn.com/images%2Fjakeseo_me%2Fpost%2F5a1bcb65-ce0a-45b7-b913-169bbcd4cace%2Fimage.png

🧐스프링 트랜잭션 API 적용

1.DI를 위해 오브젝트 관계 설정등록

![image](https://github.com/Jung-MinGi/SpringStudy/assets/118701129/449cf334-3f12-4025-b438-09568842ac36)


2.서비스 레이어에서 생성된 빈을 주입받은후 사용하면 된다.
```
public class UserService {   
          ...
    private PlatformTransactionManager transactionManager;
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    public void upgradeLevels() {
        TransactionStatus status =
                transactionManager.getTransaction(new DefaultTransactionDefinition());
       ....생략...
    }
}
```
