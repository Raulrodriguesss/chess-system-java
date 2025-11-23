# ♟️ Chess Game — Java (Console Application)

Este projeto é uma implementação completa do jogo de xadrez em Java, executado via terminal.  
Foi desenvolvido com focono aprendizado de orientação a objetos, boas práticas de arquitetura,  
e modelagem de estruturas reais em computação.

---

## Funcionalidades

- ✔️ Sistema de turnos (brancas e pretas)  
- ✔️ Validação completa de movimentos  
- ✔️ Cada peça com comportamento próprio (polimorfismo)  
- ✔️ Detecção de movimentos inválidos  
- ✔️ Exceções personalizadas (`ChessException`)  
- ✔️ Estrutura modular com pacotes organizados  
- ✔️ Renderização do tabuleiro no terminal  
- ✔️ Captura de peças e listas de peças em jogo  
- ✔️ Mapeamento de posições (matriz → notas do xadrez, ex.: *e4*)  

---

## 🧠 Arquitetura do Projeto

O projeto segue uma arquitetura separada por responsabilidades:

application/ → Entrada do programa e interface via terminal
boardgame/ → Lógica genérica de tabuleiro, posições e peças
chess/ → Regras específicas do jogo de xadrez


### Principais classes:

- **Board** – Representação genérica do tabuleiro  
- **Position** – Coordenadas internas (linha/coluna)  
- **Piece** – Classe abstrata base para peças  
- **ChessPiece** – Comportamentos específicos de peças de xadrez  
- **ChessMatch** – Controla a partida, turnos e regras  
- **ChessException** – Trata erros de movimentos inválidos  

---

Durante o desenvolvimento foram aplicados:

Programação orientada a objetos

Encapsulamento e polimorfismo

Tratamento de erros com exceções

Design modular e reutilização de componentes

## Como executar

1. Certifique-se de ter o **Java 17+** instalado.  
2. Compile o projeto com:

javac application/Program.java

3. Execute
java application.Program
