# 🎮 Tetris PRO (Java)

Implementação completa do clássico Tetris desenvolvida em Java com interface gráfica usando Swing.

---

## 📌 Funcionalidades

* ✅ Peças clássicas (I, O, T, L, J, S, Z)
* 🔄 Rotação de peças
* ⬇️ Gravidade automática
* 💥 Remoção de linhas completas
* 🎯 Sistema de pontuação
* 📈 Níveis com aumento de velocidade
* 👻 Ghost piece (pré-visualização de queda)
* 🔄 Hold (armazenar peça)
* ⏭️ Preview da próxima peça
* ⚡ Hard drop (queda instantânea)
* 🧱 Detecção de colisão
* 🛑 Game Over

---

## 🖥️ Tecnologias utilizadas

* Java
* Swing (interface gráfica)

---

## ▶️ Como executar

### 1. Pré-requisitos

* Java JDK 8 ou superior instalado

### 2. Compilar

```bash
javac TetrisPro.java
```

### 3. Executar

```bash
java TetrisPro
```

---

## 🎮 Controles

Setas esquerda/direita: Mover peça
Seta para baixo: Descer peça mais rápido
Seta para cima: Rotacionar peça
Espaço: Queda rápida (hard drop)
C: Segurar peça (hold)
P: Pausar/despausar o jogo


---

## 🧠 Lógica do jogo

O jogo segue a estrutura clássica:

* Um **tabuleiro 20x10**
* Peças (tetraminós) geradas aleatoriamente
* Sistema de colisão para limites e blocos existentes
* Linhas completas são removidas e geram pontuação
* A velocidade aumenta conforme o jogador avança de nível

---

## 📊 Sistema de pontuação

| Linhas removidas | Pontos |
| ---------------- | ------ |
| 1 linha          | 100    |
| 2 linhas         | 300    |
| 3 linhas         | 500    |
| 4 linhas         | 800    |

---

## 🚀 Possíveis melhorias

* 🎵 Adicionar sons e música
* 🎨 Melhorar interface (UI/UX)
* 💾 Salvar ranking de pontuação
* 🌐 Versão web (JavaScript)
* 🤖 IA para jogar automaticamente
* 🧩 Sistema de combos (T-Spin, Back-to-Back)

---

## 📂 Estrutura

Projeto simples em **arquivo único**:

```
TetrisPro.java
README.md
```

---

## 📜 Licença

Uso livre para fins educacionais.

---

## 👨‍💻 Autor

Projeto criado para aprendizado e prática de lógica de programação e desenvolvimento de jogos em Java.
