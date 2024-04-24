<h3>팩토리 빈(factory bean)</h3>
스프링의 일반적인 빈 생성방식으로 빈을 생성할 수 없을때 스프링을 대신해서 오브젝트의 생성을 담당하는 빈.

스프링은 FactoryBean인터페이스를 구현한 클래스가 빈으로 등록되면 내가 등록한 오브젝트의 getObject()메서드를 이용해 나오는 반환오브젝트(실제 우리가 원하는 클래스의 오브젝트)가 실제 빈으로 등록되는거임

🧐생성자로 만들 수 없는 클래스 하나 생성
```
public class NotCreateByConstructor {
    private String value;

    private NotCreateByConstructor(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public static NotCreateByConstructor newNotCreateByConstructor(String value){
        return new NotCreateByConstructor(value);
    }
}
```

이 클래스는 정적메서드로만 오브젝트를 만들 수 있다. 

🧐NotCreateByConstructor를 대신 생성해주는 팩토리빈만들기
```
public class NotCreateByConstructorFactoryBean implements FactoryBean {
    String value;

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public NotCreateByConstructor getObject() throws Exception {
        return NotCreateByConstructor.newNotCreateByConstructor(value);
    }

    @Override
    public Class getObjectType() {
        return NotCreateByConstructorFactoryBean.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
```
setValue()--실제생성할 오브젝트(NotCreateByConstructor)가 필요한 오브젝트를 팩토리빈에서 대신 DI받을 수 있게 함.

getObject()--실제 빈으로 사용될 오브젝트를 생성후 반환

isSingleTon()--팩토리빈이 반환해준 오브젝트가 싱글톤인지 아닌지 알려줌



🧐 팩토리 빈 클래스 설정정보에 빈으로 등록하기

![image](https://github.com/Jung-MinGi/SpringStudy/assets/118701129/64bf12d3-7b57-4e8c-bbeb-ef28f798db47)


❗❗주의) 이 빈의 타입은 팩토리빈을 구현할 클래스가 아니라 실제 우리가 원하는 클래스 타입이라는 점!!

테스트 해보기 
```
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:test-applicationContext.xml")
class FactoryBeanTest {
    @Autowired
    ApplicationContext context;

    @Test
    @DisplayName("팩토리 빈 테스트")
    void factoryBean(){
        Object bean = context.getBean("notCreateByConstructor");
        Assertions.assertThat(bean).isInstanceOf(NotCreateByConstructor.class);
        Assertions.assertThat(((NotCreateByConstructor)bean).getValue()).isEqualTo("FactoryBean!!");
    }
}
```
결론

FactoryBean인터페이스를 구현한 클래스를 스프링 빈으로 만들어 두면 getObject()라는 메서드가 생성해주는 오브젝트가 실제 빈의 오브젝트로 대치된다.
