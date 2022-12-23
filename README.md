# [❗️ 리팩토링 중입니다... ❗]️️
# 🧩 Java FileShare Desktop

## 📃 Project Intro
```
📚 What
   신원을 노출시키지 않고 타인과 안전하게 메시지를 주고 받을 수 있는 데스크탑 프로그램입니다. 
   TCP/IP를 활용한 소켓 통신을 활용하여 구현했습니다.  
   PGP Structure를 활용한 메시지 암복호화가 프로젝트의 핵심 기능입니다.  

👻 Why
   컴퓨터 보안 강의과 블록체인을 수강하면서 배운 내용을 응용할 수 있는 프로젝트를 고민했습니다. 
   암복호화 방식에서 흥미를 느꼈기 때문에 암복호화 응용이 가능한 프로젝트를 선정했습니다. 
   개인 프로젝트를 진행하면서 배운 내용을 직접 구현하려 했고 PGP Structure를 직접 구현했습니다. 
   타인에게 신원을 노출하지 않고 다른 사람들과 자유롭게 소통하고 공유할 수 있는 프로젝트를 선정하여 진행했습니다. 

🚕 How
   배웠던 내용을 기반으로 직접 코드로 구현하고자 했습니다. 
   PGP Structure는 배웠던 원리를 기반으로 직접 구현했습니다. 
```

## 👨‍💻 Tech
```
📕 Hash
   고유한 값을 도출하기에 무결성을 검증하는데 유용하게 사용될 수 있음. 
   
📗 AES
   암복호화 과정에서 동일한 키를 사용하므로 빠른 속도를 보장함. 
   
📘 RSA
   공개키 암호화에 사용될 수 있고 오로지 자신만 복호화할 수 있는 비밀키를 제공함. 
 
📙 PGP Structure
   안전하게 공통의 키를 나눠가질 수 있는 방식으로 Direct Message 구현 시 유용하게 쓰임. 
   
📓 TCP/IP
   UDP 보다 안정적이고 신뢰할 수 있는 통신방식으로 채팅 메시지를 전달하는데 적합함. 
   
📔 MerkleTree
   블록체인에서 사용한 개념으로 대용량 파일의 무결성을 검증하는데 유용하게 사용될 수 있음. 
```


## ✔️Concept
```
🔪 공개키 암복호화
   PGP 구조를 구현하기 위해 RSA를 활용한 공개키 암복호화를 제공. 
     
💉 대칭키 암복호화
   PGP 구조를 활용해 얻어낸 대칭키를 사용해 대칭키 암복호화 기능을 제공. 
     
🔬 PGP 구조 구현
   인터넷 상에서 대칭키를 노출시키지 않고 안전하게 교환할 수 있는 기능을 제공. 
     
✏ 이미지 암복호화
   이미지를 대칭키로 암호화하여 전달하고 복호화할 수 있는 기능을 제공. 
     
📐 키지갑
   대칭키와 비대칭키를 관리할 수 있는 지갑 기능을 제공. 
     
🔖 TCP/IP 통신
   자바에서 지원하는 Socket프로그래밍을 활용하여 Client와 Server간에 메시지를 전송 기능을 제공.  
```

## 📈Progress
```
👍 오픈채팅 기능 구현
👍 PGP Structure 구현
👍 비대칭키 생성 및 암복호화 기능 구현 
👍 대칭키 생성 및 암복호화 기능 구현
👍 이미지 암복호화 기능 구현
👍 키 지갑 기능 구현
👍 Direct Message 기능 구현 
```

## ⚠️Improvements
```
😇 웹 백엔드 구현 이후 백엔드에서 통신 가능한 다른 사용자들의 publicKey 리스트를 가져올 수 있도록 발전.
😇 대용량 파일 전송 기능 추가하기 
😇 대용량 파일 전송 시 Merkle Tree 활용하기 
😇 비디오 및 오디오 전송 기능 추가 가능
😇 리팩토링하기 
```

## 🔗 Reference

1️⃣ Merkle Tree:
   - https://www.pranaybathini.com/2021/05/merkle-tree.html
   - https://www.lesstif.com/security/merkle-tree-125305097.html 
   - https://www.banksalad.com/contents/%EC%89%BD%EA%B2%8C-%EC%84%A4%EB%AA%85%ED%95%98%EB%8A%94-%EB%B8%94%EB%A1%9D%EC%B2%B4%EC%9D%B8-%EB%A8%B8%ED%81%B4%ED%8A%B8%EB%A6%AC-Merkle-Trees-%EB%9E%80-ilULl 
   - https://academy.binance.com/ko/articles/merkle-trees-and-merkle-roots-explained 

2️⃣ RSA encryption and decryption:
   - https://this-programmer.tistory.com/259

3️⃣ AES implementation:
   - https://stackoverflow.com/questions/15900831/difference-between-stringvalue-and-value-tostring-new-longvalue-and-lon

4️⃣ Digtal Signature:
   - https://cornswrold.tistory.com/49

5️⃣ PGP structure:
   - https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=wnrjsxo&logNo=221161639001
   - https://www.javatpoint.com/computer-network-pgp

6️⃣ TCP/IP client server example:
   - https://ssons.tistory.com/50

7️⃣ Extra:
   - javax.crypto.SecretKey: https://tmxhsk99.tistory.com/204
   - String: https://stackoverflow.com/questions/15900831/difference-between-stringvalue-and-value-tostring-new-longvalue-and-lon
   - Blockchain: https://medium.com/programmers-blockchain/blockchain-development-mega-guide-5a316e6d10df
   - Blockchain: https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa
   - Blockchain: https://medium.com/programmers-blockchain/creating-your-first-blockchain-with-java-part-2-transactions-2cdac335e0ce
   - Change console log to Swing: https://www.codejava.net/java-se/swing/redirect-standard-output-streams-to-jtextarea
   - Diffie-Hellman: https://docs.oracle.com/javase/7/docs/technotes/guides/security/crypto/CryptoSpec.html#DH2Ex

## 🏠 Web Backend Repository
> [웹 백엔드 리포지토리](https://github.com/yuny0623/FileShare-Web-Backend)
