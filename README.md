# CloneZelda

Protótipo 2D em Java para estudo de lógica de jogos, sistemas interativos em tempo real e estruturação de entidades.

## Descrição breve
`CloneZelda` é um projeto em Java (AWT/Swing) que implementa um protótipo 2D de ação em tempo real inspirado em jogos top-down. O foco está na lógica de jogo: loop principal, movimentação, colisão, leitura de mapa por imagem, IA simples de inimigos e gerenciamento de estados.

## Objetivo de estudo
Este repositório foi desenvolvido como estudo prático de:
- lógica de jogos 2D;
- sistemas interativos em tempo real;
- organização básica de entidades e mundo;
- controle de estado de jogo (menu, gameplay e game over).

## Conceitos aplicados
- **Game loop** com atualização e renderização contínuas.
- **Controle de entrada** por teclado e mouse.
- **Colisão AABB** com máscaras por entidade.
- **Câmera 2D** com clamp nos limites do mapa.
- **Spawn por mapa em imagem** (pixels representam parede, player, inimigo e pickups).
- **Máquina de estados simples** com três estados globais (`MENU`, `NORMAL`, `GAME_OVER`).
- **Sistemas de atributos** (vida e mana) e feedback visual básico.

## Tecnologias
- **Java**
- **Java AWT/Swing** (renderização, janela e eventos)
- **BufferedImage / ImageIO** (spritesheet e mapas)
- Estrutura de projeto compatível com **Eclipse** (`.project`, `.classpath`)

## Como rodar
### Pré-requisitos
- JDK 8+ instalado

### Executar via terminal
```bash
mkdir -p out
javac -d out $(find src -name "*.java")
cp -r res/* out/
java -cp out com.grochevski.main.Game
```

### Executar via Eclipse
1. Importe o projeto existente (há arquivos `.project` e `.classpath`).
2. Garanta que a pasta `res` esteja no classpath de runtime.
3. Execute `com.grochevski.main.Game`.

## Arquitetura / estrutura
```text
src/com/grochevski/
├── main/
│   └── Game.java          # loop principal, estados globais e input
├── entities/
│   ├── Entity.java        # base de entidades e colisão
│   ├── Player.java        # controle do jogador, vida, mana e disparo
│   ├── Enemy.java         # IA de perseguição e dano ao jogador
│   ├── Spell.java         # projétil temporizado
│   ├── Lifepack.java      # pickup de vida
│   ├── Mana.java          # pickup de mana
│   └── Weapon.java        # pickup de arma visual
├── world/
│   ├── World.java         # leitura de mapa por pixel e render de tiles
│   ├── Tile.java          # tile base
│   ├── FloorTile.java     # tile de chão
│   ├── WallTile.java      # tile de parede
│   └── Camera.java        # viewport e clamp
└── graficos/
    ├── Spritesheet.java   # recorte de sprites
    ├── UI.java            # barra de vida
    └── Menu.java          # menu e navegação

res/
├── spritesheet.png
├── level1.png
└── level2.png
```

## Mecânicas implementadas
- Movimentação 4 direções do player com colisão em paredes.
- Câmera seguindo o jogador.
- Inimigos com perseguição local por zona de detecção.
- Dano por contato entre inimigo e jogador.
- Disparo de projéteis por teclado (`F`) e mouse (direção angular).
- Consumo de mana ao disparar.
- Pickups de mana, vida e arma.
- Progressão simples de fases ao eliminar todos os inimigos.
- Tela de menu, pausa por `ESC` e estado de game over com reinício por `ENTER`.

## Estado atual
Projeto funcional como **protótipo de estudo** com duas fases (`level1` e `level2`) e ciclo completo básico (menu → jogo → game over/reinício). O código prioriza clareza de lógica e experimentação de sistemas em tempo real.

## Limitações
- Estado global concentrado em campos estáticos, gerando alto acoplamento.
- Comparações de string com `==` em vários pontos (comportamento frágil em Java).
- IA de inimigo sem pathfinding avançado.
- Ausência de persistência real para opção “carregar jogo”.
- Sem suíte de testes automatizados.
- Balanceamento e feedbacks visuais/sonoros ainda básicos.

## Melhorias futuras
- Migrar estados para `enum` e encapsular regras de transição.
- Separar responsabilidades (input, física, render e regras) em sistemas dedicados.
- Introduzir gerenciador de entidades e eventos para reduzir dependências globais.
- Refinar IA (patrulha, cooldown de ataque, pathfinding por grid).
- Adicionar HUD mais completo (mana em barra, feedback de dano, cooldown).
- Implementar save/load real e telemetria simples de partida.
- Incluir testes unitários para colisão, parsing de mapa e regras de progressão.

## Aprendizados
- Estruturar um loop de jogo estável exige pensar em atualização, renderização e input como partes sincronizadas.
- Pequenos projetos de jogo revelam rapidamente problemas de acoplamento e estado global.
- Representar fases por imagem acelera prototipação e torna o design mais orientado a dados.
- Mesmo projetos simples ganham muito valor quando são documentados com clareza técnica.
