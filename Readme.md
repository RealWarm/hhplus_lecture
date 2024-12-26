## Erd 설계
![erd](https://github.com/user-attachments/assets/4df57b17-4133-418f-b2d9-a332e21e53eb)



### Lectures 테이블
- 수강 가능 인원과 현재 수강인원을 소스코드에서 관리하려했으나
  Registrations테이블에 Count를 하여 현재인원을 세면 읽기 성능을
  낭비한다 판단하여 테이블에 직접넣음

### Registrations 테이블
- UserId와 LectureId 만을 같는 pk로 이루어진 테이블 