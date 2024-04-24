애플리케이션 컨텍스트에 설정정보 등록하는 법

🧐 XML을 이용한 설정

![image](https://github.com/Jung-MinGi/SpringStudy/assets/118701129/06835185-6ed2-4d21-8db6-423ef2cbafdc)





bean 태그 안에 id == 빈의 이름을 정의

bean태그안에 class==실제 구현체의 클래스 풀네임

xml에서는 property 태그로 의존오브젝트와의 관계를  정의한다

property태그의  name으로 수정자 메서드를 찾는다

property태그의 ref는 수정자 메서드를 통해 의존관계를 주입해줄 빈의 이름

수정자메서드의 파라미터로 오브젝트의 레퍼런스 값이 아닌 단순 값을 주입해주는 경우 value속성을 사용한다.

