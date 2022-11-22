2022-11-20

DirectMessage 기능을 어떻게 만들어낼까요...
어떻게 해야 간단하게 구현할 수 있을까?
새롭게 Socket 을 생성하는게 사실은 가장 간단한 문제인데 이게 상당히 애매하네...

지금 DirectMessage 아예 처음 접근부터 틀렸음.
새롭게 방파주는 방식으로 하는게 맞는듯.

--> DirectMessageSocketThread 를 새로 만들어주는 방식으로 진행합시다.

--> ㄴㄴ 위 방식 별로임 Message Queue 를 응용하는 방식을 쓸게요. 대신 식별자는 Nickname 입니다.

