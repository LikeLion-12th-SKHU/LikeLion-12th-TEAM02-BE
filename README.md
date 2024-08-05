# LikeLion-12th-TEAM02-BE
🦁 멋쟁이사자처럼 12기 SKHU 해커톤 2팀 백엔드 레포지토리입니다.

<br/>

## 🐯 이공없는 이공계

- MoodFriend 대표 캐릭터인 호야가 사용자의 친구가 되어 감정 관리를 도와주는 서비스
- `2024. 07. 14~ 08. 05`

<br/>

### ✨ 주요 기능

1. 호야와의 대화
 
   - 자체, 카카오, 구글 로그인을 통한 호야와의 1:1 대화 기능
   - 프롬프트 엔지니어링을 통한 응답 품질 개선
   - DeepL API를 통한 토큰 절약
  
2. 감정 트래커
 
   - cron 표현식을 통해 지정된 시간에 호야와의 일일 대화 내용이 요약되어 AI 일기에 저장
   - AI 일기 조회 및 삭제 기능
   - 사용자 일기 CRUD 기능
   - 캘린터 형식으로 월별 감정 조회
   - 지도 API를 통한 사용자 주변 전문 의료기관 추천
    
3. 친구
   
   - 다른 사용자에게 친구 요청 전송, 요청을 받은 사용자가 수락하면 서로 친구로 등록
   - 친구로 등록된 사용자의 최근 감정 상태 조회 가능
   - 한쪽에서 친구를 삭제하면 양방향으로 삭제

4. 오브제 꾸미기

   - 결제(PortOne) API를 통한 마일리지 충전
   - 보유 마일리지로 상점에서 오브제 구매 가능
   - 보유 오브제 상태 관리(true/false)로 꾸미기 가능

<br/>

### 👥 Member

| FrontEnd | BackEnd |
| :------: | :-----: |
| [김은혜](https://github.com/GraceKim527), [오수경](https://github.com/SuKyeong2002) | [강두오](https://github.com/kduoh99), [지정욱](https://github.com/dkdpawjddnr) |

<br/>

### 🔗 Repository & Deploy Links

- [FrontEnd Repo](https://github.com/LikeLion-12th-SKHU/LikeLion-12th-TEAM02-FE) | [FrontEnd Deploy](https://moodfriend.vercel.app)
- [BackEnd Repo](https://github.com/LikeLion-12th-SKHU/LikeLion-12th-TEAM02-BE) | [BackEnd Deploy](https://moodfriend.site)

<br/>

### 📃 API Documentation

- [Swagger](https://moodfriend.site/swagger-ui/index.html)

<br/>

### 🗃️ ERD

![호야](https://github.com/user-attachments/assets/5fd90ffc-69c6-4132-b810-4353bbd359be)

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
