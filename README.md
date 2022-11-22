FileShare Desktop
============================

### 1. 프로젝트 소개

   자신의 신원을 노출시키지 않고 타인과 파일을 공유하거나 채팅할 수 있는 클라이언트 프로그램임. 
   
   서버에서 지원받는 기능이 존재하지만 클라이언트 프로그램만으로 사용할 수 있도록 TCP/IP를 활용한 소켓 통신을 구현하였음. 
   PGP Structure 를 활용한 메시지의 암복호화가 프로젝트의 핵심 기능임.  

-----------------------
### 2. 프로젝트를 진행한 이유

   학부에서 컴퓨터 보안과 블록체인을 수강하면서 흥미를 느끼게 되었음. 
   
   컴퓨터 보안에서 공개키 방식에 대해 배우면서 
   응용할 수 있는 곳이 많은 기술이라고 생각했고 암호화의 방식 자체에서 매력을 느끼게 되었음. 마찬가지로 블록체인을 수강하면서 
   블록체인에서 사용되는 개념들이 흥미로웠음. 
   
   머클트리와 지갑에 대해 배우게 되었고 이 또한 응용해볼 수 있는 곳이 많다는 생각을 하게 되었음. 
   

   개인 프로젝트를 진행하면서 이 같은 개념들을 직접 구현해보고자 했고 PGP Structure 를 직접 구현하여
   프로젝트에서 유용하게 쓰고자 하였음. 
   
   다른 사람에게 자신의 신원을 노출시키지 않고 여러 사람과 자유롭게 소통하고 파일을 공유할 수 있는
   서비스의 경우 위와 같은 개념들이 장점을 발휘할 수 있는 프로젝트라고 생각되어 진행하게 되었음. 

-----------------------
### 3. 프로젝트를 진행한 방법

   배웠던 내용을 기반으로 직접 코드로 구현하고자 했음. PGP 의 경우 알고 있는 작동 원리를 기반으로 
   직접 코드 상에 구현하여 곧바로 호출하여 사용할 수 있게 만들고자 했고 그 과정에서 여러 자료를 찾아보았고 
   대칭키, 비대칭키 암호화 알고리즘 등을 유용하게 응용해서 만들어낼 수 있었음. 

-----------------------
### 4. 사용한 기술 소개와 선정 이유

   - Hash  
     - 선정 이유: 고유한 값을 도출하기에 무결성을 검증하는데 유용하게 사용될 수 있음. 
   
   - AES 
     - 선정 이유: 암복호화 과정에서 동일한 키를 사용하므로 빠른 속도를 보장함. 
   
   - RSA 
     - 선정 이유: 공개키 암호화에 사용될 수 있고 오로지 자신만 복호화할 수 있는 비밀키를 제공함. 
   
   - PGP Structure 
     - 선정 이유: 안전하게 공통의 키를 나눠가질 수 있는 방식으로 Direct Message 구현 시 유용하게 쓰임. 
   
   - TCP/IP 통신 
     - 선정 이유: UDP 보다 안정적이고 신뢰할 수 있는 통신방식으로 채팅 메시지를 전달하는데 적합함. 
   
   - MerkleTree 
     - 선정 이유: 블록체인에서 사용한 개념으로 대용량 파일의 무결성을 검증하는데 유용하게 사용될 수 있음. 

-----------------------
### 5. 전체 구조



-----------------------
### 6. 기능 소개 

   - 공개키 암복호화
   - 대칭키 암복호화
   - PGP 구조 구현
   - 파일 암복호화
   - 이미지 암복호화
   - 키지갑
   - TCP/IP 통신


-----------------------
### 7. 진행사항
   - 오픈채팅 기능 구현
   - PGP Structure 구현
   - 비대칭키 생성 및 암복호화 기능 구현 
   - 대칭키 생성 및 암복호화 기능 구현
   - 이미지 암복호화 기능 구현
   - 키 지갑 기능 구현
   - Direct Message 기능 구현중... 

-----------------------
### 8. 향후 방향성(향후 추가할 수 있는 기능)
   > - 웹 백엔드 구현 이후 백엔드에서 통신 가능한 다른 사용자들의 publicKey 리스트를 가져올 수 있도록 발전.
   > - 대용량 파일 전송 기능 추가하기 
   > - 대용량 파일 전송 시 Merkle Tree 활용하기 
   > - 비디오 및 오디오 전송 기능 추가 가능

----------------------
### 9. Reference 

1) Merkle Tree:
   - https://www.pranaybathini.com/2021/05/merkle-tree.html
   - https://www.lesstif.com/security/merkle-tree-125305097.html 
   - https://www.banksalad.com/contents/%EC%89%BD%EA%B2%8C-%EC%84%A4%EB%AA%85%ED%95%98%EB%8A%94-%EB%B8%94%EB%A1%9D%EC%B2%B4%EC%9D%B8-%EB%A8%B8%ED%81%B4%ED%8A%B8%EB%A6%AC-Merkle-Trees-%EB%9E%80-ilULl 
   - https://academy.binance.com/ko/articles/merkle-trees-and-merkle-roots-explained 

2) RSA encryption and decryption:
   - https://this-programmer.tistory.com/259

3) AES implementation:
   - https://stackoverflow.com/questions/15900831/difference-between-stringvalue-and-value-tostring-new-longvalue-and-lon
    
4) Digtal Signature:
   - https://cornswrold.tistory.com/49

4) PGP structure:
   - https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=wnrjsxo&logNo=221161639001
   - https://www.javatpoint.com/computer-network-pgp

5) TCP/IP client server example:
   - https://ssons.tistory.com/50

6) Extra:
   - javax.crypto.SecretKey: https://tmxhsk99.tistory.com/204
   - String: https://stackoverflow.com/questions/15900831/difference-between-stringvalue-and-value-tostring-new-longvalue-and-lon
   - Blockchain: https://medium.com/programmers-blockchain/blockchain-development-mega-guide-5a316e6d10df
   - Blockchain: https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa
   - Blockchain: https://medium.com/programmers-blockchain/creating-your-first-blockchain-with-java-part-2-transactions-2cdac335e0ce
   - Change console log to Swing: https://www.codejava.net/java-se/swing/redirect-standard-output-streams-to-jtextarea
   - Diffie-Hellman: https://docs.oracle.com/javase/7/docs/technotes/guides/security/crypto/CryptoSpec.html#DH2Ex



