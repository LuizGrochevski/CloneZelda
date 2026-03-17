# Análise Técnica — CloneZelda

## Escopo
Avaliação técnica do projeto como estudo de **lógica de jogos** e **sistemas interativos em tempo real**, sem assumir funcionalidades não implementadas.

## Pontos fortes do projeto
- **Loop principal funcional** com cadência de atualização/renderização consistente e controle de FPS exibido em console.
- **Pipeline de gameplay completo**: menu, jogo, game over, reinício e troca de fase.
- **Modelagem orientada a entidades** com classe base (`Entity`) e especializações para player, inimigos, projéteis e pickups.
- **Mapa orientado por dados** via leitura de pixels (`World`), permitindo criação de fases sem alterar lógica central.
- **Sistema de câmera 2D** com clamp nos limites do mapa, mantendo o foco no player.
- **Colisão com máscara** (AABB), útil para ajustar interação visual x hitbox.
- **Interatividade em tempo real** com múltiplas entradas (teclado + mouse) e resposta por frame.

## Limitações atuais
- **Uso intenso de estado global estático** (`Game.entities`, `Game.player`, `Game.world` etc.), dificultando escalabilidade.
- **Controle de estado por string literal** (`"MENU"`, `"NORMAL"`, `"GAME_OVER"`) com comparação por `==`.
- **Menu parcialmente incompleto**: existe opção “carregar”, mas não há implementação de persistência.
- **IA simples de perseguição local** sem navegação avançada em mapas complexos.
- **Ausência de testes automatizados** para regras de colisão, ciclo de fases e transições de estado.
- **Camada de apresentação acoplada** a regras (ex.: HUD e desenho misturados com regras no fluxo principal).

## Problemas de organização de código
- **God class em `Game`**: concentra janela, loop, input, estado global e parte de HUD/fluxo.
- **Baixa separação de responsabilidades** entre lógica de domínio, renderização e controle de entrada.
- **Dependência bidirecional por acesso estático** entre entidades e `Game`, elevando acoplamento.
- **Nomenclatura e consistência**: pequenos desvios de naming/ortografia (`showMassageGameOver`, `damegeFrames`) e mistura de idiomas.
- **Fluxos de UI e estado parcialmente duplicados** (menu pausa/normal, restart e enter espalhados em diferentes pontos).

## Melhorias pequenas que aumentariam valor de portfólio
1. **Trocar strings de estado por `enum GameState`**
   - ganho: robustez, legibilidade e prevenção de bugs por comparação incorreta.

2. **Centralizar transições de estado em um método dedicado**
   - ganho: rastreabilidade de fluxo (menu → jogo → game over) e manutenção mais simples.

3. **Extrair constantes de configuração**
   - ex.: velocidades, dano, mana por tiro, tempo de vida de projétil.
   - ganho: balanceamento rápido e código menos “hardcoded”.

4. **Criar um `EntityManager` simples**
   - encapsular update/render e remoções seguras.
   - ganho: reduz lógica repetida e clarifica ciclo de entidades.

5. **Documentar mapeamento de cores dos níveis**
   - README com tabela de cores → objeto.
   - ganho: evidencia design data-driven para recrutadores.

6. **Adicionar 3 testes unitários estratégicos**
   - colisão AABB, clamp da câmera e parsing mínimo de mapa.
   - ganho: demonstra maturidade de engenharia sem elevar demais a complexidade.

7. **Refino de feedback visual mínimo**
   - barra de mana e feedback de hit em inimigo/jogador já existente, mas pode ser padronizado.
   - ganho: melhora percepção de qualidade com baixo custo.

## Conclusão
`CloneZelda` é um bom artefato de estudo para demonstrar fundamentos de jogos 2D em Java: loop, estado, colisão, câmera, entidades e leitura de fase por dados. Com ajustes pequenos de organização e robustez, o projeto ganha valor significativo como portfólio técnico focado em lógica e sistemas interativos.
