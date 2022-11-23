2022-11-21

ClientGui 부터 먼저 개선해야할듯?

server 에서 받는건 String 으로 받는데 이건 pattern matching 으로 잡아야할듯?
굳이 input 받는 쪽에서 전부 parsing 하기엔 너무 보기안좋음

Swing Layout 사용 예시:
https://kkh0977.tistory.com/590

어디서부터 시작?
1. client gui 부터 레이아웃 세팅 다시하시고... client gui 에서 userinfo 랑 directMessage 확인할 수 있게 합시다.
그리고 DirectMessage 보낼때 그 구조가 socket 타고 전송되는건데 아마 그게 기존의 chatting room 쪽으로 일부가 전송되고
또 일부는 DirectMessage 쪽으로 보내지는듯 --> 이거 그냥 client GUI 에서 전부 다 받게 바꾸세요.

--> 그냥 clientGui 부터 수정하면 됨.