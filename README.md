# LikeLion-12th-TEAM02-BE
🦁 멋쟁이사자처럼 12기 SKHU 해커톤 2팀 백엔드 레포지토리

<br/>

## 🐯 이공없는 이공계 - MoodFriend

MoodFriend는 현대인의 건강(wellness) 문제를 해결하기 위한 서비스로, 감정 관리와 정신적 웰빙을 돕기 위해 개발되었다. 대표 캐릭터 호야가 사용자와 1:1 대화를 통해 감정을 분석하고 관리해준다. 이를 통해 사용자들이 자신의 감정 상태를 체계적으로 관리하고, 정신적 건강을 증진시킬 수 있도록 돕는 것을 목표로 한다.

<br/>

### ✨ 주요 기능

1. 호야와의 대화 

- 자체, 카카오, 구글 로그인을 통한 호야와의 1:1 대화 기능
- 프롬프트 엔지니어링을 통한 응답 품질 개선
- DeepL API를 통한 토큰 절약
  
2. 감정 트래커
 
- 지정된 시간에 호야와의 일일 대화 내용 요약 및 AI 일기에 저장 (cron 표현식 사용)
- AI 일기 조회 및 사용자 일기 CRUD 기능 (캘린더 형식)
- 지도 API를 통한 사용자 주변 전문 의료기관 추천
    
3. 친구
   
- 다른 사용자에게 친구 요청 전송, 수락 시 서로 친구로 등록
- 친구로 등록된 사용자의 최근 감정 상태 조회
- 친구 삭제 시 양방향으로 삭제

4. 오브제

- 결제(PortOne) API를 통한 마일리지 충전
- 보유 마일리지로 상점에서 오브제 구매
- 보유 오브제 상태 관리로 꾸미기 기능

<br/>

### 👥 Member

| [김은혜_FE](https://github.com/GraceKim527) | [오수경_FE](https://github.com/SuKyeong2002) | [강두오_BE](https://github.com/kduoh99) | [지정욱_BE](https://github.com/dkdpawjddnr) |
| :------: | :------: | :------: | :------: |
| <img src="https://github.com/GraceKim527.png" width="150"> | <img src="https://github.com/SuKyeong2002.png" width="150"> | <img src="https://github.com/kduoh99.png" width="150"> | <img src="https://github.com/dkdpawjddnr.png" width="150"> |


<br/>

### 🔗 Repository & Deploy Links

- [FrontEnd Repo](https://github.com/LikeLion-12th-SKHU/LikeLion-12th-TEAM02-FE) | [FrontEnd Deploy](https://moodfriend.vercel.app)
- [BackEnd Repo](https://github.com/LikeLion-12th-SKHU/LikeLion-12th-TEAM02-BE) | [BackEnd Deploy](https://moodfriend.site)

<br/>

### 📃 API Documentation

- [Swagger](https://moodfriend.site/swagger-ui/index.html)

<br/>

### 🗃️ ERD

![호야](https://github.com/user-attachments/assets/45c251de-6c98-42a7-8d97-d3931c4ef869)

<br/>

### 🛠️ Tech Stack

#### FrontEnd
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![Styled Components](https://img.shields.io/badge/styled--components-DB7093?style=for-the-badge&logo=styled-components&logoColor=white)
![React Query](https://img.shields.io/badge/React_Query-FF4154?style=for-the-badge&logo=react-query&logoColor=white)
![Recoil](https://img.shields.io/badge/Recoil-3578e5?style=for-the-badge&logo=Recoil&logoColor=white)
![React Router DOM](https://img.shields.io/badge/React_Router-CA4245?style=for-the-badge&logo=react-router&logoColor=white)
![Vercel](https://img.shields.io/badge/Vercel-000000?style=for-the-badge&logo=vercel&logoColor=white)

#### BackEnd
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=hibernate&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazonwebservices&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

#### Communication & Tools
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)
![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white)
![Figma](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)

<br/>

### 🌱 Git Flow

1. 작업 폴더 생성 및 초기화

```bash
git init
git remote add upstream [레포 주소]
git remote add origin [레포 주소]
```

2. local main에서 upstream pull 후, origin push

```bash
git switch main
git pull upstream main
git push origin main
```

3. 이슈 생성 및 브랜치 생성

```bash
git switch -c [브랜치명]
```

4. 작업 및 커밋

```bash
git add .
git commit -m "작업 내용"
```

5. 충돌 해결 후, PR 올리기

```bash
git pull upstream main
git push origin [브랜치명]
```

6. merge 후, 작업공간으로 돌아오기

```bash
git checkout main
```
