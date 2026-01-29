# Fluxos do Sistema

## Perfis
- Professor
- Aluno

## Fluxo 1 — Cadastro de Aluno

Ator: Professor

Pré-condições:
- Professor autenticado no sistema.

Fluxo principal:
1. Professor acessa tela "Alunos".
2. Clica em "Novo Aluno".
3. Preenche nome e dados básicos.
4. Sistema valida os campos.
5. Professor confirma.
6. Sistema cria o aluno no banco.
7. Sistema gera código público do aluno.
8. Sistema redireciona para tela do aluno.

Pós-condições:
- Aluno criado e disponível para criação de ficha.

Exceções:
- Dados inválidos → sistema não salva.
- Nome vazio → sistema exibe erro.
- Aluno duplicado → sistema alerta.

## Fluxo 2 — Criação de Ficha Inicial

Ator: Professor

Pré-condições:
- Aluno existente.
- Professor autenticado.

Fluxo principal:
1. Professor acessa perfil do aluno.
2. Clica em "Criar ficha".
3. Sistema cria ficha em estado RASCUNHO.
4. Professor informa:
   - Datas.
   - Intervalo.
   - Intensidade.
   - Observações.
5. Professor salva.
6. Sistema persiste ficha.

Pós-condições:
- Ficha criada sem treinos ainda.

Exceções:
- Dados inválidos → sistema não salva.

## Fluxo 3 — Montagem dos Treinos

Ator: Professor

Pré-condições:
- Ficha existente em estado RASCUNHO.

Fluxo principal:
1. Professor abre ficha.
2. Clica em "Adicionar treino".
3. Define nome (A, B, C, etc.).
4. Sistema cria treino com ordem.
5. Professor adiciona blocos de exercícios:
   5.1 Seleciona o tipo de execução do bloco:
      - Simples
      - Conjugado (+)
      - Bi-set (U)
   5.2 Sistema cria o bloco de treino.
   5.3 Professor adiciona exercícios ao bloco:
      - Seleciona exercício.
      - Seleciona máquina.
      - Informa séries e repetições.
   5.4 Sistema cria os itens do bloco.
6. Professor ajusta ordem de treinos, blocos e itens se necessário.
7. Professor salva ficha.

Pós-condições:
- Treinos estruturados com blocos e exercícios.

Exceções:
- Bloco sem exercícios -> Sistema impede de salvar.
- Dados inválidos -> Sistema impede de salvar.

## Fluxo 4 — Finalização da Ficha

Ator: Professor

Pré-condições:
- Ficha com pelo menos um treino válido.

Fluxo principal:
1. Professor clica em "Finalizar ficha".
2. Sistema valida:
   - Existência de treinos.
   - Existência de blocos.
   - Existência de exercícios.
3. Sistema gera QR Code público.
4. Sistema gera versão PDF da ficha.
5. Sistema marca ficha como ATIVA.
6. Sistema inativa ficha anterior do aluno caso exista.

Pós-condições:
- Ficha ativa pronta para uso.

## Fluxo 5 — Ajuste Rápido de Exercício

Ator: Professor

Pré-condições:
- Ficha ativa.
- Professor autenticado.

Fluxo principal:
1. Professor abre ficha ativa.
2. Professor seleciona o treino desejado.
3. Professor seleciona o bloco de exercícios.
4. Professor clica para editar um exercício do bloco.
5. Professor pode:
   - Substituir o exercício.
   - Alterar máquina.
   - Ajustar séries e repetições.
   - Alterar ordem dentro do bloco.
   - Mover o exercício para outro bloco.
   - Alterar o tipo de execução do bloco.
6. Sistema valida as alterações.
7. Sistema salva a atualização.

Pós-condições:
- Ficha atualizada sem criar nova versão.

Exceções:
- Bloco vazio -> Sistema impede de salvar.
- Dados inválidos -> Sistema impede de salvar.

## Fluxo 6 — Reavaliação do Aluno

Ator: Professor

Pré-condições:
- Aluno existente.

Fluxo principal:
1. Professor seleciona aluno.
2. Clica em "Nova ficha".
3. Sistema cria ficha vinculada ao aluno.
4. Sistema inativa a ficha anterior.
5. Professor monta nova ficha.

Pós-condições:
- Histórico preservado.

## Fluxo 7 — Impressão da Ficha

Ator: Professor

Pré-condições:
- Professor autenticado no sistema.
- Aluno possui ficha ativa.
- Ficha está finalizada.

Fluxo principal:
1. Professor acessa a lista de alunos.
2. Seleciona um aluno.
3. Sistema exibe a ficha ativa do aluno.
4. Professor clica na opção "Imprimir ficha".
5. Sistema gera versão PDF no layout padrão da academia.
6. Sistema exibe pré-visualização do PDF.
7. Professor confirma a impressão.
8. Sistema envia o arquivo para impressão ou download.

Pós-condições:
- Ficha física disponível.

Exceções:
- Aluno sem ficha ativa → sistema exibe mensagem.
- Erro ao gerar PDF → sistema informa falha.

## Fluxo 8 — Acesso do Aluno

Ator: Aluno

Pré-condições:
- Ficha ativa com QR Code.

Fluxo principal:
1. Aluno escaneia QR Code.
2. Sistema identifica código público.
3. Sistema valida se aluno existe.
4. Sistema busca ficha ativa.
5. Sistema exibe treinos organizados.

Pós-condições:
- Aluno visualiza treino online.

